package eParticipation.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.UsuarioService;

public class IniciativaTest {		
		
	Fabrica f = Fabrica.getInstancia();	
	IniciativaService is = f.getIniciativaService();
	UsuarioService us = f.getUsuarioService();
	PersistenciaLocal bd = f.getPersistenciaService();
	
	@Test  
	public void ejectTest() {
		try {
			controladorIniciativaTest();
		} catch (Exception e) {	}
		
	}	
				
	public void controladorIniciativaTest() {
		try {
			DtCiudadano dtc = new DtCiudadano("4", "5040", "Ciud2", "c2@c2", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			us.altaUsuario(dtc, null);						
			assertTrue(bd.buscarUsuario("c2@c2") != null);
			Organizacion ong = new Organizacion(Long.valueOf("11"), "orgi", "ORG4");
			bd.altaOrganizacion(ong);
			ong.setNombre("weno");
			bd.modificarOrganizacion(ong);
			is.altaIniciativa("iniTest", "desc_test1", "2022-12-30", "c2@c2", "recurso");
			assertTrue(is.buscarIniciativa("iniTest") != null);
			assertTrue(is.buscarIniciativa("iniTest123123") == null);
			assertTrue(is.listarIniciativas().size() > 0);
			is.altaIniciativa("iniTest2", "desc_test2", "2022-06-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest3", "desc_test3", "2022-08-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest4", "desc_test4", "2022-09-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest5", "desc_test5", "2021-08-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest6", "desc_test6", "2021-09-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest7", "desc_test7", "2023-01-30", "c2@c2", "recurso");
			is.altaIniciativa("iniTest8", "desc_test8", "2023-02-30", "c2@c2", "recurso");
			Iniciativa k = bd.buscarIniciativa("iniTest8");
			k.setNombre("iniTestanga8");
			bd.modificarIniciativa(k);
			try {
				is.altaIniciativa("iniTest8", "desc_test6", "2023-02-30", "c2@c2", "recurso");
			} catch (Exception e) {	}
			assertTrue(is.listarIniciativasFechas("30/01/2023","30/03/2023").size() > 1);
			assertTrue(is.listarIniciativasFechas("29/06/2022","30/10/2022").size() == 3);
			
			DtCiudadano dtc2 = new DtCiudadano("5", "5041", "Ciud3", "c3@c3", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			us.altaUsuario(dtc2, null);	
			DtCiudadano dtc3 = new DtCiudadano("6", "5042", "Ciud4", "c4@c4", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			us.altaUsuario(dtc3, null);	

			is.adherirUsuario("iniTest", "c2@c2");
			assertTrue(is.listarAdheridos("iniTest").size() > 0);
			assertTrue(is.listarRelacionados().size() > 0);
			assertTrue(is.contarIniciativas() > 0);
			assertTrue(is.contarAdhesiones() > 0);
			
			is.adherirUsuario("iniTest2", "c2@c2");
			is.adherirUsuario("iniTest3", "c2@c2");
			is.adherirUsuario("iniTest4", "c2@c2");
			is.adherirUsuario("iniTest5", "c2@c2");
			is.adherirUsuario("iniTest6", "c2@c2");
			is.adherirUsuario("iniTest7", "c2@c2");
			is.adherirUsuario("iniTest8", "c2@c2");			

			try {
				is.adherirUsuario("iniTest", "c2@c2");
			} catch (Exception e) {
				try {
					is.adherirUsuario("iniTest123", "c2@c2");
				} catch (Exception e2) {
					try {
						is.adherirUsuario("iniTest", "c23@c23");
					} catch (Exception e3) {	}
				}
			}
			is.desadherirUsuario("iniTest", "c2@c2");
			try {
				is.desadherirUsuario("iniTest", "c2@c2");
			} catch (Exception e4) {
				try {
					is.desadherirUsuario("iniTest123", "c2@c2");
				} catch (Exception e5) {
					try {
						is.desadherirUsuario("iniTest", "c23@c23");
					} catch (Exception e6) { }
				}
			}			
			
			is.agregarSeguidor("iniTest", "c2@c2");
			assertTrue(is.listarSeguidores("iniTest").size() > 0);
			try {
				is.agregarSeguidor("iniTest", "c2@c2");
			} catch (Exception e) {
				try {
					is.agregarSeguidor("iniTest123", "c2@c2");
				} catch (Exception e2) {
					try {
						is.agregarSeguidor("iniTest", "c23@c23");
					} catch (Exception e3) { }
				}
			}
			is.quitarSeguidor("iniTest", "c2@c2");
			try {
				is.quitarSeguidor("iniTest", "c2@c2");
			} catch (Exception e5) {
				try {
					is.quitarSeguidor("iniTest23", "c2@c2");
				} catch (Exception e4) {
					try {
						is.quitarSeguidor("iniTest", "c23@c223");
					} catch (Exception e) {	}
				}
			}
			
			DtIniciativa dti = is.buscarIniciativa("iniTest");
			dti.setNombre("cambiado");
			is.modificarIniciativa(dti);
			is.altaIniciativa("iniTest9", "desc_test9", "2023-02-30", "c2@c2", null);
			dti = is.buscarIniciativa("iniTest9");			
			dti.setNombre("cambiado");
			is.modificarIniciativa(dti);
			bd.modificarIniciativa("iniTest2");
			bd.modificarIniciativa("iniTest2");
			bd.modificarIniciativa("iniTest2");
			bd.modificarIniciativa("iniTest2");
			bd.modificarIniciativa("iniTest2");		
			is.bajaIniciativa("cambiado");			
			assertTrue(is.buscarIniciativa(null) == null);			
			is.evaluarIniciativa(dti);			
			is.comentarIniciativa("iniTest8", "c2@c2", "comentario");	
			
			try {
				is.comentarIniciativa("iniTest123", "c2@c2", "comentario");
			} catch (Exception e5) {
				try {
					is.comentarIniciativa("iniTest8", "c223@c2", "comentario");
				} catch (Exception e6) {
					try {
						is.comentarIniciativa("iniTest8", "c2@c2", "");
					} catch (Exception e7) { }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();				
		}
		
	}
	


}
