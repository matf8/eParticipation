package eParticipation.backend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import eParticipation.backend.dto.DtProceso;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Proceso.findAll", query="select i from ProcesoParticipativo i")
public class ProcesoParticipativo {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProceso;
	
	@Column(nullable=false)
	private String nombre;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;	
	
	private String descripcionAlcance; // edad minima, departamentos
	
	@ElementCollection(targetClass=String.class)
	private List<String> idInstrumento = new ArrayList<>();
	
	@ElementCollection(targetClass=String.class)
	private List<String> idComentario = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinTable(name = "proParticipantes", joinColumns = { @JoinColumn(name="proceso_id") },
    inverseJoinColumns = { @JoinColumn(name = "id_usuariofo") } )
	private List<Ciudadano> participantes = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	@JoinTable(name = "proSeguidores", joinColumns = { @JoinColumn(name="proceso_id") },
    inverseJoinColumns = { @JoinColumn(name = "id_usuariofo") } )
	private List<Ciudadano> seguidores = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private FaseProceso fase;
	
	private String creador;
	
	public ProcesoParticipativo() {	}

	public ProcesoParticipativo(String nombre, Date fecha, String descripcionAlcance, List<String> instrumentos, String fase, String creador) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.descripcionAlcance = descripcionAlcance;
		this.idInstrumento = instrumentos;
		this.fase = FaseProceso.valueOf(fase);
		this.creador = creador;
	}
	
	public ProcesoParticipativo(String nombre, Date fecha, String descripcionAlcance, List<String> instrumentos, FaseProceso fase, String creador) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.descripcionAlcance = descripcionAlcance;
		this.idInstrumento = instrumentos;
		this.fase = fase;
		this.creador = creador;
	}

	public DtProceso getDt() {
		List<String> a = new ArrayList<>();
		for (Ciudadano c: participantes)
			a.add(c.getCorreo());	
		List<String> k = new ArrayList<>();
		for (Ciudadano c: seguidores)
			k.add(c.getCorreo());	
		List<String> cms = new ArrayList<>();	
		return new DtProceso(this.idProceso.toString(), this.nombre, this.fecha.toString(), this.descripcionAlcance, this.fase.toString(), this.creador, a, k, cms);
	}
	
	public void agregarUsuarioParticipante(Ciudadano c) { 	
		participantes.add(c);	
	}	
	
	public void quitarUsuarioParticipante(Ciudadano c) {
		participantes.remove(c);			
	}		
	
	public void agregarInstrumento(String c) { 	
		idInstrumento.add(c);	
	}	
	
	public void quitarInstrumento(String c) {
		idInstrumento.remove(c);			
	}		

	public void agregarComentario(String c) { 	
		idComentario.add(c);	
	}	
	
	public void quitarComentario(String c) {
		idComentario.remove(c);			
	}
	
	public void agregarSeguidor(Ciudadano c) { 	
		seguidores.add(c);	
	}	
	
	public void quitarSeguidor(Ciudadano c) {
		seguidores.remove(c);	
	}

	public void removeIns() {
		idInstrumento.removeAll(idInstrumento);
	}

	public void removeP() {
		participantes.removeAll(participantes);
		seguidores.removeAll(seguidores);
	}	
	
}
