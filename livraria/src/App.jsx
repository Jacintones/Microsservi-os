import './App.css';
import {BrowserRouter, Routes, Route} from 'react-router-dom'
import Home from './pages/Home';
import NavBar from './component/NavBar';
import Register from './pages/Register'; 
import MyBooks from './pages/MyBooks';
import StorePage from './pages/StorePage';
import Book from './pages/Book';
import Cart from './pages/Cart';
import Payment from './pages/Payment';
import Initial from './pages/Initial';
import RecoveryPassword from './pages/RecoveryPassword';
import Profits from './pages/Profits';


function App() {
  return (
    <div className='login'>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path='/' element={<Initial />}/>
          <Route path="/store" element={<StorePage />} />
          <Route path="/login" element={<Home />}/>
          <Route path="/cadastro" element={<Register />} /> 
          <Route path='/livros/:id' element={<MyBooks />}/>
          <Route path='/loja/:id' element={<Book />} />
          <Route path='/cart' element={<Cart />}/>
          <Route path='/payment' element={<Payment/>}/>
          <Route path='/changePassword' element={<RecoveryPassword />}/>
          <Route path='/profits' element={<Profits />}/>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
