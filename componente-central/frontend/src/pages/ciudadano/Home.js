import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { NotiError, Noti } from "../../components/Notification";
import { Button } from "react-bootstrap";
import Modal, { ModalProvider } from "styled-react-modal";
import Poll from "@gidesan/react-polls";
import {
  getIniciativasPublicadas,
  getProcesosCiudadano,
  updateProceso,
  getUsuario,
  ciudadanoAdheridoIniciativa,
  participarProceso,
  ciudadanoParticipoProceso,
  eleccionProcesoCiudadano,
  ciudadanoSigueIniciativa,
  ciudadanoSigueProceso,
  seguirAIniciativa,
  adherirAIniciativa,
  fetchUserID,
  seguirAProceso,
} from "../../services/Requests";

import picture from "../../assets/default-picture.jpg";

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
    height: 45rem;
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
    height: 45rem;
    border-radius: 10px;
    overflow: scroll;
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
  const [iniciativa] = useState({nombre: ""});
  const [proc] = useState({nombre: "" });
  const [eleccionCiudadano, setEleccion] = useState("");

  const onSeguir = () => {
    ciudadanoSigueIniciativa(iniciativa.nombre, ciudadano.correo).then((response) => {
      console.log(response.data);
      if(response.data === false) {
        seguirAIniciativa(iniciativa.nombre, ciudadano.correo).then((res) => {
          if (res.status === 200) {
            Noti("Estas siguiendo a la iniciativa " + iniciativa.nombre);
            setTimeout(() => {
              window.location.reload();
            }, 3000);
          } else {
            NotiError("hubo un error inesperado");
          }
        })
      } else {
        NotiError("ya seguis a esta iniciativa");
      }
    })
  .catch((error) => {
    console.log(error.data)
  });
  };

  const onSeguirProceso = () => {
    ciudadanoSigueProceso(proc.nombre, ciudadano.correo).then((response) => {
      console.log(response.data);
      if(response.data === false) {
        seguirAProceso(proc.nombre, ciudadano.correo).then((res) => {
          if (res.status === 200) {
            Noti("Estas siguiendo al proceso " + proc.nombre);
            setTimeout(() => {
              window.location.reload();
            }, 3000);
          } else {
            NotiError("hubo un error inesperado");
          }
        })
      } else {
        NotiError("ya seguis a esta iniciativa");
      }
    })
  .catch((error) => {
    console.log(error.data)
  });
  };

  const [participarOpen, isParticiparOpen] = useState();
  const [pregunta, setPregunta] = useState();
  const [opciones, setOpciones] = useState();
  const [proceso, setProceso] = useState({
    id: "",
    nombre: "",
    descripcionAlcance: "",
    fecha: "",
    fase: "",
    creador: "",
    participantes: [],
    instrumento: "",
    contenidoInstrumento: [],
  });

  const participar = () => {
    isParticiparOpen(!participarOpen);
  };

  const formatearRespuesta = (res) => {
    let retorno = [];
    for(let i = 0; i < res.length; i++) {
      const opcion = obtenerOpcionRetorno(res[i]);
      retorno.push(opcion);
    }
    return retorno;
  }

  const obtenerOpcionRetorno = (content) => {
    const valueOption = content.option;
    const valueVotes = content.votes.toString();
    return "option:" + valueOption + ",votes:" + valueVotes;
  }

  const onParticipar = (opcion) => {
    const newAnswers = opciones.map((answer) => {
      if (answer.option === opcion) {
        answer.votes++;
      }
      return answer;
    });
    const respuesta = [proceso.instrumento, pregunta, opcion];
    participarProceso({proceso: proceso.nombre, user: ciudadano.correo, respuesta: respuesta});
    proceso.contenidoInstrumento = formatearRespuesta(newAnswers);
    sessionStorage.removeItem("tieneOpciones");
    console.log(proceso);
    updateProceso(proceso);
    participar();
    Notification("participacion registrada!");
  };

  const onAdherirse = () => {
    ciudadanoAdheridoIniciativa(iniciativa.nombre, ciudadano.correo).then((response) => {
      if(response.data === false) {
        adherirAIniciativa(iniciativa.nombre, ciudadano.correo).then((res) => {
          if (res.status === 200) {
            Noti("Estas adherido a la iniciativa " + iniciativa.nombre);
            setTimeout(() => {
              window.location.reload();
            }, 3000);
          } else {
            NotiError("hubo un error inesperado");
          }
        });
      } else {
        NotiError("ya estas adherido a esta iniciativa");
      }
    }).catch((error) => {console.log(error.data);});
  };

  const [iniciativas, setIniciativas] = useState([]);
  const [procesos, setProcesos] = useState([]);
  const [ciudadano, setCiudadano] = useState({
    nombre: "",
    correo: "",
    fechaNac: "",
    domicilio: "",
    nacionalidad: "",
  });

  const sacarPregunta = (p) => {
      const pregunta = p.contenidoInstrumento[p.contenidoInstrumento.length - 1];
      setPregunta(pregunta);
  }

  const obtenerOpcion = (content) => {
    const array = content.split(",");//array[0] es la option y array[1] es el votes
    const valueOption = array[0].split(":")[1];
    const valueVotes = array[1].split(":")[1];
    return {"option": valueOption, "votes": valueVotes};    
  }

  const sacarOpciones = (pr) => {
    const contenido = pr.contenidoInstrumento;
    let tempOpciones = [];
    if (contenido != null) {
      for(let i = 0; i < contenido.length-1; i++) {
        const opcion = obtenerOpcion(contenido[i]);
        tempOpciones.push(opcion);
      }
    } else {
      NotiError("Proceso sin instrumento");
    }
    setOpciones(tempOpciones);
    sacarPregunta(pr);
    sessionStorage.setItem("tieneOpciones", true);
  }

  useEffect(() => {
    getIniciativasPublicadas()
      .then((response) => {
        setIniciativas(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
        NotiError(error.response.data);
      });
    getProcesosCiudadano(usuario)
      .then((response) => {
        console.log(response.data);
        setProcesos(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
        NotiError(error.response.data);
      });
    getUsuario(usuario)
      .then((response) => {
        setCiudadano(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const pollStyles1 = {
    questionSeparator: true,
    questionSeparatorWidth: "question",
    questionBold: true,
    questionColor: "#303030",
    align: "center",
    theme: "blue",
  };

  const ciudadanoSigue = (id) => {
    ciudadanoSigueProceso(id, usuario).then((res) => {
      return res;
    });
  }

  const ciudadanoAdherido = (id) => {
    ciudadanoAdheridoIniciativa(id, usuario)
      .then((res) => {
        return res;
      })
  };

  const eleccionCiudadanoProc = (p, u) => {
    eleccionProcesoCiudadano(p, u).then((res) => {     
      setEleccion(res.data);
    });
  }

  return (
    <Styles>
      <nav>
        <h1 align="center" id="header">Bienvenido {ciudadano.nombre}!!</h1>
      </nav>
      <aside id="izquierda">
        {iniciativas.length !== 0 ? iniciativas.map((ini, index) => {
          return (
            <article key={index}>
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
              {ciudadanoSigue(ini.nombre) === true && <h6>siguiendo</h6>}
              {ciudadanoAdherido(ini.nombre) === true && <h6>adherido</h6>}
              <p>{ini.descripcion}</p>
              <Button href={"/iniciativa?nombre=" + ini.nombre}>Ver mas</Button>
              <Button
              id="botonSeguir"
              onClick={() => {
                iniciativa.nombre = ini.nombre;
                onSeguir();
              }}
              >
                Seguir
              </Button>
              <Button
                id="adButton"
                onClick={() => {
                  iniciativa.nombre = ini.nombre;
                  onAdherirse();
                }}
              >
                Adherirse
              </Button>
            </article>
          );
        }): <h1>no hay iniciativas</h1>}
        <Button id="todasButton" variant="light" href="/iniciativas">
          Ver mas iniciativas
        </Button>
      </aside>
      <aside id="derecha">
        {procesos.length !== 0 ? procesos.map((proceso) => {
          return (
            <article key={proceso.id}>
              <h6>{proceso.fecha}</h6>
              <h1>{proceso.nombre}</h1>
              <p>Alcance: {proceso.descripcionAlcance}</p>
              <Button href={"proceso?nombre=" + proceso.nombre}>Ver mas</Button>
              <Button
              id="botonSeguir"
              onClick={() => {
                proc.nombre = proceso.nombre;
                onSeguirProceso();
              }}
              >
                Seguir
              </Button>
              <Button
                onClick={() => {
                  setProceso(proceso);
                  ciudadanoParticipoProceso(proceso.nombre, ciudadano.correo).then((response) => {
                    const participo = response.data;
                    console.log(response.data);
                    console.log(proceso);
                    if(!participo) {
                      sacarOpciones(proceso);
                    } else {
                      sessionStorage.setItem("tieneOpciones", false);
                    }
                    eleccionCiudadanoProc(proceso.nombre, ciudadano.correo);
                    participar();
                  })
                }}
              >
                Participar
              </Button>
            </article>
          );
        }): <h1>no hay procesos</h1>}
        <Button id="todasButton" variant="dark" href="/procesos">
          Ver mas procesos
        </Button>
      </aside>
      <ModalProvider>
        <StyledModal
          isOpen={participarOpen}
          onBackgroundClick={participar}
          onEscapeKeydown={participar}
        >
          <h4>Participar en {proceso.nombre}</h4>
          <hr />
          <div className="cuerpo">
            {sessionStorage.getItem("tieneOpciones") === "true" ? (
              <Poll
                id="poll"
                question={pregunta}
                answers={opciones}
                onVote={onParticipar}
                customStyles={pollStyles1}
                noStorage
              />
            ) : (
              <h6>ya votaste en la opcion {eleccionCiudadano} en este proceso</h6>
            )}
          </div>
          <div className="abajo">
            <Button
              variant="danger"
              onClick={() => {
                participar();
              }}
            >
              Termine
            </Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}
