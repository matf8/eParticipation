package eParticipation.backend.notificaciones;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Startup
@Slf4j
public class FirebaseServer {
	
	 @PostConstruct
	 private void initFirebase() {
	    try {
	      if (FirebaseApp.getApps().isEmpty()) {
	        InputStream firebaseSecret = getClass().getClassLoader().getResourceAsStream("eparticipation-uy-firebase.json");
	        FirebaseOptions options = new FirebaseOptions.Builder()
	          .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(firebaseSecret)))
	          .build();
	        FirebaseApp.initializeApp(options);
	        FirestoreClient.getFirestore();
	        log.warn("Firebase initialized");
	      }
	    } catch (IOException e) {
	      log.error("Firebase error: {}", e.getMessage());
	    }
	 }	
}
