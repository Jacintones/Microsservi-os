import React, { useReducer } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "./Css/Register.css";

const initialState = {
    user: {
        name: "",
        email: "",
    },
    name: "",
    email: "",
    password: "",
    equalSenha: "",
    role: "USER",
    loading: false,
    error: null
};

const reducer = (state, action) => {
    switch (action.type) {
        case 'SET_FIELD':
            return {
                ...state,
                [action.field]: action.value,
                error: null
            };
        case 'SET_ERROR':
            return {
                ...state,
                error: action.error
            };
        case 'SET_LOADING':
            return {
                ...state,
                loading: action.loading
            };
        case 'RESET_FIELDS':
            return {
                ...initialState,
                loading: false
            };
        default:
            return state;
    }
};

const Register = () => {
    const url = "http://localhost:8765/auth-service/api/usuarios"
    const authUrl = "http://localhost:8765/auth-service/auth"
    const navigate = useNavigate()
    const [state, dispatch] = useReducer(reducer, initialState)
    const { user, name, email, password, equalSenha, role, loading, error } = state

    const handleSubmit = async (e) => {
        e.preventDefault();
        dispatch({ type: 'SET_ERROR', error: null });

        // Validação de entrada
        if (!name || !email || !password || !equalSenha) {
            dispatch({ type: 'SET_ERROR', error: "Por favor, preencha todos os campos." });
            return;
        }

        if (password !== equalSenha) {
            dispatch({ type: 'SET_ERROR', error: "As senhas não correspondem." });
            return;
        }

        dispatch({ type: 'SET_LOADING', loading: true });

        try {
            // Enviar solicitação de registro
            const res = await axios.post(url, {
                name,
                email,
                password
                        });
            
            const authToken = await axios.post(authUrl, {
                email,
                password
            });
        
            const usuario = res.data;

            // Limpar campos de entrada
            dispatch({ type: 'RESET_FIELDS' });

            // Redirecionar para página principal passando os dados
            navigate("/store", {
                state: {
                    token: authToken.data,
                    id: usuario.id,
                    user: usuario
                }
            });
        } catch (error) {
            dispatch({ type: 'SET_LOADING', loading: false });
            dispatch({ type: 'SET_ERROR', error: error.response?.data?.message || error.message || "Erro ao registrar usuário." });
            console.error("Erro ao registrar usuário:", error);
        }
    };

    const handleChange = (e) => {
        dispatch({ type: 'SET_FIELD', field: e.target.name, value: e.target.value });
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="form_container">
                <h1 className="cadastro">Cadastrar Cliente</h1>
                {error && <div className="error">{error}</div>}
                <label>
                    Nome:
                    <input type="text" name="name" value={name} onChange={handleChange} />
                </label>
                <label>
                    E-mail:
                    <input type="text" name="email" value={email} onChange={handleChange} />
                </label>
                <label>
                    Senha:
                    <input type="password" name="password" value={password} onChange={handleChange} />
                </label>
                <label>
                    Confirmar Senha:
                    <input type="password" name="equalSenha" value={equalSenha} onChange={handleChange} />
                </label>
                <div className='btn_container'>
                    <button type="submit" className="btn_cadastro_2" disabled={loading}>Cadastrar</button>
                </div>
            </form>
        </div>
    );
};

export default Register;
