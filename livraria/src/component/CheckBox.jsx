import React, { useState } from 'react';
import "./Css/CheckBox.css"

const CheckBox = ({ livros, setLivros }) => {
    const [orderNome, setOrderNome] = useState('asc'); // Estado para controlar a ordem atual de ordenação por nome
    const [orderAutor, setOrderAutor] = useState('asc'); // Estado para controlar a ordem atual de ordenação por autor
    const [orderPaginas, setOrderPaginas] = useState('asc'); // Estado para controlar a ordem atual de ordenação por número de páginas
    const [orderPreco, setOrderPreco] = useState('asc'); // Estado para controlar a ordem atual de ordenação por preço

    // Função para alternar a ordem entre ascendente e descendente
    const toggleOrder = (type) => {
        switch (type) {
            case 'nome':
                setOrderNome(orderNome === 'asc' ? 'desc' : 'asc');
                break;
            case 'autor':
                setOrderAutor(orderAutor === 'asc' ? 'desc' : 'asc');
                break;
            case 'paginas':
                setOrderPaginas(orderPaginas === 'asc' ? 'desc' : 'asc');
                break;
            case 'preco':
                setOrderPreco(orderPreco === 'asc' ? 'desc' : 'asc');
                break;
            default:
                break;
        }
    }

    // Funções para ordenar os livros com base na ordem atual
    const handleSortByNome = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderNome === 'asc') {
                return a.titulo.localeCompare(b.titulo);
            } else {
                return b.titulo.localeCompare(a.titulo);
            }
        });
        setLivros(sortedLivros);
        toggleOrder('nome');
    }

    const handleSortByAutor = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderAutor === 'asc') {
                return a.autor.localeCompare(b.autor);
            } else {
                return b.autor.localeCompare(a.autor);
            }
        });
        setLivros(sortedLivros);
        toggleOrder('autor');
    }

    const handleSortByPaginas = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderPaginas === 'asc') {
                return a.paginas - b.paginas;
            } else {
                return b.paginas - a.paginas;
            }
        });
        setLivros(sortedLivros);
        toggleOrder('paginas');
    }

    const handleSortByPreco = () => {
        const sortedLivros = [...livros].sort((a, b) => {
            if (orderPreco === 'asc') {
                return a.preco - b.preco;
            } else {
                return b.preco - a.preco;
            }
        });
        setLivros(sortedLivros);
        toggleOrder('preco');
    }

    return (
        <div className='checkBoxContainer'>
            <h1>Filtros</h1>
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
