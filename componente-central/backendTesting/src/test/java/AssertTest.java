package eParticipation.testing;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

import org.junit.jupiter.api.Test;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtEstadisticas;
import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtIniciativaEv;
import eParticipation.backend.dto.DtIniciativaNotificacion;
import eParticipation.backend.dto.DtInsignia;
import eParticipation.backend.dto.DtMensaje;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.dto.DtParticipa;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.dto.DtTipoCertificado;
import eParticipation.backend.model.Administrador;
import eParticipation.backend.model.Alcance;
import eParticipation.backend.model.AlcanceId;
import eParticipation.backend.model.Autoridad;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.CfgNoti;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Estado;
import eParticipation.backend.model.FaseProceso;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.model.ProcesoParticipativo;
import eParticipation.backend.model.TipoAlcance;
import eParticipation.backend.model.TipoCertificado;
import eParticipation.backend.model.TipoInsignia;

@SuppressWarnings("unused")
public class AssertTest {
	
	@Test
	public void IgualesTest() {
		List<String> lm = new ArrayList<>(); 
		lm.add(new String());
		List<Certificado> ctf = new ArrayList<>();	
		ctf.add(new Certificado());
		List<CfgNoti> ln = new ArrayList<>();	
		ln.add(new CfgNoti());
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, 2000);
	    calendar.set(Calendar.MONTH, 0);
	    calendar.set(Calendar.DATE, 31);
	    Date d = calendar.getTime();
	    
	    Ciudadano a1 = new Ciudadano(Long.valueOf(1),"405","Colo ciudadano", "colo@gmail", d, "UY", "Miami 2344", lm, ctf, ln);
		Ciudadano a2 = new Ciudadano();		

		a2.setIdUsuarioFO(a1.getIdUsuarioFO());
		a2.setCedula(a1.getCedula());
		a2.setNombreCompleto(a1.getNombreCompleto());
		a2.setCorreo(a1.getCorreo());
		a2.setFNac(a1.getFNac());
		a2.setNacionalidad(a1.getNacionalidad());
		a2.setDomicilio(a1.getDomicilio());
		a2.setMensajes(lm);		
		a2.setCertificados(ctf);
		a2.setNotificaciones(ln);		
		a2.toString();		
		a2.hashCode();		
		DtCiudadano dt1 = ((Ciudadano)a1).getDt();
		DtCiudadano dt2 = a2.getDt();
		dt2.toString();
		dt2.hashCode();
		TipoInsignia k = new TipoInsignia(new byte[1], new Date());		
		k.toString();
		k.hashCode();	
		try {
			k.getDt();
			TipoInsignia k2 = new TipoInsignia();
			k2.setIdInsignia(k.getIdInsignia());
			k2.setLogo(k.getLogo());
			k2.setFechaObtenido(k.getFechaObtenido());
			assertEquals(k,k2);
			DtInsignia dti = new DtInsignia();
			DtInsignia dti2 = new DtInsignia("asd", d.toString());
			dti.setLogo(dti2.getLogo());
			dti.setFechaObtenido(dti2.getFechaObtenido());
			assertEquals(dti,dti2);
			dti.toString();
			dti.hashCode();
		} catch (UnsupportedEncodingException e1) {	}		
		
		Certificado c = new Certificado("CertCreador1", k, 1, "Creador");
		try {
			DtCertificado dtc = c.getDt();	
			DtCertificado dtc2 = new DtCertificado();	
			dtc.toString();
			dtc.hashCode();
			c.toString();
			c.hashCode();	
			c.equals(c);
			dtc.equals(dtc2);
			c.setTipo(null);
			c.setNombre(null);
			c.setNivel(null);
			c.setInsignia(null);
			c.setIdCertificado(null);
			c.equals(c);
		} catch (UnsupportedEncodingException e) {	}		
	
		a2.agregarCertificado(c);
		a1.agregarCertificado(c);
		a2.quitarCertificado(c);
		a1.quitarCertificado(c);
		assertEquals(dt1,dt2);
		assertEquals(a1,a2);		
		
