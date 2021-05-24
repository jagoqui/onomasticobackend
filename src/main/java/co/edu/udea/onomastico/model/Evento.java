package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.*;

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

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "evento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Evento implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name ="idevento")
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre", nullable = false, length = 45)
	private String nombre;
	
	@JsonView(Views.Public.class)
	@Column(name = "fecha", nullable = false)
	@Temporal(TemporalType.DATE)
	private java.util.Date fecha;
	
	@JsonView(Views.Public.class)
	@Column(name = "estado", nullable = false, length = 10)
	private String estado;
	
	@JsonView(Views.Public.class)
	@Column(name = "recurrencia", nullable = false, length = 10)
	private String recurrencia;
	
	@JsonView(Views.Public.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plantilla_idplantilla")
	private Plantilla plantilla;
	
	@JsonView(Views.Public.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "evento_idevento")
    private List<Condicion> condicionesEvento = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Evento evento = (Evento) o;
		return id == evento.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
