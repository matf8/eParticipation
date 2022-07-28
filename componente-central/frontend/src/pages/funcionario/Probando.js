import React, { useState } from "react";
import styled from "styled-components";
import Encuesta from "../../components/Encuesta";
import Votacion from "../../components/Votacion";

const Styles = styled.div``;

function Probando() {
  //luego de crear el proceso hay que guardar las opciones en mongo
  const [opcionesEncuesta] = useState([
    { option:"React",votes:0},
    { option:"Angular",votes:0},
    { option:"Ember",votes:0},
    { option:"Vue",votes:0},
  ]);
  /* const [opcionesAux] = useState([]);
  for(let i = 0; i < opcionesEncuesta.length; i++) {
    opcionesAux.push(JSON.stringify(opcionesEncuesta[i]));
  }
  console.log(opcionesAux); */
  console.log(opcionesEncuesta);
  const [opcionesVotacion] = useState([
    { option: "Si", votes: 0 },
    { option: "No", votes: 0 },
  ]);

  var facebook = sessionStorage.getItem("facebookLogin") === "true";
  var k = "";
  if (facebook === true) {
    k = "/logoutFacebook";

  } else {
    k = "/logout";
  }
  console.log("variable facebook: ");
  console.log(k);
  return (
    <Styles>
      <div className="row">
        <div>
          <h1>Encuesta</h1>
          <Encuesta
            pregunta={"cual framework preferis?"}
            opciones={opcionesEncuesta}
          />
        </div>
      </div>
      <div className="row">
        <div>
          <h1>Votacion</h1>
          <Votacion
            opciones={opcionesVotacion}
            pregunta={"che, ta bueno react?"}
          />
        </div>
      </div>
    </Styles>
  );
}

export default Probando;
