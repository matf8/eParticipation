package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Organizacion {
	
	private String nombre;
	
	public Organizacion() { }
	
	public Organizacion(String nombre) {	
		this.nombre = nombre;
	}


}
