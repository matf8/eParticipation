package eParticipation.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Data;

@Data
@Entity
public class IniciativaNodo {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idIniciativaNodo;
	private String nombreIniciativa;
	private String creador;
	private String nodo;
	
	public IniciativaNodo() { }
	
	public IniciativaNodo(String n, String cr, String nd) {
		this.nombreIniciativa = n;
		this.creador = cr;
		this.nodo = nd;
	}	

}
