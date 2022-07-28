package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Documento extends Recurso {
	
	private String texto;
	private Float size;
	private String formato; //.doc, .pdf, .txt 	

	public Documento() {	}
}
