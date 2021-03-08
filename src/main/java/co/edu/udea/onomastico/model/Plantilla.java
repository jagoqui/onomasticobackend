package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	@Column(name = "texto")
	private String texto;
	
	@JsonView(Views.Internal.class)
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name = "asociacion_por_plantilla", 
            joinColumns = { @JoinColumn(name = "plantilla_idplantilla") }, 
            inverseJoinColumns = { @JoinColumn(name = "asociacion_id") })
    private Set<Asociacion> asociacionesPorPlantilla = new HashSet<Asociacion>();
	
	public Plantilla() {
		super();
	}
	
	public Plantilla(int id, String texto, Set<Asociacion> asociacionesPorPlantilla) {
		super();
		this.id = id;
		this.texto = texto;
		this.asociacionesPorPlantilla = asociacionesPorPlantilla;
	}

	public Set<Asociacion> getAsociacionesPorPlantilla() {
		return asociacionesPorPlantilla;
	}

	public void setAsociacionesPorPlantilla(Set<Asociacion> asociacionesPorPlantilla) {
		this.asociacionesPorPlantilla = asociacionesPorPlantilla;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String cuerpoTexto) {
		this.texto = cuerpoTexto;
	}

}
