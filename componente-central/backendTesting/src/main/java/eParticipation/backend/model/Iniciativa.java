package eParticipation.backend.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import eParticipation.backend.dto.DtIniciativa;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Iniciativa.findAll", query="select i from Iniciativa i")
public class Iniciativa {
	
	@Id
	private Long idIniciativa;
	
	private String nombre;
		
	private String descripcion;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	private String creador;
	
	@ElementCollection(targetClass=String.class)
	private List<String> idRecurso = new ArrayList<>();
	
	@ElementCollection(targetClass=String.class)
	private List<String> idComentario = new ArrayList<>();	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinTable(name = "iniAdheridos", joinColumns = { @JoinColumn(name="iniciativa_id") },
    inverseJoinColumns = { @JoinColumn(name = "id_usuariofo") } )
	private List<Ciudadano> adheridos = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinTable(name = "iniSeguidores", joinColumns = { @JoinColumn(name="iniciativa_id") },
    inverseJoinColumns = { @JoinColumn(name = "id_usuariofo") } )
	private List<Ciudadano> seguidores = new ArrayList<>();

	public Iniciativa() {	}

	public Iniciativa(Long id, String nombre, String descripcion, Date fecha, String creador, Estado estado) {
		this.idIniciativa = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;
		this.creador = creador;
	}
	
	public Iniciativa(String nombre, String descripcion, Date fecha, String creador, Estado estado, List<String> recursos) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;		
		this.idRecurso = recursos;
		this.creador = creador;
	}
	
	public Iniciativa(DtIniciativa d) {			
		try {
			Date f = new SimpleDateFormat("dd/MM/yyyy").parse("10/02/1996");
			this.nombre = d.getNombre();
			this.fecha = f;
			this.estado = Estado.valueOf(d.getEstado());
			this.creador = d.getCreador();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	public void agregarUsuarioAdherido(Ciudadano c) { 	
		adheridos.add(c);	
		seguidores.add(c);	
	}	
	
	public void quitarUsuarioAdherido(Ciudadano c) {
		adheridos.remove(c);	
		seguidores.remove(c);			
	}
	
	public void agregarUsuarioSeguidor(Ciudadano c) { 	
		seguidores.add(c);	
	}	
	
	public void quitarUsuarioSeguidor(Ciudadano c) {
		seguidores.remove(c);			
	}		
	
	public void agregarRecurso(String id) {
		idRecurso.add(id);
	}
	
	public void quitarRecurso(String id) {
		idRecurso.remove(id);
	}
	
	public void agregarComentario(String id) {	
		idComentario.add(id);
	}
	
	public void quitarComentario(String id) {
		idComentario.remove(id);
	}
		
	public DtIniciativa getDt() {
		List<String> a = new ArrayList<>();
		for (Ciudadano c: adheridos)
			a.add(c.getCorreo());
		List<String> s = new ArrayList<>();
		for (Ciudadano c: seguidores)
			s.add(c.getCorreo());	
		String r = null;
		List<String> c = new ArrayList<>();		
		if (idRecurso.size() > 0)
			r = idRecurso.get(0);
		return new DtIniciativa(this.idIniciativa.toString(), this.nombre, this.descripcion, this.fecha.toString(), this.estado.toString(), this.creador, r, a, s, c);	
	}
	
}
