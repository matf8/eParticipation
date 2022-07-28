package eParticipation.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtCiudadano {
	
	private String id;
	private String cedula;
	private String nombreCompleto;
	private String correo;
	private String fNac;
	private String nacionalidad;
	private String domicilio;
	private String rol;
	private List<String> iniSeguidas;
	private List<String> iniAdheridas;
	private List<String> iniCreadas;
	private List<String> certificados;
	private List<String> proPart;

	public DtCiudadano() {	}

	public DtCiudadano(String id, String cedula, String nombreCompleto, String correo, String fNac, String nacionalidad,
			String domicilio, String role) {
		this.id = id;
		this.cedula = cedula;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.fNac = fNac;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.rol = role;

	}
	
	public DtCiudadano(String cedula, String nombreCompleto, String correo, String fNac, String nacionalidad,
			String domicilio, String role) {
		this.cedula = cedula;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.fNac = fNac;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.rol = role;

	}


}
