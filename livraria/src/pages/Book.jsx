import { useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import "./Css/Book.css";

const Book = () => {
  const location = useLocation();
  const book = location.state.book;
  const id = location.state.id;
  const navigate = useNavigate();

  // Adicionando livros
  const [dono] = useState(location.state.idUser);
  const [isbn] = useState(book.isbn);
  const [titulo] = useState(book.titulo);
  const [autor] = useState(book.autor);
  const [estoque, setEstoque] = useState(book.estoque);
  const [paginas] = useState(book.paginas);
  const [anoLancamento] = useState(book.anoLancamento);
  const [preco] = useState(book.preco);
  const [sinopse] = useState(book.sinopse);
  const [imagem] = useState(book.imagem);

  // URL para enviar os dados do livro
  const url = "http://localhost:8765/book-service/api/livros/cadastrar";

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (estoque > 0) {
      try {
        // Atualizar o estado do estoque localmente
        const updatedStock = estoque - 1;
        setEstoque(updatedStock);
  
        // Realizar a solicitação POST para cadastrar o livro
        await fetch(url, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            dono,
            isbn,
            titulo,
            autor,
            paginas,
            anoLancamento,
            preco,
            sinopse,
            imagem
          })
        });
  
        // Realizar a solicitação PUT para atualizar o estoque do livro
        await fetch(`http://localhost:8765/book-service/store/atualizar/${id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            estoque: updatedStock,
            dono,
            isbn,
            titulo,
            autor,
            paginas,
            anoLancamento,
            preco,
            sinopse,
            imagem
          })
        });

        // Exibir um alerta de compra efetuada com sucesso
        alert('Compra efetuada com sucesso!');
        
      } catch (error) {
        console.error('Erro:', error);
      }
    }
  };

  return (
    <div className='container_livro'>
      <img src={book.imagem} alt={book.titulo} className="book-image" />
      <div className='container_infos'>
        <h2>{book.titulo}</h2>
        <p>por: {book.autor}</p>
        <p className='sinopse'>Sinopse: {book.sinopse}</p>
        <p>páginas: {book.paginas}</p>
        <p>isbn: {book.isbn}</p>
        <h2 className='book_preco'> R$ {book.preco}</h2>
        {estoque ? (
          <h2 className='estoque'>
            Em estoque
          </h2>
        ) : (<h2>Sem disponibilidade</h2>)}
              <p className='txt_quantidade'>  Unidades : {estoque} </p>
        <button className='btn_comprar' onClick={handleSubmit}>Comprar</button>
      </div>
    </div>
  );
}

export default Book;
