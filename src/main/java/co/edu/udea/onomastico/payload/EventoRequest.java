package co.edu.udea.onomastico.payload;

import java.util.Date;
import java.util.List;
import java.util.Set;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.Evento;
import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoRequest {
	@JsonView(Views.Public.class)
	private int id;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private String nombre;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private java.util.Date fecha;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private String estado;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	private String recurrencia;
	@JsonView(Views.Public.class)

	@NotNull
	@NotBlank
	private Plantilla plantilla;

	@JsonView(Views.Public.class)
	private List<CondicionRequest> condicionesEvento;

	public static Evento toModelCreate(EventoRequest eventoToCreate){
		return Evento.builder().nombre(eventoToCreate.getNombre())
				.fecha(eventoToCreate.getFecha())
				.plantilla(eventoToCreate.getPlantilla()).estado(eventoToCreate.getEstado())
				.recurrencia(eventoToCreate.getRecurrencia()).build();
	}

	public static Evento toModelUpdate(EventoRequest eventoToUpdate, List<Condicion> condicionesEvento){
		return Evento.builder().id(eventoToUpdate.getId())
				.nombre(eventoToUpdate.getNombre()).fecha(eventoToUpdate.getFecha())
				.plantilla(eventoToUpdate.getPlantilla()).estado(eventoToUpdate.getEstado())
				.recurrencia(eventoToUpdate.getRecurrencia())
				.condicionesEvento(condicionesEvento).build();
	}
}
