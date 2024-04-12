import { useState, useEffect } from 'react'
import { useParams, useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios';
import "./Css/MyBooks.css";
import AvailableComponent from '../component/AvailableComponent';
import AddressComponent from '../component/AddressComponent';
import TopBar from '../component/TopBar';
import SalesMap from '../component/SalesMap';

const MyBooks = () => {
    const navigate = useNavigate()
    const location = useLocation()
    const navigator = useNavigate()
    const user = location.state.user
    const token = location.state.token
    const { id } = useParams()
    const idUser = location.state.id
    const [books, setBooks] = useState([])
    const [sales, setSales] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [imageUser, setImageUser] = useState(null)
    const [addresses, setAddresses] = useState([])
    const [addressVisible, setAddressVisible] = useState(false)

    const config = {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }

    const handleOpenAddress = () =>{
        setAddressVisible(true)
    }

    const handleFileInputChange = async (event) => {
        const file = event.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
        
            try {
                await axios.put(`http://localhost:8765/auth-service/api/usuarios/imagem/${user.id}`, formData, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'multipart/form-data'
                    }
                })
                console.log('Imagem enviada com sucesso')
        
                fetchData()
            } catch (error) {
                console.error('Erro ao enviar imagem:', error)
            }
        }        
    }

    useEffect(() => {
        if (idUser === null) {
            navigator("/login");
            return;
        }
        fetchData();
    }, [id, idUser, navigator]);

    const fetchData = async () => {
        try {
            const response = await axios.get(`http://localhost:8765/auth-service/api/usuarios/${user.id}`, config);
            setBooks();
            console.log(response.data.sales)
            setSales(response.data.sales)
            setAddresses(response.data.addresses)
            setIsLoading(false)
            console.log(response.data)
            // Setar a URL completa da imagem
            setImageUser(`http://localhost:8765/auth-service/api/usuarios/imagem/${response.data.image}`)

        } catch (error) {
            setError(error)
            setIsLoading(false)
        }
    }

    const handleChangePassword = () => {
        navigate("/changePassword")
      }

    if (isLoading) {
        return <div>Carregando...</div>;
    }

    if (error) {
        return <div>Erro: {error.message}</div>;
    }

    return (
        <div>
            <TopBar search={""} setSearch={""} />
            
            <div className="perfil-txt">
                <h1>Perfil</h1>
            </div>
    
            <div className="enderecos-txt">
                <h1>Endereços</h1>
            </div>
            <div className='container-superior'>
                <div className='address_content'>
                    <ul className="livros">
                        {addresses.map(address => (
                            <li key={address.id} className="livro-item-address" >
                                <div className="address-info">
                                    <p>País: {address.country}</p>
                                    <p>CEP: {address.zipCode}</p>
                                    <p>Cidade: {address.city}</p>
                                    <p>Bairro: {address.neighborhood}</p>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
                <div className='perfil'>
                    <div className="perfil-content">
                        <img className="imagem_perfil" src={imageUser ? imageUser : 'iconImage.png'} alt="" />
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
                            <button className='btn_senha' onClick={handleChangePassword}>Alterar senha</button>
                            <button className='btn_endereco' onClick={handleOpenAddress}>Cadastrar Endereço</button>
                        </div>
                    </div>
                    <div className='div_infos'>
                        <p><strong>Nome:</strong> {user.name}</p>
                        <p><strong>E-mail:</strong> {user.email}</p>
                    </div>
                </div>
            </div>
            {addressVisible && 
                <div className="overlayAddress">
                    <AddressComponent id={user.id}/>
                    <button className="close-button" onClick={() => setAddressVisible(false)}>X</button>
                </div>
            }            
            <div className='seusPedidos'>
                <h1>Seus Pedidos</h1>
            </div>
            <SalesMap sales={sales} user={user}/>
        </div>
    )
}       

export default MyBooks;
