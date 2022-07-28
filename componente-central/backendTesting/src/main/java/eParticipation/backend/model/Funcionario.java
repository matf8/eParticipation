package eParticipation.backend.model;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import eParticipation.backend.dto.DtFuncionario;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("F")
public class Funcionario extends UsuarioFrontOffice {

	private String cargo;

	@Enumerated(EnumType.STRING)
	private static Rol role = Rol.Funcionario;

	@ManyToOne
	private Organismo oficina;

	public Funcionario() {	}

	public Funcionario(Long id, String cedula, String nombre, String correo, Date fNac, String nacionalidad, String domicilio,
			List<String> m, String cargo, Organismo o) {
		super(id, cedula, nombre, correo, fNac, nacionalidad, domicilio, m);
		this.cargo = cargo;
		this.oficina = o;
	}
	
	public DtFuncionario getDt() {
		return new DtFuncionario(getIdUsuarioFO().toString(), getCedula(), getNombreCompleto(), getCorreo(), getFNac().toString(), getNacionalidad(),
				getDomicilio(), this.cargo, Funcionario.role.toString());
	}
	
	@Override
	public String toString() {
		return "Funcionario(cedula=" + this.getCedula() + ", nombre=" + this.getNombreCompleto() + ", correo=" + this.getCorreo() +
				", nacimiento=" + this.getFNac().toString() + ", nacionalidad=" + this.getNacionalidad() + ", domicilio=" + this.getDomicilio() +
				", mensajes=" + this.getMensajes() + ", cargo=" + this.cargo + ", organismo=" + oficina.getNombre() + ")";
		
	}

}
