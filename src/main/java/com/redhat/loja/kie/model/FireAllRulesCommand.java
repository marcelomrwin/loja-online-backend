package com.redhat.loja.kie.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FireAllRulesCommand implements ExecutionCommand {

	@SerializedName("fire-all-rules")
	private Object fireAllRules;
	
	@Override
	public JsonObject toJson() {
		this.fireAllRules = new Object();
		return new Gson().toJsonTree(this).getAsJsonObject();
	}

}
