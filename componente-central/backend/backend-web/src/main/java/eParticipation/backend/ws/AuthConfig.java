package eParticipation.backend.ws;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import lombok.Data;

@ApplicationScoped
@Data
public class AuthConfig {

	private String authorizeEndpoint;
    private String clientId;
    private String clientSecret;
    private String openid;
	private String response_type;
	private String redirect_uri;
	private String tokenEndpoint;
	private String userInfoEndpoint;

    @PostConstruct
    public void init() {
        // Se obtienen los valores del archivo web.xml
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            // Endpoint de autenticación
            this.authorizeEndpoint = (String) env.lookup("authorizeEndpoint");
            // clientId de testing
            this.clientId = (String) env.lookup("clientId");
            // clientSecret de testing
            this.clientSecret = (String) env.lookup("clientSecret");
            // scope, al menos requiere `openid`
            this.openid = (String) env.lookup("scope");
            this.response_type = (String) env.lookup("response_type");
            this.redirect_uri = (String) env.lookup("redirect_uri");
            this.tokenEndpoint = (String) env.lookup("tokenEndpoint");
            this.userInfoEndpoint = (String) env.lookup("userInfoEndpoint");
        } catch (NamingException e) {
            throw new IllegalArgumentException("Unable to lookup auth0 configuration properties from web.xml", e);
        }

        if (this.authorizeEndpoint == null || this.clientId == null || this.clientSecret == null || this.openid == null || this.response_type == null || this.redirect_uri == null || this.tokenEndpoint == null) {
            throw new IllegalArgumentException("domain, clientId, clientSecret, response_type, redirect_uri and scope must be set in web.xml");
        }
    }

}
