import React, { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import styled from "styled-components";
import { Footer } from "../components/Footer";
import { Layout } from "../components/Layout";
import { Form } from "react-bootstrap";
import { HiClipboardCopy } from "react-icons/hi";
import Comentario from "../components/Comentario";
import Modal, { ModalProvider } from "styled-react-modal";
import { getIniciativa, fetchUserID } from "../services/Requests";
import { Noti, NotiError } from "../components/Notification";
import { useSearchParams } from "react-router-dom";
import { TwitterShareButton} from 'react-twitter-embed';
import { comentarIniciativa } from "../services/Requests";

import picture from "../assets/default-picture.jpg";

const StyledModal = Modal.styled`
  border-radius: 5px;
  padding: 1.5%;
  width: 25%;
  align-items: center;
  justify-content: center;
  background-color: white;
  overflow-y:inherit !important;

  .cuerpo{
    margin-bottom: 15px;
  }
  .abajo{
    text-align: right;
  }
  Button {
    margin-left: 5px;
  }
`;

const Styles = styled.div`
  #imagen {
    margin-top: 50px;
  }
  #invisible {
    padding-bottom: 35px;
  }
  h3 {
    margin-top: 50px;
  }
  Button {
    margin-bottom: 5px;
    margin-right: 20px;
  }
  #comentarButton {
    background-color: transparent;
    color: black;
    font-weight: bold;
    border-width: medium;
    float: right;
  }
  .comentarios {
    margin-top: 5px;
    margin-bottom: 10px;
    text-align: center;
  }
`;

export default function Iniciativa() {
  const usuario = fetchUserID();
  const [params] = useSearchParams();
  const nombre = params.get("nombre");
  const [isOpen, setIsOpen] = useState(false);
  const [comment, setComment] = useState("");

  const [ini, setIniciativa] = useState({
    nombre: "",
    descripcion: "",
    fecha: "",
    estado: "",
    creador: "",
    adheridos: [],
    seguidores: [],
    comentarios: [],
    recurso: "",
  });

  useEffect(() => {
    getIniciativa(nombre)
      .then((response) => {
        console.log(response.data);
        setIniciativa(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const toggleModal = () => {
    if (fetchUserID() === "") {
      NotiError("debes estar logueado");
    } else {
      setIsOpen(!isOpen);
    }
  };

  const comentar = () => {
    comentarIniciativa(comment, usuario, ini.nombre).then(() => {
      Noti("comentario realizado con exito!!");
    })
    .catch((error) => {
      NotiError(error.data);
    });
    toggleModal();
    Notification("comentario realizado con exito!!");
  };

  const compartirWpp = () => {
    navigator.clipboard.writeText(window.location.href);
    Noti("URL compiada con exito!!");
  };

  const handleCommentChange = (e) => {
    setComment(e.target.value);
  };

  return (
    <Styles>
      <Layout>
        <header>{ini.fecha}</header>
        <nav align="center">
          <h1>{nombre}</h1>
          <h6>creador: {ini.creador}</h6>
        </nav>
        <p align="center">{ini.descripcion}</p>
        <figure id="imagen" align="center">
          <img
            src={ini.recurso ? ini.recurso : picture}
            alt="imagen"
            width="160"
            height="115"
          />
        </figure>
        <h4>Seguidores: {ini.seguidores.length}</h4>
        <h4>Adheridos: {ini.adheridos.length}</h4>
        <div>
          <h6>Compartir:</h6>
          <Button onClick={compartirWpp} id="comentarButton">
              <HiClipboardCopy />
          </Button>
          <br />
          <TwitterShareButton 
            url={window.location.href}
          />
          <Button
            id="comentarButton"
            onClick={() => {
              toggleModal();
            }}
          >
            Comentar
          </Button>
        </div>
        <br/><br/>
        <div className="comentarios">
          {ini.comentarios.length !== 0 ? ini.comentarios.map((com, index) => {
            return <Comentario key={index} data={com} />;
          }):<h3>No hay comentarios</h3>}
        </div>
      </Layout>
      <ModalProvider>
        <StyledModal
          isOpen={isOpen}
          onBackgroundClick={toggleModal}
          onEscapeKeydown={toggleModal}
        >
          <h4>Comentario en {nombre}</h4>
          <hr />
          <div className="cuerpo">
            <h6>{usuario}</h6>
          </div>
          <Form.Group className="mb-3">
            <Form.Label>Comentario:</Form.Label>
            <Form.Control
              onChange={handleCommentChange}
              as="textarea"
              rows={5}
            />
          </Form.Group>
          <div className="abajo">
            <Button onClick={comentar}>Comentar</Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <div>
        <p id="invisible"></p>
      </div>
      <Footer />
    </Styles>
  );
}
