import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { Footer } from "../../components/Footer";
import { Layout } from "../../components/Layout";
import { Button, ListGroup } from "react-bootstrap";
import DatePicker from "react-datepicker";
import { NotiError } from "../../components/Notification";
import { getIniciativas, deleteIniciativa, getIniciativasRango, getIniciativa } from "../../services/Requests";
import Modal, { ModalProvider } from "styled-react-modal";
import { MdOutlineModeEditOutline } from "react-icons/md";
import { CgClose } from "react-icons/cg";

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
  }
  #adherirmeButton {
  }
  #listado::-webkit-scrollbar {
    display: none;
  }
  #nombre {
    border-radius: 10px;
  }
`;

function Iniciativas() {
  const [data, setData] = useState([]);
  const [nombre, setNombre] = useState({ nombre: "" });
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  useEffect(() => {
    getIniciativas(nombre, startDate, endDate)
      .then((response) => {
        console.log(response.data);
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [iniciativaId, setIniciativaId] = useState();
  const [seguidores, setSeguidores] = useState([]);
  const [adheridos, setAdheridos] = useState([]);
  const [isOpenSeguidores, setIsOpenSeguidores] = useState(false);
  const [isOpenAdheridos, setIsOpenAdheridos] = useState(false);

  const toggleModalSeguidores = () => {
    setIsOpenSeguidores(!isOpenSeguidores);
  };

  const toggleModalAdheridos = () => {
    setIsOpenAdheridos(!isOpenAdheridos);
  };

  const [eliminarOpen, isEliminarOpen] = useState();
  const [iniciativa, setIniciativa] = useState();

  const eliminarIniciativa = () => {
    isEliminarOpen(!eliminarOpen);
  };

  const onDelete = () => {
    deleteIniciativa(iniciativa)
      .then((res) => {
        console.log(res);
        window.location.reload();
      });
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

  const handleSubmit = (e) => {
    e.preventDefault();
    if(nombre.nombre !== "") {
      setData([]);
      getIniciativa(nombre.nombre).then((response) => {
        if(response.data === "Dato null") {
          NotiError("iniciativa no existe");
        } else {
          let newData = [];
          newData.push(response.data);
          setData(newData);
        }
      })
      .catch((error) => {console.log(error.data)});
    } else if(nombre.nombre === "" && startDate === null && endDate === null) {
      getIniciativas()
      .then((response) => {
        setData(response.data);
      })
      .catch((error) => {
        NotiError(error.response.data);
      });
    } else {
      getIniciativasRango(startDate, endDate).then((response) => {
        setData(response.data);
      })
      .catch((error) => {console.log(error.data)});
    }
  }

  const onClearDate = () => {
    setStartDate(null);
    setEndDate(null);
  };
  return (
    <Styles>
      <ModalProvider>
        <Layout>
          <form onSubmit={handleSubmit} id="buscador">
            <div class="row align-items-center">
              <div class="col-md-4">
                <label htmlFor="nombre" style={{ color: 'black'}}>Nombre</label>
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
              data.map((ini) => {
                return (
                  <ListGroup class="pb-5">
                    <ListGroup.Item
                      id="item"
                      class="d-flex justify-content-between align-items-center mb-3"
                    >
                      <div class="container">
                        <div class="row">
                          <div class="col-md-2">
                            <p> {ini.nombre} </p>
                          </div>
                          <div id="comment" class="col-md-2">
                            <p>{ini.fecha}</p>
                            <p>{ini.comentario}</p>
                          </div>
                          <div id="adherirmeButton" class="col-md-2">
                            <Button
                              onClick={() => {
                                setIniciativaId(ini.nombre);
                                setAdheridos(ini.adheridos);
                                toggleModalAdheridos();
                              }}
                            >
                              Ver adheridos
                            </Button>
                          </div>
                          <div class="col-md-2">
                            <Button
                              onClick={() => {
                                setIniciativaId(ini.nombre);
                                setSeguidores(ini.seguidores);
                                toggleModalSeguidores();
                              }}
                            >
                              Ver seguidores
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-1">
                            <Button
                              href={"/modificariniciativa?nombre=" + ini.nombre}
                            >
                              <MdOutlineModeEditOutline />
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-1">
                            <Button>
                              <CgClose
                                onClick={() => {
                                  setIniciativa(ini.nombre);
                                  eliminarIniciativa();
                                }}
                              />
                            </Button>
                          </div>
                          <div id="verMasButton" class="col-md-1">
                            <Button href={"/iniciativa?nombre=" + ini.nombre}>
                              mas
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
          }): <h4>esta iniciativa no tiene seguidores</h4>}
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
          }): <h4>esta iniciativa no tiene adheridos</h4>}
          <div className="abajo">
            <Button onClick={toggleModalAdheridos}>Ok, termine</Button>
          </div>
        </StyledModal>
        <StyledModal
          isOpen={eliminarOpen}
          onBackgroundClick={eliminarIniciativa}
          onEscapeKeydown={eliminarIniciativa}
        >
          <h4>Eliminar {iniciativa}</h4>
          <hr />
          <div className="cuerpo">
            <h6>Seguro que quieres eliminar la iniciativa {iniciativa}?</h6>
          </div>
          <div className="abajo">
            <Button variant="secondary" onClick={eliminarIniciativa}>
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                onDelete();
                eliminarIniciativa();
              }}
            >
              Confirmar
            </Button>
          </div>
        </StyledModal>
      </ModalProvider>
      <Footer />
    </Styles>
  );
}

export default Iniciativas;
