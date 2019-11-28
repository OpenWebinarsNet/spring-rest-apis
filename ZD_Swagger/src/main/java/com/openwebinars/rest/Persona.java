package com.openwebinars.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Persona {
	
	private long id;
	private String nombre;
	private String apellidos;

}
