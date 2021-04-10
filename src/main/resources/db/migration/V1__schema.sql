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
);

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

CREATE TABLE chatRoomTypes
(
    id SERIAL4,
    type varchar(60) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE chatRoom
(
    id SERIAL4,
    title varchar,
    type_id bigint NOT NULL,
    image_id bigint unique,
    CONSTRAINT fk_chat_room_type_id FOREIGN KEY (type_id) REFERENCES chatRoomTypes (id),
    PRIMARY KEY (id)
);

CREATE TABLE chatRoom_users
(
    user_id bigint NOT NULL,
    chatroom_id bigint NOT NULL,
    PRIMARY KEY (user_id, chatroom_id),
    CONSTRAINT fk_user_chatroom_id FOREIGN KEY (chatroom_id) REFERENCES chatRoom (id),
    CONSTRAINT fk_chatroom_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE messageContentType
(
    id SERIAL4,
    type varchar(60) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE messageContent
(
    id SERIAL4,
    type_id bigint NOT NULL,
    content varchar(1000)  NOT NULL,
    CONSTRAINT fk_message_content_type_id FOREIGN KEY (type_id) REFERENCES messageContentType (id),
    PRIMARY KEY (id)
);

CREATE TABLE chatMessage
(
    id SERIAL4,
    chat_id bigint  NOT NULL,
    sender_id bigint NOT NULL,
    sender_name varchar(15)  NOT NULL,
    content_id bigint NOT NULL,
    status_id bigint NOT NULL,
    CONSTRAINT fk_chat_message_status_id FOREIGN KEY (status_id) REFERENCES messageStatus (id),
    CONSTRAINT fk_chat_message_chat_id FOREIGN KEY (chat_id) REFERENCES chatRoom (id),
    CONSTRAINT fk_chat_message_content_id FOREIGN KEY (content_id) REFERENCES messageContent (id),
    PRIMARY KEY (id)
);

CREATE TABLE chatNotification
(
    id SERIAL4,
    recipient_id bigint NOT NULL,
    sender_name varchar(15)  NOT NULL unique ,
    PRIMARY KEY (id)
);