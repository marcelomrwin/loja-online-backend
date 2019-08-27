package com.redhat.loja.kie.model;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartProcessCommand implements ExecutionCommand {
	private String processId;

	@Override
	public JsonObject toJson() {
		JsonObject ret = new JsonObject();
		JsonObject pid = new JsonObject();
		pid.addProperty("processId", processId);
		ret.add("start-process", pid);
		
		return ret;
	}

}