		Organismo of1 = new Organismo();		
		Organismo of2 = new Organismo("org", "mvd");
		of1.toString();
		of1.hashCode();
		of1.equals(of1);
		of1.setIdOrganismo(Long.valueOf(1));
		DtOrganismo dof1 = of1.getDt();
		List<String> l = null;
		DtOrganismo dof2 = new DtOrganismo("1","nombreOrg", "artigas");
		DtOrganismo dof3 = new DtOrganismo();
		dof1.toString();
		dof1.hashCode();
		of2.setIdOrganismo(of1.getIdOrganismo());
		of1.setNombre(of2.getNombre());
		of1.setDepartamento(of2.getDepartamento());		
		assertEquals(of1,of2);	
		dof1.equals(dof1);
		dof1.setDepartamento("mvd");
		dof1.setId("add");
		dof1.setNombre("acc");
		dof1.setEmpleados(null);
		dof2.equals(dof2);
		
		Funcionario b1 = new Funcionario(Long.valueOf(1), "404","Pepito funcionario", "pepe@gmail", d, "UY", "Miami 2344", lm, "empleado", of1);
		Funcionario b2 = new Funcionario();
		b2.setIdUsuarioFO(b1.getIdUsuarioFO());
		b2.setCedula(b1.getCedula());
		b2.setNombreCompleto(b1.getNombreCompleto());
		b2.setCorreo(b1.getCorreo());
		b2.setFNac(b1.getFNac());
		b2.setNacionalidad(b1.getNacionalidad());
		b2.setDomicilio(b1.getDomicilio());
		b2.setMensajes(lm);		
		b2.setCargo(b1.getCargo());
		b2.setOficina(of2);		
		b2.toString();		
		b2.hashCode();
		DtFuncionario df1 = b1.getDt();
		DtFuncionario df2 = b2.getDt();
		df1.toString();
		df2.hashCode();
		assertEquals(df1,df2);
		assertEquals(b1,b2);
		of1.agregarEmpleado(b1);
		dof1 = of1.getDt();
		of1.quitarEmpleado(b1);
		DtFuncionario df3 = new DtFuncionario("asd", "asd", "asd", "10/10/2021", "asd", "asd", "asd", "Funcionario");
		DtFuncionario df4 = new DtFuncionario();
		df4.setCargo(null);
		df4.setCedula(null);
		df4.setFNac(null);
		df4.setCorreo(null);
		df4.setDomicilio(null);
		df4.setId(null);
		df4.setNacionalidad(null);
		df4.setOrganismo(null);
		df4.setRol(null);		
		DtCiudadano dc4 = new DtCiudadano();
		dc4.setCedula(null);
		dc4.setFNac(null);
		dc4.setCorreo(null);
		dc4.setDomicilio(null);
		dc4.setId(null);
		dc4.setNacionalidad(null);
		dc4.setRol(null);
		DtAutoridad da4 = new DtAutoridad();
		DtAutoridad da5 = new DtAutoridad("asd","asd","asd","asd","asd");
		da4.setCorreo(null);
		da4.setPassword(null);
		da4.setToken(null);
		da4.setRol(null);
		da4.setId(null);
		DtAdministrador dad4 = new DtAdministrador();
		DtAdministrador dad5 = new DtAdministrador("asd","asd","asd","asd","asd");
		dad4.setCorreo(null);
		dad4.setPassword(null);
		dad4.setToken(null);
		dad4.setRol(null);
		dad4.setId(null);

		of1.agregarEmpleado(b1);
		of2.agregarEmpleado(b2);
		of1.quitarEmpleado(b1);
		of2.quitarEmpleado(b2);
	
	    Administrador c1 = new Administrador(Long.valueOf(1),"406","pass", "adm@gmail", "Administrador");
		Administrador c2 = new Administrador();		
		c2.setIdAdmin(c1.getIdAdmin());
		c2.setCedula(c1.getCedula());
		c2.setNombreCompleto(c1.getNombreCompleto());
		c2.setCorreo(c1.getCorreo());
		c2.setPassword(c1.getPassword());		
		c2.toString();
		c2.hashCode();
		DtAdministrador da1 = c1.getDt();
		DtAdministrador da2 = c2.getDt();
		da1.toString();
		da2.hashCode();
		assertEquals(da1,da2);
		assertEquals(c1,c2);		
		
