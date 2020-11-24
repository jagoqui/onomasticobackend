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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "condicion")
public class Condicion {

	@Id
	@Column(name ="idcondicion")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "parametro_a", nullable = false, length = 45)
	private String parametroA;
	
	@Column(name = "parametro_b", nullable = false, length = 45)
	private String parametroB;
	
	@Column(name = "coparacion", nullable = false, length = 10)
	private String comparacion;

	@ManyToMany(mappedBy = "condicionesEvento")
    private Set<Evento> usuariosCorreoCondicion = new HashSet<Evento>();
	public Condicion() {
		super();
	}
	public Condicion(int id, String parametroA, String parametroB, String comparacion,
			Set<Evento> usuariosCorreoCondicion) {
		super();
		this.id = id;
		this.parametroA = parametroA;
		this.parametroB = parametroB;
		this.comparacion = comparacion;
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParametroA() {
		return parametroA;
	}
	public void setParametroA(String parametroA) {
		this.parametroA = parametroA;
	}
	public String getParametroB() {
		return parametroB;
	}
	public void setParametroB(String parametroB) {
		this.parametroB = parametroB;
	}
	public String getComparacion() {
		return comparacion;
	}
	public void setComparacion(String comparacion) {
		this.comparacion = comparacion;
	}
	public Set<Evento> getUsuariosCorreoCondicion() {
		return usuariosCorreoCondicion;
	}
	public void setUsuariosCorreoCondicion(Set<Evento> usuariosCorreoCondicion) {
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}
}
