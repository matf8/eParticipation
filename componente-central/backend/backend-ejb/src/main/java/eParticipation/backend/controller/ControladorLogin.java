package eParticipation.backend.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eParticipation.backend.service.LoginService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.UsuarioService;
import eParticipation.backend.service.ConverterService;
import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Autoridad;

@Stateless
public class ControladorLogin implements LoginService {

	@EJB private PersistenciaLocal bd;		
	@EJB private UsuarioService ac;
	@EJB private ConverterService cc;

	public ControladorLogin() { }
	
	public Object login(String u, String pass) throws Exception {		
		Object o = null;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		try {
			o = bd.buscarUsuario(u);			
			if (o != null) {
				if (o instanceof Administrador) {					
					DtAdministrador user = ((Administrador) o).getDt();
					if (encoder.matches(pass, user.getPassword())) {
						String token = ac.crearToken(user.getCedula(),user.getRol());
						user.setToken(token);
						return cc.jsonAdmin(user);
					} else
						throw new Exception("Credenciales incorrectas");
				} else if (o instanceof Autoridad) {
					DtAutoridad user = ((Autoridad) o).getDt();				
					if (encoder.matches(pass, ((DtAutoridad) user).getPassword())) {
						String token = ac.crearToken(((DtAutoridad) user).getCedula(), ((DtAutoridad) user).getRol());
						((DtAutoridad) user).setToken(token);
						return cc.jsonAutoridad((DtAutoridad) user);
					} else {						
						throw new Exception("Credenciales incorrectas");
					}
				}
			} 
		} catch (Exception e) {			
			throw e;
		}
		return null;
	}

}
