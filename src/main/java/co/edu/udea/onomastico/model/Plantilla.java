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

@Entity
@Table(name = "plantilla")
public class Plantilla {

	@Id 
	@Column(name ="idplantilla")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "cuerpo_texto")
	private String cuerpoTexto;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "imagen_archivo_id")
	private ImagenArchivo imagenArchivo;
	
	public Plantilla() {
		super();
	}

	public Plantilla(int id, String cuerpoTexto, ImagenArchivo imagenArchivo) {
		super();
		this.id = id;
		this.cuerpoTexto = cuerpoTexto;
		this.imagenArchivo = imagenArchivo;
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

	

	public ImagenArchivo getImagenArchivo() {
		return imagenArchivo;
	}

	public void setImagenArchivo(ImagenArchivo imagenArchivo) {
		this.imagenArchivo = imagenArchivo;
	}

}
