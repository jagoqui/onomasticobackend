package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;

public class PlantillaResponse {
	@JsonView(Views.Public.class)
	private UploadFileResponse uploadFileResponse;
	@JsonView(Views.Public.class)
    private Plantilla plantilla;
    
	public PlantillaResponse(UploadFileResponse uploadFileResponse, Plantilla plantilla) {
		super();
		this.uploadFileResponse = uploadFileResponse;
		this.plantilla = plantilla;
	}
	public UploadFileResponse getUploadFileResponse() {
		return uploadFileResponse;
	}
	public void setUploadFileResponse(UploadFileResponse uploadFileResponse) {
		this.uploadFileResponse = uploadFileResponse;
	}
	public Plantilla getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(Plantilla plantilla) {
		this.plantilla = plantilla;
	}
}