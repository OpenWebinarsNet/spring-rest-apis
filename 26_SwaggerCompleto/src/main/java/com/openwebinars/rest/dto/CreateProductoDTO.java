package com.openwebinars.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductoDTO {

	@ApiModelProperty(value="Nombre del producto", dataType = "String", example="Jamón ibérico de bellota", position=1)
	private String nombre;
	
	@ApiModelProperty(value="Precio del producto", dataType = "float", example="253.27", position=2)
	private float precio;

	@ApiModelProperty(value="ID de la Categoría del producto", dataType = "long", example="1", position=3)
	private long categoriaId;

}
