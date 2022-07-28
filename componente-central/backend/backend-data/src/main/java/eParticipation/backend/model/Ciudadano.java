package eParticipation.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import eParticipation.backend.dto.DtCiudadano;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("C")
public class Ciudadano extends UsuarioFrontOffice {

	@Enumerated(EnumType.STRING)
	private static Rol role = Rol.Ciudadano;

	@ManyToMany(cascade = CascadeType.ALL)	
	private List<Certificado> certificados = new ArrayList<>();

	@OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
	private List<CfgNoti> notificaciones = new ArrayList<>();
		
	public Ciudadano() {
		super();
	}

	public Ciudadano(String cedula, String nombre, String correo, Date fNac, String nacionalidad, String domicilio,
			List<String> m, List<Certificado> certificados, List<CfgNoti> notificaciones) {
		super(cedula, nombre, correo, fNac, nacionalidad, domicilio, m);
		this.certificados = certificados;
		this.notificaciones = notificaciones;
	}
	
	public void agregarCertificado(Certificado c) {
		this.certificados.add(c);
	}
	
	public void quitarCertificado(Certificado c) {
		this.certificados.remove(c);
	}
	
	public DtCiudadano getDt() {
		return new DtCiudadano(getIdUsuarioFO().toString(), getCedula(), getNombreCompleto(), getCorreo(), getFNac().toString(), getNacionalidad(),
				getDomicilio(), Ciudadano.role.toString());
	}	

	@Override
	public String toString() {
		return "Ciudadano(cedula=" + this.getCedula() + ", nombre=" + this.getNombreCompleto() + ", correo=" + this.getCorreo() +
				", nacimiento=" + this.getFNac().toString() + ", nacionalidad=" + this.getNacionalidad() + ", domicilio=" + this.getDomicilio() +
				", mensajes=" + this.getMensajes().toString() + ", certificados=" + this.certificados + ", notificaciones=" + this.notificaciones+ ")";
		
	}

	
	
}
