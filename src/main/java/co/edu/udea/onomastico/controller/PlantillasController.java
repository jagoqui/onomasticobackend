package co.edu.udea.onomastico.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.PlantillaRepository;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.PlantillaService;

@RestController
public class PlantillasController {

	@Autowired
	PlantillaRepository plantillaRepository;
	
	@Autowired
	PlantillaService plantillaService;
	
	@Autowired
	FeignClientInterceptor interceptor;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas")
	public List<Plantilla> getAllPlantillas(){
		return plantillaRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/usuario")
	public List<Plantilla> getAllPlantillasPorAsociacion(@RequestParam Integer id){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return plantillaService.getAllPlantillasByUsuario(usuarioId);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/usuario/pag")
	public List<Plantilla> getAllPlantillasPorAsociacionPag(@RequestParam Integer id,@RequestParam Integer npage,@RequestParam Integer psize,@RequestParam String sort){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return plantillaService.getAllPlantillasByUsuarioPag(usuarioId, npage, psize, sort);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<Plantilla> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        return plantillaService.getAllPlantillas(pageNo, pageSize, sortBy);
    }
	
//	@JsonView(Views.Public.class)
//	@PostMapping("/plantillas/{usuarioId}")
//	public ResponseEntity<PlantillaResponse> addPlantilla(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla, @RequestPart("tempImage") String tempImg, @PathVariable(value = "usuarioId") Integer usuarioId){
//		return  plantillaService.addPlantilla(file, plantilla, usuarioId, tempImg);
//	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/plantillas")
	public ResponseEntity<Plantilla> addPlantilla(@RequestPart("plantilla") Plantilla plantilla){
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return  plantillaService.addPlantilla(plantilla, userId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/{id}")
	public Plantilla getPlantillaById(@PathVariable(value = "id") Integer plantillaId) {
	    return plantillaService.getPlantillaById(plantillaId);
	}
	
//	@JsonView(Views.Public.class)
//	@PutMapping("/plantillas/{id}/{usuarioId}")
//	public ResponseEntity<PlantillaResponse> updatePlantilla(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla,  @RequestPart("tempImage") String tempImg, @PathVariable(value = "id") Integer plantillaId, @PathVariable(value = "usuarioId") Integer usuarioId) {
//	    return plantillaService.updatePlantilla(file, plantilla, plantillaId, usuarioId, tempImg);
//	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/plantillas/{id}")
	public ResponseEntity<Plantilla> updatePlantilla( @RequestPart("plantilla") Plantilla plantilla,  @PathVariable(value = "id") Integer plantillaId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return plantillaService.updatePlantilla(plantilla, plantillaId, userId);
	}
	
	@DeleteMapping("/plantillas/{id}")
	public ResponseEntity<?> deletePlantilla(@PathVariable(value = "id") Integer plantillaId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return plantillaService.deletePlantilla(plantillaId, userId);
	}
}
