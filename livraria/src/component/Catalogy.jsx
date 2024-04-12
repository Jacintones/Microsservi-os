import { useState } from "react";

const Catalogy = ({ books }) => {
    // Estado para controlar o gênero selecionado
    const [selectedGenre, setSelectedGenre] = useState(null);

    // Função para filtrar os livros com base no gênero selecionado
    const filterBooksByGenre = (genre) => {
        setSelectedGenre(genre);
    };

    // Função para calcular a média de avaliação
    const calculateMedia = (book) => {
        let contador = 0;
        let totalAvaliacoes = 0; // Variável para acompanhar o número total de avaliações
        for (const available of book.availables) {
            contador += available.value;
            totalAvaliacoes++; 
        }
        const mediaTotal = contador / totalAvaliacoes; 
        return Math.round(mediaTotal);
    }

    // Função para contar o número de avaliações
    const countAvaliacoes = (book) => {
        let totalAvaliacoes = 0 
        for (const available of book.availables) {
            totalAvaliacoes++
        }
        return totalAvaliacoes    
    }

    // Filtrar os livros com base na categoria selecionada
    const bookFiltered = selectedCategory ? books.filter(book => book.categoryType === selectedCategory) : books;

    // Agrupar os livros por categoria
    const groupedBooksByCategory = {};
    books.forEach(book => {
        if (!groupedBooksByCategory[book.categoryType]) {
            groupedBooksByCategory[book.categoryType] = [];
        }
        groupedBooksByCategory[book.categoryType].push(book);
    });

    return (
        <div>
            {/* Renderizar botões de filtro por gênero */}
            <div className="GenreButtons">
                <button onClick={() => filterBooksByGenre(null)}>Todos</button>
                {Object.keys(groupedBooksByGenre).map(genre => (
                    <button key={genre} onClick={() => filterBooksByGenre(genre)}>{genre}</button>
                ))}
            </div>
            {/* Renderizar livros filtrados por gênero */}
            {Object.keys(groupedBooksByGenre).map(genre => (
                <div key={genre} className={selectedGenre === genre ? "ListContainer" : "hidden"}>
                    <h2>{genre}</h2>
                    <div className="LivroList">
                        {groupedBooksByGenre[genre].map(book => (
                            <div className="LivroItem" key={book.id} onClick={() => { handleBookClick(book) }}>
                                <img className="Thumbnail" src={book.image} />
                                <p className="TitlePane">{book.title}</p>
                                <StarRating rating={calculateMedia(book)} numAvaliacoes={countAvaliacoes(book)} />
                                <p className="PricePane">R${book.price}</p>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default Catalogy;
