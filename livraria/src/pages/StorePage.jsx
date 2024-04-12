import { useState, useEffect, useRef, useMemo } from "react"
import { useFetch } from "../hooks/UseFetch"
import { useLocation } from 'react-router-dom'
import TopBar from "../component/TopBar"
import LookBook from "../component/LookBook"
import CheckBox from "../component/CheckBox"
import "./Css/StorePage.css"

const StorePage = () => {
    const location = useLocation()
    const url = "http://localhost:8765/auth-service/api/livros"
    const { data, loading, error } = useFetch(url)
    const [search, setSearch] = useState('')
    const [filteredBooks, setFilteredBooks] = useState([]) // Novo estado para os livros filtrados
    const token =  location.state ? location.state.token : null

    // Criar referências separadas para cada seção de livros
    const adventureRef = useRef(null)
    const horrorRef = useRef(null)
    const fantasyRef = useRef(null)

    const searchLowerCase = search.toLowerCase(); 

    useEffect(() => {
        if (data) {
            const filteredBooks = data.filter(book => 
                book.title.toLowerCase().includes(searchLowerCase) && book.stock > 0
            )
            setFilteredBooks(filteredBooks)
        }
    }, [data, searchLowerCase])

    const adventureBooks = useMemo(() => {
        if (!filteredBooks) return []

        return filteredBooks.filter(book => book.categoryType === 'ADVENTURE')
    }, [filteredBooks])

    const horrorBooks = useMemo(() => {
        if (!filteredBooks) return []

        return filteredBooks.filter(book => book.categoryType === 'HORROR')
    }, [filteredBooks])

    const fantasyBooks = useMemo(() => {
        if (!filteredBooks) return []

        return filteredBooks.filter(book => book.categoryType === 'FANTASY')
    }, [filteredBooks])


    
    return (
        <div>
            <TopBar search={search} setSearch={setSearch} token={token}/>
            <CheckBox livros={filteredBooks} setLivros={setFilteredBooks} /> 
            <div>
                <div className="Wrapper">
                    <div className="ListContainer">
                        <div className="LivroList">
                            <h1 className="adventure-title">AVENTURA</h1>
                            <LookBook books={adventureBooks} scrollRef={adventureRef} />
                            <h1 className="horror-title">TERROR</h1>
                            <LookBook books={horrorBooks} scrollRef={horrorRef} />
                            <h1 className="fantasy-title">FANTASIA</h1>
                            <LookBook books={fantasyBooks} scrollRef={fantasyRef} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default StorePage;
