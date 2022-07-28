package eParticipation.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import eParticipation.backend.dto.DtAutoridad;
import lombok.Data;

@Data
@Entity
public class Autoridad {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAutoridad;	
	
	@Column(unique = false)		
	private String cedula;
	
	@Column(nullable = false)
	private String password;
	
	@Column(unique = true)
	private String correo;
	
	private String nombreCompleto; 	
	
	@Enumerated(EnumType.STRING)
	private Rol rol = Rol.Autoridad;

	public Autoridad() {	}

	public Autoridad(String cedula, String password, String correo, String nombreCompleto) {
		super();
		this.cedula = cedula;
		this.password = password;
		this.correo = correo;
		this.nombreCompleto = nombreCompleto;
	}
	
	public DtAutoridad getDt() {
		return new DtAutoridad(this.idAutoridad.toString(), this.cedula, this.password, this.correo, this.nombreCompleto, this.rol.toString());
	}
	
}
