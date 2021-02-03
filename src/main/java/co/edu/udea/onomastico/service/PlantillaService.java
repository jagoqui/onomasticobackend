package co.edu.udea.onomastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.udea.onomastico.payload.UploadFileResponse;

@Service
public class PlantillaService {

	@Autowired
    FileService fileService;
	
	Logger logger = LoggerFactory.getLogger(PlantillaService.class);
	
	public UploadFileResponse uploadPlantillaImage(MultipartFile file, String id) {
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/"+id+"/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
}
