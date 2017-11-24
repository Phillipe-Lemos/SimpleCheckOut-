--- client inserts 
INSERT INTO client (id, client_name) values (1, 'Client A');
INSERT INTO client (id, client_name) values (2, 'Client B');
INSERT INTO client (id, client_name) values (3, 'Client C');

--- SKU
insert into sku (id, sku_name, sku_actual_price) values (1, 'A', 10.50);
insert into sku (id, sku_name, sku_actual_price) values (2, 'B', 31.50);
insert into sku (id, sku_name, sku_actual_price) values (3, 'C', 6.50);
insert into sku (id, sku_name, sku_actual_price) values (4, 'D', 6.50);

--- SKU-PROMOTION
insert into sku_promotion(id, sku_id, qtd_minimal, price) values (1,1,5, 7);
insert into sku_promotion(id, sku_id, qtd_minimal, price) values (2,1,15, 37);
insert into sku_promotion(id, sku_id, qtd_minimal, price) values (3,2,15, 38);