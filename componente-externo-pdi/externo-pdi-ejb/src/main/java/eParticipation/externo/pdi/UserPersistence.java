package eParticipation.externo.pdi;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import eParticipation.externo.data.User;

@Singleton
public class UserPersistence {
	
	private Map<String, User> users;

    public UserPersistence() {  }    
        
    @PostConstruct
    public void loadUsers() {
    	users = new HashMap<String, User>();
        
		User u1 = new User();
		u1.setCedula("1234567");
		u1.setNombre("User 1");
		u1.setBirth("7/6/2022");
		u1.setNacionalidad("UY");
		u1.setDomicilio("Montevideo");
		users.put(u1.getCedula(), u1);
		
		User u2 = new User();
		u2.setCedula("7654321");
		u2.setNombre("User 2");
		u2.setBirth("7/6/2021");
		u2.setNacionalidad("UY");
		u2.setDomicilio("Artigas");
		users.put(u2.getCedula(), u2);
		
		User u3 = new User();		
		u3.setCedula(generateRandomCedula());
		u3.setNombre("User 3");
		u3.setBirth("7/6/2021");
		u3.setNacionalidad("UY");
		u3.setDomicilio("Canelones");
		users.put(u3.getCedula(), u3);	
		
		User u4 = new User();
		u4.setCedula("50266567");
		u4.setNombre("Mathias Fernandez");
		u4.setBirth("10/2/1996");
		u4.setNacionalidad("UY");
		u4.setDomicilio("Canelones");
		users.put(u4.getCedula(), u4);		
		
		User u5 = new User();
		u5.setCedula("999");
		u5.setNombre("Yisus");
		u5.setBirth("25/12/1980");
		u5.setNacionalidad("UY");
		u5.setDomicilio("Lavalleja");
		users.put(u5.getCedula(), u5);
		
		User u6 = new User();
		u6.setCedula("55213703");
		u6.setNombre("Mauricio Iglesias");
		u6.setBirth("26/8/1999");
		u6.setNacionalidad("UY");
		u6.setDomicilio("Montevideo");
		users.put(u6.getCedula(), u6);
		
		User u7 = new User();
		u7.setCedula("53463017");
		u7.setNombre("Manuel Biurrun");
		u7.setBirth("31/5/1999");
		u7.setNacionalidad("UY");
		u7.setDomicilio("Montevideo");
		users.put(u7.getCedula(), u7);
		
    }
    
    public void addUser(User u) {
    	users.put(u.getCedula(), u);
    }
    
    public User getUser(String cedula) {
    	return users.get(cedula);
    }    
    
    public List<User> getUsers() {
    	return new ArrayList<User>(users.values());
    }        
    
    private static String generateRandomCedula() {
    	 Random r = new Random();
    	 int number = r.nextInt(9999999);
    	 String cedulaSinDigito = String.format("%06d", number);    	
    	 return cedulaSinDigito;
    }	

}