		Autoridad d1 = new Autoridad(Long.valueOf(1), "407","pass", "aut@gub", "Autoridad");
		Autoridad d2 = new Autoridad();
		d2.setIdAutoridad(d1.getIdAutoridad());
		d2.setCedula(d1.getCedula());
		d2.setNombreCompleto(d1.getNombreCompleto());
		d2.setCorreo(d1.getCorreo());
		d2.setPassword(d1.getPassword());	
		d2.toString();		
		d2.hashCode();
		DtAutoridad db1 = d1.getDt();
		DtAutoridad db2 = d2.getDt();
		db2.toString();		
		db2.hashCode();
		assertEquals(db1,db2);
		assertEquals(d1,d2);
		
		Iniciativa i1 = new Iniciativa(Long.valueOf(1), "ini1", "desc", d, "creador", Estado.Aceptado);
		Iniciativa i2 = new Iniciativa();
		i2.setIdIniciativa(i1.getIdIniciativa());
		i2.setNombre(i1.getNombre());
		i2.setDescripcion(i1.getDescripcion());
		i2.setFecha(i1.getFecha());
		i2.setCreador(i1.getCreador());
		i2.setEstado(i1.getEstado());
		i1.agregarUsuarioAdherido(a1);
		i2.agregarUsuarioAdherido(a1);	
		i1.agregarUsuarioSeguidor(a1);
		i2.agregarUsuarioSeguidor(a1);			
		i1.agregarRecurso("1");
		i2.agregarRecurso("1");		
		DtIniciativa dti1 = i1.getDt();
		DtIniciativa dti2 = i2.getDt();
		i2.quitarRecurso("1");
		i1.quitarRecurso("1");
		i1.quitarUsuarioSeguidor(a1);
		i2.quitarUsuarioSeguidor(a1);
		i1.quitarUsuarioAdherido(a1);
		i2.quitarUsuarioAdherido(a1);
		i1.agregarComentario("hola");
		i1.quitarComentario("hola");
		dti1.toString();
		dti1.hashCode();
		i2.toString();
		i2.hashCode();
		assertEquals(dti1,dti2);
		assertEquals(i1,i2);	
		i1.setAdheridos(null);
		i1.setIdRecurso(null);
		i1.setSeguidores(null);
		i1.setIdComentario(null);
		
		dti1.setFecha(null);
		Iniciativa ik2 = new Iniciativa(dti1);
				
		List<String> rec = null;
		Iniciativa ic1 = new Iniciativa("ini2", "desc2", d, "creador2", Estado.Aceptado, rec);
		
		List<String> li = null;
		ProcesoParticipativo p1 = new ProcesoParticipativo(Long.valueOf(1), "pro1", d, "18", li, "fase_inicial", "creador");
		ProcesoParticipativo p2 = new ProcesoParticipativo();
		p2.setIdProceso(p1.getIdProceso());
		p2.setNombre(p1.getNombre());
		p2.setFecha(p1.getFecha());
		p2.setDescripcionAlcance(p1.getDescripcionAlcance());
		p2.setIdInstrumento(li);
		p2.setFase(p1.getFase());
		p2.setCreador(p1.getCreador());
		p2.hashCode();
		p2.toString();
		p2.equals(p2);
		assertEquals(p1,p2);
		p2.agregarComentario("hola");
		p2.quitarComentario("hola");
		p2.agregarSeguidor(a1);
		p2.quitarSeguidor(a1);
		p2.agregarUsuarioParticipante(a1);
		p2.quitarUsuarioParticipante(a1);
		p2.agregarInstrumento("ins");
		p2.quitarInstrumento("ins");		
		DtProceso dtp1 = p1.getDt();
		DtProceso dtp2 = p2.getDt();
		DtProceso dtp3 = new DtProceso("pro2", d.toString(), "20", "fase_testing", "creador");
		DtProceso dtp4 = new DtProceso();
		dtp1.toString();
		dtp1.hashCode();
		assertEquals(dtp1,dtp2);		
		p2.setIdComentario(null);
		p2.setParticipantes(null);
		p2.setSeguidores(null);
		dtp1.agregarComentario(null);
		dtp1.quitarComentario(null);
		ProcesoParticipativo p3 = new ProcesoParticipativo("procesoWa", new Date(), "16", li, FaseProceso.valueOf("fase_inicial"), "pepe@gmail");
		
