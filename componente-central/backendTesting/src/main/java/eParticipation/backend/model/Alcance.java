package eParticipation.backend.model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Data
@Entity
@IdClass(AlcanceId.class)
public class Alcance {
	@Id
	private String idCiudadano;
	@Id
	private String idProceso;
	
	@Enumerated(EnumType.STRING)	
	private TipoAlcance tipo;
	
	public static TipoAlcance consultarAlcance(Integer edadMinima, Integer edadUsuario) {
		TipoAlcance ret = TipoAlcance.Fuera_de_Rango;
		if (edadUsuario >= edadMinima) 
			ret = TipoAlcance.Disponible;
		return ret;		
	}
	
	public static TipoAlcance consultarAlcance(String[] departamentoDisponible, String departamentoUsuario) {
		TipoAlcance ret = TipoAlcance.Fuera_de_Rango;	
		if (Arrays.asList(departamentoDisponible).contains(departamentoUsuario)) 
			ret = TipoAlcance.Disponible;
		return ret;		
	}
	
}
