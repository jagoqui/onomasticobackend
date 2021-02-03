package co.edu.udea.onomastico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "plantilla")
public class Plantilla {

	@JsonView(Views.Public.class)
	@Id 
	@Column(name ="idplantilla")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "cuerpo_texto")
	private String cuerpoTexto;
	
	
	public Plantilla() {
		super();
	}

	public Plantilla(int id, String cuerpoTexto) {
		super();
		this.id = id;
		this.cuerpoTexto = cuerpoTexto;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCuerpoTexto() {
		return cuerpoTexto;
	}

	public void setCuerpoTexto(String cuerpoTexto) {
		this.cuerpoTexto = cuerpoTexto;
	}

}
