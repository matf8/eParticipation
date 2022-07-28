import React, { useState, useEffect } from "react";
import Poll from "@gidesan/react-polls";

function Votacion({ opciones, pregunta }) {
  const [pollAnswers, setPollAnswers] = useState([]);

  useEffect(() => {
    setPollAnswers(opciones);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [votacion, setVotacion] = useState(opciones);

  const guardarVotacion = () => {
    console.log(votacion);
  };

  const handleVote = (voteAnswer) => {
    const newPollAnswers = votacion.map((answer) => {
      if (answer.option === voteAnswer) {
        answer.votes++;
      }
      return answer;
    });
    setVotacion(newPollAnswers);
    guardarVotacion();
  };
  const pollStyles2 = {
    questionSeparator: false,
    questionSeparatorWidth: "question",
    questionBold: false,
    questionColor: "#4F70D6",
    align: "center",
    theme: "blue",
  };
  return (
    <div>
      <Poll
        question={pregunta}
        answers={pollAnswers}
        onVote={handleVote}
        customStyles={pollStyles2}
        noStorage
      />
    </div>
  );
}

export default Votacion;
