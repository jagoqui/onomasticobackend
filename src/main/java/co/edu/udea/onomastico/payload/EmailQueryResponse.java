package co.edu.udea.onomastico.payload;

public class EmailQueryResponse {
	private String email;
	private String nombre;
	private int asociacionId;
	private int vinculacionId;
	private int programaAcademicoId;
	
	public EmailQueryResponse() {
		super();
	}

	public EmailQueryResponse(String email, String nombre, int asociacionId, int vinculacionId,
			int programaAcademicoId) {
		super();
		this.email = email;
		this.nombre = nombre;
		this.asociacionId = asociacionId;
		this.vinculacionId = vinculacionId;
		this.programaAcademicoId = programaAcademicoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAsociacionId() {
		return asociacionId;
	}

	public void setAsociacionId(int asociacionId) {
		this.asociacionId = asociacionId;
	}

	public int getVinculacionId() {
		return vinculacionId;
	}

	public void setVinculacionId(int vinculacionId) {
		this.vinculacionId = vinculacionId;
	}

	public int getProgramaAcademicoId() {
		return programaAcademicoId;
	}

	public void setProgramaAcademicoId(int programaAcademicoId) {
		this.programaAcademicoId = programaAcademicoId;
	}
}
