import React, { useState } from 'react';
import "./Css/SearchBar.css"
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

const SearchBar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const id = location.state ? location.state.id : null;
  const user = location.state ? location.state.user: null;
  const token =  location.state ? location.state.token : null;

  const handleChange = (event) => {
    setSearch(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
  };
  
  const handleLogin = () => {
    navigate("/login")
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
  });  }
  
  return (
    <div className='container_store'> 
    <div className="search-form" onSubmit={handleSubmit}>
    </div>
      <button type='submit' className='login_button' onClick={handleLogin}>Login</button>
      <button type='submit' className='register_button' onClick={handleRegister}>Cadastro</button>
      <button type='submit' className='perfil_button' onClick={handleMyBooks}>Meu Perfil</button>
    </div>

  );
  
};

export default SearchBar;
