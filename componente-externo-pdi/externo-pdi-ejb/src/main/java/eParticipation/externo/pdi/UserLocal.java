package eParticipation.externo.pdi;

import java.util.List;

import javax.ejb.Local;

import eParticipation.externo.data.User;

@Local
public interface UserLocal {
	
	public void addUser(User u);
	public User getUser(String nombre);
	public List<User> getUsers();	

}
