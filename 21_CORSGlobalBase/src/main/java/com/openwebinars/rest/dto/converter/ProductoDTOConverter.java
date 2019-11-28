package com.openwebinars.rest.dto.converter;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.openwebinars.rest.dto.ProductoDTO;
import com.openwebinars.rest.modelo.Producto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductoDTOConverter {
	
	private final ModelMapper modelMapper;
	
	
	@PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<Producto, ProductoDTO>() {

			@Override
			protected void configure() {
				map().setCategoria(source.getCategoria().getNombre());
			}
		});
	}
	
	public ProductoDTO convertToDto(Producto producto) {
		return modelMapper.map(producto, ProductoDTO.class);
		
	}

}
