package eParticipation.backend.service;

import java.util.List;

import javax.ejb.Local;

import eParticipation.backend.dto.DtParticipa;

@Local
public interface PersistenciaNSQLocal {
	
	public String subirRecurso(String r) throws Exception;
	public String subirInstrumento(String instrumento, List<String> contenidoInstrumento) throws Exception;
	public void subirRespuesta(String id, DtParticipa a) throws Exception;
	public String subirComentarios(String cm, String u) throws Exception;
	
	public String getRecursoIniciativa(String id) throws Exception;
	public String getComentarios(String string) throws Exception;	
	public String getComentarioUsuario(String id, String correo) throws Exception;
	public List<String> getInstrumentoProceso(String string) throws Exception;	
	public String getParticipacion(String string, String user) throws Exception;
	public String getResultVotacion(String string) throws Exception;
	public String getResultEncuesta(String string) throws Exception;
	
	public void updateInstrumento(String id, List<String> contenidoModificado) throws Exception;
	public void updateRecurso(String id, String r) throws Exception;

	public String borrarComentario(String comentario) throws Exception;


}
