package eParticipation.backend.model;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import eParticipation.backend.dto.DtCertificado;
import lombok.Data;

@Data
@Entity
@NamedQuery(name="Certificado.findAll", query="select i from Certificado i")
public class Certificado {
	
	@Id @GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long idCertificado;
	
	private String nombre;
	
	@OneToOne(cascade = CascadeType.ALL)
	private TipoInsignia insignia;
		
	private Integer nivel;
	
	private String tipo;
		
	public Certificado() {	}

	public Certificado(String nombre, TipoInsignia insignia, Integer nivel, String tipo) {
		super();
		this.nombre = nombre;
		this.insignia = insignia;
		this.nivel = nivel;
		this.tipo = tipo;
	}
	
	public DtCertificado getDt() throws UnsupportedEncodingException {					
		return new DtCertificado(this.nombre, new String(Base64.getEncoder().encode(this.insignia.getLogo())), this.insignia.getFechaObtenido().toString());
	}
		
}
