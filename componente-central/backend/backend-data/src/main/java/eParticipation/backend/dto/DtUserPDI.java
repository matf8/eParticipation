package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtUserPDI {
	
	private String cedula;
	private String nombre;
	private String nacionalidad;
	private String domicilio;
	private String fNac;
	
	public DtUserPDI() {	}

	public DtUserPDI(String cedula, String nombre, String nacionalidad, String domicilio, String fNac) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.fNac = fNac;
	}
	

}
