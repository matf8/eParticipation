package eParticipation.backend.ws;

import java.io.IOException;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eParticipation.backend.helpers.AuthConfig;

@WebServlet(urlPatterns = "/auth")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AuthConfig config;
	
	@Inject
	public LoginServlet(AuthConfig config) throws NamingException {
		this.config = config;
	}
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String authUri = config.getAuthGubUy();
    	String clientId = config.getClientId();
    	String redirectUri = config.getRedirect_uri();
    	String scope = config.getScope();
        // Recibe el llamado del boton login gub uy y genera el code para el token, se mantiene el state login para diferenciar cuando llega al redirect_uri
        String authURL = authUri + "?response_type=code"
        		  + "&client_id=" + clientId
        		  + "&redirect_uri=" + redirectUri
        		  + "&scope=" + scope
        		  + "&state=" + "login";
        response.sendRedirect(authURL);
    }
}
