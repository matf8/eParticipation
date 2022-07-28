package eParticipation.backend.dto;

public enum DepartamentosUY {
	  Artigas(1),
	  Canelones(2),
	  CerroLargo(3),
	  Colonia(4),
	  Durazno(5),
	  Flores(6),
	  Florida(7),
	  Lavalleja(8),
	  Maldonado(9),
	  Montevideo(10),
	  Paysandu(11),
	  RioNegro(12),
	  Rivera(13),
	  Rocha(14),
	  Salto(15),
	  SanJose(16),
	  Soriano(17),
	  Tacuarembo(18),
	  Treinta_Y_Tres(19);

	  private final int code;

	  DepartamentosUY(int code) {
	    this.code = code;
	  }

	  public int getCode() {
	    return code;
	  }
}