		DtEstadisticas dte1 = new DtEstadisticas();
		DtEstadisticas dte2 = new DtEstadisticas();
		dte1.setCantCiudadanos(1); dte1.setCantFuncionarios(1);
		dte1.setCantAdministradores(1);	dte1.setCantAutoridades(1);
		dte1.setCantIniciativas(1);	dte1.setCantProcesos(1);
		dte1.setCantAdhesionesMayo(1); dte1.setCantAdhesionesJunio(1); 		
		dte2.setCantCiudadanos(dte1.getCantCiudadanos()); dte2.setCantFuncionarios(dte1.getCantFuncionarios());
		dte2.setCantAdministradores(dte1.getCantAdministradores());	dte2.setCantAutoridades(dte1.getCantAutoridades());
		dte2.setCantIniciativas(dte1.getCantIniciativas());	dte2.setCantProcesos(dte1.getCantProcesos());
		dte2.setCantAdhesionesMayo(dte1.getCantAdhesionesMayo()); dte2.setCantAdhesionesJunio(dte1.getCantAdhesionesJunio());
		dte2.equals(dte1);
		dte1.toString();
		dte1.hashCode();
		assertEquals(dte1, dte2);
		
		Evaluador e1 = new Evaluador();
		Evaluador e2 = new Evaluador("ev2");
		e2.setIdEvaluador(e1.getIdEvaluador());
		e2.setNombre(e1.getNombre());		
		e2.hashCode();
		e2.toString();
		DtEvaluador dtev1 = e1.getDt();
		DtEvaluador dtev2 = e2.getDt();
		assertEquals(dtev1,dtev2);
		assertEquals(e1,e2);	
		dtev1.toString();
		dtev1.hashCode();
		dtev1.equals(dtev2);
	
		Alcance tac1 = new Alcance();
		Alcance tac2 = new Alcance();
		tac2.setIdCiudadano(tac1.getIdCiudadano());
		tac2.setIdProceso(tac1.getIdProceso());		
		tac2.hashCode();
		tac2.toString();
		tac2.equals(tac1);
		assertEquals(tac1,tac2);
		
		TipoAlcance alc = Alcance.consultarAlcance(18,20);
		assertTrue(alc == TipoAlcance.Disponible);
		TipoAlcance alc2 = Alcance.consultarAlcance(20,18);
		assertTrue(alc2 == TipoAlcance.Fuera_de_Rango);

		String[] adp = new String[2];
		String mvd = "Montevideo"; String art = "Artigas"; String can = "Canelones";
		adp = ArrayUtils.add(adp, mvd);
		adp = ArrayUtils.add(adp, art);		
		TipoAlcance alc3 = Alcance.consultarAlcance(adp, mvd);
		assertTrue(alc3 == TipoAlcance.Disponible);		
		TipoAlcance alc4 = Alcance.consultarAlcance(adp, can);
		assertTrue(alc4 == TipoAlcance.Fuera_de_Rango);
		
		AlcanceId aid1 = new AlcanceId();
		AlcanceId aid2 = new AlcanceId();
		aid2.setIdCiudadano(aid1.getIdCiudadano());
		aid2.setIdProceso(aid1.getIdProceso());	
		aid2.hashCode();
		aid2.toString();
		try {
			TipoCertificado tpc = TipoCertificado.getInstancia();
			tpc.setIdTipoCertificado(null);
			String tk = tpc.getTipo("Seguidores");
			assertEquals(tk,"Seguidores");
			String tipo = "Imaginarios";
			tpc.agregarTipoCertificado(tipo);
			assertFalse(tpc.agregarTipoCertificado(tipo));
			String tk2 = tpc.getTipo(tipo);
			assertEquals(tk2,tipo);
			tpc.quitarTipoCertificado(tipo);
			DtTipoCertificado dt = new DtTipoCertificado();
			dt.setTipo(null);
			dt.setMomento(0);
			dt.toString();
			dt.hashCode();
			dt.equals(dt);
			tpc.hashCode();
			tpc.toString();
			tpc.equals(tpc);
			assertEquals(tpc, tpc);
			String tk3 = tpc.getTipo(tipo);				
		} catch (Exception e) {	}
		
