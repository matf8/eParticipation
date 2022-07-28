package eParticipation.periferico.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class DtEvaluacion {	
	private String descripcion;
	private boolean resultado;
	
	public DtEvaluacion() {	}
		
	public DtEvaluacion(String desc, boolean resultado) {		
		this.descripcion = desc;
		this.resultado = resultado;
	}
	

}
