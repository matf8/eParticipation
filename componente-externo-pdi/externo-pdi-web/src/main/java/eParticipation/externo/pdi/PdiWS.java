package eParticipation.externo.pdi;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import eParticipation.externo.data.User;

@WebService(serviceName = "GestorPdi")
public class PdiWS {
	
	@EJB UserLocal ul;
	
	@WebMethod
	public void addUser(User u) {
		ul.addUser(u);    	
    }
	
	@WebMethod 
	public User getUser(String cedula) {		 
		 return ul.getUser(cedula);	    	
	}
	
	@WebMethod 
	public List<User> getUsers(String cedula) {		 
		 return ul.getUsers();	    	
	}	

}
