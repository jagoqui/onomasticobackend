package co.edu.udea.onomastico.payload;

public class EmailQueryResponse {
	private String email;
	private String nombre;
	private int unidadAdministrativaId;
	private int vinculacionId;
	private int programaAcademicoId;
	
	public EmailQueryResponse() {
		super();
	}

	public EmailQueryResponse(String email, String nombre, int unidadAdministrativaId, int vinculacionId,
							  int programaAcademicoId) {
		super();
		this.email = email;
		this.nombre = nombre;
		this.unidadAdministrativaId = unidadAdministrativaId;
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

	public int getUnidadAdministrativaId() {
		return unidadAdministrativaId;
	}

	public void setUnidadAdministrativaId(int unidadAdministrativaId) {
		this.unidadAdministrativaId = unidadAdministrativaId;
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
