package com.redhat.loja.kie.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KieBatchExecution {

	private String lookup = "kieless";

	private List<ExecutionCommand> commands = new ArrayList<>();

	public String toJson() {

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();

		JsonObject root = new JsonObject();
		root.addProperty("lookup", lookup);
		JsonArray cmds = new JsonArray();
		for (ExecutionCommand cmd : commands) {
			cmds.add(cmd.toJson());
		}
		root.add("commands", cmds);

		return gson.toJson(root);

	}
}

/*
 * 
 * sb.append(
 * "\"lookup\":\"kieless\",\"commands\":[{\"insert\":{\"entry-point\":\"DEFAULT\",\"out-identifier\":\"Compra\",\"return-object\":\"true\","
 * ); sb.append("\"object\":{\"com.redhat.loja_online.Compra\":");
 * sb.append(json); sb.append("}}},{\"start-process\":{\"processId\":\"" +
 * processId + "\"}},{\"fire-all-rules\":{}}]}");
 * 
 */