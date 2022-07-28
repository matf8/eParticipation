package eParticipation.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import eParticipation.backend.dto.DtOrganizacion;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Organizacion.findAll", query="select i from Organizacion i")
public class Organizacion {
	
	@Id
	private Long idOrganizacion;
	private String nombre;
	private String correo;
	
	public Organizacion() {	}

	public Organizacion(Long id, String nombre, String correo) {
		super();
		this.idOrganizacion = id;
		this.nombre = nombre;
		this.correo = correo;
	}
	
	public DtOrganizacion getDt() {
		return new DtOrganizacion(this.idOrganizacion.toString(), this.nombre, this.correo, "Organización");
	}

}
