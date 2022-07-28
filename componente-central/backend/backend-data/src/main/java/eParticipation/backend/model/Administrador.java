package eParticipation.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import eParticipation.backend.dto.DtAdministrador;
import lombok.Data;

@Data
@Entity
public class Administrador {	// si podemos usar lombok y no les carga los get and set, primer comment: https://stackoverflow.com/questions/11803948/lombok-is-not-generating-getter-and-setter
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAdmin;
	
	@Column(unique = true)
	private String cedula;
	
	@Column(nullable = false)	
	private String password;
	
	@Column(unique = true)
	private String correo;
	private String nombreCompleto; 	
	
	@Enumerated(EnumType.STRING)
	private Rol rol = Rol.Administrador;

	public Administrador() {	}

	public Administrador(String cedula, String password, String correo, String nombreCompleto) {
		super();
		this.cedula = cedula;
		this.password = password;
		this.correo = correo;
		this.nombreCompleto = nombreCompleto;
	}

	public DtAdministrador getDt() {
		return new DtAdministrador(this.idAdmin.toString(), this.cedula, this.password, this.correo, this.nombreCompleto, this.rol.toString());
	}
	
	
	
}
