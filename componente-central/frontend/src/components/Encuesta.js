import React, { useEffect, useState } from "react";
import Poll from "@gidesan/react-polls";
import styled from "styled-components";

const Styles = styled.div`
  /* .app {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    min-height: 100vh;
  } */
  .app .main {
    display: flex;
    justify-content: center;
    align-self: center;
  }

  .app .main > div {
    margin: 0 20px;
    border-radius: 5px;
    box-shadow: 0px 0px 7px 0px rgba(0, 0, 0, 0.2);
    width: 322px;
  }

  @media (max-width: 768px) {
    .app .main {
      flex-direction: column;
    }

    .app .main > div {
      margin: 0 0 20px 0;
    }
  }
`;

function Encuesta({ pregunta, opciones }) {
  const [pollAnswers, setPollAnswers] = useState([]);
  const [voted, setVoted] = useState(false);
  const [respuesta, setRespuesta] = useState("");

  useEffect(() => {
    setPollAnswers(opciones);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [encuesta, setEncuesta] = useState(opciones);

  const guardarEncuesta = () => {
    console.log(encuesta);
  };

  const handleVote = (voteAnswer) => {
    const newPollAnswers = encuesta.map((answer) => {
      if (answer.option === voteAnswer) {
        answer.votes++;
      }
      return answer;
    });
    setEncuesta(newPollAnswers);
    guardarEncuesta();
    setRespuesta(voteAnswer);
    setVoted(true);
  };

  const pollStyles1 = {
    questionSeparator: true,
    questionSeparatorWidth: "question",
    questionBold: true,
    questionColor: "#303030",
    align: "center",
    theme: "blue",
  };

  return (
    <Styles>
      {!voted ? (
        <div className="app">
          <main className="main">
            <div>
              <Poll
                id="poll"
                question={pregunta}
                answers={pollAnswers}
                onVote={handleVote}
                customStyles={pollStyles1}
                noStorage
              />
            </div>
          </main>
        </div>
      ) : (
        <h1>Has votado {respuesta}</h1>
      )}
    </Styles>
  );
}

export default Encuesta;
