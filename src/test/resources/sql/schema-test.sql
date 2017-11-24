create table client (
	id           BIGINT      NOT NULL,
	client_name	 VARCHAR(100) NOT NULL,
	constraint pk_client primary key(id)
);

create table sku (
	id               BIGINT  NOT NULL,
	sku_name         VARCHAR(100) NOT NULL,
	sku_actual_price DOUBLE NOT NULL,
	constraint pk_sku primary key(id)
);

create table sku_promotion(
	id               BIGINT  NOT NULL,
	sku_id           BIGINT  NOT NULL,
	qtd_minimal      DOUBLE NOT NULL,
	price            DOUBLE,
	constraint pk_promotion primary key(id)
);

create table sku_order(
	id               		BIGINT     NOT NULL,
	created          		TIMESTAMP  NOT NULL,
	finished         		TIMESTAMP,
	client_id        		BIGINT     NOT NULL,
	tot_quantity     		DOUBLE,
	tot_discount     		DOUBLE,
	total_before_discount   DOUBLE,
	amout_pay               DOUBLE,
	constraint pk_sku_order primary key(id)
);

create table sku_order_item(
	id               		BIGINT     NOT NULL,
	sku_order_id     		BIGINT     NOT NULL,
	sku_id           		BIGINT     NOT NULL,
	tot_quantity            DOUBLE,
	constraint sku_order_item primary key(id)
);

--- adding foreign key

ALTER TABLE sku_promotion 
ADD CONSTRAINT FK_PROMOCAO_SKU foreign key (sku_id) REFERENCES sku(id);

ALTER TABLE sku_order 
ADD CONSTRAINT FK_SKU_ORDER_CLIENT foreign key (client_id) REFERENCES client(id);

ALTER TABLE sku_order_item 
ADD CONSTRAINT FK_SKU_ORDER_ORDER_ITEM foreign key (sku_order_id) REFERENCES sku_order(id);

ALTER TABLE sku_order_item 
ADD CONSTRAINT FK_SKU_ORDER_ORDER_SKU foreign key (sku_id) REFERENCES sku(id);