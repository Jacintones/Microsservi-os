import { useState } from "react";
import "./Css/AvailableComponent.css";

const AvailableComponent = ({ id, bookId }) => {
    const [desc, setDesc] = useState("");
    const [rating, setRating] = useState(0);
    const [visivel, setVisivel] = useState(true);

    const handleSubmit = async () => {
        try {
            const data = await fetch("http://localhost:8765/auth-service/available", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    description: desc,
                    value: rating, 
                    book: {
                        id: bookId,
                    },
                }),
            })

            alert("Avaliação feita com sucesso")
        } catch (error) {
            console.error('Erro ao enviar a avaliação:', error);
        }
    };

    const handleRatingClick = (value) => {
        setRating(value);
    };

    const handleClose = () => {
        setVisivel(false);
    };

    return (
        <div className={`modalAvailable ${visivel ? 'show' : 'hide'}`}>
            <h1>Escreva sua Avaliação</h1>
            <textarea
                value={desc}
                onChange={(e) => setDesc(e.target.value)}
                cols="10"
                rows="10"
                style={{ resize: "none" }}
            />
            <div className="rating">
                {[1, 2, 3, 4, 5].map((value) => (
                    <span
                        key={value}
                        className={`star ${value <= rating ? "filled" : ""}`}
                        onClick={() => handleRatingClick(value)}
                    >
                        &#9733;
                    </span>
                ))}
            </div>
            <button onClick={handleSubmit} className="btn_mandar">Salvar</button>
        </div>
    );
};

export default AvailableComponent;
