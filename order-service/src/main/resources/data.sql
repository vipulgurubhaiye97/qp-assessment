-- Create orders table if not exists
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL
    );

-- Create order_items table if not exists
CREATE TABLE IF NOT EXISTS order_items (
     id SERIAL PRIMARY KEY,
     order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    grocery_id INT REFERENCES groceries(id),
    quantity INT NOT NULL
    );

-- Insert dummy data into orders table
delete from orders;
insert into orders (id, user_id) values (1, 'user1');
insert into orders (id, user_id) values (2, 'user2');

-- Insert dummy data into order_items table
delete from order_items;
insert into order_items (id, order_id, grocery_id, quantity) values (1, 1, 1, 2);
insert into order_items (id, order_id, grocery_id, quantity) values (2, 1, 3, 1);
insert into order_items (id, order_id, grocery_id, quantity) values (3, 2, 2, 5);
insert into order_items (id, order_id, grocery_id, quantity) values (4, 2, 4, 2);