package eParticipation.backend.backoffice;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.faces.application.FacesMessage;
import eParticipation.backend.service.LoginService;
import lombok.Data;

@Named("cl")
@SessionScoped
@Data
public class BackLogin implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB
	private LoginService loginService;
	
	private String perfil = "pub";
	
	private boolean logRender = true;
	
	private String user, pass;	
	 	
	public BackLogin() {	}
	
	public void toggleLogRender() {
		setLogRender(!this.logRender);
	}	
		
	public void login() {
        JsonObject a = Json.createObjectBuilder().build();
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {			
			a = (JsonObject) loginService.login(user, pass);
			if (a != null) {	
				String rol = a.getString("rol");
				if (rol.equals("Administrador")) {		
					ctx.getExternalContext().getSessionMap().put("usuarioLogged", a);
					ctx.addMessage(null, new FacesMessage("Bienvenido " +  a.getString("nombre")));
					toggleLogRender();
					setPerfil("adm");
				} else if (rol.equals("Autoridad")) {				
					ctx.getExternalContext().getSessionMap().put("usuarioLogged", a);
					ctx.addMessage(null, new FacesMessage("Bienvenido " +  a.getString("nombre")));
					toggleLogRender();
					setPerfil("aut");
				}
			} 
		}
		catch (Exception e) {	        
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), "Error"));
		}			
		
	}

	public void logoff() {	
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioLogged");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cya next time"));
		toggleLogRender();
		setPerfil("pub");
	}
		
}
