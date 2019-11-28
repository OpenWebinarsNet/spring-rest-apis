package com.openwebinars.rest.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Producto {

	@ApiModelProperty(value="ID del Producto", dataType="long",  example="1", position=1)
	@Id @GeneratedValue
	private Long id;
	
	@ApiModelProperty(value="Nombre del producto", dataType="String", example="Jamón ibérico de bellota", position=2)
	private String nombre;
	
	@ApiModelProperty(value="Precio del producto", dataType = "float", example="253.27", position=3)
	private float precio;
	
	@ApiModelProperty(value="Imagen del producto", dataType = "String", example="http://www.midominio.com/files/12345-imagen.jpg", position=4)
	private String imagen;
	
	
	@ApiModelProperty(value="Categoría del producto", dataType="Categoria", position=5)
	@ManyToOne
	@JoinColumn(name="categoria_id")
	private Categoria categoria;
	
}
