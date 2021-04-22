package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "rol")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rol implements Serializable{
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@JsonView(Views.Public.class)
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	public Rol() {
		super();
	}

	public Rol(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Rol [id=" + id + ", nombre=" + nombre + "]";
	}
	
	
}
