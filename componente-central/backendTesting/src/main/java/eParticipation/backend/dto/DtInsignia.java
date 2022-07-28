package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtInsignia {
		
	private String logo;
	private String fechaObtenido;
	
	public DtInsignia() {	}

	public DtInsignia(String logo, String fechaObtenido) {		
		this.logo = logo;
		this.fechaObtenido = fechaObtenido;
	}	
	

}
