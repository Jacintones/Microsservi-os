import React, { useState } from 'react'
import AvailableComponent from './AvailableComponent'

const SalesMap = ({sales, user}) => {

    const [selectedBookId, setSelectedBookId] = useState(null)
    const [availableComponentVisible, setAvailableComponentVisible] = useState(false)

    const handleWriteReview = (id) => {
        setSelectedBookId(id);
        setAvailableComponentVisible(true); 
    }

    const handleAvailableComponentClose = () => {
        setAvailableComponentVisible(false); 
    }

  return (
    <div>
        <div className='compras_container'>
            {sales.map(sale => (
                <div key={sale.id}>
                    <ul className="livros">
                        {sale.orders.map(order => (
                            order.books.map(book => (
                                <li key={book.id} className="livro-item">
                                    <div className="book-info">
                                        <div className="image-wrapper">
                                            <img src={book.image} alt={book.title} />
                                        </div>
                                        <div className="info-wrapper">
                                            <h3>{book.title}</h3>
                                            <p>Autor: {book.author}</p>
                                            <p>Páginas: {book.pages}</p>
                                            <p>Sinopse: {book.synopsis}</p>
                                            <button className='btn_avaliacao' onClick={() => handleWriteReview(book.id)}>Escrever Avaliação</button>
                                            {selectedBookId === book.id && 
                                                <div className="overlay">
                                                    <AvailableComponent id={user.id} bookId={selectedBookId} onClose={handleAvailableComponentClose} />
                                                    <button className="close-button" onClick={() => setSelectedBookId(null)}>X</button>
                                                </div>
                                            }
                                        </div>
                                    </div>
                                </li>
                            ))
                        ))}
                    </ul>
                </div>
            ))}
        </div>      
    </div>
  )
}

export default SalesMap
