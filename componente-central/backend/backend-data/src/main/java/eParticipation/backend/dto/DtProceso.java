package eParticipation.backend.dto;

import java.util.List;
import lombok.Data;

@Data
public class DtProceso {
	
	private String id;
	private String nombre;		
	private String fecha;	
	private String descripcionAlcance; 	
	private String fase;
	private String creador;
	private List<String> participantes; 
	private List<String> seguidores; 
	private List<String> comentarios; 
	private String instrumento;
	private String[] contenidoInstrumento;
	
	public DtProceso() { }
	
	public DtProceso(String id, String nombre, String fecha, String descripcionAlcance, String fase, String creador, List<String> k, List<String> w, List<String> z) {
		super();
		this.id = id;		
		this.nombre = nombre;
		this.fecha = fecha;
		this.descripcionAlcance = descripcionAlcance;
		this.fase = fase;
		this.creador = creador;	
		this.participantes = k;
		this.seguidores = w;
		this.comentarios = z;		
	}
	
	public DtProceso(String nombre, String fecha, String descripcionAlcance, String fase, String creador) {
		super();	
		this.nombre = nombre;
		this.fecha = fecha;
		this.descripcionAlcance = descripcionAlcance;
		this.fase = fase;
		this.creador = creador;
	}	
	
	public void agregarComentario(String c) {
		comentarios.add(c);
	}
	
	public void quitarComentario(String c) {
		comentarios.remove(c);
	}

}
