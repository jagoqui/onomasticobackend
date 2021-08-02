package co.edu.udea.onomastico.payload;

import co.edu.udea.onomastico.model.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioCorreoResquest {

    @NotBlank
    private UsuarioCorreoId id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private java.util.Date fechaNacimiento;

    @NotBlank
    private String estado;

    @NotBlank
    private String genero;

    @NotEmpty
    private Set<UnidadAdministrativa> unidadAdministrativaPorCorreoUsuario = new HashSet<>();

    private Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo = new HashSet<>();

    private Set<Plataforma> plataformaPorUsuarioCorreo = new HashSet<>();

    private Set<Vinculacion> vinculacionPorUsuarioCorreo = new HashSet<>();

    public static UsuarioCorreo toModel(UsuarioCorreoResquest usuarioCorreoResquest){
        return UsuarioCorreo.builder().id(usuarioCorreoResquest.getId()).nombre(usuarioCorreoResquest.getNombre())
                .apellido(usuarioCorreoResquest.getApellido()).email(usuarioCorreoResquest.getEmail())
                .fechaNacimiento(usuarioCorreoResquest.getFechaNacimiento())
                .estado(usuarioCorreoResquest.getEstado()).genero(usuarioCorreoResquest.getGenero())
                .unidadAdministrativaPorCorreoUsuario(usuarioCorreoResquest.getUnidadAdministrativaPorCorreoUsuario())
                .plataformaPorUsuarioCorreo(usuarioCorreoResquest.getPlataformaPorUsuarioCorreo())
                .programaAcademicoPorUsuarioCorreo(usuarioCorreoResquest.getProgramaAcademicoPorUsuarioCorreo())
                .vinculacionPorUsuarioCorreo(usuarioCorreoResquest.getVinculacionPorUsuarioCorreo()).build();
    }
}
