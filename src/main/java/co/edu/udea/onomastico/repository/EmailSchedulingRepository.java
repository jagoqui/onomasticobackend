package co.edu.udea.onomastico.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.payload.EmailQueryResponse;
import co.edu.udea.onomastico.service.AsociacionService;
import co.edu.udea.onomastico.service.VinculacionService;

@Repository
public class EmailSchedulingRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String BASIC_QUERY = "SELECT * FROM usuario_correo uc WHERE estado = 'ACTIVO'";
	private static String JOIN_ASOCIACION ="JOIN asociacion_por_correo_usuario au ON au.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND au.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN asociacion ON asociacion.id = au.asociacion_id ";
	private static String JOIN_VINCULACION ="JOIN vinculacion_por_usuario_correo vu ON vu.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND  vu.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN vinculacion v ON v.idvinculacion = vu.vinculacion_idvinculacion ";
	private static String JOIN_PROGRAMA ="JOIN programa_academico_por_usuario_correo pa ON pa.usuario_correo_tipo_identificacion = uc.tipo_identificacion AND pa.usuario_correo_numero_identificacion = uc.numero_identificacion JOIN programa_academico p ON p.codigo = pa.programa_academico_codigo ";

	public List<EmailQueryResponse> selectUsuariosCorreo(Set<Condicion> condiciones) {
		StringBuilder sql = new StringBuilder(BASIC_QUERY);
		if(condiciones!=null) {
		condiciones.forEach(condicion -> {
			if (condicion.getId().getCondicion().contains("fecha_nacimiento")) {
				sql.append("AND DAY(fecha_nacimiento)= DAY(CURRENT_DATE) AND Month(fecha_nacimiento)=Month(CURRENT_DATE) ");
			}
			if (condicion.getId().getCondicion().contains("genero")) {
				sql.append("AND genero = "+condicion.getId().getParametro()+" ");
			}
			if (condicion.getId().getCondicion().contains("asociacion")) {
				if(!sql.toString().contains("asociacion")) {
					sql.insert(32, JOIN_ASOCIACION);
					sql.append("AND asociacion_id = ");
					sql.append(condicion.getId().getParametro()+" ");
				}else {
					sql.append("OR asociacion_id = ");
					sql.append(condicion.getId().getParametro()+" ");
				}
			}
			if (condicion.getId().getCondicion().contains("vinculacion")) {
				if(!sql.toString().contains("vinculacion")) {
					sql.insert(32,JOIN_VINCULACION);
					sql.append("AND idvinculacion = ");
					sql.append(condicion.getId().getParametro()+" ");
				}else {
					sql.append("OR idvinculacion = ");
					sql.append(condicion.getId().getParametro()+" ");
				}
			}
			if (condicion.getId().getCondicion().contains("programa_academico")) {
				if(!sql.toString().contains("programa_academico")) {
					sql.insert(32, JOIN_PROGRAMA);
					sql.append("AND programa_academico_codigo =");
					sql.append(condicion.getId().getParametro()+" ");
				}else {
					sql.append("OR programa_academico_codigo =");
					sql.append(condicion.getId().getParametro()+" ");
				}
			}
		});
		}
		String query = sql.toString();
		return queryResponseWithUsersInfo(query);
	}
	
	public List<EmailQueryResponse> queryResponseWithUsersInfo(String query){
		return jdbcTemplate.query(query, (rs, rowNum) -> mapRow(rs,rowNum, query));
	}
	
	 public EmailQueryResponse mapRow(ResultSet rs, int rowNum, String query) throws SQLException {
		    EmailQueryResponse em = new EmailQueryResponse();
		    em.setEmail(rs.getString("email"));
	        em.setNombre(rs.getString("nombre"));
	        if(query.contains("asociacion_por_correo_usuario")) em.setAsociacionId(rs.getInt("asociacion_id"));
	        if(query.contains("idvinculacion")) em.setVinculacionId(rs.getInt("idvinculacion"));
	        if(query.contains("programa_academico_codigo")) em.setProgramaAcademicoId(rs.getInt("codigo"));
	        return em;
	    }
	
}
