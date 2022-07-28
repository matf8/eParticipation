package eParticipation.backend.helpers;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class TokenSecurity {
	
	private static RsaJsonWebKey rsaJsonWebKey = null;
	private static String issuer = "e-participation.uy";
	private static int timeToExpire = 60;
	
	// 	Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
	static {
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String crearToken(String id, String tipoUsuario ) throws Exception {
	    // Give the JWK a Key ID (kid), which is just the polite thing to do
	    rsaJsonWebKey.setKeyId("k1");

	    JwtClaims claims = new JwtClaims();
	    // a unique identifier for the token
	    claims.setGeneratedJwtId(); 
	    claims.setIssuer(issuer);  
	    claims.setExpirationTimeMinutesInTheFuture(timeToExpire); 	   
	    // when the token was issued/created (now)
	    claims.setIssuedAtToNow();  	  
	    claims.setClaim("id", id); 
	    claims.setClaim("rol", tipoUsuario);	
	    
	    JsonWebSignature jws = new JsonWebSignature();
	    // The payload of the JWS is JSON content of the JWT Claims
	    jws.setPayload(claims.toJson());
	    // The JWT is signed using the private key
	    jws.setKey(rsaJsonWebKey.getPrivateKey());

	    // Set the Key ID (kid) header because it's just the polite thing to do.
	    // We only have one key in this example but a using a Key ID helps
	    // facilitate a smooth key rollover process
	    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

	    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);	   
	    String jwt = jws.getCompactSerialization();
	    return jwt;
	}
		
	// Retorna el id si el JWT es valido
	public static JwtClaims validarToken(String jwt) throws InvalidJwtException {
		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
    		// the JWT must have an expiration time
            .setRequireExpirationTime() 
            // but the  expiration time can't be too crazy
            .setMaxFutureValidityInMinutes(300) 
            // allow some leeway in validating time based claims to account for clock skew
            .setAllowedClockSkewInSeconds(60) 
            // whom the JWT needs to have been issued by
            .setExpectedIssuer(issuer) 
            // verify the signature with the public key
            .setVerificationKey(rsaJsonWebKey.getKey())
            .build(); 

        //  Validate the JWT and process it to the Claims
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        System.out.println("JWT validation succeeded!" + jwtClaims); 
        return jwtClaims;
	}
		
	
	public static String getIdClaim(JwtClaims jwtClaims) {
		return jwtClaims.getClaimsMap().get("id").toString();
	}
	
	public static String getRolClaim(JwtClaims jwtClaims) {
		return jwtClaims.getClaimsMap().get("rol").toString();
	}
}
