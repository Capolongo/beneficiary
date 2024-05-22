CREATE TABLE PROCESS_COUNTER (
  ID INT NOT NULL,
  PROCESS VARCHAR2(255) NOT NULL,
  COUNT INT NOT NULL,
  ORDER_ID VARCHAR2(255) NULL,
  CREATE_DATE TIMESTAMP WITH TIME ZONE,
  LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE PROCESS_COUNTER_SEQ START WITH 1;
CREATE INDEX PROCESS_COUNTER_ID_INDEX on PROCESS_COUNTER(ORDER_ID) online;
ALTER TABLE PROCESS_COUNTER ADD CONSTRAINT PK_PROCESS_COUNTER_ID PRIMARY KEY (ID) ENABLE;
ALTER TABLE PROCESS_COUNTER ADD CONSTRAINT FK_PROCESS_COUNTER_ORDERS_ID FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID);