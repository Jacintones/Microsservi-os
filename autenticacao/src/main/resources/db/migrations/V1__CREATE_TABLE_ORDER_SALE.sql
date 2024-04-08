CREATE TABLE orders (
    id_order SERIAL PRIMARY KEY,
    complet BOOLEAN NOT NULL,
    date TIMESTAMP NOT NULL,
    id_user BIGINT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE order_book (
    id_order BIGINT,
    id_book BIGINT,
    PRIMARY KEY (id_order, id_book),
    FOREIGN KEY (id_order) REFERENCES orders(id_order),
    FOREIGN KEY (id_book) REFERENCES books(id_book)
);


