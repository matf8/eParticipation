package eParticipation.backend.service;

import java.util.List;
import javax.ejb.Local;

import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;

@Local
public interface ProcesoService {
	public void datosIniciales();
	
	public void altaProceso(DtProceso d) throws Exception;
	public DtProceso buscarProceso(String nombre) throws Exception;
	public List<DtProceso> listarProcesos() throws Exception;
	public int contarProcesos();
	public void bajaProceso(String nombre) throws Exception;
	public void modificarProceso(DtProceso p) throws Exception;
	public void agregarParticipanteProceso(DtParticipa a) throws Exception;
	public void quitarParticipanteProceso(String proceso, String correo) throws Exception;
	public List<DtProceso> listarProcesosFechas(String d1, String d2) throws Exception;
	public void comentarProceso(String proceso, String user, String comentario) throws Exception;
	public void borrarComentarioProceso(String comentario, String proceso);
	public void agregarSeguidor(String proceso, String correo) throws Exception;
	public void quitarSeguidor(String proceso, String correo) throws Exception;
	public List<String> listarSeguidores(String proceso);
	public String resultVotacion(String proceso) throws Exception;
	public String resultEncuesta(String proceso) throws Exception;
}
