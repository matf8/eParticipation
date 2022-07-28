import React, { Fragment } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./Home";
import { NavigationBar } from "./NavBar";
import styled from "styled-components";
import Perfil from "./Perfil";
import Iniciativa from "../Iniciativa";
import Iniciativas from "./Iniciativas";
import CrearProceso from "./CrearProceso";
import CrearProcesoEncuesta from "./CrearProcesoEncuesta";
import CrearProcesoVotacion from "./CrearProcesoVotacion";
import LogoutGubUy from "../LogoutGubUy";
import ModificarIniciativa from "./ModificarIniciativa";
import Proceso from "../Proceso";
import Procesos from "./Procesos";
import Probando from "./Probando";
import Ours from "../Ours";
import backgroundColorful from "../../assets/backgroundColorful.jpg";
import ModificarProceso from "./ModificarProceso";

const Styles = styled.div`
  #page-container {
    position: relative;
    min-height: calc(100vh - 3.5rem);
    padding-bottom: 0rem; //doble footer
  }
`;

export default function Funcionario() {
  return (
    <Styles>
      <div id="page-container" style={{ backgroundImage: 'url(' + backgroundColorful + ')'}}>
        <NavigationBar />
        <BrowserRouter>
          <Routes>
            <Fragment>
              <Route exact path="/" element={<Home />} />
              <Route exact path="/perfil" element={<Perfil />} />
              <Route exact path="/iniciativa" element={<Iniciativa />} />
              <Route exact path="/proceso" element={<Proceso />} />
              <Route exact path="/procesos" element={<Procesos />} />
              <Route exact path="/iniciativas" element={<Iniciativas />} />
              <Route exact path="/crearproceso" element={<CrearProceso />} />
              <Route
                exact
                path="/crearprocesovotacion"
                element={<CrearProcesoVotacion />}
              />
              <Route
                exact
                path="/modificariniciativa"
                element={<ModificarIniciativa />}
              />
              <Route
                exact
                path="/modificarproceso"
                element={<ModificarProceso />}
              />
              <Route
                exact
                path="/crearprocesoencuesta"
                element={<CrearProcesoEncuesta />}
              />
              <Route exact path="/probando" element={<Probando />} />
              <Route exact path="/logout" element={<LogoutGubUy />} />
              <Route exact path="/Ours" element={<Ours />} />
            </Fragment>
          </Routes>
        </BrowserRouter>
      </div>
    </Styles>
  );
}
