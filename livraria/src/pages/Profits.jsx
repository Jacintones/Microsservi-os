import React, { useState, useEffect } from "react";
import { useFetch } from "../hooks/UseFetch";
import Chart from "react-apexcharts";
import TopBar from "../component/TopBar";
import "./Css/Profits.css";
import { useLocation, useNavigate } from 'react-router-dom'

const Profits = () => {
    const navigate = useNavigate()
    const location = useLocation()
    const idUser = location.state ? location.state.id : null
    const user = location.state ? location.state.user : null
    const token =  location.state ? location.state.token : null
    const { data, loading, error } = useFetch(`http://localhost:8765/auth-service/api/sale`);
    const [bookSale, setBookSale] = useState([]);
    const [sales, setSales] = useState([]);

    const pushBook = () => {
        const books = [];
        for (const sale of sales) {
            for (const order of sale.orders) {
                for (const book of order.books) {
                    books.push(book);
                }
            }
        }
        setBookSale(books);
    }

    useEffect(() => {
        if (data) {
            setSales(data);
            pushBook();
        }
    }, [data])

    const calculateProfit = () => {
        let lucroTotal = 0;
        let custoTotal = 0;
        //For triplo para iterar sobre as vendas, depois orders, depois books
        for (const sale of sales) {
            for (const order of sale.orders) {
                for (const book of order.books) {
                    const profit = book.price - book.costPrice;
                    custoTotal += book.costPrice;
                    lucroTotal += profit;
                }
            }
        }
        return {
            lucroTotal: lucroTotal.toFixed(2),
            custoTotal: custoTotal.toFixed(2)
        }
    }

    const totalAmount = () => {
        return sales.reduce((total, sale) => total + sale.amount, 0);
    }

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    const { lucroTotal, custoTotal } = calculateProfit();
    const valorBruto = totalAmount().toFixed(2);

    const chartOptions = {
        series: [{
            name: 'Valor Bruto',
            data: [valorBruto]
        }, {
            name: 'Lucro Total',
            data: [lucroTotal]
        }, {
            name: 'Custo Total',
            data: [custoTotal]
        }],
        options: {
            chart: {
                type: 'bar'
            },
            xaxis: {
                categories: ['Valores']
            }
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
            <div className="profits-container">
                <h1>VALORES</h1>
                <Chart options={chartOptions.options} series={chartOptions.series} type="bar" height={350} />
            </div>
            <h1 className="pedido-sales">Pedidos</h1>
            <div className="book-sales">
                
                <ul>
                    {bookSale.map(book => (
                        <li key={book.id}>
                            <p>{book.title}</p>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    )
}

export default Profits;
