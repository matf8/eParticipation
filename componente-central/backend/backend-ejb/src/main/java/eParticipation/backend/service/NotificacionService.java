package eParticipation.backend.service;

import javax.ejb.Local;

@Local
public interface NotificacionService {
	
	public void notificaIniciativaCambio(String c, String n);
	

}
