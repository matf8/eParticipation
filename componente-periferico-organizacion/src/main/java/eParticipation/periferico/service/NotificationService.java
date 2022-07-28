package eParticipation.periferico.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
	final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	public void addEmitter(final SseEmitter emitter) {
		emitters.add(emitter);
	}

	public void removeEmitter(final SseEmitter emitter) {
		emitters.remove(emitter);
	}
	
	public void doNotify(String n) throws IOException {
		List<SseEmitter> deadEmitters = new ArrayList<>();
		emitters.forEach(emitter -> {
			try {
				if (n != null) {
					emitter.send(SseEmitter.event().data(DATE_FORMATTER.format(new Date()) + " : " + n));
					log.info(n);
				} 
			} catch (Exception e) {
				deadEmitters.add(emitter);
			}
		});
		emitters.removeAll(deadEmitters);
	}

}