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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "imagen_archivo")
public class ImagenArchivo {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "url_imagen", nullable = false, length = 250)
	private String urlImagen;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asociacion_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Asociacion asociacion;

	public ImagenArchivo() {
		super();
	}

	public ImagenArchivo(int id, String urlImagen, Asociacion asociacion) {
		super();
		this.id = id;
		this.urlImagen = urlImagen;
		this.asociacion = asociacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public Asociacion getAsociacion() {
		return asociacion;
	}

	public void setAsociacion(Asociacion asociacion) {
		this.asociacion = asociacion;
	}
	
	
}
