package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtMensaje {
	
	private String contenido;	
	private String destinatario;
	
	public DtMensaje() {	}

	public DtMensaje(String contenido, String destinatario) {
		super();
		this.contenido = contenido;
		this.destinatario = destinatario;
	}	

}
