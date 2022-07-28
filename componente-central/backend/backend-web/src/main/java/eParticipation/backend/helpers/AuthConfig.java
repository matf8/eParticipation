package eParticipation.backend.helpers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import lombok.Data;

@ApplicationScoped
@Data
public class AuthConfig {

	private String authGubUy;    
	private String tokenGubUy;
	private String userGubUy;
    private String scope;
	private String response_type;
	private String redirect_uri;	
	private String clientId;
    private String clientSecret;

    @PostConstruct
    public void init() {
        // variables de entorno seteadas en web.xml p
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            this.authGubUy = (String) env.lookup("authGubUy");          
            this.clientId = (String) env.lookup("clientId");    
            this.clientSecret = (String) env.lookup("clientSecret");
            this.scope = (String) env.lookup("scope");
            this.response_type = (String) env.lookup("response_type");
            this.redirect_uri = (String) env.lookup("redirect_uri");
            this.tokenGubUy = (String) env.lookup("tokenGubUy");
            this.userGubUy = (String) env.lookup("userGubUy");
        } catch (NamingException e) {
            throw new IllegalArgumentException("Web.xml error look up", e);
        }
        
        if (this.authGubUy == null || this.tokenGubUy == null || this.redirect_uri == null || this.scope == null || this.response_type == null || this.clientId == null || this.clientSecret == null) {
            throw new IllegalArgumentException("authGubUy, tokenGubUy, redirect_uri, scope, response_type, clientId, clientSecret error check web.xml");
        }
    }

}
