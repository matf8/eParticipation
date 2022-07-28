import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Button, ListGroup } from "react-bootstrap";
import DatePicker from "react-datepicker";
import { NotiError, Noti } from "../../components/Notification";
import Poll from "@gidesan/react-polls";
import { 
  getProcesos, 
  getProceso, 
  getProcesosRango,
  updateProceso,
  participarProceso,
  ciudadanoParticipoProceso,
  eleccionProcesoCiudadano,
  fetchUserID,
  getUsuario,
  ciudadanoSigueProceso,
  seguirAProceso,
 } from "../../services/Requests";
import Modal, { ModalProvider } from "styled-react-modal";

import "react-datepicker/dist/react-datepicker.css";

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
  #nombre {
    border-radius: 10px;
  }
`;

const Styles = styled.div`
  #buscador {
    position: absolute;
    height: 10%;
    width: 70%;
    margin-top: 15px;
    margin-left: 40px;
    margin-bottom: 15px;
    border-radius: 10px;
    color: black;
    padding-top: 50px;
    padding-left: 5%;
  }

  #listado {
    position: absolute;
    width: 70%;
    height: 70%;
    margin-top: 150px;
    margin-left: 40px;
    border-radius: 10px;
    overflow: scroll;
    border-style: solid;
    color: black;
  }
  input {
    margin-left: 5px;
  }
  #verMasButton {
    float: right;
  }
  #listado::-webkit-scrollbar {
    display: none;
  }
`;

function Procesos() {
  const usuario = fetchUserID();
  const [data, setData] = useState([]);
  const [nombre, setNombre] = useState({ nombre: "" });
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [ciudadano, setCiudadano] = useState({
    nombre: "",
    correo: "",
    fechaNac: "",
    domicilio: "",
    nacionalidad: "",
  });

  useEffect(() => {
    getProcesos(nombre, startDate, endDate)
      .then((response) => {
        setData(response.data);
      })
      .catch((error) => {
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

  const handleChange = (e) => {
    e.persist();
    setNombre((nombre) => ({
      ...nombre,
      [e.target.name]: e.target.value,
    }));
  };

  const onChangeDate = (dates) => {
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if(nombre.nombre !== "") {
      setData([]);
      getProceso(nombre.nombre).then((response) => {
        console.log(response.data);
        if(response.data === "proceso null") {
          NotiError("proceso no existe");
        } else {
          let newData = [];
          newData.push(response.data);
          setData(newData);
        }
      })
      .catch((error) => {console.log(error.data)});
    } else if(nombre.nombre === "" && startDate === null && endDate === null) {
      getProcesos()
      .then((response) => {
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    } else {
      getProcesosRango(startDate, endDate).then((response) => {
        setData(response.data);
      })
      .catch((error) => {console.log(error.data)});
    }
  }

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
    updateProceso(proceso);
    participar();
    Notification("participacion registrada!");
  };

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

  const pollStyles1 = {
    questionSeparator: true,
    questionSeparatorWidth: "question",
    questionBold: true,
    questionColor: "#303030",
    align: "center",
    theme: "blue",
  };

  const [proc] = useState({nombre: "" });
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

  const onClearDate = () => {
    setStartDate(null);
    setEndDate(null);
  };
  return (
    <Styles>
      <Layout>
        <form onSubmit={handleSubmit} id="buscador">
          <div class="row align-items-center">
            <div class="col-md-4">
              <label htmlFor="nombre">Nombre</label>
              <input
                onChange={handleChange}
                name="nombre"
                id="nombre"
                type="text"
              />
            </div>
            <div class="col-md-3">
              <DatePicker
                id="fechas"
                name="fechas"
                selected={startDate}
                onChange={onChangeDate}
                startDate={startDate}
                endDate={endDate}
                selectsRange
                dateFormat="yyyy-MM-dd"
                placeholderText="Fechas Entrega"
              />
            </div>
            <div class="col-md-3">
              <Button onClick={() => onClearDate()} variant="danger">
                clear date
              </Button>
            </div>
            <div class="col-md-2">
              <Button type="submit" variant="secondary">Buscar</Button>
            </div>
          </div>
        </form>
        <div id="listado">
          {data !== null &&
            data.map((p) => {
              return (
                <ListGroup class="pb-5">
                  <ListGroup.Item
                    id="item"
                    class="d-flex justify-content-between align-items-center mb-3"
                  >
                    <div class="container">
                      <div class="row">
                        <div class="col-md-3">
                          <p> {p.nombre} </p>
                        </div>
                        <div id="comment" class="col-md-4">
                          <p>{p.fecha}</p>
                          <p>{p.fase}</p>
                        </div>
                        <div id="verMasButton" class="col-md-2">
                          <Button
                            id="verMasButton"
                            href={"/proceso?nombre=" + p.nombre}
                          >
                            ver mas
                          </Button>
                        </div>
                        <div id="verMasButton" class="col-md-2">
                          <Button
                            id="verMasButton"
                            onClick={() => {
                              proc.nombre = p.nombre;
                              onSeguirProceso();
                            }}
                          >
                            Seguir
                          </Button>
                        </div>
                        <div id="verMasButton" class="col-md-2">
                          <Button
                            id="verMasButton"
                            onClick={() => {
                              setProceso(proceso);
                              ciudadanoParticipoProceso(p.nombre, ciudadano.correo).then((response) => {
                                const participo = response.data;
                                console.log(response.data);
                                console.log(p);
                                if(!participo) {
                                  sacarOpciones(p);
                                } else {
                                  sessionStorage.setItem("tieneOpciones", false);
                                }
                                participar();
                              })
                            }}
                          >
                            participar
                          </Button>
                        </div>
                      </div>
                    </div>
                  </ListGroup.Item>
                </ListGroup>
              );
            })}
        </div>
      </Layout>
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
              <h6>ya votaste en la opcion {eleccionProcesoCiudadano(proceso.nombre, usuario)} en este proceso</h6>
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

export default Procesos;
