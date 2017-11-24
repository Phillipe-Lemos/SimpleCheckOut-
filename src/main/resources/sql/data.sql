--- client inserts 
INSERT INTO client (id, client_name) values (1, 'Client A');
INSERT INTO client (id, client_name) values (2, 'Client B');
INSERT INTO client (id, client_name) values (3, 'Client C');

--- SKU
insert into sku (id, sku_name, sku_actual_price) values (1, 'A', 40.00);
insert into sku (id, sku_name, sku_actual_price) values (2, 'B', 10.00);
insert into sku (id, sku_name, sku_actual_price) values (3, 'C', 30.00);
insert into sku (id, sku_name, sku_actual_price) values (4, 'D', 25.00);

--- SKU-PROMOTION
insert into sku_promotion(id, sku_id, qtd_minimal, price) values (1, 1, 3, 70);
insert into sku_promotion(id, sku_id, qtd_minimal, price) values (2, 3, 2, 15);
