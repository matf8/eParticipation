package eParticipation.backend.ws;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import eParticipation.backend.service.ConverterService;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.service.UsuarioService;
import eParticipation.backend.helpers.TokenSecurity;

@RequestScoped
@Path("/externalAuth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class LoginFacebook {
	
	@EJB private UsuarioService uc;
	@EJB private ConverterService cc;
		
	@POST
	@Path("/facebook")
    public Response doFacebook(JsonObject r) {
		System.out.println(r.toString());
		String correo = null;
		try {
			correo = r.getString("mail");				
		} catch (Exception e) {
			correo = r.getString("id");	
		}
		JSONObject dtc = null;
		try {
			if (correo != null) {
				Object dt = uc.buscarUsuario(correo);
				if (dt != null) {
					if (dt instanceof DtCiudadano) {
						String token = TokenSecurity.crearToken(((DtCiudadano) dt).getCorreo(), ((DtCiudadano) dt).getRol());													
						dtc = cc.jsonCiudadanoConToken((DtCiudadano) dt, token);	
						return Response.ok(dtc).build();				
					} else return Response.status(401).build();								
				}
			}
		} catch (Exception e) {	
			try {	
				DtCiudadano c = new DtCiudadano(r.getString("ci"), r.getString("name"), correo, "30/11/2000", "", "", "Ciudadano");
				uc.altaCiudadanoFacebook(c);				
				Object dt = uc.buscarUsuario(correo);
				if (dt != null) {
					String token = TokenSecurity.crearToken(((DtCiudadano) dt).getCorreo(), ((DtCiudadano) dt).getRol());		
					dtc = cc.jsonCiudadanoConToken((DtCiudadano) dt, token);					
					return Response.ok(dtc).build();								
				}
			} catch (Exception e2) {
				return Response.ok(e2.getMessage()).build();
			}				
		}
		return Response.status(404).build();
	}

}
