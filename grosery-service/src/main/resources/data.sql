-- Create groceries table if not exists
CREATE TABLE IF NOT EXISTS groceries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL
    );

-- Insert dummy data into groceries table
delete from groceries;
insert into groceries (id, name, price, quantity) values (1, 'Apple', 1.5, 100);
insert into groceries (id, name, price, quantity) values (2, 'Banana', 0.5, 200);
insert into groceries (id, name, price, quantity) values (3, 'Milk', 2.0, 50);
insert into groceries (id, name, price, quantity) values (4, 'Bread', 1.2, 80);
insert into groceries (id, name, price, quantity) values (5, 'Eggs', 3.0, 30);