package eParticipation.backend.service;

import java.util.List;

import eParticipation.backend.dto.DtCertificado;
import eParticipation.backend.dto.DtEvaluador;
import eParticipation.backend.dto.DtOrganismo;
import eParticipation.backend.dto.DtOrganizacion;
import eParticipation.backend.dto.DtProceso;

public interface UsuarioService {
	
	public void altaUsuario(Object a, String organismo) throws Exception;
	public Object buscarUsuario(String correo) throws Exception;	
	public boolean checkIdUser(Object id) throws Exception;
	public void modificarUsuario(Object d) throws Exception;
	public void updatePassword(String id, String rol, String pass);
	public String crearToken(String correo, String role);
	public List<Object> listarUsuarios() throws Exception;	
	public boolean usuarioSigueIniciativa(String iniciativa, String user);
	public boolean usuarioSigueProceso(String nombre, String correo);
	public boolean usuarioAdheridoIniciativa(String iniciativa, String user);
	public boolean usuarioCreadorIniciativa(String iniciativa, String user);
	public boolean usuarioCreadorProceso(String iniciativa, String user);
	public boolean usuarioParticipaProceso(String nombre, String correo);
	public void altaCiudadano(String correo, String cedula) throws Exception;	
	public int contarCiudadanos();
	public int contarFuncionarios();
	public int contarAdministradores();
	public int contarAutoridades();
	public int[] enAlcanceDeProceso(String correo, String nombreProceso) throws Exception;	
	public List<DtProceso> listarProcesosUsuarioEnAlcance(String correo) throws Exception;
	public List<DtCertificado> listarCertificadosUsuario(String correo) throws Exception;	
	public void bajaCiudadano(String delU2);
	public void bajaFuncionario(String delU2);
	public void bajaAdmin(String delU1);
	public void bajaAutoridad(String delU1);	
	
	public void altaOrganismo(String nombre, String depart);
	public void quitarOrganismo(String nombre);
	public List<DtOrganismo> listaOrganismo();
	
	public void altaEvaluador(String nombre);
	public void altaOrganizacion(String nombre, String correo);
	public List<DtEvaluador> getEvaluadores();
	public List<DtOrganizacion> getOrganizaciones();	
	public void borrarEvaluador(String ev);	
	public void borrarOrganizacion(String org);
}
