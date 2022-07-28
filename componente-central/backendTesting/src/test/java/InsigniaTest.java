package eParticipation.testing;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.InsigniaService;
import eParticipation.backend.service.PersistenciaLocal;

public class InsigniaTest {
	
	Fabrica f = Fabrica.getInstancia();	
	InsigniaService is = f.getInsigniaService();
	PersistenciaLocal bd = f.getPersistenciaService();
	
	@Test  
	public void ejectTest() {
		try {
			controladorInsigniaTest();
		} catch (Exception e) {	}
		
	}	
				
	public void controladorInsigniaTest() {
		try {			
			assertTrue(is.getLogos().size() == 0);	
			is.sendLogo(new byte[1]);
			is.addTipo("TipoTest");
			is.altaCertificado("CertTest", 2, "TipoTest", 20);
			assertTrue(is.getLogos().size() > 0);	
			assertTrue(is.getTipos().size() >= 5);		
			bd.getLogro(2);
			bd.listarDtCertificados();
			bd.buscarCertificadoObject(2);
			bd.buscarCertificado(2);
			bd.contarParticipaciones();
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}
		

}
