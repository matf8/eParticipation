package eParticipation.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
@Entity
@Table(name="usuarios")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipoUser",discriminatorType=DiscriminatorType.STRING)
public abstract class UsuarioFrontOffice {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuarioFO;
		
	@Column(unique = true)
	private String cedula;
	
	@Column(name = "nombre")
	private String nombreCompleto;
	
	@Column(unique = true)
	private String correo;
	
	@Temporal(TemporalType.DATE)
	private Date fNac;
	
	private String nacionalidad;
	private String domicilio; 	
	
	@ElementCollection(targetClass=String.class)
	private List<String> mensajes = new ArrayList<>();

	public UsuarioFrontOffice() {	}

	public UsuarioFrontOffice(String cedula, String nombreCompleto, String correo, Date fNac, String nacionalidad,
			String domicilio, List<String> mensajes) {		
		this.cedula = cedula;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.fNac = fNac;
		this.nacionalidad = nacionalidad;
		this.domicilio = domicilio;
		this.mensajes = mensajes;
	}
	
	public void agregarMensaje(String m) {
		this.mensajes.add(m);
	}
	
	public void quitarMensaje(String m) {
		this.mensajes.remove(m);
	}	
	
	public void quitarTodos() {
		this.mensajes.removeAll(mensajes);
	}	
	
}
