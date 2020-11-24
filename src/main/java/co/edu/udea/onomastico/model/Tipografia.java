package co.edu.udea.onomastico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="tipografia")
public class Tipografia {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name ="idtipografia")
	private int id;
	
	@Column(name = "nombre_fuente", length = 45, nullable = false)
	private String nombreFuente;
	
	@Column(name = "url_fuente", length = 200, nullable = false)
	private String urlFuente;
	
	@Column(name = "italic", nullable = false)
	private boolean italic;
	
	@Column(name = "bold", nullable = false)
	private boolean bold;
	
	public Tipografia() {
		super();
	}

	public Tipografia(int id, String nombreFuente, String urlFuente, boolean italic, boolean bold) {
		super();
		this.id = id;
		this.nombreFuente = nombreFuente;
		this.urlFuente = urlFuente;
		this.italic = italic;
		this.bold = bold;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreFuente() {
		return nombreFuente;
	}

	public void setNombreFuente(String nombreFuente) {
		this.nombreFuente = nombreFuente;
	}

	public String getUrlFuente() {
		return urlFuente;
	}

	public void setUrlFuente(String urlFuente) {
		this.urlFuente = urlFuente;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}
}
