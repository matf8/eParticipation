package eParticipation.periferico.model;

import lombok.Data;

@Data
public abstract class Recurso {
			
	private Iniciativa iniciativaConRecurso;
	
	public Recurso() {	}

}
