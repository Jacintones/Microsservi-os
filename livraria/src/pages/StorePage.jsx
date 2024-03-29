import { useState, useEffect } from "react"
import styled from 'styled-components'
import { useFetch } from "../hooks/UseFetch"
import "./Css/StorePage.css"
import SearchBar from "../component/SearchBar"
import { useNavigate } from "react-router-dom"
import { useLocation } from 'react-router-dom'
import CheckBox from "../component/CheckBox"

const ListContainer = styled.div`
    border-radius: 5px;
    display: flex;
    position: relative;
    top: 175px;
    left: 105px;
    background-color: #fff;
    width: 88%;
    color: #29303b;
    padding: 10px;
    min-height: 600px;
`;

const VerticalLine = styled.div`
    border-left: 2px solid #353535;
    height: 100vh; /* Define a altura como 100% da altura da viewport */
    position: fixed; /* Define a posição fixa */
    top: 0; /* Alinha o topo do elemento ao topo da viewport */
    bottom: 0; /* Alinha a parte inferior do elemento à parte inferior da viewport */
`;


const Wrapper = styled.div`
    display: flex;
    flex-direction: row-reverse;
`;

const LivroList = styled.ul`
    display: flex;
    flex-wrap: wrap;
    list-style-type: none;
    padding: 0;
`;

const LivroItem = styled.li`
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    border-radius: 10px;
    padding: 10px;
    margin-right: 50px;
    margin-bottom: 50px;
    width: 190px;
    height: 320px;
    list-style: none;
    border: 1px solid #ddd;
    transition: border-color 0.3s;
    &:hover {
        background-color: #f5f5f5;
        border-color: transparent;
    }
`;

const TitlePane = styled.div`
    font-weight: 700;
    margin-bottom: 5px;
    font-family: Montserrat;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: pre-wrap;
    word-wrap: break-word;
`;

const Thumbnail = styled.img`
    width: 150px;
    height: 230px;
    border: 0;
    vertical-align: middle;
`;

const PricePane = styled.div`
    margin-top: 25px;
    font-family: Montserrat;
    font-weight: bold;
    color : #125ab5;
`;

const StorePage = () => {
    const location = useLocation()
    const idUser = location.state ? location.state.id : null
    const navigate = useNavigate()
    const url = "http://localhost:8765/book-service/store/comercio"
    const { data, loading, error } = useFetch(url)
    const [books, setBooks] = useState([])
    const [search, setSearch] = useState('')
    const searchLowerCase = search.toLowerCase()
    const bookFiltered = books.filter(livro => livro.titulo.toLowerCase().includes(searchLowerCase) && livro.estoque > 0)

    useEffect(() => {
        if (data) {
            setBooks(data)
        }        
    }, [data])

    const handleBookClick = (book) => {
        console.log("Livro clicado:", book)
        navigate(`/loja/${book.id}`, {
            state: {
                id : book.id,
                idUser : idUser,
                book : book
            }
        })
    }

    return (
        <div>
            <div className="barra_pesquisa">
                <h2 className="title_store">Jacintos Library</h2>
                <input className="effect-19" placeholder="Pesquisar" type="search" value={search} onChange={(e) => setSearch(e.target.value)}/>
                <span className="focus-border"></span>
                <div className="elements_buttons">
                    <SearchBar />
                </div>
            </div>
            <div>
                <Wrapper>
                    <div className="informaçoes">
                        <h1>Livros Disponíveis:</h1>
                    </div>
                    <VerticalLine className="vertical-line" />
                    <ListContainer>
                        <LivroList>
                            {bookFiltered.map(book => (
                                <LivroItem key={book.id} onClick={() => handleBookClick(book)}>
                                    <Thumbnail src={book.imagem} />
                                    <TitlePane>{book.titulo}</TitlePane>
                                    <PricePane>R${book.preco}</PricePane>
                                </LivroItem>
                            ))}
                        </LivroList>
                    </ListContainer>
                </Wrapper>
            </div>
            <div className="SeletoresWrapper">
                <div className="Seletores">
                    <CheckBox livros={books} setLivros={setBooks} />
                </div>
            </div>
        </div>
    );
}

export default StorePage;
