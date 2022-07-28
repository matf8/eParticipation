package eParticipation.backend.service;

import java.util.List;

import eParticipation.backend.dto.DtCertificado;

public interface InsigniaService {	
	
	public List<String> getLogos();	
	public void altaCertificado(String nombre, Integer nivel, String tipo, Integer momento);	
	public void sendLogo(byte[] file);
	public List<String> getTipos();
	public void addTipo(String nombreTipo);
	public void obtenerCertificadoDeParticipacion(String user, String tipo) throws Exception;	
	public List<DtCertificado> listarDtCertificados();


}
