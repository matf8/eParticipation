package eParticipation.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import eParticipation.backend.dto.DtEvaluador;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Evaluador.findAll", query="select i from Evaluador i")
public class Evaluador {
	
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEvaluador;
	
	@Column(unique = true)	
	private String nombre;
	
	public Evaluador() {	}
	
	public Evaluador(String n) {
		this.nombre = n;
	}
	
	public DtEvaluador getDt() {
		return new DtEvaluador(idEvaluador, nombre);
	}
}
