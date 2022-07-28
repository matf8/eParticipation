package eParticipation.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.IniciativaService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.ProcesoService;
import eParticipation.backend.service.UsuarioService;

public class UsuarioTest {			
	
	Fabrica f = Fabrica.getInstancia();	
	UsuarioService us = f.getUsuarioService();
	IniciativaService is = f.getIniciativaService();
	ProcesoService ps = f.getProcesoService();
	PersistenciaLocal bd = f.getPersistenciaService();
	
	@Test  
	public void ejectTest() {
		try {
			controladorUsuarioaTest();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
				
	public void controladorUsuarioaTest() {
		try {					
			bd.altaCertificadoInicial(1);
			DtAdministrador dta = new DtAdministrador("1","5026", "a", "a@i", "mathias fernández", "Administrador");	
			us.altaUsuario(dta, null);			
			us.buscarUsuario("5026");	
			us.updatePassword("1", "Administrador", "hh");
			DtAutoridad dtb = new DtAutoridad("1","5027", "g", "gub@f", "leandro fernández", "Autoridad");	
			us.altaUsuario(dtb, null);
			us.buscarUsuario("5027");	
			us.updatePassword("1", "Autoridad", "hh");
			Organismo org = new Organismo("org1", "mvd");
			bd.altaOrganismo(org);
			Organismo org2 = bd.buscarOrganismo("org1");
			assertEquals(org,org2);			
			bd.modificarOrganismo(org2);
			dta.setCedula("50265657");
			us.modificarUsuario(dta);
			dtb.setCedula("50277678");
			us.modificarUsuario(dtb);
			DtFuncionario dtf = new DtFuncionario("2", "5028", "Func1", "f@f", "07/08/2018", "UY", "Miami 2344", "empleado", "Funcionario");	
			us.altaUsuario(dtf, "org1");
			us.checkIdUser(dtf);
			us.checkIdUser(null);			
			us.altaOrganismo("org3", "can");			
			us.quitarOrganismo("org3");
			org2.setNombre("org2Mod");
			bd.modificarOrganismo(org2);
			assertTrue(us.listaOrganismo().size() > 0);			
			us.altaEvaluador("evtest1");
			us.altaEvaluador("evtest2");
			Evaluador ex = bd.buscarEvaluador("evtest2");
			ex.setNombre("evtest22");
			bd.modificarEvaluador(ex);
			us.altaOrganizacion("orgtest1", "orgtest1");		
			is.altaIniciativa("iniTest10", "desc_test10", "2023-02-30", "orgtest1", "recurso");
			Organizacion ox = bd.buscarOrganizacion("orgtest1");
			bd.listarIniciativasCreadasUsuario(null, ox);

			Organizacion kf = new Organizacion(Long.valueOf(222),"pro","pro");
			kf.getDt();
			kf.equals(kf);
			kf.hashCode();
			assertTrue(us.getEvaluadores().size() <= us.getOrganizaciones().size());			
			us.borrarOrganizacion("correo@test.com");
			us.borrarEvaluador("evtest1");
			us.bajaAdmin("50265657");
			us.bajaAutoridad("50277678");
						
			try {
				us.altaUsuario(dtf, "org3000");			
			} catch (Exception eg) {	}
			us.buscarUsuario("f@f");				
			us.altaCiudadano("correo@gmail","5029");
			DtCiudadano dtc3 = (DtCiudadano) us.buscarUsuario("correo@gmail");	
			dtc3.setCorreo("cambiado@gmail");
			us.modificarUsuario(dtc3);
			
			DtCiudadano dtc = new DtCiudadano("114", "5030", "Ciud1", "c@c", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			us.altaUsuario(dtc, null);						
			assertTrue(bd.buscarUsuario("c@c") != null);	
			
			String t1 = us.crearToken(dtc.getCorreo(), dtc.getRol());
			String t2 = us.crearToken(dtc.getCorreo(), dtc.getRol());
			assertNotEquals(t1, t2);
			try {
				us.buscarUsuario("");	
			} catch (Exception ef) {	}
			int cc = us.contarCiudadanos();
			assertTrue(cc > 0);
			cc = us.contarFuncionarios();
			assertTrue(cc > 0);
			cc = us.contarAdministradores();
			assertTrue(cc == 0);
			cc = us.contarAutoridades();
			assertTrue(cc == 0);			
						
			List<Object> lU = us.listarUsuarios();
			assertFalse(lU.isEmpty());	
			assertTrue(bd.buscarUsuario("c@c") != null);	
			bd.altaIniciativa(new Iniciativa(Long.valueOf(1), "ini_0", "desc0", new Date(), "c@c", Estado.En_espera));
			us.usuarioAdheridoIniciativa("ini_0", "c@c");
			us.usuarioSigueIniciativa("ini_0", "c@c");
			bd.adherirUsuarioIniciativa("ini_0","c@c");
			us.usuarioAdheridoIniciativa("ini_0", "c@c");
			us.usuarioSigueIniciativa("ini_0", "c@c");
			us.usuarioAdheridoIniciativa("ini1122", "c@c");
			us.usuarioSigueIniciativa("ini1122", "c@c");
			us.usuarioCreadorIniciativa("ini_0", "c@c2");
			assertTrue(us.usuarioCreadorIniciativa("ini_0", "c@c"));
			List<String> lIN = null;
			bd.altaProceso(new ProcesoParticipativo(Long.valueOf(2), "pro2", new Date(), "15", lIN, "fase_inicial", "f@f"));
			bd.seguirUsuarioProceso("pro2", "c@c");
			us.usuarioSigueProceso("pro2", "c@c");
			bd.dejarSeguirUsuarioProceso("pro2", "c@c");
			us.usuarioSigueProceso("pro2", "c@c");
			DtParticipa d = new DtParticipa("pro2", "c@c", null);
			ps.agregarParticipanteProceso(d);
			us.usuarioParticipaProceso("pro2", "c@c");
			ps.quitarParticipanteProceso("pro2", "c@c");
			us.usuarioParticipaProceso("pro2", "c@c");
			assertTrue(us.usuarioCreadorProceso("pro2", "f@f"));
			int[] _a = us.enAlcanceDeProceso("c@c", "pro2");
			assertTrue(_a[2] == 1);
			DtCiudadano dtc2 = new DtCiudadano("5031", "Ciud2", "c2@c", "01/01/2010", "UY", "Miami 2344", "Ciudadano");	
			us.altaUsuario(dtc2, null);	
			assertTrue(us.enAlcanceDeProceso("c2@c", "pro2")[2] == 0);
			
			List<DtProceso> lP = us.listarProcesosUsuarioEnAlcance("c@c");
			assertFalse(lP.isEmpty());
			
			List<DtCertificado> lC = us.listarCertificadosUsuario("c@c");		
			assertTrue(lC.size() == 0);						
			try {
				//bd.altaIniciativa(new Iniciativa("ini2", "desc2", new Date(), "c@c", Estado.En_espera, null));
				is.altaIniciativa("ini_1", "desc_1", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_2", "desc_2", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_3", "desc_3", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_4", "desc_4", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_5", "desc_5", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_6", "desc_6", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_7", "desc_7", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_8", "desc_8", "2022-06-30", "c@c", "recurso");
				is.altaIniciativa("ini_cert1", "desc_cert1", "2022-06-30", "c@c", "recurso");
				is.adherirUsuario("ini_1", "c@c");
				is.adherirUsuario("ini_2", "c@c");
				is.adherirUsuario("ini_3", "c@c");
				is.adherirUsuario("ini_4", "c@c");
				is.adherirUsuario("ini_5", "c@c");
				is.adherirUsuario("ini_6", "c@c");
				is.adherirUsuario("ini_7", "c@c");
				is.adherirUsuario("ini_8", "c@c");
				is.adherirUsuario("ini_cert1", "c@c");
				is.quitarSeguidor("ini_cert1", "c@c");
				is.agregarSeguidor("ini_cert1", "c@c");
			} catch (Exception e) { }	
						
			lC = us.listarCertificadosUsuario("c@c");		
			assertTrue(lC.size() == 3);
			
			us.bajaCiudadano("c2@c");
			DtFuncionario dtmd = (DtFuncionario) us.buscarUsuario("f@f");
			dtmd.setNombreCompleto("changed");
			us.modificarUsuario(dtmd);
			us.bajaFuncionario("f@f");				
						
			try {
				assertTrue(bd.buscarUsuario("c2@c") == null);				
			} catch (Exception e) {	}
			finally {
				try {
					assertTrue(bd.buscarUsuario("c2@c") == null);
				} catch (Exception e) {	}
			}			
						
			DtCiudadano cdt = new DtCiudadano("214", "6630", "11", "md@md", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			bd.altaCiudadano(cdt);
			cdt.setNombreCompleto("weno");
			us.modificarUsuario(cdt);			
			
			DtFuncionario fdt = new DtFuncionario("215", "6631", "12", "mdf@mdf", "01/01/2000", "UY", "Miami 2344", "kk", "Funcionario");	
			bd.altaOrganismo(new Organismo("org4", "canelones"));
			us.altaUsuario(fdt, "org4");			
			fdt.setNombreCompleto("weno");
			us.modificarUsuario(fdt);			
			
			DtAutoridad adt = new DtAutoridad("2222","22227", "223g", "gub@f23", "2323 fernández", "Autoridad");		
			us.altaUsuario(adt, null);
			adt.setNombreCompleto("weno");
			us.modificarUsuario(adt);
			
			DtAdministrador adta = new DtAdministrador("2331","50232326", "a232", "123a@i", "m123123athias fernández", "Administrador");	
			us.altaUsuario(adta, null);
			adta.setNombreCompleto("weno");
			us.modificarUsuario(adta);			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}	
	
}
