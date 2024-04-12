import React, { useState, useRef } from "react";
import StarRating from "./StarRating";
import { useNavigate } from "react-router-dom";
import { useLocation } from 'react-router-dom';
import "./Css/LookBook.css";

const LookBook = ({ books, scrollRef }) => {
    const location = useLocation();
    const idUser = location.state ? location.state.id : null;
    const user = location.state ? location.state.user : null;
    const token =  location.state ? location.state.token : null;
    const navigate = useNavigate();
    const [scrollPosition, setScrollPosition] = useState(0); // Estado para a posição de rolagem

    const calculateMedia = (book) => {
        let contador = 0;
        let totalAvaliacoes = 0; 
        for (const available of book.availables) {
            contador += available.value;
            totalAvaliacoes++;
        }
        const mediaTotal = contador / totalAvaliacoes;
        return Math.round(mediaTotal);
    };

    const contAvaliacoes = (book) => {
        let totalAvaliacoes = 0; 
        for (const available of book.availables) {
            totalAvaliacoes++;
        }
        return totalAvaliacoes;
    }

    const handleBookClick = (book) => {
        console.log("Livro clicado:", book);
        navigate(`/loja/${book.id}`, {
            state: {
                id : book.id,
                idUser : idUser,
                book : book,
                token : token,
                user : user
            }
        });
    };

    const handleScrollLeft = () => {
        const container = scrollRef.current;
        const newScrollPosition = scrollPosition - 200; 
        setScrollPosition(Math.max(newScrollPosition, 0));
        container.scrollTo({
            left: newScrollPosition,
            behavior: "smooth"
        });
    };

    const handleScrollRight = () => {
        const container = scrollRef.current;
        const newScrollPosition = scrollPosition + 200;
        setScrollPosition(newScrollPosition);
        container.scrollTo({
            left: newScrollPosition,
            behavior: "smooth"
        });
    };

    return (
        <div className="lookbook-container">
            <div className="horizontal-books" ref={scrollRef} style={{ transform: `translateX(-${scrollPosition}px)` }}>
                {books.map(book => (
                    <div className="LivroItem" key={book.id} onClick={() => { handleBookClick(book)}}>
                        <img className="Thumbnail" src={book.image} />
                        <p className="TitlePane">{book.title}</p>
                        <StarRating rating={calculateMedia(book)} numAvaliacoes={contAvaliacoes(book)} />
                        <p className="PricePane">R${book.price}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default LookBook;
