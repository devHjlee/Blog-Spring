CREATE TABLE BATCH_TEST (
                                 ID BIGINT auto_increment NOT NULL,
                                 NAME varchar(100) NOT NULL,
                                 CONSTRAINT BATCH_TEST_PK PRIMARY KEY (ID)
);


CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        grade VARCHAR(255) NOT NULL,
                        mileage INT NOT NULL
);

CREATE TABLE vip (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL
);