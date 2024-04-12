import { useState } from "react";
import "./Css/AddressComponent.css";

const AddressComponent = ({ id }) => {
    // Criar um objeto para armazenar os valores das variáveis de endereço
    const [address, setAddress] = useState({
        zipCode: "",
        country: "",
        state: "",
        city: "",
        neighborhood: "",
        street: "",
        number: ""
    })

    // Extrair os valores das variáveis de endereço do estado
    const { zipCode, country, state, city, neighborhood, street, number } = address

    const [visivel, setVisivel] = useState(true)

    const handleSubmit = async () => {
        try {
            const data = await fetch("http://localhost:8765/auth-service/api/address", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    ...address, 
                    user: {
                        id: id,
                    },
                }),
            })

            alert("Avaliação feita com sucesso")
        } catch (error) {
            console.error('Erro ao enviar a avaliação:', error)
        }
    }

    const handleClose = () => {
        setVisivel(false)
    }

    // Função para atualizar o estado das variáveis de endereço
    const handleChange = (e) => {
        setAddress({
            ...address, // Manter os valores existentes
            [e.target.name]: e.target.value // Atualizar o valor da variável correspondente
        })
    }

    return (
        <div className={`modalAddress-view ${visivel ? 'show' : 'hide'}`}>
            <h1>Cadastrar</h1>
            <form action="" className="form-address">
                <input 
                    type="text"
                    value={zipCode} 
                    name='zipCode' 
                    onChange={handleChange} 
                    placeholder="CEP"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={country} 
                    name='country' 
                    onChange={handleChange} 
                    placeholder="País"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={state} 
                    name='state' 
                    onChange={handleChange} 
                    placeholder="Estado"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={city} 
                    name='city' 
                    onChange={handleChange} 
                    placeholder="Cidade"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={neighborhood} 
                    name='neighborhood' 
                    onChange={handleChange} 
                    placeholder="Bairro"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={street} 
                    name='street' 
                    onChange={handleChange} 
                    placeholder="Rua"
                    className="form_login-input"
                />
                <input 
                    type="text"
                    value={number} 
                    name='number' 
                    onChange={handleChange} 
                    placeholder="Número"
                    className="form_login-input"
                />
            </form>
            <button onClick={handleSubmit} className='btn_addresses'>Salvar</button>
        </div>
    )
}

export default AddressComponent;
