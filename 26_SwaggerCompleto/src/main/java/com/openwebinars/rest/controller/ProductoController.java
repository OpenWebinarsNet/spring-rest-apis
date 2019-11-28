package com.openwebinars.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.openwebinars.rest.dto.CreateProductoDTO;
import com.openwebinars.rest.dto.ProductoDTO;
import com.openwebinars.rest.dto.converter.ProductoDTOConverter;
import com.openwebinars.rest.error.ApiError;
import com.openwebinars.rest.error.ProductoNotFoundException;
import com.openwebinars.rest.modelo.Categoria;
import com.openwebinars.rest.modelo.CategoriaRepositorio;
import com.openwebinars.rest.modelo.Producto;
import com.openwebinars.rest.modelo.ProductoRepositorio;
import com.openwebinars.rest.upload.StorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductoController {

	private final ProductoRepositorio productoRepositorio;
	private final CategoriaRepositorio categoriaRepositorio;
	private final ProductoDTOConverter productoDTOConverter;
	private final StorageService storageService;

	/**
	 * Obtenemos todos los productos
	 * 
	 * @return 404 si no hay productos, 200 y lista de productos si hay uno o más
	 */
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
	@ApiOperation(value="Obtener un producto por su ID", notes="Provee un mecanismo para obtener todos los datos de un producto por su ID")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="OK", response=Producto.class),
			@ApiResponse(code=404, message="Not Found", response=ApiError.class),
			@ApiResponse(code=500, message="Internal Server Error", response=ApiError.class)
	})
	@GetMapping("/producto/{id}")
	public Producto obtenerUno(@ApiParam(value="ID del producto", required=true, type = "long") @PathVariable Long id) {
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
	@ApiOperation(value="Crear un nuevo Producto", notes="Provee la operación para crear un nuevo Producto a partir de un CreateProductoDto y devuelve el objeto creado")
	@ApiResponses(value= {
			@ApiResponse(code=201, message="OK", response=Producto.class),
			@ApiResponse(code=400, message="Bad Request", response=ApiError.class),
			@ApiResponse(code=500, message="Internal Server Error", response=ApiError.class)
	})	
	@PostMapping(value = "/producto", consumes= MediaType.MULTIPART_FORM_DATA_VALUE) //Aunque no es obligatorio, podemos indicar que se consume multipart/form-data
	public ResponseEntity<?> nuevoProducto(
			@ApiParam(value = "Datos del nuevo producto", type="CreateProductoDTO.class") 
			@RequestPart("nuevo") CreateProductoDTO nuevo, 
			@ApiParam(value="imagen para el nuevo producto", type="application/octet-stream") 
			@RequestPart("file") MultipartFile file) {
		
		// Almacenamos el fichero y obtenemos su URL
		String urlImagen = null;
		
		if (!file.isEmpty()) {
			String imagen = storageService.store(file);
			urlImagen = MvcUriComponentsBuilder
							// El segundo argumento es necesario solo cuando queremos obtener la imagen
							// En este caso tan solo necesitamos obtener la URL
							.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
							.build().toUriString();
		}
		
		// Construimos nuestro nuevo Producto a partir del DTO
		// Como decíamos en ejemplos anteriores, esto podría ser más bien código
		// de un servicio, pero lo dejamos aquí para no hacer más complejo el código.
		Producto nuevoProducto = new Producto();
		nuevoProducto.setNombre(nuevo.getNombre());
		nuevoProducto.setPrecio(nuevo.getPrecio());
		nuevoProducto.setImagen(urlImagen);
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
