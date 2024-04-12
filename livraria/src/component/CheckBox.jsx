import { useState } from 'react'
import "./Css/CheckBox.css"

const CheckBox = ({ livros, setLivros }) => {
    const [orderNome, setOrderNome] = useState('asc') // Estado para controlar a ordem atual de ordenação por nome
    const [orderAutor, setOrderAutor] = useState('asc') // Estado para controlar a ordem atual de ordenação por autor
    const [orderPaginas, setOrderPaginas] = useState('asc') // Estado para controlar a ordem atual de ordenação por número de páginas
    const [orderPreco, setOrderPreco] = useState('asc') // Estado para controlar a ordem atual de ordenação por preço

    // Função para alternar a ordem entre ascendente e descendente
    const toggleOrder = (type) => {
        switch (type) {
            case 'nome':
                setOrderNome(orderNome === 'asc' ? 'desc' : 'asc')
                break
            case 'autor':
                setOrderAutor(orderAutor === 'asc' ? 'desc' : 'asc')
                break
            case 'paginas':
                setOrderPaginas(orderPaginas === 'asc' ? 'desc' : 'asc')
                break
            case 'preco':
                setOrderPreco(orderPreco === 'asc' ? 'desc' : 'asc')
                break
            default:
                break
        }
    }

    // Funções para ordenar os livros com base na ordem atual
    const handleSortByNome = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderNome === 'asc') {
                return a.title.localeCompare(b.title)
            } else {
                return b.title.localeCompare(a.title)
            }
        })
        setLivros(sortedLivros)
        toggleOrder('nome')
    }

    const handleSortByAutor = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderAutor === 'asc') {
                return a.author.localeCompare(b.author)
            } else {
                return b.author.localeCompare(a.author)
            }
        })
        setLivros(sortedLivros)
        toggleOrder('autor')
    }

    const handleSortByPaginas = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderPaginas === 'asc') {
                return a.pages - b.pages
            } else {
                return b.pages - a.pages
            }
        });
        setLivros(sortedLivros)
        toggleOrder('paginas')
    }

    const handleSortByPreco = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderPreco === 'asc') {
                return a.price - b.price
            } else {
                return b.price - a.price
            }
        })
        setLivros(sortedLivros)
        toggleOrder('preco')
    }

    return (
        <div className='checkBoxContainer'>
            <button className='btn_paginas' onClick={handleSortByPaginas}>
                Páginas {orderPaginas === 'asc' ? '▲' : '▼'}
            </button>
            <br />
            <button className='btn_autor' onClick={handleSortByAutor}>
                Autor {orderAutor === 'asc' ? '▲' : '▼'}
            </button>
            <br />
            <button className='btn_nome' onClick={handleSortByNome}>
                Nome {orderNome === 'asc' ? '▲' : '▼'}
            </button>
            <br />
            <button className='btn_preco' onClick={handleSortByPreco}>
                Preço {orderPreco === 'asc' ? '▲' : '▼'}
            </button>
        </div>
    );
}

export default CheckBox;
