package eParticipation.backend.service;

import java.util.List;
import javax.ejb.Local;

import eParticipation.backend.dto.DtIniciativa;

@Local
public interface IniciativaService {
	public void comentariosIniciales();

	public void altaIniciativa(String nombre, String descripcion, String fecha, String creador, String recurso) throws Exception;
	public String evaluarIniciativa(DtIniciativa di, String nodo) throws Exception;
	public DtIniciativa buscarIniciativa(String nombre) throws Exception;
	public List<DtIniciativa> listarIniciativas() throws Exception;
	public List<DtIniciativa> listarIniciativasFechas(String fechaInicial, String fechaFinal) throws Exception;
	public void adherirUsuario(String i, String u) throws Exception;
	public void desadherirUsuario(String i, String u) throws Exception;
	public List<String> listarAdheridos(String iniciativa);		
	public List<String> listarSeguidores(String iniciativa);
	public List<DtIniciativa> listarRelacionados();	
	public void agregarSeguidor(String iniciativa, String correo) throws Exception;
	public void quitarSeguidor(String iniciativa, String correo) throws Exception;
	public int contarIniciativas();
	public int contarAdhesiones();
	public void bajaIniciativa(String n) throws Exception;
	public void modificarIniciativa(DtIniciativa d);
	public void comentarIniciativa(String nombreIniciativa, String user, String comentario) throws Exception;
	public void borrarComentarioIniciativa(String comentario, String ini);
	public int contarSeguidores();
	public int contarComentarios();
	public List<DtIniciativa> listarIniciativasPublicadas() throws Exception;

}
