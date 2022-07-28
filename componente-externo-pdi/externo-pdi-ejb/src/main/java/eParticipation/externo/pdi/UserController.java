package eParticipation.externo.pdi;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import eParticipation.externo.data.User;

@Stateless
public class UserController implements UserLocal {
	
	@EJB
	private UserPersistence up;
	
    public UserController() {   }
    
    public void addUser(User u) {
    	up.addUser(u);    	
    }
    
    public User getUser(String cedula) {
    	return up.getUser(cedula);
    }
    
    public List<User> getUsers() {
    	return up.getUsers();
    }
    
}
