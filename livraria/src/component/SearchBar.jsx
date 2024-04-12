import "./Css/SearchBar.css"
import { useNavigate } from 'react-router-dom'
import { useLocation } from 'react-router-dom'

const SearchBar = ({token}) => {
  const navigate = useNavigate()
  const location = useLocation()
  const id = location.state ? location.state.id : null
  const user = location.state ? location.state.user : null

  const handleSubmit = (event) => {
    event.preventDefault();
  }
  
  const handleLogin = () => {
    navigate("/login")
  }
  const handleMyCart = () => {
    navigate("/cart" , {
      state:{
        user : user,
        token : token
      }
    })
  }
  const handleRegister = () => {
    navigate("/cadastro")
  }

  const handleMyBooks = () => {
    navigate(`/livros/${id}`, {
      state: {
          token: token,
          id : id,
          user : user
      }
  })  }
  
  return (
    <div className='container_store'> 
      <div className="search-form" onSubmit={handleSubmit}></div>
      {user ? (
        <div className="txt-welcome">
          <p className="welcome">Welcome {user.name}</p>
        </div>
      ) : (
        <div className="btns_logins">
          <button type='submit' className='login_button' onClick={handleLogin}>Sing in</button>
          <button type='submit' className='register_button' onClick={handleRegister}>Sing up</button>
        </div>
      )}
      <div className="icon_buttons"> 
        <button type='submit' className='cart_button' onClick={handleMyCart}>
          <img src="cart.png" alt=""  className="cart-icon"/>
        </button>
        <button type='submit' className='perfil_button' onClick={handleMyBooks}>
          <img src="icon.png" alt="" className="icon-user"/>
        </button>
      </div>
    </div>
  )
}

export default SearchBar;
