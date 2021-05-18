package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonView;

@Embeddable
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CorreoEnviadoId implements Serializable{
	
	@JsonView(Views.Public.class)
	@Column(name = "email")
	private String email;
	
	@JsonView(Views.Public.class)
	@Column(name = "fecha", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date fecha;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CorreoEnviadoId that = (CorreoEnviadoId) o;
		return email.equals(that.email) && fecha.equals(that.fecha);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, fecha);
	}
}
