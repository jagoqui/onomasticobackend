package co.edu.udea.onomastico.payload;

public class CondicionRequest {
	private String condicion;
	private String parametro;
	public CondicionRequest(String condicion, String parametro) {
		super();
		this.condicion = condicion;
		this.parametro = parametro;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
}