		Organizacion orgk1 = new Organizacion();		
		Organizacion orgk2 = new Organizacion();
		orgk2.setIdOrganizacion(orgk1.getIdOrganizacion());
		orgk2.setNombre(orgk1.getNombre());
		orgk2.setCorreo(orgk1.getCorreo());
		Organizacion orgk3 = new Organizacion(Long.valueOf(2), "orgtest", "test@correo"); 
		DtOrganizacion dtorgk1 = orgk3.getDt();
		DtOrganizacion dtorgk2 = orgk3.getDt();
		assertEquals(dtorgk1, dtorgk2);
		orgk2.hashCode();
		orgk2.toString();
		dtorgk2.hashCode();
		dtorgk2.toString();
		
		DtIniciativaNotificacion dtn1 = new DtIniciativaNotificacion();
		dtn1.setEstado("estado");
		dtn1.setNombre("nombre1");
		dtn1.setDescripcion("desc");
		dtn1.toString();
		dtn1.hashCode();
		DtIniciativaNotificacion dtn2 = new DtIniciativaNotificacion();
		dtn2.setEstado(dtn1.getEstado());
		dtn2.setNombre(dtn1.getNombre());
		dtn2.setDescripcion(dtn1.getDescripcion());
		assertEquals(dtn1, dtn2);
			
		DtIniciativaEv dtiev1 = new DtIniciativaEv();
		DtIniciativaEv dtiev2 = new DtIniciativaEv("1", "nomb1", "desc1", d.toString(), "colo@gmail", "Aceptado", "rec", "ok", true);
		dtiev1.setDescripcionEvaluacion(dtiev2.getDescripcionEvaluacion());
		dtiev1.setAceptada(dtiev2.isAceptada());
		dtiev1.toString();
		dtiev1.hashCode();
		dtiev1.equals(dtiev1);
		
		DtMensaje dtmsj1 = new DtMensaje();
		DtMensaje dtmsj2 = new DtMensaje("contenidoMsj", "dest");		
		dtmsj1.setContenido(dtmsj2.getContenido());
		dtmsj1.setDestinatario(dtmsj2.getDestinatario());
		dtmsj1.toString();
		dtmsj1.hashCode();
		dtmsj1.equals(dtmsj1);
		assertEquals(dtmsj1, dtmsj2);
		
		DtParticipa dtpr1 = new DtParticipa();
		String[] a = new String[1];
		ArrayUtils.add(a, "hola");	
		DtParticipa dtpr2 = new DtParticipa("pro1", "user", a);		
		dtpr1.setProceso(dtpr2.getProceso());
		dtpr1.setUser(dtpr2.getUser());
		dtpr1.setRespuesta(dtpr2.getRespuesta());
		dtpr1.addElement("chau");
		dtpr1.removeElement(0);
		dtpr1.toString();
		dtpr1.hashCode();
		dtpr1.equals(dtpr1);
		assertEquals(dtmsj1, dtmsj2);	
		
		CfgNoti cfgn1 = new CfgNoti();
		CfgNoti cfgn2 = new CfgNoti(Long.valueOf(1), "texto", a1);
		cfgn1.setIdCfgNoti(cfgn2.getIdCfgNoti());
		cfgn1.setTextoNoti(cfgn2.getTextoNoti());
		cfgn1.setDestinatario(cfgn2.getDestinatario());
		cfgn1.equals(cfgn1);
		assertEquals(cfgn1, cfgn2);			
	

	}
	
	

}
