#For this solution we used the following domain classes

* Client - represents a customer who makes a purchase.
* Sku - product available for purchase.
* Promotion - They represent products promotions on sale.
* SkuOrder - It is the total of a purchase from a customer. Every time the customer requests the total of their purchase, it is closed.
* SkuOrderItem - It is a customer purchase item.

#Notes :
* For simplicity I did not keep price and promotions history.
* To document the API was used Swagger, which can be consulted at http://localhost:8080 