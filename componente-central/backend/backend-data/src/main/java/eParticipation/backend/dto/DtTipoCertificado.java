package eParticipation.backend.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtTipoCertificado implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String tipo;
	private int momento;
	
	

}
