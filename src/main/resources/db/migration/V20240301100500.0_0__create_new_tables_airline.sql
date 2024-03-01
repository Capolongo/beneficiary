DECLARE
col_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO col_count
FROM user_tab_columns
WHERE table_name = 'FLIGHT_LEG'
  AND column_name = 'AIRLINE';

IF col_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FLIGHT_LEG ADD AIRLINE VARCHAR2(255)';
END IF;
END;
/

ALTER TABLE FLIGHT_LEG DROP COLUMN AIRLINE;


DECLARE
col_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO col_count
FROM user_tab_columns
WHERE table_name = 'FLIGHT_LEG'
  AND column_name = 'MANAGED_BY';

IF col_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FLIGHT_LEG ADD MANAGED_BY VARCHAR2(255)';
END IF;
END;
/
ALTER TABLE FLIGHT_LEG DROP COLUMN MANAGED_BY;

DECLARE
col_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO col_count
FROM user_tab_columns
WHERE table_name = 'FLIGHT_LEG'
  AND column_name = 'AIRLINE_ID';

IF col_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FLIGHT_LEG DROP COLUMN AIRLINE_ID';
END IF;
END;
/

DECLARE
table_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO table_count
FROM user_tables
WHERE table_name = 'AIRLINE';

IF table_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP TABLE AIRLINE';
END IF;
END;
/

DECLARE
seq_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO seq_count
FROM user_sequences
WHERE sequence_name = 'AIRLINE_SEQ';

IF seq_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP SEQUENCE AIRLINE_SEQ';
END IF;
END;
/

DECLARE
table_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO table_count
FROM user_tables
WHERE table_name = 'AIRLINE_MANAGED_BY';

IF table_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP TABLE AIRLINE_MANAGED_BY';
END IF;
END;
/

DECLARE
seq_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO seq_count
FROM user_sequences
WHERE sequence_name = 'AIRLINE_MANAGED_BY_SEQ';

IF seq_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP SEQUENCE AIRLINE_MANAGED_BY_SEQ';
END IF;
END;
/

CREATE TABLE AIRLINE_MANAGED_BY (
                            ID INT NOT NULL,
                            IATA VARCHAR2(255)  NOT NULL,
                            DESCRIPTION VARCHAR2(255)  NOT NULL,
                            CREATE_DATE TIMESTAMP WITH TIME ZONE,
                            LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE AIRLINE_MANAGED_BY_SEQ START WITH 1;
ALTER TABLE AIRLINE_MANAGED_BY ADD CONSTRAINT PK_AIRLINE_MANAGED_BY_ID PRIMARY KEY (ID) ENABLE;
COMMENT ON COLUMN AIRLINE_MANAGED_BY.IATA is 'IATA DA COMPANHIA AEREA';
COMMENT ON COLUMN AIRLINE_MANAGED_BY.DESCRIPTION is 'DESCRIÇÃO DA COMPANHIA AEREA';


DECLARE
table_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO table_count
FROM user_tables
WHERE table_name = 'AIRLINE_OPERATED_BY';

IF table_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP TABLE AIRLINE_OPERATED_BY';
END IF;
END;
/

DECLARE
seq_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO seq_count
FROM user_sequences
WHERE sequence_name = 'AIRLINE_OPERATED_BY_SEQ';

IF seq_count > 0 THEN
        EXECUTE IMMEDIATE 'DROP SEQUENCE AIRLINE_OPERATED_BY_SEQ';
END IF;
END;
/

CREATE TABLE AIRLINE_OPERATED_BY (
                                    ID INT NOT NULL,
                                    IATA VARCHAR2(255)  NOT NULL,
                                    DESCRIPTION VARCHAR2(255)  NOT NULL,
                                    CREATE_DATE TIMESTAMP WITH TIME ZONE,
                                    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE AIRLINE_OPERATED_BY_SEQ START WITH 1;
ALTER TABLE AIRLINE_OPERATED_BY ADD CONSTRAINT PK_AIRLINE_OPERATED_BY_ID PRIMARY KEY (ID) ENABLE;
COMMENT ON COLUMN AIRLINE_MANAGED_BY.IATA is 'IATA DA COMPANHIA AEREA';
COMMENT ON COLUMN AIRLINE_MANAGED_BY.DESCRIPTION is 'DESCRIÇÃO DA COMPANHIA AEREA';

CREATE TABLE AIRLINE (
                                     ID INT NOT NULL,
                                     AIRLINE_MANAGED_BY_ID INT NOT NULL,
                                     AIRLINE_OPERATED_BY_ID INT NOT NULL,
                                     CREATE_DATE TIMESTAMP WITH TIME ZONE,
                                     LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);
CREATE SEQUENCE AIRLINE_SEQ START WITH 1;
ALTER TABLE AIRLINE ADD CONSTRAINT PK_AIRLINE_ID PRIMARY KEY (ID) ENABLE;
CREATE INDEX AIRLINE_MANAGED_BY_ID_INDEX on AIRLINE(AIRLINE_MANAGED_BY_ID) online;
CREATE INDEX AIRLINE_OPERATED_BY_ID_INDEX on AIRLINE(AIRLINE_OPERATED_BY_ID) online;
ALTER TABLE AIRLINE ADD CONSTRAINT fk_AIRLINE_AIRLINE_MANAGED_BY_ID FOREIGN KEY (AIRLINE_MANAGED_BY_ID) REFERENCES AIRLINE_MANAGED_BY (ID);
ALTER TABLE AIRLINE ADD CONSTRAINT fk_AIRLINE_AIRLINE_OPERATED_BY_ID FOREIGN KEY (AIRLINE_OPERATED_BY_ID) REFERENCES AIRLINE_OPERATED_BY (ID);

ALTER TABLE FLIGHT_LEG ADD AIRLINE_ID INT DEFAULT 1 NOT NULL;
CREATE INDEX FLIGHT_LEG_AIRLINE_ID_INDEX on FLIGHT_LEG(AIRLINE_ID) online;
ALTER TABLE FLIGHT_LEG ADD CONSTRAINT fk_FLIGHT_LEG_AIRLINE_ID FOREIGN KEY (AIRLINE_ID) REFERENCES AIRLINE (ID);
