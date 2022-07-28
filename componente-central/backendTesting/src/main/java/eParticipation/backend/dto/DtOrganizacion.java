package eParticipation.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtOrganizacion {
	
	private String cedula;
	private String nombreCompleto;
	private String correo;	
	private String rol;
	private List<String> iniciativaCreada;
	
	public DtOrganizacion() {	}

	public DtOrganizacion(String cedula, String nombre, String correo, String rol) {		
		this.nombreCompleto = nombre;
		this.correo = correo;	
		this.rol = rol;
		this.cedula = cedula;
	}

	public void agregarIniciativaCreada(String nombre) {
		this.iniciativaCreada.add(nombre);
	}

	public void quitarIniciativaCreada(String nombre) {
		this.iniciativaCreada.remove(nombre);
	}	
}
