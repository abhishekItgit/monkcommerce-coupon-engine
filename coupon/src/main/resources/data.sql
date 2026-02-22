-- Sample CART_WISE coupon
INSERT INTO COUPON (ID, TYPE, METADATA, ACTIVE, EXPIRATION_DATE)
VALUES (
1,
'CART_WISE',
'{"threshold":100,"discountPercent":10}',
true,
NULL
);

-- Sample PRODUCT_WISE coupon
INSERT INTO COUPON (ID, TYPE, METADATA, ACTIVE, EXPIRATION_DATE)
VALUES (
2,
'PRODUCT_WISE',
'{"productId":1,"discountPercent":20}',
true,
NULL
);

-- Sample BXGY coupon
INSERT INTO COUPON (ID, TYPE, METADATA, ACTIVE, EXPIRATION_DATE)
VALUES (
3,
'BXGY',
'{"buyQty":2,"getQty":1,"repetitionLimit":3}',
true,
NULL
);