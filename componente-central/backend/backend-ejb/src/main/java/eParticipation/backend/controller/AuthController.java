package eParticipation.backend.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;


import eParticipation.backend.service.PersistenciaLocal;


@Stateless
public class AuthController {

	@EJB
	private PersistenciaLocal bd;

	public AuthController() {	}
	

}
