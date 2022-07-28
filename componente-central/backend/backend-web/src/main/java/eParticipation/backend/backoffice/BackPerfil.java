package eParticipation.backend.backoffice;

import java.io.Serializable;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.JsonObject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.service.UsuarioService;

import lombok.Data;

@Named("cp")
@SessionScoped
@Data
public class BackPerfil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UsuarioService u;
	
	private boolean renderPass = false;	
	private DtAdministrador adm;
	private DtAutoridad aut;	
	private String pass1;
	private String pass2;
	private String pass3;
			
	public BackPerfil() {	}
		
	public String redirectPerfil() {
		return "/perfilBack?faces-redirect=true";
	}
	
	public String redirectIndex()  {
		setRenderPass(false);
		setPass1(null);
		setPass2(null);
		setPass3(null);		
		return "/index?faces-redirect=true";
	}
		
	public void verificarSesion() throws Exception {		
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			JsonObject user = (JsonObject) ctx.getExternalContext().getSessionMap().get("usuarioLogged");				
			if (user == null) {
				String token = (String) ctx.getExternalContext().getSessionMap().get("usuarioToken");
				if (token == null)				
					ctx.getExternalContext().redirect("permisos.xhtml");				
			}	
			else {
				String token = user.getString("token");				
				String[] partesToken = token.split("\\.");			
				Base64.Decoder decoder = Base64.getUrlDecoder();
				String rol = new String(decoder.decode(partesToken[1])); // me traigo el subject usuario
				
				// doble parse para quedarme con "autoridad" o "administrador" xd a lo zabalza
				
				partesToken = rol.split("\\:");								
				rol = partesToken[3];	// se queda con la parte rol:...				
				
				partesToken = rol.split("\"");		
				rol = partesToken[1];	// me sirve sin comillas y sin llave }	
				if (rol.equals("Administrador")) 			
					this.adm = new DtAdministrador(user.getString("cedula"), user.getString("password"), user.getString("correo"), user.getString("nombre"), "Administrador");
				else if (rol.equals("Autoridad"))
					this.aut = new DtAutoridad(user.getString("cedula"), user.getString("password"), user.getString("correo"), user.getString("nombre"), "Autoridad");
				// remuevo el json completo y dejo el token guardado en la sesion; lo de arriba es para cuando quiere editar su perfil le aparezcan sus datos.
				ctx.getExternalContext().getSessionMap().remove("usuarioLogged"); 
				cambiarAToken(token);				
			}
		} catch (Exception e) { 
			throw e;
		}
	}
	
	public void modificarPassword() {
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (!pass3.equals(pass2)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseñas no coinciden", ""));	
				setPass1(null);
				setPass2(null);
				setPass3(null);	
			} else {		
				if (this.adm != null) {
					if (!encoder.matches(pass1, adm.getPassword())) 	
						ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña antigua incorrecta", ""));					
					 else {
						PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
						u.updatePassword(adm.getCedula(), adm.getRol(), passwordEncoder.encode(pass3));
						ctx.addMessage(null, new FacesMessage("Contraseña cambiada"));
						toggleRenderPass();
					}
				    setPass1(null);
					setPass2(null);
					setPass3(null);
				} else if (this.aut != null) {		
					System.out.println(aut.toString());
					DtAutoridad dt = (DtAutoridad) u.buscarUsuario(aut.getCedula());
					String k = null;
					if (dt != null)
						k = dt.getPassword();
					else k = aut.getPassword();
					System.out.println(k);
					if (!encoder.matches(pass1, k))		
						ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña antigua incorrecta", ""));						
					else {
						PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
						u.updatePassword(aut.getCedula(), aut.getRol(), passwordEncoder.encode(pass3));
						ctx.addMessage(null, new FacesMessage("Contraseña cambiada"));
						toggleRenderPass();						
					}
				}
			}
		} catch (Exception e) {	
					
		}
		setPass1(null);
		setPass2(null);
		setPass3(null);		
	}			

	
	public void toggleRenderPass() {
		setRenderPass(!this.renderPass);
		
	}	
	
	public void cambiarAToken(String token) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioToken", token);	
	}
	
}
