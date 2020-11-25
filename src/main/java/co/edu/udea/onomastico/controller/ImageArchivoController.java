package co.edu.udea.onomastico.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.edu.udea.onomastico.model.ImagenArchivo;
import co.edu.udea.onomastico.repository.ImagenArchivoRepository;
import co.edu.udea.onomastico.service.FileUploadService;

@RestController
public class ImageArchivoController {

	@Autowired
	FileUploadService fileUploadService;
	
	@Autowired
	ImagenArchivoRepository imagenArchivoRepository;
	
	@PostMapping("/images/upload")
	public ImagenArchivo uploadImage(@RequestParam("file") MultipartFile file, @RequestBody ImagenArchivo imagen) throws IOException {
		String url =fileUploadService.uploadFile(file);
		imagen.setUrlImagen(url);
		return imagen;
	}
	
	@GetMapping("/images")
	public List<ImagenArchivo> getAllimages(){
		return imagenArchivoRepository.findAll();
	}
	
}
