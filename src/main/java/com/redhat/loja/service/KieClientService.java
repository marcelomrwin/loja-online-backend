package com.redhat.loja.service;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.exception.KieServicesException;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.redhat.loja_online.Compra;

@Service
public class KieClientService {

	private static final String KIE_PASSWD = "r3dh4t1!";
	private static final String KIE_USER = "rhdmAdmin";
	private static final String KIE_URL = "http://localhost:8080/kie-server/services/rest/server";
	private static final String KIE_SESSION_NAME = "kieless";
	private static final String CONTAINER_ID = "loja-online_1.0.0-SNAPSHOT";

	KieCommands commandsFactory = KieServices.Factory.get().getCommands();
	RuleServicesClient rulesClient = null;
	Logger logger = LoggerFactory.getLogger(getClass());

	public KieClientService() {
		KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(KIE_URL, KIE_USER, KIE_PASSWD);
		conf.setMarshallingFormat(MarshallingFormat.JSON);
		KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
		rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
	}

	public Compra calcularTotalCompra(Compra compra) {

		List<Command<?>> commands = new ArrayList<>();
		commands.add(commandsFactory.newInsert(compra, "Compra", true, "DEFAULT"));
		commands.add(commandsFactory.newStartProcess("totalizar"));
		commands.add(commandsFactory.newFireAllRules());

		BatchExecutionCommand batchCommand = commandsFactory.newBatchExecution(commands, KIE_SESSION_NAME);
		ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults(CONTAINER_ID,
				batchCommand);

		if (executeResponse.getType() == ResponseType.SUCCESS) {
			ExecutionResults results = executeResponse.getResult();

			Compra c = (Compra) results.getValue("Compra");
						
			compra.setTotalDescontos(c.getTotalDescontos());
			compra.setFrete(c.getFrete());
			compra.setValorTotal(c.getValorTotal());
			
		} else {
			String message = "Error calculating prices. " + executeResponse.getMsg();
			logger.error(executeResponse.getResult().toString());
			throw new KieServicesException(message);
		}
		return compra;
	}

}
