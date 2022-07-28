package eParticipation.backend.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DtEstadisticas implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private Integer cantCiudadanos, cantFuncionarios, cantAdministradores, cantAutoridades;
	private Integer cantIniciativas, cantProcesos;
	private Integer cantAdhesionesMayo, cantAdhesionesJunio;
	
	public DtEstadisticas() {	}
	
}
