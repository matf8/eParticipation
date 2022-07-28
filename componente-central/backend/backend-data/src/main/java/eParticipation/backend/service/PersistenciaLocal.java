package eParticipation.backend.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import eParticipation.backend.dto.DtAdministrador;
import eParticipation.backend.dto.DtAutoridad;
import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtCiudadano;
import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtFuncionario;
import eParticipation.backend.dto.DtIniciativa;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.dto.DtProceso;
import eParticipation.backend.model.Certificado;
import eParticipation.backend.model.Ciudadano;
import eParticipation.backend.model.Evaluador;
import eParticipation.backend.model.Funcionario;
import eParticipation.backend.model.Iniciativa;
import eParticipation.backend.model.IniciativaNodo;
import eParticipation.backend.model.NodosPerifericos;
import eParticipation.backend.model.Organismo;
import eParticipation.backend.model.Organizacion;
import eParticipation.backend.model.ProcesoParticipativo;

@Local
public interface PersistenciaLocal {
	public void datosIniciales();

	public void altaAdmin(DtAdministrador a) throws Exception;
	public void bajaAdmin(String c);
	public void altaAutoridad(DtAutoridad a) throws Exception;
	public void bajaAutoridad(String c);
	public void altaFuncionario(DtFuncionario a, Organismo o) throws Exception;
	public void bajaFuncionario(String c) throws Exception;
	public void altaCiudadano(Ciudadano a) throws Exception;
	public void altaCiudadano(DtCiudadano a) throws Exception;
	public void bajaCiudadano(String c) throws Exception;
	public void mensajeEvaluacion(String creador, String notificacion) throws Exception;
	public List<String> listarMensajesUsuario(String correo) throws Exception;
	public void eliminarMensajeEvaluacion(String user, String mensaje) throws Exception;
	public void eliminarMensajesEvaluacion(String user) throws Exception;
	public void updatePassword(String id, String rol, String pass);
	public boolean checkIdUser(Object user) throws Exception;
	public Object buscarUsuario(String user) throws Exception;
	public void modificarUsuario(Object u, String t, String o, String c, String pass) throws Exception;
	public List<Object> listarUsuarios() throws Exception;
	public int contarCiudadanos();
	public int contarFuncionarios();
	public int contarAdministradores();
	public int contarAutoridades();
	public void altaOrganismo(Organismo i);
	public void bajaOrganismo(String nombre) throws Exception;
	public List<DtOrganismo> listaOrganismos();
	public void modificarOrganismo(Organismo c);
	public Organismo buscarOrganismo(String nombre) throws Exception;
	
	public void altaUri(NodosPerifericos n);
	public void bajaUri(String n, String f) throws Exception;
	public void altaOrganizacion(Organizacion o);
	public Organizacion buscarOrganizacion(String correo) throws Exception; 
	public List<DtOrganizacion> listarOrganizaciones();
	public void borrarOrganizacion(String org);
	public void altaEvaluador(Evaluador o);
	public Evaluador buscarEvaluador(String n) throws Exception;
	public List<DtEvaluador> listarEvaluadores();
	public void borrarEvaluador(String ev);	
	public void persistNodoCreador(IniciativaNodo i);
	public String getNodoCreador(String creador, String nombre) throws Exception;

	public void altaIniciativa(Iniciativa a) throws Exception;
	public Iniciativa buscarIniciativa(Long id);
	public Iniciativa buscarIniciativa(String id);
	public Iniciativa checkIniciativaDisp(String nombre) throws Exception;
	public void bajaIniciativa(String nombre) throws Exception;
	public void modificarIniciativa(String nombre);
	public void modificarIniciativa(DtIniciativa d);
	public void modificarIniciativa(Iniciativa d);
	public List<DtIniciativa> listarIniciativas() throws Exception;
	public List<Iniciativa> listarIniciativasObject() throws Exception;
	public List<DtIniciativa> listarIniciativasPublicadas() throws Exception;
	public List<DtIniciativa> listarIniciativasFechas(Date i, Date f) throws Exception;
	public void adherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception;
	public void desadherirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception;
	public void seguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception;
	public void dejarSeguirUsuarioIniciativa(String nombreIniciativa, String user) throws Exception;
	public List<String> listarAdheridosIniciativa(String nombreIniciativa);
	public List<String> listarSeguidoresIniciativa(String nombreIniciativa);
	public List<DtIniciativa> listarRelacionadosIniciativas();
	public List<String> listarIniciativasAdheridasUsuario(Ciudadano u) throws Exception;
	public List<String> listarIniciativasSeguidasUsuario(Ciudadano u) throws Exception;
	public List<String> listarIniciativasCreadasUsuario(Ciudadano u, Organizacion o) throws Exception;
	public int contarIniciativas();
	public int contarAdhesiones();
	public int contarSeguidores();
	public int contarComentarios();
	
	public void altaProceso(ProcesoParticipativo p) throws Exception;
	public ProcesoParticipativo buscarProceso(String nombre);
	public List<DtProceso> listarProcesos() throws Exception;
	public List<String> listarProcesoCreadosUsuario(Funcionario f) throws Exception;
	public List<DtProceso> listarProcesosFechas(Date dInicio, Date dFinal) throws Exception;
	public int contarProcesos();
	public void modificarProceso(DtProceso d);
	public void modificarProceso(ProcesoParticipativo d);
	public void bajaProceso(String nombre) throws Exception;
	public void persistParticipante(ProcesoParticipativo p);
	public void quitarParticipanteProceso(String proceso, String correo) throws Exception;
	public void seguirUsuarioProceso(String proceso, String user) throws Exception;
	public void dejarSeguirUsuarioProceso(String proceso, String user) throws Exception;
	public List<String> listarSeguidoresProceso(String proceso);
	public List<String> listarProcesoParticipandoUsuario(Ciudadano c);
	public List<String> listarProcesoSeguidoUsuario(Ciudadano o);
	
	public void altaTipoCertificado(String tipo);
	public void altaCertificadoInicial(Integer c) throws Exception;
	public void altaCertificado(Certificado c) throws Exception;
	public void agregarCertificadoUsuario(String c, Certificado cert) throws Exception;
	public DtCertificado buscarCertificado(Integer nivel) throws Exception;
	public Certificado buscarCertificadoObject(Integer nivel) throws Exception;
	public Certificado buscarCertificadoTipo(Integer nivel, String tipo) throws Exception;
	public List<DtCertificado> listarCertificadosUsuario(String u) throws Exception;
	public List<DtCertificado> listarDtCertificados();
	public String getLogro(Integer nivel) throws Exception;
	public List<String> logos();
	public void borrarCertificado(String nombre) throws Exception;

	public void persistComentario(ProcesoParticipativo k);









	
}
	
