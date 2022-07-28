package eParticipation.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Mensaje {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idMensaje;
	private String contenido;
	
	@ManyToOne
	private UsuarioFrontOffice destinatario;

	public Mensaje() {	}

}
