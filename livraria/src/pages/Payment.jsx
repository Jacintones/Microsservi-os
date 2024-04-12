import { useState } from 'react'
import {useNavigate } from 'react-router-dom'
import { useLocation } from 'react-router-dom'
import axios from 'axios' // Importe o axios para fazer requisições HTTP

import "./Css/Payment.css"


const Payment = () => {
    const [method, setMethod] = useState("")
    const navigate = useNavigate()
    const location = useLocation()
    const orders = location.state.orders
    const token = location.state.token
    const user = location.state.user
    
    // Função para selecionar um método de pagamento e desmarcar os outros
    const handleMethodSelect = (selectedMethod) => {
        setMethod(selectedMethod);
    };

    const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }

    const calculateTotal = () => {
        let total = 0    
        orders.forEach((order) => {
            if (Array.isArray(order.books)) {
                order.books.forEach((book) => {
                    total += book.price
                })
            }
        })
        return total.toFixed(2); // Formata o total com duas casas decimais
    }

    const handleBuy = async () => {
        try {
            const incompleteOrders = orders.filter(order => !order.complet)
            let allBooks = []
            
            // Agrupar todos os livros de todos os pedidos em um único array
            incompleteOrders.forEach(order => {
                allBooks = allBooks.concat(order.books);
            })
    
            // Atualizar o formato dos pedidos para conter apenas os IDs dos livros
            const orderData = incompleteOrders.map(order => ({
                id: order.id,
            }))
    
            // Fazer uma requisição PUT para atualizar o estoque de todos os livros
            await Promise.all(
                allBooks.map(book =>
                    fetch(`http://localhost:8765/auth-service/api/livros/update/stock/${book.id}`, {
                        method: "PUT",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        }
                    })
                )
            )
    
            // Fazer uma requisição POST para registrar a venda com todos os pedidos
            const response = await axios.post("http://localhost:8765/auth-service/api/sale", {
                amount: calculateTotal(), // Total de todos os livros
                user: {
                    id: user.id,
                },
                orders: orderData,
                date: new Date().toISOString(),
                paymentMethod: method
            }, config)
    
            // Atualizar o status de todos os pedidos para completos
            await Promise.all(
                incompleteOrders.map(order =>
                    fetch(`http://localhost:8765/auth-service/api/order/status/${order.id}`, {
                        method: "PUT",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        }
                    })
                )
            );
    
            console.log("Compra cadastrada com sucesso:", response.data)
            alert("Compra feita com sucesso")
        } catch (error) {
            console.error("Erro ao cadastrar compra:", error)
        }
    }
    
    const handleStore = () =>{
        navigate("/store" , {
            state:{
              user : user,
              token : token
            }
          })
    }

    return (
        <div>
            <div className='logo-store'>
                <button className='btn-library' onClick={handleStore}>Jacinto's Libray</button>
            </div>
            <div className='page-payment'>
                <div className='title-payment'>
                    <h2>Finalizar compra</h2>
                </div>
                <div className='payment-select-credit' onClick={() => handleMethodSelect('CREDIT_CARD')}>
                    <input type="checkbox" name='Cartão de crédito' checked={method === 'CREDIT_CARD'} readOnly />
                    <img src="card.png" alt="" className='card-img' />
                    <label className='lbl-payment' >Cartão de crédito</label>
                </div>
                <div className='payment-select-debit' onClick={() => handleMethodSelect('DEBIT_CARD')}>
                    <input type="checkbox" name='Cartão de débito' checked={method === 'DEBIT_CARD'} readOnly />
                    <img src="card.png" alt="" className='card-img' />
                    <label className='lbl-payment'>Cartão de débito</label>
                </div>
                <div className='payment-select-pix' onClick={() => handleMethodSelect('PIX')}>
                    <input type="checkbox" name='PIX' checked={method === 'PIX'} readOnly />
                    <img src="logo-pix-520x520.png" alt="" className='pix-image'/>
                    <label className='lbl-payment' >Pix</label>
                </div>
            </div>
            <div className='total-payment'>
                <div className='prices-cart'>
                    <h1>Resumo</h1>
                    <p className='priceTotal'>Total: R${calculateTotal()}</p>
                    <button className="btn-buy-payment" onClick={handleBuy}>Finalizar a compra</button>
                </div>
            </div>
        </div>
    )
}

export default Payment
