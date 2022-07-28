package eParticipation.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.service.Fabrica;
import eParticipation.backend.service.ProcesoService;
import eParticipation.backend.service.PersistenciaLocal;
import eParticipation.backend.service.UsuarioService;

public class ProcesoTest {
	
	Fabrica f = Fabrica.getInstancia();	
	ProcesoService ps = f.getProcesoService();
	UsuarioService us = f.getUsuarioService();
	PersistenciaLocal bd = f.getPersistenciaService();	
	
	@Test  
	public void ejectTest() {
		try {			
			controladorProcesoTest();
		} catch (Exception e) {	}		
	}	
	
	public void controladorProcesoTest() {
		try {
			Organismo org = new Organismo("orgProc", "mvd");			
			bd.altaOrganismo(org);
			DtFuncionario dtf = new DtFuncionario("500", "500", "funcProc", "fp@fp", "07/08/1996", "UY", "Miami 2344", "empleado", "Funcionario");	
			us.altaUsuario(dtf, "orgProc");			
			ps.altaProceso(new DtProceso("40000","procTest1", "2022-06-30", "16", "fase_inicial", "fp@fp", null, null, null));
			try {
				ps.altaProceso(new DtProceso("40000","procTest1", "2022-06-30", "16", "fase_inicial", "fp@fp", null, null, null));
			} catch (Exception e) {
				try {
					DtCiudadano dtc = new DtCiudadano("501", "Ciud3", "cp@cp", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
					us.altaUsuario(dtc, null);				
					ps.altaProceso(new DtProceso("400001","procTest1", "2022-06-30", "16", "fase_inicial", "cp@cp", null, null, null));
				} catch (Exception e2) {
					try {
						ps.altaProceso(new DtProceso("400001","procTest1", "2022-06-30", "16", "fase_inicial", "", null, null, null));
					} catch (Exception e3) { }
				}
			}	
			ps.altaProceso(new DtProceso("procTest2", "2022-06-30", "16", "fase_inicial", "fp@fp"));
			try {
				ps.altaProceso(new DtProceso("procTest3", "2022-06-30", "16", "fase_inicial", "fp33@f33p"));
			} catch (Exception e3) { }
			assertTrue(ps.listarProcesos().size() > 0);
			assertTrue(ps.contarProcesos() > 0);
			DtProceso dtmd = ps.buscarProceso("procTest1");
			dtmd.setNombre("procTestChanged1");
			ps.modificarProceso(dtmd);
			String[] sa = new String[0];
 			DtParticipa dtp = new DtParticipa("procTestChanged1", "cp@cp", sa);
			ps.agregarParticipanteProceso(dtp);
			bd.listarUsuarios();
			
			try {
				ps.agregarParticipanteProceso(dtp);
			} catch (Exception e) {
				try {
		 			DtParticipa dtp2 = new DtParticipa("procTestChanged1", "cp2@cp2", sa);
					ps.agregarParticipanteProceso(dtp2);
				} catch (Exception e2) {
					try {		 			
						DtParticipa dtp3 = new DtParticipa("procTestChanged12222", "cp2@cp2", sa);						
						ps.agregarParticipanteProceso(dtp3);
					} catch (Exception e3) {}
				}	
			}
			bd.listarProcesoParticipandoUsuario("cp@cp");
			ps.quitarParticipanteProceso("procTestChanged1", "cp@cp");
			ps.bajaProceso("procTestChanged1");		
			
			ps.altaProceso(new DtProceso("50000","proc40", "2022-06-22", "16", "fase_inicial", "fp@fp", null, null, null));
			ps.altaProceso(new DtProceso("50001","proc41", "2022-06-23", "16", "fase_inicial", "fp@fp", null, null, null));
			ps.altaProceso(new DtProceso("50002","proc42", "2022-06-24", "16", "fase_inicial", "fp@fp", null, null, null));
			assertTrue(ps.listarProcesosFechas("2022-06-22","2022-06-24").size() > 0);
			DtCiudadano dtc = new DtCiudadano("177", "6030", "Ciud60", "60@60", "01/01/2000", "UY", "Miami 2344", "Ciudadano");	
			bd.altaCiudadano(dtc);
			ps.agregarSeguidor("proc40", "60@60");
			assertTrue(ps.listarSeguidores("proc40").size() > 0);
			ps.quitarSeguidor("proc40", "60@60");
			assertTrue(ps.listarSeguidores("proc40").size() == 0);
			ps.comentarProceso("proc40", "60@60", "comment");
			try {
				ps.comentarProceso("proc401", "60@60", "comentario");
			} catch (Exception e5) {
				try {
					ps.comentarProceso("proc40", "606@c2312", "comentario");
				} catch (Exception e6) {
					try {
						ps.comentarProceso("proc40", "60@60", "");
					} catch (Exception e7) {
						try {							
							ps.comentarProceso("proc40", null, "comentario");
						} catch (Exception e9) {	}						
					}
				}
			}			
		} catch (Exception e) {			
			e.printStackTrace();		
		}
	}
	
	
}
