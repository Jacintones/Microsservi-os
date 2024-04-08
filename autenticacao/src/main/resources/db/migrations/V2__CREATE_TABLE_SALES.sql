CREATE TABLE sales (
                       idSale SERIAL PRIMARY KEY,
                       amount NUMERIC(10,2) NOT NULL,
                       idUser BIGINT NOT NULL,
                       date TIMESTAMP NOT NULL,
                       FOREIGN KEY (idUser) REFERENCES users(id_user)
);