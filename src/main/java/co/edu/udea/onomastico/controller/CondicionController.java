package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CondicionId;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.CondicionRepository;

@RestController
public class CondicionController {
	
	@Autowired
	CondicionRepository condicionRepository;
	
	@JsonView(Views.Public.class)
	@GetMapping("/condicion")
	public List<Condicion> getAllCondiciones() {
	    return condicionRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/condicion")
	public Condicion addCondicion(@RequestBody Condicion condicion) {
		condicionRepository.save(condicion);
		return condicion;
		
	}
	@JsonView(Views.Public.class)
	@GetMapping("/condicion/{condicion}/{parametro}")
	public Condicion getCondicionById(@PathVariable(value = "condicion") String condicion,
			@PathVariable(value = "parametro") String parametro) {
		CondicionId condicionId = new CondicionId(condicion,parametro);
	    return condicionRepository.findById(condicionId)
	            .orElseThrow(() -> new ResourceNotFoundException("condicion"+"id"+condicionId));
	}
	@PutMapping("/condicion/{condicion}/{parametro}")
	public Condicion updateCondicion(@PathVariable(value = "condicion") String condicion,
			@PathVariable(value = "parametro") String parametro,
	                                         @RequestBody Condicion detallesCondicion) {
		CondicionId condicionId = new CondicionId(condicion,parametro);
		Condicion  condicionToupdate =  condicionRepository.findById(condicionId)
	            .orElseThrow(() -> new ResourceNotFoundException("condicion" + "id"+condicionId));

		condicionToupdate.setId(detallesCondicion.getId());
		Condicion updatedcondicion = condicionRepository.save(condicionToupdate);
	    return updatedcondicion;
	}
	
	@DeleteMapping("/condicion/{condicion}/{parametro}")
	public ResponseEntity<?> deleteCondicion(@PathVariable(value = "condicion") String condicion,
			@PathVariable(value = "parametro") String parametro) {
		CondicionId condicionId = new CondicionId(condicion,parametro);
		Condicion condicionToDelete = condicionRepository.findById(condicionId)
	            .orElseThrow(() -> new ResourceNotFoundException("condicion"+"id"+condicionId));

		condicionRepository.delete(condicionToDelete);

	    return ResponseEntity.ok().build();
	}
}
