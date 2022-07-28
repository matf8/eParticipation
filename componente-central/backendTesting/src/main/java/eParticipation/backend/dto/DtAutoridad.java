package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtAutoridad {
	
	private String id;
	private String cedula;
	private String password;
	private String correo;
	private String nombreCompleto; 	
	private String rol;
	private String token;
	
	public DtAutoridad() {	}
 
	public DtAutoridad(String id, String cedula, String password, String correo, String nombreCompleto, String rol) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.password = password;
		this.correo = correo;
		this.nombreCompleto = nombreCompleto;
		this.rol = rol;
	}	
	
	public DtAutoridad(String cedula, String password, String correo, String nombreCompleto, String rol) {
		super();		
		this.cedula = cedula;
		this.password = password;
		this.correo = correo;
		this.nombreCompleto = nombreCompleto;
		this.rol = rol;
	}	
	
}

	

