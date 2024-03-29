import { Link } from 'react-router-dom'; 

const NavBar = () => {
  return (
    <nav>
      <Link to="/"></Link>
      <Link to="/login"></Link> 
      <Link to="/cadastro"></Link>
      <Link to="/livros/:id"></Link>
      <Link to="/loja/:id"></Link>
    </nav>
  );
};

export default NavBar;
