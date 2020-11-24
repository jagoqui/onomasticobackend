package co.edu.udea.onomastico.model;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "evento")
public class Evento {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name ="idevento")
	private int id;
	
	@Column(name = "nombre", nullable = false, length = 45)
	private String nombre;
	
	@Column(name = "fecha", nullable = false)
	@Temporal(TemporalType.DATE)
	private java.util.Date fecha;
	
	@Column(name = "estado", nullable = false, length = 10)
	private String estado;
	
	@Column(name = "recurrencia", nullable = false, length = 10)
	private String recurrencia;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asociacion_id", nullable = false)
	private Asociacion asociacion;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plantilla_idplantilla", nullable = false)
	private Plantilla plantilla;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "condicion_por_evento", 
            joinColumns = { @JoinColumn(name = "evento_idevento") }, 
            inverseJoinColumns = { @JoinColumn(name = "condicion_idcondicion") })
    private Set<Condicion> condicionesEvento = new HashSet<Condicion>();
	
//	@OneToMany(mappedBy = "usuarioCorreoEnviado")
//    Set<CorreoEnviado> correoEnviados;

	public Evento() {
		super();
	}

public Evento(int id, String nombre, Date fecha, String estado, String recurrencia, Asociacion asociacion,
		Plantilla plantilla, Set<Condicion> condicionesEvento) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.fecha = fecha;
	this.estado = estado;
	this.recurrencia = recurrencia;
	this.asociacion = asociacion;
	this.plantilla = plantilla;
	this.condicionesEvento = condicionesEvento;
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

public java.util.Date getFecha() {
	return fecha;
}

public void setFecha(java.util.Date fecha) {
	this.fecha = fecha;
}

public String getEstado() {
	return estado;
}

public void setEstado(String estado) {
	this.estado = estado;
}

public String getRecurrencia() {
	return recurrencia;
}

public void setRecurrencia(String recurrencia) {
	this.recurrencia = recurrencia;
}

public Asociacion getAsociacion() {
	return asociacion;
}

public void setAsociacion(Asociacion asociacion) {
	this.asociacion = asociacion;
}

public Plantilla getPlantilla() {
	return plantilla;
}

public void setPlantilla(Plantilla plantilla) {
	this.plantilla = plantilla;
}

public Set<Condicion> getCondicionesEvento() {
	return condicionesEvento;
}

public void setCondicionesEvento(Set<Condicion> condicionesEvento) {
	this.condicionesEvento = condicionesEvento;
}

	
}
