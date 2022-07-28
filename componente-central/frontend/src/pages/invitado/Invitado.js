import React, { Fragment } from "react";
import styled from "styled-components";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { NavigationBar } from "./NavBar";
import Home from "./Home";
import { Footer } from "../../components/Footer";
import Iniciativa from "../Iniciativa";
import Login from "./Login";
import Iniciativas from "./Iniciativas";
import LoginGubUy from "./LoginGubUy";
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

function Principal() {
  return (
    <Styles>
      <div id="page-container" style={{ backgroundImage: 'url(' + backgroundColorful + ')'}}>
        <div>
          <NavigationBar />
          <BrowserRouter>
            <Routes>
              <Fragment>
                <Route exact path="/" element={<Home />} />
                <Route exact path="/proceso" element={<Proceso />} />
                <Route exact path="/procesos" element={<Procesos />} />
                <Route exact path="/login" element={<Login />} />
                <Route exact path="/loginGubUy" element={<LoginGubUy />} />
                <Route exact path="/iniciativa" element={<Iniciativa />} />
                <Route exact path="/iniciativas" element={<Iniciativas />} />
                <Route exact path="/Ours" element={<Ours />} />
              </Fragment>
            </Routes>
          </BrowserRouter>
        </div>
        <Footer />
      </div>
    </Styles>
  );
}

export default Principal;
