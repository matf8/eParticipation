package eParticipation.periferico;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import eParticipation.periferico.model.Iniciativa;
import eParticipation.periferico.service.EvaluadorService;
import lombok.extern.slf4j.Slf4j;
import eParticipation.periferico.model.DtIniciativaEv;
import eParticipation.periferico.model.Evaluador;
import eParticipation.periferico.controller.ControladorEvaluador;

@SpringBootApplication
@RestController
@Slf4j
public class App {	

	static EvaluadorService ie = new ControladorEvaluador();

	@GetMapping("/ping")
    public String ping() {
       	 return ie.ping();
    }
	
	@PostMapping("/evaluar")
   	public ResponseEntity<Object> evaluarIniciativa(@RequestBody Iniciativa i) {
		DtIniciativaEv iev = ie.evaluarIniciativa(i);
		log.info(iev.toString());
		return new ResponseEntity<>(iev, HttpStatus.OK);
	}
	
	@PostMapping("/addEvaluador")
   	public ResponseEntity<Object> addEvaluador(@RequestBody Evaluador i) {
		try {
			ie.addEvaluador(i);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/removeEvaluador")
   	public ResponseEntity<Object> removeEvaluador(@RequestBody Evaluador i) {
		try {
			ie.removeEvaluador(i);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getEvaluadores")
    public ResponseEntity<Object> getEvaluadores() {
       	 return new ResponseEntity<>(ie.getEvaluadores(), HttpStatus.OK);
    }	
	
	@GetMapping("/getEvaluador")
    public ResponseEntity<Object> getEvaluador(@RequestBody Long id) {
       	 return new ResponseEntity<>(ie.getEvaluador(id), HttpStatus.OK);
    }
	
    public static void main(String[] args) {
		ie.cargarEvaluadores();
		SpringApplication.run(App.class, args);
	}
}
	
