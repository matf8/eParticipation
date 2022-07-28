import React from "react";
import { Navbar, Nav } from "react-bootstrap";
import styled from "styled-components";
import { FaConnectdevelop } from "react-icons/fa";

const Styles = styled.div`
  #container {
    margin-bottom: 56px; //tamaño del navbar
    // para separar del navbar
  }

  .navbar {
    background-color: #1f618d;
    .logo {
      margin-left: 25px;
    }
  }

  .navbar-collapse {
    margin-left: 25px;
  }

  .navbar-toggler {
    margin-right: 20px;
  }

  .nav-link {
    color: white;
  }

  .navbar-brand,
  .navbar-nav .nav-link {
    color: white;

    &hover: {
      color: black;
    }
  }

  .dropdown-menu-color {
    background-color: rgb(115, 124, 250);
    a {
      font-color: white;

      &:hover {
        background-color: #ffffff;
      }
    }
  }
`;

export const NavigationBar = () => {
  return (
    <Styles>
      <div id="container">
        <Navbar expand="lg" className="navbar fixed-top">
          <Navbar.Brand className="logo" href="/">
            <div href="/">
              <FaConnectdevelop fontSize="2rem" /> e-Participation
            </div>
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse className="justify-content-end">
            <Nav>
              <Nav.Link href="/login">Iniciar Sesion</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      </div>
    </Styles>
  );
};
