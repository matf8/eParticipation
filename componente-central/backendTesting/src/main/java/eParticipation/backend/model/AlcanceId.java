package eParticipation.backend.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class AlcanceId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String idCiudadano;
	private String idProceso;	

}
