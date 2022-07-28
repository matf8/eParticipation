package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Iniciativa {	
	
	private String nombre;
	private String descripcion;
	private String fecha;	
	private String creador;
	private String recurso;
	
	public Iniciativa() { }

	public Iniciativa(String nombre, String descripcion, String fecha, String creador, String recurso) {		
		this.nombre = nombre;		
		this.descripcion = descripcion;
		this.fecha = fecha;		
		this.creador = creador;
		this.recurso = recurso;
	}
	
}
