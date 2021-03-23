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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	public UploadFileResponse uploadPlantillaImage(MultipartFile file, String name) {
        String fileName = fileService.storeFile(file, name);

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
	
//	public ResponseEntity<PlantillaResponse> addPlantilla(MultipartFile file, Plantilla plantilla, Integer usuarioId, String tempImgName){
//		Plantilla newPlantilla = plantillaRepository.save(plantilla);
//		String imageName = String.valueOf(newPlantilla.getId()) + "background.jpg";
//		String texto = plantilla.getTexto();
//		String replacement = IMAGE_SERVER + imageName;
//		String target = IMAGE_SERVER + tempImgName ;
//		String str = texto.replaceAll(target, replacement);
//		plantilla.setTexto(str);
//		newPlantilla = plantillaRepository.save(plantilla);
//		UploadFileResponse up = uploadPlantillaImage(file, imageName);
//		PlantillaResponse pr = new PlantillaResponse(up, newPlantilla);
//		return  new ResponseEntity<>(pr, HttpStatus.OK);
//	}
	
	public ResponseEntity<Plantilla> addPlantilla(Plantilla plantilla, Integer usuarioId){
		Plantilla newPlantilla = plantillaRepository.save(plantilla);
		newPlantilla = plantillaRepository.save(plantilla);
		return  new ResponseEntity<>(newPlantilla, HttpStatus.OK);
	}
	
	public Plantilla getPlantillaById(Integer plantillaId) {
	    return plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));
	}
	
//	public ResponseEntity<PlantillaResponse> updatePlantilla(MultipartFile file, Plantilla plantilla, Integer plantillaId, Integer usuarioId, String tempImgName) {
//		
//		Plantilla  plantillaToUpdate =  plantillaRepository.findById(plantillaId)
//	            .orElseThrow(() -> new ResourceNotFoundException("plantilla" + "id"+plantillaId));
//		String imageName = String.valueOf(plantillaId) + "background.jpg";
//		String texto = plantilla.getTexto();
//		String replacement = IMAGE_SERVER + imageName;
//		String target = IMAGE_SERVER + tempImgName ;
//		String str = texto.replaceAll(target, replacement);
//		plantillaToUpdate.setTexto(str);
//		plantillaToUpdate.setAsociacionesPorPlantilla(plantilla.getAsociacionesPorPlantilla());;
//
//		UploadFileResponse up = uploadPlantillaImage(file, imageName);
//		
//		Plantilla updatedPlantilla = plantillaRepository.save(plantillaToUpdate);
//		PlantillaResponse pr = new PlantillaResponse(up, updatedPlantilla);
//	    return new ResponseEntity<>(pr, HttpStatus.OK);
//	}
public ResponseEntity<Plantilla> updatePlantilla(Plantilla plantilla, Integer plantillaId, Integer usuarioId) {
		
		Plantilla  plantillaToUpdate =  plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("plantilla" + "id"+plantillaId));
		
		plantillaToUpdate.setTexto(plantilla.getTexto());
		plantillaToUpdate.setAsociacionesPorPlantilla(plantilla.getAsociacionesPorPlantilla());;
		Plantilla updatedPlantilla = plantillaRepository.save(plantillaToUpdate);
	    return new ResponseEntity<>(updatedPlantilla, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletePlantilla(Integer plantillaId, Integer usuarioId) {
		Plantilla plantilla = plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));

		plantillaRepository.delete(plantilla);
		return ResponseEntity.ok().build();
	}
}
