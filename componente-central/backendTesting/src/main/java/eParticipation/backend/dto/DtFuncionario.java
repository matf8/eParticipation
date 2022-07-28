package eParticipation.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtFuncionario {
	
	private String id;
	private String cedula;
	private String nombreCompleto;
	private String correo;
	private String fNac;
	private String nacionalidad;
	private String domicilio;	
	private String organismo;
	private String cargo;
	private String rol;
	private List<String> procesoCreado;
	
	public DtFuncionario() { }
	
	public DtFuncionario(String id, String cedula, String nombreCompleto, String correo, String fNac, String nacionalidad,
			String domicilio, String cargo, String role) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.fNac = fNac;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.cargo = cargo;
		this.rol = role;
	}
		
	public DtFuncionario(String cedula, String nombreCompleto, String correo, String fNac, String nacionalidad,
			String domicilio, String cargo, String role) {
		super();
		this.cedula = cedula;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.fNac = fNac;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.cargo = cargo;
		this.rol = role;
	}

}
