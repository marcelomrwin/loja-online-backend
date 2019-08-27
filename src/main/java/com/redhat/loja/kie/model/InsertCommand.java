package com.redhat.loja.kie.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
public class InsertCommand implements ExecutionCommand {

	@SerializedName("object")
	private KieObject object;

	@SerializedName("out-identifier")
	private String outIdentifier;

	@SerializedName("return-object")
	private boolean returnObject = true;

	@SerializedName("entry-point")
	private String entryPoint = "DEFAULT";

	@Override
	public JsonObject toJson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();

		KieObject obj = object;
		this.object = null;

		JsonElement element = gson.toJsonTree(this);
		JsonObject o = new JsonObject();
		o.add("insert", element);
		
		JsonObject insert = new JsonObject();
		JsonElement jsonObj = gson.toJsonTree(obj.getCompra());
		insert.add(obj.getObjectName(), jsonObj);
		
		JsonObject jo = element.getAsJsonObject();
		
		jo.add("object", insert);

		return o;
	}
}
