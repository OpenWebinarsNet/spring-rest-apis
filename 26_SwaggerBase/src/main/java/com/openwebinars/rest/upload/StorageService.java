package com.openwebinars.rest.upload;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Este interfaz nos permite definir una abstracción de lo que debería
 * ser un almacén secundario de información, de forma que podamos usarlo
 * en un controlador.
 * 
 * De esta forma, vamos a poder utilizar un almacen que acceda a nuestro 
 * sistema de ficheros, o también podríamos implementar otro que estuviera
 * en un sistema remoto, almacenar los ficheros en un sistema GridFS, ...
 * 
 * 
 * @author Equipo de desarrollo de Spring
 *
 */
public interface StorageService {

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);
    
    void delete(String filename);

    void deleteAll();

}