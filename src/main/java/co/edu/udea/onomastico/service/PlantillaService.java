package co.edu.udea.onomastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.payload.UploadFileResponse;
import co.edu.udea.onomastico.repository.PlantillaRepository;
@Service
public class PlantillaService {

	@Autowired
    FileService fileService;
	
	@Autowired
	PlantillaRepository plantillaRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	LogTransaccionesService transaccionesService;
	
	@Value("${app.images}")
	private String IMAGE_SERVER;
	
	Logger logger = LoggerFactory.getLogger(PlantillaService.class);
	
	public UploadFileResponse uploadPlantillaImage(HttpServletRequest request, MultipartFile file, String name) {
        String fileName = fileService.storeFile(file, name);

        String fileDownloadUri = ServletUriComponentsBuilder.fromUriString(IMAGE_SERVER)
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	
	
	public List<Plantilla> getAllPlantillas(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Plantilla> pagedResult =  plantillaRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Plantilla>();
    }
	
	public List<Plantilla> getAllPlantillasByUsuario(Integer usuarioId){
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<Plantilla> plantillas = getPlantillasByUnidadAdministrativa(asociaciones);
		return plantillas;
	}
	
	public List<Plantilla> getAllPlantillasByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException{
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<Plantilla> plantillas = getPlantillasByUnidadAdministrativa(asociaciones);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), plantillas.size());
		final Page<Plantilla> page = new PageImpl<>(plantillas.subList(start, end), paging, plantillas.size());
		return page.toList();
	}
	
	public List<Plantilla> getPlantillasByUnidadAdministrativa(Set<UnidadAdministrativa> unidadesAdministrativas){
		List<Plantilla> merge = new ArrayList<Plantilla>();
		unidadesAdministrativas.forEach(unidadAdministrativa ->{
			List<Plantilla> temp = plantillaRepository.findByUnidadAdministrativaPorPlantilla(unidadAdministrativa);
			if(temp!= null && !temp.isEmpty())merge.addAll(temp);
		});
		return merge;
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
		String transaccion = "Añadir plantilla:"+newPlantilla.getId();
		transaccionesService.createTransaccion(usuarioId, transaccion);
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
		plantillaToUpdate.setUnidadAdministrativaPorPlantilla(plantilla.getUnidadAdministrativaPorPlantilla());
		plantillaToUpdate.setUnidadAcademicaPorPlantilla(plantilla.getUnidadAcademicaPorPlantilla());
		Plantilla updatedPlantilla = plantillaRepository.save(plantillaToUpdate);
		String transaccion = "Editar plantilla:"+updatedPlantilla.getId();
		transaccionesService.createTransaccion(usuarioId, transaccion);
	    return new ResponseEntity<>(updatedPlantilla, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletePlantilla(Integer plantillaId, Integer usuarioId) {
		Plantilla plantilla = plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));

		plantillaRepository.delete(plantilla);
		return ResponseEntity.ok().build();
	}
	
	public boolean existsPlantilla(Integer id) {
		return plantillaRepository.existsById(id);
	}
}
