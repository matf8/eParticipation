package eParticipation.backend.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.Json;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.service.UsuarioService;
import eParticipation.backend.helpers.AuthConfig;
import eParticipation.backend.helpers.TokenSecurity;

@WebServlet(urlPatterns = { "/tokenMobile" })
public class LoginServletTokenMobile extends HttpServlet {
	
	@EJB private UsuarioService u;
	
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private final AuthConfig config;
	
	@Inject
	LoginServletTokenMobile(AuthConfig config) {
		this.config = config;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String urlRedirect = "http://localhost/";

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(config.getTokenGubUy());		
		
		// Creo Body Params para el POST al Token Endpoint
		Form form = new Form();
		form.param("grant_type", "authorization_code");
		form.param("code", code);
		form.param("redirect_uri", config.getRedirect_uri());
		form.param("state", state);

		// Creo Basic Auth Header
		String plainCredentials = config.getClientId() + ":" + config.getClientSecret();
		String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
		String authorizationHeader = "Basic " + base64Credentials;
		String ci;
		try {
			Invocation inv = target.request(MediaType.APPLICATION_JSON_TYPE).header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.buildPost(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
			Response res = inv.invoke();			
			JsonObject tokenResponse = res.readEntity(JsonObject.class);
			String accessToken = tokenResponse.getString("access_token");
			String idToken = tokenResponse.getString("id_token");
			LOGGER.severe("Token de acceso : " + accessToken);			
			ci = requestUserId(accessToken);			// pedido a gub uy cedula						
			DtCiudadano usuario = getUserCiudadano(ci); // voy al pdi y traigo al ciudadano, si no esta lo registro. 
			if (usuario == null) {						// Si retorna null es funcionario
				DtFuncionario funcionario = getUserFuncionario(ci); 
				if (funcionario == null) 
					throw new Exception("Fallo pedido de usuario y registro");
				else {
					response.addHeader("tokenSesion", TokenSecurity.crearToken(funcionario.getCorreo(), funcionario.getRol()));
					response.addHeader("usuarioF", funcionario.getCorreo());
				}
			} else { 
				response.addHeader("tokenSesion", TokenSecurity.crearToken(usuario.getCorreo(), usuario.getRol()));	
				response.addHeader("usuarioC", usuario.getCorreo());
			}
			response.addHeader("tokenID", idToken);			
			response.sendRedirect(urlRedirect);		
			client.close();
		} catch (Exception ex) {
			System.out.println("error request");
			LOGGER.severe(ex.getMessage());
		}
	}	
	
	public String requestUserId(String accessToken) {
		// Ultimo paso con Gub Uy. Se envia el access token para solicitar la CI del	
		String authorizationHeader = "Bearer " + accessToken;
		LOGGER.severe("authorizationHeader de tipo Bearer : " + authorizationHeader);
		try {		
			URL url = new URL(config.getUserGubUy());
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Authorization", authorizationHeader);
			http.setDoInput(true);
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			StringBuilder sb = new StringBuilder();
			int HttpResult = http.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
			} else {
				System.out.println(http.getResponseMessage());
			}
			http.disconnect();
			JsonReader jsonReader = Json.createReader(new StringReader(sb.toString()));
			JsonObject object = jsonReader.readObject();
			jsonReader.close();
			System.out.println(object.toString());
			String ci = object.getString("numero_documento");
			if (ci != null) {
				LOGGER.severe("CI Usuario: " + ci);
				return ci;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public DtCiudadano getUserCiudadano(String id) {		
		DtCiudadano c = null;
		try {
			Object o = (Object) u.buscarUsuario(id);
			if (o != null)
				if (o instanceof DtCiudadano)
					return ((DtCiudadano) o);
				else if (o instanceof DtFuncionario)
					return null;
		} catch (Exception e) {
			System.out.println("alta");
			try {
				u.altaCiudadano(id);
				c = (DtCiudadano) u.buscarUsuario(id);
				if (c != null) 
					return c;
			} catch (Exception ex) {
				return null;
			}
		}
		return c;		
	}
	
	public DtFuncionario getUserFuncionario(String id) {		
		DtFuncionario c = null;
		try {
			c = (DtFuncionario) u.buscarUsuario(id);
			if (c != null) 
				return c;
		} catch (Exception e) {		
			return null;
		}
		return c;				
	}	
}
