import TopBar from "../component/TopBar"
import "./Css/Initial.css"

import { useNavigate } from "react-router-dom"


const Initial = () => {
    const navigate = useNavigate()
    const orders = location.state ? location.state.orders : null
    const token = location.state? location.state.token : null
    const user = location.state ? location.state.user : null

    const handleChange = () => {
        navigate(`/store`)
    }

  return (
    <div>
      <div className="navBar-initial">
        <TopBar search={""} setSearch={""}/>
      </div>
        <div className="Container-intial">
            <div className="infos-home">
                <div className="info-img-container">
                    <h1>Venha conferir aqui! </h1>
                    <button onClick={handleChange} className="btn-home-store">Conferir</button>
                </div>
                <h1 className="p-infos">Nossa loja oferece todos os best-sellers disponíveis no mercado, com preços acessíveis.</h1>
            </div>
        </div>
        <div className="about-me">
            <h1>Sobre:</h1>
            <h2>Sou um estudante de Ciência da computação pela UFRPE treinando meus conhecimentos em REACT</h2>
            <p>E-mail: thiagojacinto022@gmail.com</p>
        </div>
    </div>
  )
}

export default Initial
