package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Rol;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.RolService;

@RestController
public class RolController {
	@Autowired
	RolService  rolService;
	
	@GetMapping("/roles")
	@JsonView(Views.Public.class)
	public List<Rol> getAllRoles() {
	    return rolService.getAllRoles();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/rol/{id}")
	public Rol getVinculacionById(@PathVariable(value = "id") Integer rolId) {
	    return rolService.getRolById(rolId);
	}
}
