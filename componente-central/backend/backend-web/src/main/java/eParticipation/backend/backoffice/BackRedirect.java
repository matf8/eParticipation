package eParticipation.backend.backoffice;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lombok.Data;

@Named("cr")
@SessionScoped
@Data
public class BackRedirect implements Serializable {		
	private static final long serialVersionUID = 1L;
		
	private String abm = "";
	
	public BackRedirect() {	}
	
	public String redirectIndex()  {	
		setAbm("");
		return "/index?faces-redirect=true";
	}	 
	
	public String redirectAltas() {
		setAbm("a");
		return "/gestionUser?faces-redirect=true";		
	}
	
	public String redirectBusqueda() {
		setAbm("b");
		return "/gestionUser?faces-redirect=true";
	}
	
	public String redirectReportes() {		
		return "/reportes?faces-redirect=true";
	}
	
	public String redirectProcesar() {	
		return "/procesar?faces-redirect=true";
	}
	
	public String redirectInsigniaVisor() {
		setAbm("v");			
		return "/gestionInsignia?faces-redirect=true";
	}
	
	public String redirectInsigniaGestor() {
		setAbm("g");
		return "/gestionInsignia?faces-redirect=true";
	}	
	
}
