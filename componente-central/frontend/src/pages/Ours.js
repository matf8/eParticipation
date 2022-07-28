import React from "react";
import styled from "styled-components";

const Styles = styled.p`
  p {  
    margin-top: 30px;
    font-size: 20px;
    text-align: center;
    clear:both;
  }
  h1 {
    margin-top: 30px;
    text-align: center;
    clear:both;
  }
`;

function Ours() {
  return (
    <Styles>
      <h1>
        <strong>Descripción de la iniciativa</strong>
      </h1>
      <p>
        Lugar de encuentro: TBA <br/>
        No se permiten mascotas. <br/>
        Se espera un máximo de 1500 personas. <br/>
        Por consultas específicas comunicarse al 091 326 674 o via mail: eparticipation@gmail.com <br/>
      </p> 
      <p>
        eParticipation se reserva el derecho de cambiar los términos del
        presente documento en cualquier momento.
      </p>      
    </Styles>
  );
}

export default Ours;
