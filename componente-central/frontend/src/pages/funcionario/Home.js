import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { NotiError } from "../../components/Notification";
import { Button } from "react-bootstrap";
import picture from "../../assets/default-picture.jpg";
import Modal, { ModalProvider } from "styled-react-modal";
import {
  getUsuario,
  getIniciativas,
  getProcesos,
  fetchUserID,
} from "../../services/Requests";

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
  h1 {
    padding-top: 15px;
  }
  #izquierda {
    width: 45%;
    float: left;
    color: black;
    padding: 15px;
    margin-top: 10px;
    margin-left: 30px;
    margin-bottom: 70px;
    height: 43rem;
    border-radius: 10px;
    overflow: scroll;
  }
  #izquierda::-webkit-scrollbar {
    display: none;
  }
  #derecha {
    width: 45%;
    float: right;
    color: black;
    padding: 15px;
    margin-top: 10px;
    margin-right: 30px;
    height: 43rem;
    border-radius: 10px;
    overflow: scroll;
    margin-bottom: 70px;
  }
  #derecha::-webkit-scrollbar {
    display: none;
  }
  article {
    width: 100%;
    padding: 10px;
    float: left;
    background-color: white;
    margin-bottom: 20px;
    margin-top: 10px;
    border-radius: 10px;
  }
  #todasButton {
    float: right;
  }
  Button {
    float: right;
    margin-left: 10px;
  }
  figure {
    float: right;
  }
  #header {
    color: black;
  }
