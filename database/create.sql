CREATE  TABLE conversations ( 
	conversation_id      serial  NOT NULL ,
	name                 varchar(100)   ,
	CONSTRAINT pk_conversations_conversation_id PRIMARY KEY ( conversation_id )
 );

CREATE  TABLE users ( 
	user_id              serial  NOT NULL ,
	name                 varchar(100)  NOT NULL ,
	login                varchar(100)  NOT NULL ,
	email                varchar(100)  NOT NULL ,
	"password"           bytea  NOT NULL ,
	salt                 bytea   ,
	active               boolean DEFAULT false  ,
	activation_token     bytea   ,
	CONSTRAINT pk_users_user_id PRIMARY KEY ( user_id ),
	CONSTRAINT unq_users_login UNIQUE ( login ) ,
	CONSTRAINT unq_users_email UNIQUE ( email ) 
 );

ALTER TABLE users ADD CONSTRAINT cns_users CHECK ( (active = true and activation_token is null) or (active = false and activation_token is not null) );

CREATE  TABLE membership ( 
	user_id              integer  NOT NULL ,
	conversation_id      integer  NOT NULL 
 );

CREATE  TABLE messages ( 
	user_id              integer  NOT NULL ,
	conversation_id      integer  NOT NULL ,
	send_time            timestamp DEFAULT current_timestamp NOT NULL ,
	content              text  NOT NULL 
 );

ALTER TABLE membership ADD CONSTRAINT fk_membership_users FOREIGN KEY ( user_id ) REFERENCES users( user_id );

ALTER TABLE membership ADD CONSTRAINT fk_membership_conversations FOREIGN KEY ( conversation_id ) REFERENCES conversations( conversation_id );

ALTER TABLE messages ADD CONSTRAINT fk_messages_users FOREIGN KEY ( user_id ) REFERENCES users( user_id );

ALTER TABLE messages ADD CONSTRAINT fk_messages_conversations FOREIGN KEY ( conversation_id ) REFERENCES conversations( conversation_id );
