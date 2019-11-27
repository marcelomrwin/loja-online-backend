package com.redhat.loja;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.redhat.loja.kie.model.ExecutionCommand;
import com.redhat.loja.kie.model.FireAllRulesCommand;
import com.redhat.loja.kie.model.InsertCommand;
import com.redhat.loja.kie.model.KieBatchExecution;
import com.redhat.loja.kie.model.KieObject;
import com.redhat.loja.kie.model.StartProcessCommand;
import com.redhat.loja_online.Cliente;
import com.redhat.loja_online.Compra;
import com.redhat.loja_online.Endereco;

@RunWith(BlockJUnit4ClassRunner.class)
public class ConvertObjJSONTest {

	/**
	 * O teste abaixo produz um payload para invocar o serviço REST do Decision Manager 7
	 */
	@Test
	public void test() {

		List<ExecutionCommand> commands = new ArrayList<>();
		commands.add(InsertCommand.builder().entryPoint("DEFAULT").outIdentifier("Compra").returnObject(true)
				.object(KieObject.builder().objectName("com.redhat.loja_online.Compra")
						.compra(Compra.builder().valorTotal(100.0)
								.cliente(Cliente.builder().nivel(1).idade(39)
										.endereco(Endereco.builder().cep(50931470).build()).build())
								.build())
						.build())
				.build());
		commands.add(StartProcessCommand.builder().processId("totalizar").build());
		commands.add(FireAllRulesCommand.builder().build());

		KieBatchExecution execution = KieBatchExecution.builder().lookup("kieless").commands(commands).build();

		System.out.println(execution.toJson());
	}

}