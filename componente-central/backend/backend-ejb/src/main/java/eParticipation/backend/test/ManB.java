package eParticipation.backend.test;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Autoridad;

@Named("starting")
@SessionScoped
public class ManB implements Serializable { 		
	private static final long serialVersionUID = 1L;
	
	private Administrador adm;
	private Autoridad aut;	
	
	private String message;
	
	private boolean showin = false;
	private boolean logRender = true;
		
	public ManB() {
		message = "Chau! Ponete a programar 'deployment elastic - hook ci/cd?'";		
		this.adm = new Administrador();
		this.aut = new Autoridad();
	}	
	
	public String getBienvenida() {
		return this.message;
	}	
	
	public Administrador getAdm() {
		return adm;
	}
	
	public void setAdm(Administrador adm) {
		this.adm = adm;
	}
	
	public Autoridad getAut() {
		return aut;
	}
	
	public void setAut(Autoridad aut) {
		this.aut = aut;
	}

	public boolean getShowin() {
		return showin;
	}

	public void setShowin(boolean showin) {
		this.showin = showin;
	}
	
	public boolean isLogRender() {
		return logRender;
	}

	public void setLogRender(boolean logRender) {
		this.logRender = logRender;
	}
	
	public void toggleShow() {
		setShowin(!this.showin);	
	}	
	
	
	
	
	
}