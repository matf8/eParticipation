package eParticipation.backend.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtIniciativaNotificacion;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.NotificacionService;
import eParticipation.backend.service.PerifericoService;
import eParticipation.backend.service.PersistenciaLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
public class ControladorNotificacion implements NotificacionService {
	
	@EJB private PersistenciaLocal bd;
	@EJB private PerifericoService ps;
	
	public void notificaIniciativaCambio(String n, String nodo) {
		try {		
			// add mensaje to dest
			if (n != null) {
				Iniciativa c = bd.buscarIniciativa(n);
				String dest = c.getCreador();
				if (!dest.contains("ORG")) {
					Firestore db = FirestoreClient.getFirestore();
					// send notificacion a server
					DtIniciativaNotificacion dt = new DtIniciativaNotificacion();
					dt.setVisto(false);
				    dt.setNombre(c.getNombre());
				    dt.setEstado(c.getEstado().toString());
				    dt.setEstado(c.getIdIniciativa().toString());
				    dt.setDescripcion("La iniciativa cambio de estado a: " + c.getEstado());
				    db.collection("eParticipationFS").add(dt).get();
				    String notificacion = "La iniciativa " + c.getNombre() + " cambio de estado a: " + c.getEstado();
				    bd.mensajeEvaluacion(c.getCreador(), notificacion);
				    if (c.getDt().isPushNotify()) {
				    	String topic = "IniciativaEstado";
				    	Notification notfy = Notification.builder().setTitle("eParticipation")
				    		.setBody("Estado de iniciativa").build();				    
					    Message message = Message.builder().setNotification(notfy)
			    	      .putData("Tipo", "cambio de estado iniciativa")
			    	      .putData("Estado", c.getEstado().toString())
			    	      .putData("Nombre", c.getNombre())    	      
			    	      .setTopic(topic).build();				   
			    	   FirebaseMessaging.getInstance().send(message);
			     	   log.info("Firebase iniciativa notification: " + c.getNombre() + " a " + dest);		
				    }
				} else {
					Client client = ClientBuilder.newClient();					
				  	String uriNotificar = nodo+"/recibirNotificacion";
					WebTarget target = client.target(uriNotificar);	
					String ntf = dest + ": " + "Se le notifica que su iniciativa " + c.getNombre() + " cambio de estado a " + c.getEstado().toString();
					Invocation invocation = target.request().buildPost(Entity.entity(ntf, MediaType.APPLICATION_JSON));
					invocation.invoke();
			     	log.info("Notificacion enviada a " + dest);		
				}
			} 
	    } catch (Exception e) {
	      log.error("Firebase error: {}", e.getMessage());			
		}		
		
	}
}