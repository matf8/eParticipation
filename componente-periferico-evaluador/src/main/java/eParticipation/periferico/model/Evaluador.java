package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Evaluador {
	
	Long id;
	String nombre;
	
	public Evaluador() {	}

	public Evaluador(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	
}

