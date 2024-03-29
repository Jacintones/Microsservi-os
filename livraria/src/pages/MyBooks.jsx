import { useState, useEffect } from 'react'
import { useParams, useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import "./Css/MyBooks.css"

const MyBooks = () => {
    const location = useLocation()
    const navigator = useNavigate()
    const user = location.state.user
    const token = location.state.token
    const { id } = useParams()
    const idUser = location.state.id
    const [books, setBooks] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [image, setImage] = useState(null)

    const handleFileInputChange = async (event) => {
        const file = event.target.files[0]
    
        if (file) {
            const formData = new FormData()
            formData.append('file', file)
    
            try {
                await axios.put(`http://localhost:8765/auth-service/api/usuarios/imagem/${idUser}`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: `Bearer ${token}`
                    }
                })
                console.log('Imagem enviada com sucesso')
                
                // Atualizar a imagem após o envio
                fetchData()
            } catch (error) {
                console.error('Erro ao enviar imagem:', error)
            }
        }
    }
    
    useEffect(() => {
        if (idUser === null){
            navigator("/login")
            return;
        }
        fetchData();
    }, [id, token, idUser, navigator])

    const fetchData = async () => {
        const config = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
        try {
            const response = await axios.get(`http://localhost:8765/auth-service/api/usuarios/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            setBooks(response.data.livros)
            setIsLoading(false)
            
            // Setar a URL completa da imagem
            setImage(`http://localhost:8765/auth-service/api/usuarios/imagem/${response.data.imagem}`, config)
        } catch (error) {
            setError(error)
            setIsLoading(false)
        }
    };

    if (isLoading) {
        return <div>Carregando...</div>
    }

    if (error) {
        return <div>Erro: {error.message}</div>
    }

    return (
        <div>
            <h1 className='txt_perfil'>Perfil</h1>
            <div className='perfil'>
                <div className="perfil-content">
                    <img className="imagem_perfil" src={image} alt="" />
                    <div className="info">
                        <input
                            type="file"
                            id="fileInput"
                            style={{ display: 'none' }} 
                            onChange={handleFileInputChange}
                        />
                        <label htmlFor="fileInput" className="custom-file-upload">
                            Alterar<br />imagem
                        </label>
                        <button className='btn_senha'>Alterar senha</button>
                    </div>
                </div>
                <div className='div_infos'>
                    <p><strong>Nome:</strong> {user.nome}</p>
                    <p><strong>E-mail:</strong> {user.email}</p>
                </div>
            </div>
            <h1 className='txt_compras'>Seus Pedidos</h1>
            <div className='compras_container'>
                <ul className="livros">
                    {books.map(book => (
                        <li key={book.id} className="livro-item">
                            <div className="book-info">
                                <div className="image-wrapper">
                                    <img src={book.imagem} alt={book.titulo} />
                                </div>
                                <div className="info-wrapper">
                                    <h3>{book.titulo}</h3>
                                    <p>Autor: {book.autor}</p>
                                    <p>Páginas: {book.paginas}</p>
                                    <p>Sinopse: {book.sinopse}</p>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default MyBooks;
