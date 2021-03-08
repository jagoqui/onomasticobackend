package co.edu.udea.onomastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.payload.PlantillaResponse;
import co.edu.udea.onomastico.payload.UploadFileResponse;
import co.edu.udea.onomastico.repository.PlantillaRepository;

@Service
public class PlantillaService {

	@Autowired
    FileService fileService;
	
	@Autowired
	PlantillaRepository plantillaRepository;
	
	@Value("${app.images}")
	private String IMAGE_SERVER;
	
	Logger logger = LoggerFactory.getLogger(PlantillaService.class);
	
	public UploadFileResponse uploadPlantillaImage(MultipartFile file, String id) {
        String fileName = fileService.storeFile(file, id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	
	
	public List<Plantilla> getAllUsuariosCorreo(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Plantilla> pagedResult =  plantillaRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Plantilla>();
    }
	
	public ResponseEntity<PlantillaResponse> addPlantilla(MultipartFile file, Plantilla plantilla){
		Plantilla newPlantilla = plantillaRepository.save(plantilla);
		StringBuilder text = new StringBuilder("<div id=\"editorContent\" style=\"background-image: url('"+IMAGE_SERVER);
		text.append(String.valueOf(newPlantilla.getId()) + "background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 100%; color: black;\">");
		text.append(plantilla.getTexto());
		plantilla.setTexto(text.toString());
		newPlantilla = plantillaRepository.save(plantilla);
		UploadFileResponse up = uploadPlantillaImage(file, String.valueOf(newPlantilla.getId()));
		PlantillaResponse pr = new PlantillaResponse(up, newPlantilla);
		return  new ResponseEntity<>(pr, HttpStatus.OK);
	}
	
	public Plantilla getPantillaById(Integer plantillaId) {
	    return plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));
	}
	
	public ResponseEntity<PlantillaResponse> updatePlantilla(MultipartFile file, Plantilla plantilla, Integer plantillaId) {
		
		Plantilla  plantillaToUpdate =  plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("plantilla" + "id"+plantillaId));
	
		StringBuilder text = new StringBuilder("<div id=\"editorContent\" style=\"background-image: url('"+IMAGE_SERVER);
		text.append(String.valueOf(plantillaId) + "background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 100%; color: black;\">");
		text.append(plantilla.getTexto());
		plantillaToUpdate.setTexto(text.toString());
		plantillaToUpdate.setAsociacionesPorPlantilla(plantilla.getAsociacionesPorPlantilla());;
		UploadFileResponse up = uploadPlantillaImage(file, String.valueOf(plantillaId));
		
		Plantilla updatedPlantilla = plantillaRepository.save(plantillaToUpdate);
		PlantillaResponse pr = new PlantillaResponse(up, updatedPlantilla);
	    return new ResponseEntity<>(pr, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletePlantilla(Integer plantillaId) {
		Plantilla plantilla = plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+plantillaId));

		plantillaRepository.delete(plantilla);
		return ResponseEntity.ok().build();
	}
}
