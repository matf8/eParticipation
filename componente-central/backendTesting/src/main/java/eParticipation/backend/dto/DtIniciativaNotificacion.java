package eParticipation.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DtIniciativaNotificacion extends DtNotificacion {
	private String estado;
	private String nombre;
	private String descripcion;
}
