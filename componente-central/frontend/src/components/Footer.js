import React from "react";
import styled from "styled-components";
import { FcAndroidOs } from "react-icons/fc";

const Styles = styled.div`
  .footer {
    position: fixed;
    bottom: 0%;
    width: 100%;
    height: 60px; /* Height of the footer */
    background-color: #1f618d;
    font-family: "Poppins", sans-serif;
    color: #f2f2f2;
  }

  a:link {
    color: white;
  }
  #url {
    float: right;
  }

  #correo {
    float: left;
  }
`;

export const Footer = () => (
  <Styles>
    <React.Fragment>
      <footer className="footer py-3">
        <div className="container" align="left">
          <span id="correo"> eparticipation.fing.uy@gmail.com</span>
          <span id="url"><FcAndroidOs size={30}/><a href="https://drive.google.com/uc?id=1DPn8aNIEizcaiEuhJDwsaIEEnU7R48Os&export=download&confirm=t" className="badge badge-success">APK</a></span>
        </div>
      </footer>
    </React.Fragment>
  </Styles>
);
