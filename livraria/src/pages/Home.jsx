import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { Link } from 'react-router-dom'
import "./Css/Home.css"

const Home = () => {
  const userApiUrl = "http://localhost:8765/auth-service/api/usuarios/user/"
  const navigate = useNavigate()
  const [user, setUser] = useState({
     email: "",
     password: "" 
    })
  const [token, setToken] = useState("")
  const [showPassword, setShowPassword] = useState(false);// Estado para mostrar/esconder a senha

  const handleSubmit = async (e) => {
    e.preventDefault()

    try {
        // Autenticar usuário para gerar o token e passa-lo para proximas páginas
        const authResponse = await fetch("http://localhost:8765/auth-service/auth", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })

        if (!authResponse.ok) {
            throw new Error('Erro ao autenticar') 
        }

        //Token é a resposta do auth
        const authToken = await authResponse.text() // Obter token de autenticação
        setToken(authToken)

        // Obter ID do usuário
        const userResponse = await axios.get(userApiUrl + user.email)
        const userId = userResponse.data
    
        // Configurar o cabeçalho da solicitação com o token de autenticação
        const config = {
          headers: {
            Authorization: `Bearer ${authToken}`
          }
        }
        
        //Obter os dados do usuário pelo ID usando o token de autenticação
        const userData = await axios.get(`http://localhost:8765/auth-service/api/usuarios/${userId}`, config)
        const usuario = userData.data

        // Limpar campos de entrada
        setUser({ email: "", password: "" })

        // Navegar para a próxima rota com o ID do usuário e o token de autenticação
        navigate(`/store`, {
            state: {
                token: authToken,
                id : userId,
                user : usuario,
            }
        });
    } catch (error) {
        console.error('Erro ao autenticar:', error)
    }
  }
  
  const handleShowPassword = (e) => {
    setShowPassword(!showPassword);
    const btn = e.target;
    if (showPassword) {
      btn.src = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8SA14-A-zoRaiJ2GdUiESsisaFiHNYrUZtjtjZqnth0D_KdfkwzQWIdCjbzhAoYKPTvs&usqp=CAU'
    } else {
      btn.src = 'https://icon-library.com/images/icon-eyes/icon-eyes-12.jpg'
    }
  }

  const handleChangePassword = () => {
    navigate("/changePassword")
  }

  return (
    <div>
      <div className='form-container-10'>
        <form onSubmit={handleSubmit} className="form_login">
          <h1>Login</h1>
          <label className="label-container">
            E-mail:
            <input 
            type="text"
            value={user.email} 
            name='email' 
            onChange={(e) => setUser({ ...user, email: e.target.value })} 
            className="form_login-input" />
          </label>
          <label className="label-container">
            Senha:
            <div className="password-input-container">
              <input 
                type={showPassword ? "text" : "password"} 
                value={user.password} 
                name='password' 
                onChange={(e) => setUser({ ...user, password: e.target.value })} 
                style={{ paddingRight: "40px" }}
                className="form_login-password"
              />
              <button type="button" className='btn_show_password' onClick={handleShowPassword}>
                <img src="https://icon-library.com/images/icon-eyes/icon-eyes-12.jpg" 
                alt="Mostrar Senha" 
                style={{ width: "30px", height: "30px" }} />
              </button>
            </div>
          </label>
          <div className='btn_container'>
            <button className='btn_login' type="submit">Entrar</button>
            <Link to="/cadastro">
              <button className='btn_cadastro'>Cadastra-se</button>
            </Link>
          </div>
        </form>
      </div>
      <div className='div-esqueceu'>
        <button className='btn_esqueceu' onClick={handleChangePassword}>Esqueceu a senha?</button>
      </div>
    </div>
  );  
}  

export default Home;
