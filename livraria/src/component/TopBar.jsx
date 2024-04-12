import "./Css/TopBar.css"
import SearchBar from "./SearchBar"

const TopBar = ({search, setSearch, token}) => {
  return (
    <div className="topnav">
      <a className="active" href="/">Home</a>
      <a href="/store">Store</a>
      <input className="effect-19" placeholder="Pesquisar" type="search" value={search} onChange={(e) => setSearch(e.target.value)}/>
      <div className="search-buttons">
        <SearchBar token={token}/>
      </div>
    </div>
  )
}

export default TopBar;
