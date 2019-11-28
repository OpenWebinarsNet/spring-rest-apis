package com.openwebinars.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductoDTO {
	
	private String nombre;
	private float precio;
	private long categoriaId;

}
