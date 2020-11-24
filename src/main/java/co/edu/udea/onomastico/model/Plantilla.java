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
	@Column(name ="idplantilla", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "cuerpo_texto",nullable = false, length = 250)
	private String cuerpoTexto;
	
	@Column(name = "margen_horizontal", nullable = false)
	private int margenHorizontal;
	
	@Column(name = "margen_vertical", nullable = false)
	private int margenVertical;
	
	@Column(name = "interlineado", nullable = false)
	private int interlineado;
	
	@Column(name = "color_texto", nullable = false, length = 7)
	private String colorTexto;
	
	@Column(name = "tamaño_texto", nullable = false)
	private int tamañoTexto;
	
	@Column(name = "alineacion_texto", nullable = false, length = 10)
	private String alineacionTexto;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "imagen_archivo_id", nullable = false)
	private ImagenArchivo imagenArchivo;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipografia_idtipografia", nullable = false)
	private Tipografia tipografia;

	public Plantilla() {
		super();
	}

	public Plantilla(int id, String cuerpoTexto, int margenHorizontal, int margenVertical, int interlineado,
			String colorTexto, int tamañoTexto, String alineacionTexto, ImagenArchivo imagenArchivo,
			Tipografia tipografia) {
		super();
		this.id = id;
		this.cuerpoTexto = cuerpoTexto;
		this.margenHorizontal = margenHorizontal;
		this.margenVertical = margenVertical;
		this.interlineado = interlineado;
		this.colorTexto = colorTexto;
		this.tamañoTexto = tamañoTexto;
		this.alineacionTexto = alineacionTexto;
		this.imagenArchivo = imagenArchivo;
		this.tipografia = tipografia;
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

	public int getMargenHorizontal() {
		return margenHorizontal;
	}

	public void setMargenHorizontal(int margenHorizontal) {
		this.margenHorizontal = margenHorizontal;
	}

	public int getMargenVertical() {
		return margenVertical;
	}

	public void setMargenVertical(int margenVertical) {
		this.margenVertical = margenVertical;
	}

	public int getInterlineado() {
		return interlineado;
	}

	public void setInterlineado(int interlineado) {
		this.interlineado = interlineado;
	}

	public String getColorTexto() {
		return colorTexto;
	}

	public void setColorTexto(String colorTexto) {
		this.colorTexto = colorTexto;
	}

	public int getTamañoTexto() {
		return tamañoTexto;
	}

	public void setTamañoTexto(int tamañoTexto) {
		this.tamañoTexto = tamañoTexto;
	}

	public String getAlineacionTexto() {
		return alineacionTexto;
	}

	public void setAlineacionTexto(String alineacionTexto) {
		this.alineacionTexto = alineacionTexto;
	}

	public ImagenArchivo getImagenArchivo() {
		return imagenArchivo;
	}

	public void setImagenArchivo(ImagenArchivo imagenArchivo) {
		this.imagenArchivo = imagenArchivo;
	}

	public Tipografia getTipografia() {
		return tipografia;
	}

	public void setTipografia(Tipografia tipografia) {
		this.tipografia = tipografia;
	}
	
}
