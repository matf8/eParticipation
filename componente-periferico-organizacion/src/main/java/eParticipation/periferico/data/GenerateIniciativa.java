package eParticipation.periferico.data;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.apache.commons.io.IOUtils;

import eParticipation.periferico.model.Iniciativa;

public class GenerateIniciativa extends Iniciativa {
	
	private static String recurso;
	
	public GenerateIniciativa() {	}	
	
	public static String setRandomDate() throws ParseException {
		Date today = new Date();
		String futureS = "2022-08-31";
		Date future = new SimpleDateFormat("yyyy-MM-dd").parse(futureS);
		Date randomDate = new Date(ThreadLocalRandom.current().nextLong(today.getTime(), future.getTime()));			
		DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");				
		String output = outputFormatter.format(randomDate);
		return output;		
	}	
	
	public static String setRandomName() {
		NameGenerator generator = new NameGenerator();
	
		// generate 5000 male and female names.
		List<Name> names = generator.generateNames(60);
		
		Random rand = new Random();
		// Obtain a number between [0 - 50].
		int n = rand.nextInt(60);		
		int ini = n / 2;
		int n2 = rand.nextInt(6);	
		String lugar = getRandomLugar(n2);
		if (ini < 10) {		
			recurso = "inversion";
			return "Conoce al inversionista de " + lugar + ", " + names.get(n).toString();
		} else if (ini >= 10 && ini < 15) {
			recurso = "podcast";
			return "Unete a " + names.get(n).toString() + " en su nuevo podcast sobre el Medio Ambiente";
		} else if (ini >= 15 && ini < 22) {
			recurso = "caminata";
			return "Sumate a la caminata por el Monte Pluxia, invitado especial  " + names.get(n).toString();
		} else if (ini >= 22) {
			recurso = "musica";
			return "Música a la Uruguaya y la experiencia de " + names.get(n).toString();
		} else 						
			return null;
	}
	
	private static String getRandomLugar(int n2) {
		if (n2 == 1) 
			return "New York";		
		else if (n2 == 2) 
			return "London";
		else if (n2 == 3) 
			return "Paris";
		else if (n2 == 4) 
			return "Tokyo";	
		else return "Zúrich";			
	}

	public static String setRandomDesc() {
		Random rand = new Random();		
		int n = rand.nextInt(50);
		if (n / 2 > 30) 
			return "Descripción TBA";		
		else if (n / 2 < 20) 
			return "Descripción en we-Participation.web.elasticloud.uy/Ours";
		else if (n / 2 == 25)
			return "Descripción de la organización: Creada en 2021 para el pueblo";
		else return "Descripción vacia";		
	}
	
	public static String setRecurso() {
		try {
			if (recurso.equals("inversion")) {		
				InputStream is = GenerateIniciativa.class.getClassLoader().getResourceAsStream("inversiones.jpg");			
				byte[] img = IOUtils.toByteArray(is);
				return "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(img);
			} else if (recurso.equals("podcast")) {			
				InputStream is = GenerateIniciativa.class.getClassLoader().getResourceAsStream("podcast.jpg");			
				byte[] img = IOUtils.toByteArray(is);
				return "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(img);
			} else if (recurso.equals("caminata")) {			
				InputStream is = GenerateIniciativa.class.getClassLoader().getResourceAsStream("caminata.jpg");			
				byte[] img = IOUtils.toByteArray(is);
				return "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(img);
			} else if (recurso.equals("musica")) {			
				InputStream is = GenerateIniciativa.class.getClassLoader().getResourceAsStream("musica.jpg");			
				byte[] img = IOUtils.toByteArray(is);
				return "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(img);
			} else return null;
		} catch (Exception e) {
			System.out.println("nullardo: " + e.getMessage());
			return null;
		}
	}
}
