package eParticipation.periferico.model;

import lombok.Data;

@Data
public class Iniciativa {	
	
	private String id;
	private String nombre;
	private String descripcion;
	private String fecha;	
	private String creador;	
	private String estado;
	private String recurso;
	
	public Iniciativa() { }

	public Iniciativa(String id, String nombre, String descripcion, String fecha, String c, String e, String r) {
		this.id = id;
		this.nombre = nombre;		
		this.descripcion = descripcion;
		this.fecha = fecha;		
		this.creador = c;
		this.estado = e;
		this.recurso = r;
	}
	
}
