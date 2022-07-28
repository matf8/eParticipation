package eParticipation.periferico.controller;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.model.Organizacion;
import eParticipation.periferico.service.OrganizacionService;
import eParticipation.periferico.data.GenerateIniciativa;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.springframework.stereotype.Repository;

@Repository
public class ControladorOrganizacion implements OrganizacionService {
	
	private List<Organizacion> organizaciones = new ArrayList<>();	
	private Map<String, Organizacion> notificaciones = new HashMap<>();	
	String uriAlta = "http://localhost:8080/backend-web/eParticipation/iniciativa/alta";
	String urify = "http://localhost:8785/getNotificacion";
	
	public String altaIniciativa(Iniciativa i) throws Exception {
		try {
			if (i != null) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(uriAlta);	
				Invocation invocation = target.request().buildPost(Entity.entity(i, MediaType.APPLICATION_JSON));
				Response r = invocation.invoke();
				return new String(r.getStatus() + " - " + r.readEntity(String.class));
			} 		
		} catch (Exception e) {
			return e.getMessage();
		}
		return "bad";		
	}

	public String ping() {
		return "pong";
	}
	
	public String proponerIniciativaR() throws ParseException {
		try {				
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(uriAlta);
			Iniciativa i = new Iniciativa(
					GenerateIniciativa.setRandomName(),
					GenerateIniciativa.setRandomDesc(),
					GenerateIniciativa.setRandomDate(),
					setRandomCreador(),
					GenerateIniciativa.setRecurso());			
			//System.out.println(i.toString());
			Invocation invocation = target.request().buildPost(Entity.entity(i, MediaType.APPLICATION_JSON));
			Response r = invocation.invoke();
			return new String(r.getStatus() + " - " + r.readEntity(String.class));		
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	private String setRandomCreador() {	
		Random rand = new Random();			
		List<Organizacion> l = getOrganizaciones();
		int r = rand.nextInt(l.size()-1);	
		if (r <= l.size())
			return l.get(r).getNombre()+"-local";
		else return l.get(0).getNombre()+"-local";
		
	}

	public void addOrganizacion(Organizacion e) {
		organizaciones.add(e);
	}

	public void removeOrganizacion(Organizacion e) {
		organizaciones.remove(e);		
	}

	public Organizacion getOrganizacion(String correo) {
		Organizacion ret = null;
		for (Organizacion o: organizaciones)
			if (o.getNombre().equals(correo)) 
				ret = o;
		return ret;
	}
	
	public List<Organizacion> getOrganizaciones() {
		return organizaciones;
	}
	
	public void cargarOrganizaciones() {
		Organizacion o = new Organizacion("ORG1");
		organizaciones.add(o);
		o = new Organizacion("ORG2");
		organizaciones.add(o);
		o = new Organizacion("ORG3");
		organizaciones.add(o);		
	}

	public void addNotificacion(String i) {		
		String[] partesToken = i.split("\\:");		
		String org = partesToken[0];
		String mensaje = partesToken[1];
		Organizacion o = getOrganizacion(org);
		notificaciones.put(mensaje, o);
		sendNotify();
	}
	
	public String showNotify() {
		String n = null;
		for(Entry<String, Organizacion> entry: notificaciones.entrySet()) {
			n = entry.getValue() + ": " + entry.getKey();
			notificaciones.clear();			
		}
		return n;		
	}
	
	public void sendNotify() {
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(urify);	
			Invocation invocation = target.request().buildGet();
			invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
