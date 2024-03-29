import React, { useState } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "./Css/Register.css";

const Register = () => {
    const url = "http://localhost:8765/auth-service/api/usuarios";
    const authUrl = "http://localhost:8765/auth-service/auth";
    const navigate = useNavigate();
    const [nome, setNome] = useState("");
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [equalSenha, setEqualSenha] = useState("");
    const [role, setRole] = useState("USER");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        // Validação de entrada
        if (!nome || !email || !senha || !equalSenha) {
            setError("Por favor, preencha todos os campos.");
            return;
        }

        if (senha !== equalSenha) {
            setError("As senhas não correspondem.");
            return;
        }

        setLoading(true);
        console.log("Enviando solicitação de registro...");

        try {
            // Enviar solicitação de registro
            const res = await axios.post(url, {
                nome,
                email,
                senha,
                role
            });
            
            const authToken = await axios.post(authUrl, {
                email,
                senha
            });
        
            const usuario = res.data
            console.log(res.data)
            console.log(authToken.data)

            // Limpar campos de entrada
            setNome('');
            setEmail('');
            setSenha('');
            setEqualSenha('');
            setLoading(false);

            // Redirecionar para página principal
            navigate("/", {
                state: {
                    token: authToken.data,
                    id: usuario.id,
                    user: usuario
                }
            });
        } catch (error) {
            setLoading(false);
            setError(error.response?.data?.message || error.message || "Erro ao registrar usuário.");
            console.error("Erro ao registrar usuário:", error);
        }
    };

    const getAuthToken = async ({ email, senha }) => {
        try {
            console.log("Obtendo token de autenticação...");
            const response = await axios.post(authUrl, { email, senha });
            return response.data.token;
        } catch (error) {
            console.error("Erro ao obter token de autenticação:", error);
            throw new Error(error.response?.data?.message || "Erro ao autenticar usuário.");
        }
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="form_container">
                <h1 className="cadastro">Cadastrar Cliente</h1>
                {error && <div className="error">{error}</div>}
                <label>
                    Nome:
                    <input type="text" value={nome} onChange={(e) => setNome(e.target.value)} />
                </label>
                <label>
                    E-mail:
                    <input type="text" value={email} onChange={(e) => setEmail(e.target.value)} />
                </label>
                <label>
                    Senha:
                    <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
                </label>
                <label>
                    Confirmar Senha:
                    <input type="password" value={equalSenha} onChange={(e) => setEqualSenha(e.target.value)} />
                </label>
                <div className="custom-select">
                    Tipo de Usuário:
                    <div className="select_options">
                   <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="USER">Usuário</option>
                        <option value="ADMIN">Administrador</option>
                    </select>
                    </div>
                </div> 
                <div className='btn_container'>
                    <button type="submit" className="btn_cadastro_2" disabled={loading}>Cadastrar</button>
                </div>
            </form>
        </div>
    );
};

export default Register;
