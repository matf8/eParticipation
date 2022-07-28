package eParticipation.backend.service;

import java.util.List;

import javax.ejb.Local;

import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtOrganizacion;

@Local
public interface PerifericoService {
	public String elegirNodoRandom(String f);
	public void altaEvaluador(String nombre, String uri);
	public void altaOrganizacion(String nombre, String correo, String uri);
	public List<DtEvaluador> getEvaluadores();
	public List<DtOrganizacion> getOrganizaciones();
	public DtEvaluador buscarEvaluador(String ev) throws Exception;
	public DtOrganizacion buscarOrganizacion(String ev) throws Exception;
	public void borrarEvaluador(String ev, String uri);	
	public void borrarOrganizacion(String org, String uri);
	public List<String> getUrisEvaluador();	
	public void addUriEvaluador(String uri) throws Exception;	
	public void bajaUri(String u, String f) throws Exception;
	public List<String> getUrisOrganizacion();	
	public void addUriOrganizacion(String uri) throws Exception;
	public void saveNodoCreador(String creador, String nombre, String nodo);	
	public String getNodoCreador(String creador, String nombre) throws Exception;		

}

