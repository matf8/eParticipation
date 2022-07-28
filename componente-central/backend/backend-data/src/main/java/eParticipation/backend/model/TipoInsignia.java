package eParticipation.backend.model;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eParticipation.backend.dto.DtInsignia;
import lombok.Data;

@Data
@Entity
public class TipoInsignia {	
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idInsignia;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] logo;
	
	@Temporal(TemporalType.DATE)
	private Date fechaObtenido;
	
	public TipoInsignia() {	}
	
	public TipoInsignia(byte[] logo, Date fechaObtenido) {		
		
		this.logo = logo;		
		this.fechaObtenido = fechaObtenido;
	}

	public DtInsignia getDt() throws UnsupportedEncodingException {
		return new DtInsignia(""/*new String(this.logo, "IBM01140")*/, this.fechaObtenido.toString());
	}
	
	@Override
	public String toString() {
		return "TipoInsignia(idInsignia=" + this.idInsignia + ", logo=" + this.logo.toString() + ", fecha=" + this.fechaObtenido + ")";
		
	}
}
