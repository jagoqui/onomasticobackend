package co.edu.udea.onomastico.payload;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CondicionId;
import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CondicionRequest {
	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private String id;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private String condicion;

	@JsonView(Views.Public.class)
	private String value;

	public static Condicion toModel(CondicionRequest condicionToSave, Integer eventoId){
		return Condicion.builder().id(new CondicionId(eventoId,condicionToSave.getCondicion(),condicionToSave.getId())).build();
	}

}
