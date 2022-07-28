package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Imagen extends Recurso {
	
	private String contenido; // base64
	private Float size;
	private String formato; // .jpg, .jpeg, .png
	
	public Imagen() {	}
	

}
