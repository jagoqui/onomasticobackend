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

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.CondicionRepository;


public class CondicionController {
	
	@Autowired
	CondicionRepository condicionRepository;
	
	@JsonView(Views.Public.class)
	@GetMapping("/condiciones")
	public List<Condicion> getAllCondiciones() {
	    return condicionRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/condiciones")
	public Condicion addCondicion(@RequestBody Condicion condicion) {
		condicionRepository.save(condicion);
		return condicion;
		
	}
	@PutMapping("/condiciones/{id}")
	public Condicion updateCondicion(@PathVariable(value = "id") Integer condicionId,
	                                         @RequestBody Condicion detallesCondicion) {

		Condicion  condicion =  condicionRepository.findById(condicionId)
	            .orElseThrow(() -> new ResourceNotFoundException("condicion" + "id"+condicionId));

		condicion.setParametroA(detallesCondicion.getParametroA());
		condicion.setParametroB(detallesCondicion.getParametroB());
		condicion.setComparacion(detallesCondicion.getComparacion());

		Condicion updatedcondicion = condicionRepository.save(condicion);
	    return updatedcondicion;
	}
	
	@DeleteMapping("/condiciones/{id}")
	public ResponseEntity<?> deleteCondicion(@PathVariable(value = "id") Integer condicionId) {
		Condicion condicion = condicionRepository.findById(condicionId)
	            .orElseThrow(() -> new ResourceNotFoundException("condicion"+"id"+condicionId));

		condicionRepository.delete(condicion);

	    return ResponseEntity.ok().build();
	}
}
