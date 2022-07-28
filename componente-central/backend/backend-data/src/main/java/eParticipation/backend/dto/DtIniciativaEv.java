package eParticipation.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class DtIniciativaEv extends DtIniciativa {
	
	private String descripcionEvaluacion;
	private boolean aceptada;
	
	public DtIniciativaEv() { }
		
	public DtIniciativaEv(String id, String nombre, String descripcion, String fecha, String creador, String estado, String r, String desc, boolean resultado) {		
		super(id, nombre, descripcion, fecha, creador, estado, r);		
		this.descripcionEvaluacion = desc;
		this.aceptada = resultado;
	}
}
