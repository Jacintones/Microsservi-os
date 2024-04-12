import { Link } from 'react-router-dom'

const NavBar = () => {
  //Links do programa
  return (
    <nav>
      <Link to="/"></Link>
      <Link to="/store"></Link>
      <Link to="/login"></Link> 
      <Link to="/cadastro"></Link>
      <Link to="/livros/:id"></Link>
      <Link to="/loja/:id"></Link>
      <Link to="/cart"></Link>
      <Link to="/payment"></Link>
      <Link to="/changePassword"></Link>
      <Link to="/profits" ></Link>
    </nav>
  );
}

export default NavBar;
