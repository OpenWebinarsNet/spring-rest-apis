package com.openwebinars.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Persona {
	
	private String dni;
	private String nombre;
	private String apellidos;

}
