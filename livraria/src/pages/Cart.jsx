import React, { useEffect, useState } from "react"
import axios from "axios"
import { useLocation } from "react-router-dom"
import { useNavigate } from 'react-router-dom'
import "./Css/Cart.css"
import TopBar from "../component/TopBar"

const Cart = () => {
  const navigate =  useNavigate()
  const location = useLocation()
  const [orders, setOrders] = useState([])
  const [books, setBooks] = useState([])
  const user = location.state.user
  const token = location.state.token

  const ordersNotComplet = () => {
    // Filtra os pedidos onde complet é false
    const ordersFalse = orders.filter(element => !element.complet)
    
    // Retorna o novo array com os pedidos não completos
    return ordersFalse
  }

  const filteredOrders = ordersNotComplet()

  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }

  const calculateTotal = () => {
    let total = 0
    const filteredOrders = orders.filter(order => !order.complet) // Filtra apenas as orders com complet false

    for (const order of filteredOrders){
      total += order.amount
    }

    return total
  }

  const handleBuy = async () => {
    navigate("/payment", {
      state:{
        orders : filteredOrders,
        token : token,
        user : user
      }
    })

  }
  
  const fetchAvailableBooks = async () => {
    try {
      const userData = await axios.get(
        `http://localhost:8765/auth-service/api/usuarios/${user.id}`,
        config
      )
      if (userData.data.orders) {
        setOrders(userData.data.orders)
        console.log(userData.data.orders)
      }
    } catch (error) {
      console.error("Erro na requisição axios:", error)
    }
  }

  useEffect(() => {
    fetchAvailableBooks()
    calculateTotal()
  }, [])

  return (
    <div>
      <TopBar search={""} setSearch={""} />
      <div className="pedidos-title">
          <h1>Pedidos</h1>
        </div>
      <div className="page">
      <ul>
        {orders.length > 0 ? (
          filteredOrders.map((order) =>
            order.books.map((book) => (
              <li key={book.id}>
                <div className="container-orders">
                  <div key={book.id} className="container-books">
                    <div className="container-unique">
                      <div className="container-infos-cart">
                        <p className="book-title">{book.title}</p>
                        <p className="price-cart">R${book.price}</p>
                      </div>
                      <img
                        src={book.image}
                        className="image-order"
                        alt=""
                      />
                    </div>
                  </div>
                </div>
              </li>
            ))
          )
        ) : (
          <li>
            <p>Nenhum pedido encontrado.</p>
          </li>
        )}
      </ul>

      <div className="order-total">
        <h2>Total: R${calculateTotal()}</h2>
        
        </div>
      <div className="div-btn-order">
        <button className="button-order" onClick={handleBuy}>
          Comprar
        </button>
      </div>
    </div>
    </div>
  )
}

export default Cart