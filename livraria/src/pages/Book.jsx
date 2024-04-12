import { useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import "./Css/Book.css";
import axios from 'axios';
import StarRating from '../component/StarRating';
import TopBar from "../component/TopBar";

const Book = () => {
  const location = useLocation()
  const book = location.state.book
  const id = location.state.id
  const idUser = location.state.idUser
  const user = location.state.user
  const navigate = useNavigate()
  const token = location.state.token

  const [stock, setStock] = useState(book.stock)
  const [availables, setAvailables] = useState([])
  const [media, setMedia] = useState("")
  const [numAvaliacoes, setNumAvaliacoes] = useState("")

  // URL para enviar os dados do livro
  const url = "http://localhost:8765/book-service/api/livros/cadastrar";

  const config = {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }

  const handleMyCart = (e) => {
    handleSubmit(e)
    navigate("/cart" , {
      state:{
        user : user,
        token : token
      }
    })
    

  }

  const mediaAvaliable = async () => {
    try {
        const response = await axios.get(`http://localhost:8765/auth-service/api/livros/${book.id}`);
        const avaliacoes = response.data.availables

        if (avaliacoes && avaliacoes.length > 0) {
            let soma = 0
            for (let i = 0; i < avaliacoes.length; i++) {
                soma += avaliacoes[i].value
            }
            const media = soma / avaliacoes.length
            setMedia(media)
            setNumAvaliacoes(avaliacoes.length)
        } else {
            setMedia(0) // Se não houver avaliações, define a média como 0
        }
    } catch (error) {
        console.error('Erro ao obter avaliações:', error);
    }
  }

  // Função para obter a disponibilidade de livros
  const fetchAvailableBooks = async () => {
    try {
      const responseAvailable = await axios.get(`http://localhost:8765/auth-service/api/livros/${book.id}`);
      setAvailables(responseAvailable.data.availables);
    } catch (error) {
      console.error('Erro na requisição axios:', error);
    }
  }
  

  useEffect(() => {
    mediaAvaliable()
    fetchAvailableBooks()
  }, [])

  

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (stock > 0) {
      try {
        // Realizar a solicitação POST para cadastrar o livro
        await fetch("http://localhost:8765/auth-service/api/order", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            books: [
              {
                id: book.id
              }
            ],
            user: {
              id: idUser
            },
            amount : book.price,
            date: new Date().toISOString(),
            complet: false,
            
          })
        })        
        
      } catch (error) {
        console.error('Erro:', error)
      }
    }

    alert("Pedido adicionado ao carrinho")
  }

  return (
    <div className='container_livro'>
      <div>
        <TopBar search={""} setSearch={""} />
      </div>
      <img src={book.image} alt={book.title} className="book-image" />
      <div className='container_infos'>
        <h2>{book.title}</h2>
        <p>por: {book.author}</p>
        <p className='sinopse'>Sinopse: {book.synopsis}</p>
        <p>páginas: {book.pages}</p>
        <p>isbn-10: {book.isbn10}</p>
        <p>isbn-13: {book.isbn13}</p>
        <StarRating rating={media} numAvaliacoes={numAvaliacoes} />
        <h2 className='book_preco'> R$ {book.price}</h2>
        {stock ? (
          <h2 className='estoque'>
            Em estoque
          </h2>
        ) : (<h2>Sem disponibilidade</h2>)}
        <p className='txt_quantidade'>  Unidades : {stock} </p>
        <div className='buttons_compra'>
          <button className='btn-cart' onClick={handleSubmit}>Adicionar ao Carrinho</button>
          <button className='btn_comprar' onClick={handleMyCart}>Comprar</button>
        </div>

      </div>
      <div className='container_avaliables'>
        <div className='avaliaçoes'>
          <h1>Avaliações</h1>
        </div>
      <ul>
        {availables.map(available => (
          <li key={available.id} className='infos_avaliables'>
            <div className="user-info">            
              <div className='description'>
                <p>{available.description}</p>
                <p>
                  {/* if ternario paras as estrelas */}
                  {available.value >= 1 ? '★' : '☆'}
                  {available.value >= 2 ? '★' : '☆'}
                  {available.value >= 3 ? '★' : '☆'}
                  {available.value >= 4 ? '★' : '☆'}
                  {available.value >= 5 ? '★' : '☆'}
              </p>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
    </div>
  );
};

export default Book;
