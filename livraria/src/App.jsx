import './App.css';
import {BrowserRouter, Routes, Route} from 'react-router-dom'
import Home from './pages/Home';
import NavBar from './component/NavBar';
import Register from './pages/Register'; 
import MyBooks from './pages/MyBooks';
import StorePage from './pages/StorePage';
import Book from './pages/Book';


function App() {
  return (
    <div className='login'>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path="/" element={<StorePage />} />
          <Route path="/login" element={<Home />}/>
          <Route path="/cadastro" element={<Register />} /> 
          <Route path='/livros/:id' element={<MyBooks />}/>
          <Route path='/loja/:id' element={<Book />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
