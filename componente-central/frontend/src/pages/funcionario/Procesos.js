import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Button, ListGroup } from "react-bootstrap";
import DatePicker from "react-datepicker";
import { NotiError } from "../../components/Notification";
import { getProcesos, deleteProceso } from "../../services/Requests";
import Modal, { ModalProvider } from "styled-react-modal";
import { CgClose } from "react-icons/cg";
import { MdOutlineModeEditOutline } from "react-icons/md";

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
  const [data, setData] = useState([]);
  const [nombre, setNombre] = useState({ nombre: "" });
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  useEffect(() => {
    getProcesos(nombre, startDate, endDate)
      .then((response) => {
        console.log(response.data);
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [procesoId, setProcesoId] = useState();
  const [involucrados, setInvolucrados] = useState([]);
  const [seguidores, setSeguidores] = useState([]);
  const [isOpenInvolucrados, setIsOpenInvolucrados] = useState(false);
  const [isOpenSeguidores, setIsOpenSeguidores] = useState(false);
  const [eliminarOpen, isEliminarOpen] = useState();
  const [proceso, setProceso] = useState();

  const eliminarProceso = () => {
    isEliminarOpen(!eliminarOpen);
  };

  const onDelete = () => {
    deleteProceso(proceso)
      .then((res) => {
        console.log(res);
        window.location.reload();
      });
  };

  const toggleModalInvolucrados = () => {
    setIsOpenInvolucrados(!isOpenInvolucrados);
  };

  const toggleModalSeguidores = () => {
    setIsOpenSeguidores(!isOpenSeguidores);
  };

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

  const onClearDate = () => {
    setStartDate(null);
    setEndDate(null);
  };
  return (
    <Styles>
      <ModalProvider>
        <Layout>
          <div id="buscador">
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
                <Button variant="secondary">Buscar</Button>
              </div>
            </div>
          </div>
          <div id="listado">
            {data !== null &&
              data.map((proc) => {
                return (
                  <ListGroup class="pb-5">
                    <ListGroup.Item
                      id="item"
                      class="d-flex justify-content-between align-items-center mb-3"
                    >
                      <div class="container">
                        <div class="row">
                          <div class="col-md-2">
                            <p> {proc.nombre} </p>
                            
                          </div>
                          <div id="comment" class="col-md-2">
                            <p>{proc.fecha}</p>
                            <p>{proc.fase}</p>
                          </div>
                          <div id="verMasButton" class="col-md-1">
                            <Button
                              href={"/modificarproceso?nombre=" + proc.nombre}
                            >
                              <MdOutlineModeEditOutline />
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-1">
                            <Button>
                              <CgClose
                                onClick={() => {
                                  setProceso(proc.nombre);
                                  eliminarProceso();
                                }}
                              />
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-2">
                            <Button
                              id="verMasButton"
                              href={"/proceso?nombre=" + proc.nombre}
                            >
                              ver mas
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-2">
                            <Button
                              id="verMasButton"
                              onClick={() => {
                                setProcesoId(proc.nombre);
                                setSeguidores(proc.seguidores);
                                toggleModalSeguidores();
                              }}
                            >
                              seguidores
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-2">
                            <Button
                              id="verMasButton"
                              onClick={() => {
                                setProcesoId(proc.nombre);
                                setInvolucrados(proc.participantes);
                                toggleModalInvolucrados();
                              }}
                            >
                              participantes
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
        <StyledModal
          isOpen={isOpenInvolucrados}
          onBackgroundClick={toggleModalInvolucrados}
          onEscapeKeydown={toggleModalInvolucrados}
        >
          <h2>Proceso {procesoId}</h2>
          <hr />
          <div className="cuerpo">
            <h6>Participantes</h6>
          </div>
          {involucrados.map((inv, index) => {
            return (
              <div>
                <li key={index}>{inv}</li>
              </div>
            );
          })}
          <div className="abajo">
            <Button onClick={toggleModalInvolucrados}>Ok, termine</Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={eliminarOpen}
          onBackgroundClick={eliminarProceso}
          onEscapeKeydown={eliminarProceso}
        >
          <h4>Eliminar {proceso}</h4>
          <hr />
          <div className="cuerpo">
            <h6>Seguro que quieres eliminar el proceso {proceso}?</h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={eliminarProceso}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDelete();
                eliminarProceso();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={isOpenSeguidores}
          onBackgroundClick={toggleModalSeguidores}
          onEscapeKeydown={toggleModalSeguidores}
        >
          <h4>Proceso {proceso}</h4>
          <hr />
          <div className="cuerpo">
            <h6>seguidores</h6>
          </div>
          {seguidores.map((s, index) => {
            return (
              <div>
                <li key={index}>{s}</li>
              </div>
            );
          })}
          <div className="abajo">
            <Button onClick={toggleModalSeguidores}>Ok, termine</Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}

export default Procesos;
