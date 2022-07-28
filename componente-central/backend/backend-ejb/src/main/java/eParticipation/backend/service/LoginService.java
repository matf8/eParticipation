package eParticipation.backend.service;

import javax.ejb.Local;

@Local
public interface LoginService {
		
	public Object login(String u, String p) throws Exception;
	
}
