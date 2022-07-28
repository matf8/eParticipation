package eParticipation.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class CfgNoti {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCfgNoti;
	
	private String textoNoti;
	
	@ManyToOne
	private Ciudadano destinatario;			

	public CfgNoti() {	}

	public CfgNoti(Long idCfgNoti, String textoNoti, Ciudadano destinatario) {
		super();
		this.idCfgNoti = idCfgNoti;
		this.textoNoti = textoNoti;
		this.destinatario = destinatario;
	}

}
