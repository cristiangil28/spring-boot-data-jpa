package com.cristian.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	private final static String UPLOADS_FOLDER="uploads";

	@Override
	public Resource load(String filename) {
		// TODO Auto-generated method stub
		Path pathFoto=getPath(filename);
		
		Resource recurso=null;
		
		
		try {
			recurso= new UrlResource(pathFoto.toUri());
			if (!recurso.exists()||recurso.isReadable()) {
				throw new RuntimeException("Error: no se puede cargar LA IMAGEN: "+pathFoto.toString());
			}
		} catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) {
		// TODO Auto-generated method stub
		Path rootPath=getPath(file.getOriginalFilename());
		try {
			Files.copy(file.getInputStream(),rootPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file.getOriginalFilename();
	}

	@Override
	public boolean delete(String filename) {
		// TODO Auto-generated method stub
		Path rootPath=getPath(filename);
		File archivo=rootPath.toFile();
		
		if(archivo.exists()&&archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			}
		}
		return false;
	}
	public Path getPath(String filename) {
		/*
		 * ruta absoluta dentro del proyecto con el metodo toAbsolutePath()
		 * resolve() este metodo concatena el nombre del archivo a la ruta
		 * UUID identificador unico, se concatena con el nombre de la imagen
		 */
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}
}
