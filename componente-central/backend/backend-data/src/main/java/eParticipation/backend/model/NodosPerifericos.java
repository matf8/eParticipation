package eParticipation.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import eParticipation.backend.excepciones.PerifericoException;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Nodos.findAll", query="select i from NodosPerifericos i")
public class NodosPerifericos {	
	
	private static NodosPerifericos instancia = null;	
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNodosPerifericos;	
	
	@ElementCollection(targetClass=String.class)
	private List<String> urisEvaluador = new ArrayList<>();
	
	@ElementCollection(targetClass=String.class)
	private List<String> urisOrganizacion = new ArrayList<>();

	public static NodosPerifericos getInstancia() {
		if (instancia == null)
			instancia = new NodosPerifericos();
		return instancia;
	}			
	
	public NodosPerifericos() {	
		this.urisEvaluador.add("https://eparticipation-evaluador.herokuapp.com");
		this.urisEvaluador.add("https://eparticipation-evaluador-2.herokuapp.com");
		this.urisEvaluador.add("http://localhost:8786");
		
		this.urisOrganizacion.add("https://eparticipation-organizacion.herokuapp.com");
		this.urisOrganizacion.add("https://eparticipation-organizador-2.herokuapp.com");
		this.urisOrganizacion.add("http://localhost:8785");
		
	}
	
	public void addUriEvaluador(String u) throws Exception {
		if (!urisEvaluador.contains(u))
			this.urisEvaluador.add(u);
		else throw new PerifericoException("URI ya existe"); 
	}
	
	public void removeUriEvaluador(String u) throws Exception {
		if (urisEvaluador.contains(u))
			this.urisEvaluador.remove(u);
		else throw new PerifericoException("URI no existía"); 	
	}

	public void addUriOrganizacion(String u) throws Exception {
		if (!urisOrganizacion.contains(u))
			this.urisOrganizacion.add(u);
		else throw new PerifericoException("URI ya existe"); 	
	}
	
	public void removeUriOrganizacion(String u) throws Exception {
		if (urisOrganizacion.contains(u))
			this.urisOrganizacion.remove(u);
		else throw new PerifericoException("URI no existía"); 	
	}
		
}
