package com.openwebinars.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/persona")
public class PersonaController {
	
	List<Persona> repo = Arrays.asList(
			new Persona(1l, "María", "Fernández López"),
			new Persona(2l, "Abel", "Miranda Martínez"),
			new Persona(3l, "Lucía", "De los Santos Miño")
			);
			
	
	
	@GetMapping("/")
	public List<Persona> obtenerTodas() {
		return repo;
	}
	
	
	@GetMapping("/{id}")
	public Persona obtenerUnaPorId(@PathVariable("id") Long id) {
		return repo.stream()
				.filter(p -> p.getId() == id)
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
	}

}
