package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;

@Repository
public class EmailSchedulingRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String BASIC_QUERY = "SELECT uc.* FROM usuario_correo uc WHERE estado = 'ACTIVO'";
	private static String JOIN_ASOCIACION ="JOIN asociacion_por_correo_usuario au ON au.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND au.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN asociacion ON asociacion.id = au.asociacion_id ";
	private static String JOIN_VINCULACION ="JOIN vinculacion_por_usuario_correo vu ON vu.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND  vu.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN vinculacion v ON v.idvinculacion = vu.vinculacion_idvinculacion ";
	private static String JOIN_PROGRAMA ="JOIN programa_academico_por_usuario_correo pa ON pa.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND pa.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN programa_academico p ON p.codigo = pa.programa_academico_codigo ";

	public List<UsuarioCorreo> selectUsuariosCorreo(Set<Condicion> condiciones) {
		StringBuilder sql = new StringBuilder(BASIC_QUERY);
		if(condiciones!=null) {
		condiciones.forEach(condicion -> {
			if (condicion.getCondicion().contains("fecha_nacimiento")) {
				sql.append("AND DAY(fecha_nacimiento)= DAY(CURRENT_DATE) AND Month(fecha_nacimiento)=Month(CURRENT_DATE) ");
			} else if (condicion.getCondicion().contains("asociacion")) {
				if(!sql.toString().contains("asociacion")) {
					sql.insert(35, JOIN_ASOCIACION);
					sql.append("AND asociacion_id = ");
					sql.append(condicion.getParametro()+" ");
				}else {
					sql.append("OR asociacion_id = ");
					sql.append(condicion.getParametro()+" ");
				}
			} else if (condicion.getCondicion().contains("vinculacion")) {
				if(!sql.toString().contains("vinculacion")) {
					sql.insert(35,JOIN_VINCULACION);
					sql.append("AND idvinculacion = ");
					sql.append(condicion.getParametro()+" ");
				}else {
					sql.append("OR idvinculacion = ");
					sql.append(condicion.getParametro()+" ");
				}
			} else if (condicion.getCondicion().contains("programa_academico")) {
				if(!sql.toString().contains("programa_academico")) {
					sql.insert(35, JOIN_PROGRAMA);
					sql.append("AND programa_academico_codigo =");
					sql.append(condicion.getParametro()+" ");
				}else {
					sql.append("OR programa_academico_codigo =");
					sql.append(condicion.getParametro()+" ");
				}
			}
		});
		}
		String query = sql.toString();
		System.out.println(query);
		return jdbcTemplate.query(query, (rs, rowNum) -> new UsuarioCorreo(
				new UsuarioCorreoId(rs.getString("tipo_identificacion"),rs.getString("numero_identificacion")),
				rs.getString("nombre"),
				rs.getString("apellido"),
				rs.getString("email"),
				rs.getDate("fecha_nacimiento"),
				rs.getString("estado"),
				rs.getString("genero"))
				);
	}
}
