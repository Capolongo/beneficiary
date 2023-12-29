CREATE TABLE ORDER_PRICE (
    ID INT NOT NULL,
    ACCRUAL_POINTS INT,
    AMOUNT NUMBER(10,2),
    POINTS_AMOUNT NUMBER(10,2),
    PARTNER_AMOUNT NUMBER(10,2),
    PRICE_LIST_ID VARCHAR2(255)  NOT NULL,
    PRICE_LIST_DESCRIPTION VARCHAR2(255) NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE ORDER_PRICE_SEQ START WITH 1;
ALTER TABLE ORDER_PRICE ADD CONSTRAINT PK_ORDER_PRICE_ID PRIMARY KEY (ID) ENABLE;

CREATE TABLE ORDERS (
    ID VARCHAR2(255) NOT NULL,
    ORDER_PRICE_ID INT,
    COMMERCE_ORDER_ID VARCHAR2(255) NOT NULL,
    PARTNER_ORDER_ID VARCHAR2(500) NOT NULL, -- verificar com relação ao token da cvc - Defini como text, pois pode haver uma quantidade não previsível para outros parceiros
    PARTNER_CODE VARCHAR2(255),
    SUBMITTED_DATE TIMESTAMP WITH TIME ZONE,
    CHANNEL VARCHAR2(100) NOT NULL,
    TIER_CODE VARCHAR2(255) NOT NULL,
    ORIGIN_ORDER VARCHAR2(255) NOT NULL,
    CUSTOMER_IDENTIFIER VARCHAR2(255) NOT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE,
    STATUS INT,
    TRANSACTION_ID VARCHAR2(255) NULL,
    EXPIRATION_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE ORDERS_SEQ START WITH 1;
CREATE INDEX ORDER_PRICE_ID_INDEX on ORDERS(ORDER_PRICE_ID) online;
CREATE INDEX ORDER_CUSTOMER_INDEX on ORDERS(CUSTOMER_IDENTIFIER) online;
CREATE INDEX ORDER_COMMERCE_ORDER_ID_INDEX on ORDERS(COMMERCE_ORDER_ID) online;
--CREATE INDEX ORDER_PARTNER_ORDER_ID_INDEX on ORDERS(PARTNER_ORDER_ID) online; --vALIDAR SE VAI SER CLOB QUANDO CRIAR A TABELA
CREATE INDEX ORDER_PARTNER_CODE_INDEX on ORDERS(PARTNER_CODE) online;
ALTER TABLE ORDERS ADD CONSTRAINT PK_ORDER_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE ORDERS ADD CONSTRAINT fk_ORDERS_order_price_id FOREIGN KEY (ORDER_PRICE_ID) REFERENCES ORDER_PRICE (ID);

CREATE TABLE ORDER_STATUS
	(
	    ID INT NOT NULL,
	    CODE VARCHAR2(50) NOT NULL,
	    DESCRIPTION VARCHAR2(255) NOT NULL,
	    PARTNER_CODE VARCHAR2(255),
	    PARTNER_DESCRIPTION VARCHAR2(255),
	    ORDER_ID VARCHAR2(255) NULL,
	    PARTNER_RESPONSE CLOB,
	    STATUS_DATE TIMESTAMP WITH TIME ZONE,
	    CREATED_DATE TIMESTAMP WITH TIME ZONE,
    	LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
	);
CREATE SEQUENCE ORDER_STATUS_SEQ START WITH 1;
CREATE INDEX ORDER_STATUS_ORDER_ID_INDEX on ORDER_STATUS(ORDER_ID) online;
CREATE INDEX ORDER_STATUS_CODE_INDEX on ORDER_STATUS(CODE) online;
CREATE INDEX ORDER_STATUS_DESCRIPTION_INDEX on ORDER_STATUS(DESCRIPTION) online;
CREATE INDEX ORDER_STATUS_PARTNER_CODE_INDEX on ORDER_STATUS(PARTNER_CODE) online;
ALTER TABLE ORDER_STATUS ADD CONSTRAINT PK_ORDER_STATUS_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE ORDER_STATUS ADD CONSTRAINT fk_order_status_order_id FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID);

CREATE TABLE ORDER_PRICE_DESCRIPTION (
    ID INT NOT NULL,
    AMOUNT NUMBER(10,2),
    POINTS_AMOUNT NUMBER(10,2),
    TYPE VARCHAR2(255) NOT NULL,
    DESCRIPTION VARCHAR2(255) NOT NULL,
    ORDER_PRICE_ID INT,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE ORDER_PRICE_DESCRIPTION_SEQ START WITH 1;
CREATE INDEX ORDER_PRICE_DESCRIPTION_ORDER_PRICE_ID_INDEX on ORDER_PRICE_DESCRIPTION(ORDER_PRICE_ID) online;
ALTER TABLE ORDER_PRICE_DESCRIPTION ADD CONSTRAINT PK_ORDER_PRICE_DESCRIPTION_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE ORDER_PRICE_DESCRIPTION ADD CONSTRAINT fk_order_price_desc_order_price_id FOREIGN KEY (ORDER_PRICE_ID) REFERENCES ORDER_PRICE (ID);

CREATE TABLE TRAVEL_INFO (
    ID INT NOT NULL,
    TYPE VARCHAR2(255) NOT NULL,
    RESERVATION_CODE VARCHAR2(255),
    ADULT_QUANTITY INT,
    CHILD_QUANTITY INT,
    BABY_QUANTITY INT,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE TRAVEL_INFO_SEQ START WITH 1;
ALTER TABLE TRAVEL_INFO ADD CONSTRAINT PK_TRAVEL_INFO_ID PRIMARY KEY (ID) ENABLE;

CREATE TABLE ORDER_ITEM_PRICE (
    ID INT NOT NULL,
    LIST_PRICE VARCHAR2(255) NOT NULL,
    AMOUNT NUMBER(15,4),
    POINTS_AMOUNT NUMBER(10,2),
    ACCRUAL_POINTS NUMBER(10,2),
    PARTNER_AMOUNT NUMBER(10,2),
    PRICE_LIST_ID VARCHAR2(255),
    PRICE_RULE CLOB NOT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE ORDER_ITEM_PRICE_SEQ START WITH 1;
ALTER TABLE ORDER_ITEM_PRICE ADD CONSTRAINT PK_ORDER_ITEM_PRICE_ID PRIMARY KEY (ID) ENABLE;

CREATE TABLE ORDER_ITEM (
    ID INT NOT NULL,
    ORDER_ID VARCHAR2(255) NULL,
    TRAVEL_INFO_ID INT NOT NULL,
    ORDER_ITEM_PRICE_ID INT NOT NULL,
    COMMERCE_ITEM_ID VARCHAR2(255) NOT NULL,
    SKU_ID VARCHAR2(255) NOT NULL,
    PRODUCT_ID VARCHAR2(255) NOT NULL,
    QUANTITY INT,
    EXTERNAL_COUPON VARCHAR2(255),
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE ORDER_ITEM_SEQ START WITH 1;
CREATE INDEX ORDER_ITEM_ORDER_ID_INDEX on ORDER_ITEM(ORDER_ID) online;
CREATE INDEX ORDER_ITEM_TRAVEL_INFO_ID_INDEX on ORDER_ITEM(TRAVEL_INFO_ID) online;
CREATE INDEX ORDER_ITEM_COMMERCE_ITEM_ID_INDEX on ORDER_ITEM(COMMERCE_ITEM_ID) online;
ALTER TABLE ORDER_ITEM ADD CONSTRAINT PK_ORDER_ITEM_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE ORDER_ITEM ADD CONSTRAINT fk_order_item_order_id FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID);
ALTER TABLE ORDER_ITEM ADD CONSTRAINT fk_order_item_travel_info_id FOREIGN KEY (TRAVEL_INFO_ID) REFERENCES TRAVEL_INFO (ID);
ALTER TABLE ORDER_ITEM ADD CONSTRAINT fk_order_item_order_item_price_id FOREIGN KEY (ORDER_ITEM_PRICE_ID) REFERENCES ORDER_ITEM_PRICE (ID);

CREATE TABLE PAX (
    ID INT NOT NULL,
    TYPE VARCHAR2(255) NOT NULL,
    FIRST_NAME VARCHAR2(255) NOT NULL,
    LAST_NAME VARCHAR2(255) NOT NULL,
    EMAIL VARCHAR2(255) NOT NULL,
    AREA_CODE VARCHAR2(2) NOT NULL,
    PHONE_NUMBER VARCHAR2(9) NOT NULL,
    GENDER VARCHAR2(1) NOT NULL,
    BIRTH_DATE TIMESTAMP WITH TIME ZONE, -- esse cara não precisa de timezone pois não vai armazenar hora, apenas DD/MM/YYYY
    DOCUMENT VARCHAR2(255) NOT NULL,
    DOCUMENT_TYPE VARCHAR2(10) NOT NULL,
    TRAVEL_INFO_ID INT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE PAX_SEQ START WITH 1;
CREATE INDEX PAX_TRAVEL_INFO_ID_INDEX on PAX(TRAVEL_INFO_ID) online;
ALTER TABLE PAX ADD CONSTRAINT PK_PAX_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE PAX ADD CONSTRAINT fk_pax_travel_info_id FOREIGN KEY (TRAVEL_INFO_ID) REFERENCES TRAVEL_INFO (ID);

CREATE TABLE SEGMENT (
    ID INT NOT NULL,
    PARTNER_ID VARCHAR2(255),
    STEP INT,
    STOPS INT,
    FLIGHT_DURATION INT,
    ORIGIN_IATA VARCHAR2(3) NOT NULL,
    ORIGIN_DESCRIPTION VARCHAR2(255) NOT NULL,
    DESTINATION_IATA VARCHAR2(3) NOT NULL,
    DESTINATION_DESCRIPTION VARCHAR2(255) NOT NULL,
    DEPARTURE_DATE TIMESTAMP WITH TIME ZONE,
    ARRIVAL_DATE TIMESTAMP WITH TIME ZONE,
    ORDER_ITEM_ID INT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE SEGMENT_SEQ START WITH 1;
ALTER TABLE SEGMENT ADD CONSTRAINT PK_SEGMENT_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE SEGMENT ADD CONSTRAINT fk_segment_order_item_id FOREIGN KEY (ORDER_ITEM_ID) REFERENCES ORDER_ITEM (ID);

CREATE TABLE FLIGHT_LEG (
    ID INT NOT NULL,
    FLIGHT_NUMBER VARCHAR2(255) NOT NULL,
    FLIGHT_DURATION INT NOT NULL,
    AIRLINE VARCHAR2(255) NOT NULL,
    MANAGED_BY VARCHAR2(255) NOT NULL,
    TIME_TO_WAIT INT,
    OPERATED_DATE TIMESTAMP WITH TIME ZONE,
    ORIGIN_IATA VARCHAR2(3) NOT NULL,
    ORIGIN_DESCRIPTION VARCHAR2(255) NOT NULL,
    DESTINATION_IATA VARCHAR2(3) NOT NULL,
    DESTINATION_DESCRIPTION VARCHAR2(255) NOT NULL,
    DEPARTURE_DATE TIMESTAMP WITH TIME ZONE,
    ARRIVAL_DATE TIMESTAMP WITH TIME ZONE,
    TYPE VARCHAR2(10) NOT NULL,
    SEGMENT_ID INT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE FLIGHT_LEG_SEQ START WITH 1;
CREATE INDEX FLIGHT_LEG_SEGMENT_ID_INDEX on FLIGHT_LEG(SEGMENT_ID) online;
ALTER TABLE FLIGHT_LEG ADD CONSTRAINT PK_FLIGHT_LEG_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE FLIGHT_LEG ADD CONSTRAINT fk_flight_leg_segment_id FOREIGN KEY (SEGMENT_ID) REFERENCES SEGMENT (ID);

CREATE TABLE LUGGAGE (
    ID INT NOT NULL,
    DESCRIPTION VARCHAR2(255) NOT NULL,
    TYPE VARCHAR2(50) NOT NULL,
    SEGMENT_ID INT NULL,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE LUGGAGE_SEQ START WITH 1;
CREATE INDEX LUGGAGE_SEGMENT_ID_INDEX on LUGGAGE(SEGMENT_ID) online;
ALTER TABLE LUGGAGE ADD CONSTRAINT PK_LUGGAGE_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE LUGGAGE ADD CONSTRAINT fk_luggage_segment_id FOREIGN KEY (SEGMENT_ID) REFERENCES SEGMENT (ID);

CREATE TABLE CANCELATION_RULES (
    ID INT NOT NULL,
    DESCRIPTION VARCHAR2(255) NOT NULL,
    TYPE VARCHAR2(50) NOT NULL,
    SEGMENT_ID INT,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE CANCELATION_RULES_SEQ START WITH 1;
CREATE INDEX CANCELATION_RULES_SEGMENT_ID_INDEX on CANCELATION_RULES(SEGMENT_ID) online;
ALTER TABLE CANCELATION_RULES ADD CONSTRAINT PK_CANCELATION_RULES_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE CANCELATION_RULES ADD CONSTRAINT fk_cancelation_rules_segment_id FOREIGN KEY (SEGMENT_ID) REFERENCES SEGMENT (ID);

CREATE TABLE CHANGE_RULES (
    ID INT NOT NULL,
    DESCRIPTION VARCHAR2(255)NOT NULL,
    TYPE VARCHAR2(50) NOT NULL,
    SEGMENT_ID INT,
    CREATE_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE CHANGE_RULES_SEQ START WITH 1;
CREATE INDEX CHANGE_RULES_SEGMENT_ID_INDEX on CHANGE_RULES(SEGMENT_ID) online;
ALTER TABLE CHANGE_RULES ADD CONSTRAINT PK_CHANGE_RULES_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE CHANGE_RULES ADD CONSTRAINT fk_change_rules_segment_id FOREIGN KEY (SEGMENT_ID) REFERENCES SEGMENT (ID);