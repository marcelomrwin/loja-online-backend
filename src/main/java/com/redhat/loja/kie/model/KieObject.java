package com.redhat.loja.kie.model;

import com.redhat.loja_online.Compra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KieObject {
	
	private transient String objectName;
	
	private transient Compra compra;
	
}
