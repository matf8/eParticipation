package eParticipation.periferico;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import eParticipation.periferico.service.OrganizacionService;
import lombok.extern.slf4j.Slf4j;
import eParticipation.periferico.controller.ControladorOrganizacion;
import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.model.Organizacion;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import eParticipation.periferico.service.NotificationService;

@SpringBootApplication(scanBasePackages = "eParticipation.periferico")
@RestController
@CrossOrigin(origins = "*")
public class App {	
	
	@Autowired NotificationService service;
	
	final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	static OrganizacionService ie = new ControladorOrganizacion();

    @GetMapping("/ping")
    public String ping() {
    	return ie.ping();
    }
	
	@GetMapping("/proponer")
	public ResponseEntity<Object> proponerIniciativaR(){
		try {			
			return new ResponseEntity<>(ie.proponerIniciativaR(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/proponerIniciativa")
	public ResponseEntity<Object> proponerIniciativa(@RequestBody Iniciativa i){		
		try {
			String res = ie.altaIniciativa(i);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/addOrganizador")
   	public ResponseEntity<Object> addOrganizacion(@RequestBody Organizacion i) {
		try {
			ie.addOrganizacion(i);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/removeOrganizador")
   	public ResponseEntity<Object> removeOrganizacion(@RequestBody Organizacion i) {
		try {
			ie.removeOrganizacion(i);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getOrganizaciones")
    public ResponseEntity<Object> getOrganizaciones() {
       	 return new ResponseEntity<>(ie.getOrganizaciones(), HttpStatus.OK);
    }	
	
	@GetMapping("/getOrganizador")
    public ResponseEntity<Object> getOrganizacion(@RequestBody String id) {
       	 return new ResponseEntity<>(ie.getOrganizacion(id), HttpStatus.OK);
    }
	
	@GetMapping("/getNotificacion")
    public ResponseEntity<SseEmitter> getNotificacion() {
		try {
			final SseEmitter emitter = new SseEmitter();
			service.addEmitter(emitter);
			String n = ie.showNotify();
			service.doNotify(n);			
			emitter.onCompletion(() -> service.removeEmitter(emitter));
			emitter.onTimeout(() -> service.removeEmitter(emitter));
			return new ResponseEntity<>(emitter, HttpStatus.OK);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
    }
		
	@PostMapping("/recibirNotificacion")
   	public ResponseEntity<Object> getNotify(@RequestBody String i) {
		try {
			ie.addNotificacion(i);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	public static void main(String[] args) {
		ie.cargarOrganizaciones();
		SpringApplication.run(App.class, args);
	}
}