`;

export default function Home() {
  const usuario = fetchUserID();

  const [iniciativas, setIniciativas] = useState([]);
  const [procesos, setProcesos] = useState([]);
  const [funcionario, setFuncionario] = useState({
    nombre: "",
    correo: "",
    fechaNac: "",
    domicilio: "",
    nacionalidad: "",
  });

  useEffect(() => {
    getIniciativas()
      .then((response) => {
        console.log(response.data);
        setIniciativas(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    getProcesos()
      .then((response) => {
        console.log(response.data);
        setProcesos(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    getUsuario(usuario)
      .then((response) => {
        setFuncionario(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [iniciativaId, setIniciativaId] = useState();
  const [procesoId, setProcesoId] = useState();
  const [seguidores, setSeguidores] = useState([]);
  const [seguidoresProc, setSeguidoresProc] = useState([]);
  const [adheridos, setAdheridos] = useState([]);
  const [participantes, setParticipantes] = useState([]);
  const [isOpenSeguidores, setIsOpenSeguidores] = useState(false);
  const [isOpenAdheridos, setIsOpenAdheridos] = useState(false);
  const [isOpenParticipantes, setIsOpenParticipantes] = useState(false);
  const [isOpenSeguidoresProc, setIsOpenSeguidoresProc] = useState(false);

  const toggleModalParticipantes = () => {
    setIsOpenParticipantes(!isOpenParticipantes);
  };

  const toggleModalSeguidores = () => {
    setIsOpenSeguidores(!isOpenSeguidores);
  };

  const toggleModalAdheridos = () => {
    setIsOpenAdheridos(!isOpenAdheridos);
  };

  const toggleModalSeguidoresProc = () => {
    setIsOpenSeguidoresProc(!isOpenSeguidoresProc);
  };

  return (
    <Styles>
      <ModalProvider>
        <nav>
          <h1 align="center" id="header">Bienvenido {funcionario.nombre}!!</h1>
        </nav>
        <aside id="izquierda">
          {iniciativas.map((ini) => {
            return (
              <article key={ini.nombre}>
                <figure>
                  <img
                    src={ini.recurso ? ini.recurso : picture}
                    alt="The Pulpit Rock"
                    width="160"
                    height="115"
                  />
                </figure>
                <h6>{ini.fecha}</h6>
                <h1>{ini.nombre}</h1>
                <p>{ini.descripcion}</p>
                <Button href={"/iniciativa?nombre=" + ini.nombre}>
                  Ver mas
                </Button>
                <Button
                  id="botonSeguir"
                  onClick={() => {
                    setIniciativaId(ini.nombre);
                    setSeguidores(ini.seguidores);
                    console.log(ini.seguidores);
                    toggleModalSeguidores();
                  }}
                >
                  Seguidores
                </Button>
                <Button
                  id="adButton"
                  onClick={() => {
                    setIniciativaId(ini.nombre);
                    setAdheridos(ini.adheridos);
                    toggleModalAdheridos();
                  }}
                >
                  Adheridos
                </Button>
              </article>
            );
          })}
          <Button id="todasButton" variant="light" href="/iniciativas">
            Ver mas iniciativas
          </Button>
        </aside>
        <aside id="derecha">
          {procesos.map((procs) => {
            return (
              <article key={procs.nombre}>
                <h6>{procs.fecha}</h6>
                <h1>{procs.nombre}</h1>
                <p>Alcance: {procs.descripcionAlcance}</p>
                <Button href={"/proceso?nombre=" + procs.nombre}>
                  Ver mas
                </Button>
                <Button
                  onClick={() => {
                    setProcesoId(procs.nombre);
                    setParticipantes(procs.participantes);
                    toggleModalParticipantes();
                  }}
                >
                  Participantes
                </Button>
                <Button
                  onClick={() => {
                    setProcesoId(procs.nombre);
                    setSeguidoresProc(procs.seguidores);
                    toggleModalSeguidoresProc();
                  }}
                >
                  Seguidores
                </Button>
              </article>
            );
          })}
          <Button id="todasButton" variant="dark" href="/procesos">
            Ver mas procesos
          </Button>
        </aside>
        <StyledModal
          isOpen={isOpenSeguidores}
          onBackgroundClick={toggleModalSeguidores}
          onEscapeKeydown={toggleModalSeguidores}
        >
          <h2>Iniciativa {iniciativaId}</h2>
          <hr />
          <div className="cuerpo">
            <h6>Seguidores</h6>
          </div>
          {seguidores.length !== 0 ? seguidores.map((seguidor, index) => {
            return (
              <div>
                <li key={index}>{seguidor}</li>
              </div>
            );
          }): <h4>Esta iniciativa no tiene seguidores</h4>}
          <div className="abajo">
            <Button onClick={toggleModalSeguidores}>Ok, termine</Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={isOpenAdheridos}
          onBackgroundClick={toggleModalAdheridos}
          onEscapeKeydown={toggleModalAdheridos}
        >
          <h2>Iniciativa {iniciativaId}</h2>
          <hr />
          <div className="cuerpo">
            <h6>Adheridos</h6>
          </div>
          {adheridos.length !== 0 ? adheridos.map((adherido, index) => {
            return (
              <div>
                <li key={index}>{adherido}</li>
              </div>
            );
          }) : <h4>Esta iniciativa no tiene adheridos</h4>}
          <div className="abajo">
            <Button onClick={toggleModalAdheridos}>Ok, termine</Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={isOpenParticipantes}
          onBackgroundClick={toggleModalParticipantes}
          onEscapeKeydown={toggleModalParticipantes}
        >
          <h2>Proceso {procesoId}</h2>
          <hr />
          <div className="cuerpo">
            <h6>participantes</h6>
          </div>
          {participantes !== [] ? participantes.map((participante, index) => {
            return (
              <div>
                <li key={index}>{participante}</li>
              </div>
            );
          }): <h4>Este proceso no tiene participantes</h4>}
          <div className="abajo">
            <Button onClick={toggleModalParticipantes}>Ok, termine</Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={isOpenSeguidoresProc}
          onBackgroundClick={toggleModalSeguidoresProc}
          onEscapeKeydown={toggleModalSeguidoresProc}
        >
          <h2>Proceso {procesoId}</h2>
          <hr />
          <div className="cuerpo">
            <h6>seguidores</h6>
          </div>
          {seguidoresProc !== [] ? seguidoresProc.map((seguidor, index) => {
            return (
              <div>
                <li key={index}>{seguidor}</li>
              </div>
            );
          }): <h4>Este proceso no tiene seguidores</h4>}
          <div className="abajo">
            <Button onClick={toggleModalSeguidoresProc}>Ok, termine</Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}
