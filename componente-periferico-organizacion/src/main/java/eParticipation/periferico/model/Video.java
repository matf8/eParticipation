package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Video extends Recurso {
	
	private String url;
	private Float size;
	
	public Video() {	}
}
