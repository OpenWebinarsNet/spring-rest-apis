package com.openwebinars.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.openwebinars.rest.dto.CreateProductoDTO;
import com.openwebinars.rest.dto.ProductoDTO;
import com.openwebinars.rest.dto.converter.ProductoDTOConverter;
import com.openwebinars.rest.error.ProductoNotFoundException;
import com.openwebinars.rest.modelo.Categoria;
import com.openwebinars.rest.modelo.CategoriaRepositorio;
import com.openwebinars.rest.modelo.Producto;
import com.openwebinars.rest.modelo.ProductoRepositorio;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductoController {

	private final ProductoRepositorio productoRepositorio;
	private final CategoriaRepositorio categoriaRepositorio;
	private final ProductoDTOConverter productoDTOConverter;

	/**
	 * Obtenemos todos los productos
	 * 
	 * @return 404 si no hay productos, 200 y lista de productos si hay uno o más
	 */
	//@CrossOrigin(origins = "http://localhost:9001")
	@GetMapping("/producto")
	public ResponseEntity<?> obtenerTodos() {
		List<Producto> result = productoRepositorio.findAll();

		if (result.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay productos registrados");
		} else {

			List<ProductoDTO> dtoList = result.stream().map(productoDTOConverter::convertToDto)
					.collect(Collectors.toList());

			return ResponseEntity.ok(dtoList);
		}

	}

	/**
	 * Obtenemos un producto en base a su ID
	 * 
	 * @param id
	 * @return 404 si no encuentra el producto, 200 y el producto si lo encuentra
	 */
	@GetMapping("/producto/{id}")
	public Producto obtenerUno(@PathVariable Long id) {
		try {
			return productoRepositorio.findById(id)
					.orElseThrow(() -> new ProductoNotFoundException(id));
		} catch (ProductoNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
				
	}

	/**
	 * Insertamos un nuevo producto
	 * 
	 * @param nuevo
	 * @return 201 y el producto insertado
	 */
	@PostMapping("/producto")
	public ResponseEntity<?> nuevoProducto(@RequestBody CreateProductoDTO nuevo) {
		// En este caso, para contrastar, lo hacemos manualmente
		
		// Este código sería más propio de un servicio. Lo implementamos aquí
		// por no hacer más complejo el ejercicio.
		Producto nuevoProducto = new Producto();
		nuevoProducto.setNombre(nuevo.getNombre());
		nuevoProducto.setPrecio(nuevo.getPrecio());
		Categoria categoria = categoriaRepositorio.findById(nuevo.getCategoriaId()).orElse(null);
		nuevoProducto.setCategoria(categoria);
		return ResponseEntity.status(HttpStatus.CREATED).body(productoRepositorio.save(nuevoProducto));
	}

	/**
	 * 
	 * @param editar
	 * @param id
	 * @return 200 Ok si la edición tiene éxito, 404 si no se encuentra el producto
	 */
	@PutMapping("/producto/{id}")
	public Producto editarProducto(@RequestBody Producto editar, @PathVariable Long id) {

		return productoRepositorio.findById(id).map(p -> {
			p.setNombre(editar.getNombre());
			p.setPrecio(editar.getPrecio());
			return productoRepositorio.save(p);
		}).orElseThrow(() -> new ProductoNotFoundException(id));
	}

	/**
	 * Borra un producto del catálogo en base a su id
	 * 
	 * @param id
	 * @return Código 204 sin contenido
	 */
	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> borrarProducto(@PathVariable Long id) {
		Producto producto = productoRepositorio.findById(id)
				.orElseThrow(() -> new ProductoNotFoundException(id));
		
		productoRepositorio.delete(producto);
		return ResponseEntity.noContent().build();
	}
	

}
