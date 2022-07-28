package eParticipation.backend.dto;

import lombok.Data;

@Data
public class DtCertificado {
	
	private String nombre;
	private String insignia; // foto, logo por ahora base64 	
	private String fecha;
	
	public DtCertificado() {	}
	
	public DtCertificado(String nombre, String insignia, String fecha) {		
		this.nombre = nombre;
		this.insignia = insignia;
		this.fecha = fecha;
	}
	
	@Override
	public String toString() {
		return "DtCertificado(nombre=" + this.nombre + ", fecha=" + this.fecha + ")";
		
	}
	
	

}
