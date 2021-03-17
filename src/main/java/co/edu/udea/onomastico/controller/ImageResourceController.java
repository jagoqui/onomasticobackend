package co.edu.udea.onomastico.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import co.edu.udea.onomastico.payload.UploadFileResponse;
import co.edu.udea.onomastico.service.FileService;

@RestController
public class ImageResourceController {

	@Autowired
	private FileService fileService;
	
	@RequestMapping(path = "/delete/{name}", method = RequestMethod.DELETE)
    public  ResponseEntity<?> deleteFile(@PathVariable(value = "name") String name) {
        fileService.deleteFile(name);
		return ResponseEntity.ok().build();
    }
	
	@RequestMapping(path = "/upload/{name}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(value = "name") String name) {
        String fileName = fileService.storeFile(file, name);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	
	@RequestMapping(path = "/images/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String fileName, HttpServletRequest request) throws IOException{
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);
        
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
