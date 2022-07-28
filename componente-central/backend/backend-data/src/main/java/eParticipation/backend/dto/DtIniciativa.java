package eParticipation.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtIniciativa {
	
	private String id;
	private String nombre;
	private String descripcion;
	private String fecha;	
	private String estado;	
	private String creador;
	private List<String> adheridos;
	private List<String> seguidores;
	private List<String> comentarios;
	private String recurso;
	
	private boolean pushNotify = true;
	
	public DtIniciativa() {	}
	
	public DtIniciativa(String id, String nombre, String descripcion, String fecha, String creador, String r) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;	
		this.creador = creador;
		this.recurso = r;
	}

	public DtIniciativa(String id, String nombre, String descripcion, String fecha, String estado, String creador, String r) {	
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;
		this.creador = creador;
		this.recurso = r;
	}
	
	public DtIniciativa(String id, String nombre, String descripcion, String fecha, String estado, String creador, String r, List<String> a, List<String> s, List<String> c) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;
		this.creador = creador;
		this.adheridos = a;
		this.seguidores = s;
		this.recurso = r;
		this.comentarios = c;
	}
	
	public void agregarComentario(String c) {
		comentarios.add(c);
	}
	
	public void quitarComentario(String c) {
		comentarios.remove(c);
	}
	
	@Override
	public String toString() {
		return "DtIniciativa(nombre=" + this.getNombre() + ", descripcion=" + this.getDescripcion() + ", estado=" + this.getEstado() +
				", fecha=" + this.getFecha().toString() + ", recurso=" + this.recurso + ", creador=" + this.getCreador() + ", adheridos=" + this.getAdheridos() +
				", seguidores=" + this.getSeguidores() + ")";
		
	}


}
