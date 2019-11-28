package com.openwebinars.rest.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Categoria {

	@ApiModelProperty(value="ID de la Categoria", dataType = "long", example="1", position=1)
	@Id @GeneratedValue
	private Long id;
	@ApiModelProperty(value="Nombre de la Categoria", dataType = "String", example="Comida", position=2)	
	private String nombre;
	
}
