package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtRecursoNSQL {
	private int id;
	private String nombre;
	private String iniciativa;
	
	public DtRecursoNSQL() { 	}
	
	public DtRecursoNSQL(int id, String nombre, String iniciativa) {
		this.id=id;
		this.nombre=nombre;
		this.iniciativa=iniciativa;
		
	}

}
