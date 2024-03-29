import axios from "axios"
import { useState } from "react"

export const useToken = async (email, senha) => {
    const[token, setToken] = useState("")

    axios.post('http://localhost:8765/auth-service/auth', {
        email: email,
        senha: senha
    })
    .then(response => {
        const authToken = response.data;
        setToken(authToken);
        console.log('Token de autenticação obtido com sucesso:', authToken)
    
    })
    .catch(error => {
        console.error('Erro ao obter o token de autenticação:', error)
    })
}
