package co.edu.udea.onomastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Rol;
import co.edu.udea.onomastico.repository.RolRepository;

@Service
public class RolService {

	@Autowired
	RolRepository rolRepository;
	
	public List<Rol> getAllRoles() {
	    return rolRepository.findAll();
	}
	
	public Rol getRolById(Integer rolId) {
	    return rolRepository.findById(rolId)
	            .orElseThrow(() -> new ResourceNotFoundException("Rol"+"id"+rolId));
	}
}
