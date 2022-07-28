package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtEvaluador {
	
	private Long id;
	private String nombre;
	
	public DtEvaluador() {	}
	
	public DtEvaluador(Long id, String n) {
		this.id = id;
		this.nombre = n;
	}


}
