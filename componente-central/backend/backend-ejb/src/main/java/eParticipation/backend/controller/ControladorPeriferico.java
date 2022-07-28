package eParticipation.backend.controller;

import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.IniciativaNodo;
import eParticipation.backend.model.NodosPerifericos;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.service.PerifericoService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.ConverterService;


@Stateless
public class ControladorPeriferico implements PerifericoService {
	
	@EJB private PersistenciaLocal bd;		
	@EJB private ConverterService cc;		
	
	public String elegirNodoRandom(String f) {		
		NodosPerifericos n = NodosPerifericos.getInstancia();
		Random rand = new Random();
		if (f.equals("e")) {
			List<String> l = n.getUrisEvaluador();			
			return l.get(rand.nextInt(l.size()));			
		}
		else if (f.equals("o")) {
			List<String> l = n.getUrisOrganizacion();
			return l.get(rand.nextInt(l.size()));
		}
		return null;
	}	
		
	private String elegirNodo(String uri, String f) {
		NodosPerifericos n = NodosPerifericos.getInstancia();	
		if (f.equals("e")) {
			List<String> l = n.getUrisEvaluador();			
			if (l.contains(uri))
				return uri;
		}
		else if (f.equals("o")) {
			List<String> l = n.getUrisOrganizacion();
			if (l.contains(uri))
				return uri;
		} 
		return null;
	}	
	
	public void altaEvaluador(String nombre, String uri) {
		if (nombre != null) {
			try {
				Evaluador e = new Evaluador(nombre);
				bd.altaEvaluador(e);	
				e = bd.buscarEvaluador(e.getNombre());
				JSONObject ev = cc.jsonEvaluador(e);
				String nodo = elegirNodo(uri, "e");
				altaEvaluadorNP(ev, nodo);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}		
	}
	
	public void altaOrganizacion(String nombre, String correo, String uri) {
		if (nombre != null) {
			Organizacion o = new Organizacion(nombre, correo);
			bd.altaOrganizacion(o);
			try {
				o = bd.buscarOrganizacion(correo);
				JSONObject org = cc.jsonOrganizacion(o);
				String nodo = elegirNodo(uri, "o");
				altaOrganizacionrNP(org, nodo);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}	
	
	public List<DtEvaluador> getEvaluadores() {
		return bd.listarEvaluadores();
	}

	public List<DtOrganizacion> getOrganizaciones() {
		return bd.listarOrganizaciones();
	}
			
	public void borrarEvaluador(String ev, String uri) {
		try {
			Evaluador ex = bd.buscarEvaluador(ev);
			JSONObject evl = cc.jsonEvaluador(ex);
			String nodo = elegirNodo(uri, "e");
			borrarEvaluadorNP(evl, nodo);
			bd.borrarEvaluador(ev);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
	
	public void borrarOrganizacion(String org, String uri) {
		try {
			Organizacion o = bd.buscarOrganizacion(org);
			JSONObject orgx = cc.jsonOrganizacion(o);
			String nodo = elegirNodo(uri, "o");
			borrarOrganizacionrNP(orgx, nodo);
			bd.borrarOrganizacion(org);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
	
	public DtEvaluador buscarEvaluador(String ev) throws Exception {
		return bd.buscarEvaluador(ev).getDt();
	}
	
	public DtOrganizacion buscarOrganizacion(String org) throws Exception {
		return bd.buscarOrganizacion(org).getDt();
	}
	
	private void altaEvaluadorNP(JSONObject e, String nodo) throws Exception {
		try {
			Client client = ClientBuilder.newClient();		
		  	String uriEvaluar = nodo+"/addEvaluador";
			WebTarget target = client.target(uriEvaluar);	
			Invocation invocation = target.request().buildPost(Entity.entity(e, MediaType.APPLICATION_JSON));
			invocation.invoke();
		} catch (Exception ex) {
			throw ex;
		}		
	}
	
	private void altaOrganizacionrNP(JSONObject o, String nodo) {
		try {
			Client client = ClientBuilder.newClient();		
		  	String uriEvaluar = nodo+"/addOrganizador";
			WebTarget target = client.target(uriEvaluar);				
			Invocation invocation = target.request().buildPost(Entity.entity(o, MediaType.APPLICATION_JSON));
			invocation.invoke();
		} catch (Exception ex) {
			throw ex;
		}		
	}
	
	private void borrarEvaluadorNP(JSONObject e, String nodo) {
		try {
			Client client = ClientBuilder.newClient();		
		  	String uriEvaluar = nodo+"/removeEvaluador";
			WebTarget target = client.target(uriEvaluar);	
			Invocation invocation = target.request().buildPost(Entity.entity(e, MediaType.APPLICATION_JSON));
			invocation.invoke();
		} catch (Exception ex) {
			throw ex;
		}		
	}
	
	private void borrarOrganizacionrNP(JSONObject e, String nodo) {
		try {
			Client client = ClientBuilder.newClient();		
		  	String uriEvaluar = nodo+"/removeOrganizador";
			WebTarget target = client.target(uriEvaluar);	
			Invocation invocation = target.request().buildPost(Entity.entity(e, MediaType.APPLICATION_JSON));
			invocation.invoke();
		} catch (Exception ex) {
			throw ex;
		}
	}

	public void addUriEvaluador(String uri) throws Exception {
		if (uri != null) {
			NodosPerifericos l = NodosPerifericos.getInstancia();
			try {
				l.addUriEvaluador(uri);			
				bd.altaUri(l);		
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	public void bajaUri(String uri, String f) throws Exception {
		if (uri != null) 
			try {
				bd.bajaUri(uri, f);
			} catch (Exception e) {
				throw e;
			}
	}
	
	public List<String> getUrisEvaluador() {
		NodosPerifericos n = NodosPerifericos.getInstancia();
		return n.getUrisEvaluador();
	}

	public void addUriOrganizacion(String uri) throws Exception {
		if (uri != null) {
			NodosPerifericos l = NodosPerifericos.getInstancia();
			try {
				l.addUriOrganizacion(uri);
				bd.altaUri(l);
			} catch (Exception e) {
				throw e;
			}
		}
	}	
		
	public List<String> getUrisOrganizacion() {
		NodosPerifericos n = NodosPerifericos.getInstancia();
		return n.getUrisOrganizacion();
	}	
	
	public void saveNodoCreador(String creador, String nombreIni, String nodo) {
		if (creador != null && nombreIni != null) {			
			IniciativaNodo i = new IniciativaNodo(nombreIni, creador, nodo);
			bd.persistNodoCreador(i);
		}
	}
	
	public String getNodoCreador(String creador, String nombre) throws Exception {
		if (creador != null && nombre != null) {				
			return bd.getNodoCreador(creador, nombre);
		}
		return null;
	}
}
