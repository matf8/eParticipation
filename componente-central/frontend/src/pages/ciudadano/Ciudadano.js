import React, { Fragment } from "react";
import styled from "styled-components";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./Home";
import { NavigationBar } from "./NavBar";
import Perfil from "./Perfil";
import CrearIniciativa from "./CrearIniciativa";
import Iniciativa from "../Iniciativa";
import Politicas from "./Politicas";
import Iniciativas from "./Iniciativas";
import LogoutGubUy from "../LogoutGubUy";
import LogoutFacebook from "../LogoutFacebook";
import Proceso from "../Proceso";
import Procesos from "./Procesos";
import Ours from "../Ours";
import backgroundColorful from "../../assets/backgroundColorful.jpg";

const Styles = styled.div`
  #page-container {
    position: relative;
    min-height: calc(100vh - 3.5rem);
    padding-bottom: 0rem; //doble footer
  }
`;

export default function Ciudadano() {
  return (
    <Styles>
      <div id="page-container" style={{ backgroundImage: 'url(' + backgroundColorful + ')'}}>
        <NavigationBar />
        <BrowserRouter>
          <Routes>
            <Fragment>
              <Route exact path="/" element={<Home />} />
              <Route exact path="/perfil" element={<Perfil />} />
              <Route
                exact
                path="/creariniciativa"
                element={<CrearIniciativa />}
              />
              <Route exact path="/iniciativa" element={<Iniciativa />} />
              <Route exact path="/iniciativas" element={<Iniciativas />} />
              <Route exact path="/proceso" element={<Proceso />} />
              <Route exact path="/procesos" element={<Procesos />} />
              <Route exact path="/politicas" element={<Politicas />} />
              <Route exact path="/logout" element={<LogoutGubUy />} />
              <Route exact path="/logoutFacebook" element={<LogoutFacebook />} />
              <Route exact path="/Ours" element={<Ours />} />
            </Fragment>
          </Routes>
        </BrowserRouter>
      </div>
    </Styles>
  );
}
