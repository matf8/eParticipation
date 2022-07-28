package eParticipation.periferico.service;

import java.text.ParseException;
import java.util.List;

import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.model.Organizacion;

public interface OrganizacionService {
	
	public String altaIniciativa(Iniciativa i) throws Exception;
	public String ping();
	public String proponerIniciativaR() throws ParseException;	
	public void addOrganizacion(Organizacion e);	
	public void removeOrganizacion(Organizacion e);		
	public Organizacion getOrganizacion(String correo);	
	public List<Organizacion> getOrganizaciones();    	
	public void cargarOrganizaciones();
	public void addNotificacion(String i);
	public String showNotify();

}