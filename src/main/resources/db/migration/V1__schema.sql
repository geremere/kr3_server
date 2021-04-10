CREATE TABLE images
(
    image_id SERIAL4,
    url varchar NOT NULL,
    type varchar  NOT NULL ,
    PRIMARY KEY (image_id)
);


CREATE TABLE users
(
    id         SERIAL,
    name       varchar(40)  NOT NULL,
    username   varchar(15)  NOT NULL unique ,
    email      varchar(40)  NOT NULL unique ,
    password   varchar(100) NOT NULL,
    image_id bigint unique,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_regtypes_type_id FOREIGN KEY (image_id) REFERENCES images (image_id) ,
    PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id         SERIAL4,
    name varchar(60) NOT NULL unique ,
    PRIMARY KEY (id)
) ;

CREATE TABLE regtypes
(
    id         SERIAL4,
    name varchar(60) NOT NULL unique ,
    PRIMARY KEY (id)
) ;

CREATE TABLE user_regtypes
(
    user_id bigint NOT NULL,
    regtype_id bigint NOT NULL,
    PRIMARY KEY (user_id, regtype_id),
    CONSTRAINT fk_user_regtypes_type_id FOREIGN KEY (regtype_id) REFERENCES regtypes (id),
    CONSTRAINT fk_user_regtypes_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ;

CREATE TABLE user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE messageStatus
(
    id         SERIAL4,
    name varchar(60) NOT NULL unique ,
    PRIMARY KEY (id)
) ;


CREATE TABLE chatRoom
(
    id SERIAL4,
    chat_id varchar(255)  NOT NULL,
    sender_id bigint NOT NULL,
    recipient_id bigint NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE chatMessage
(
    id SERIAL4,
    chat_id varchar(255)  NOT NULL,
    sender_id bigint NOT NULL,
    recipient_id bigint NOT NULL,
    sender_name varchar(15)  NOT NULL,
    recipient_name varchar(15)  NOT NULL,
    content varchar(1000) NOT NULL,
    status_id bigint NOT NULL,
    CONSTRAINT fk_chat_message_status_id FOREIGN KEY (status_id) REFERENCES messageStatus (id),
    PRIMARY KEY (id)
);

CREATE TABLE chatNotification
(
    id SERIAL,
    recipient_id bigint NOT NULL,
    sender_name varchar(15)  NOT NULL unique ,
    PRIMARY KEY (id)
);


