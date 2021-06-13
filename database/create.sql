create sequence conversation_id;
create sequence user_id;

CREATE  TABLE conversations ( 
	conversation_id      int  NOT NULL default nextval('conversation_id'),
	name                 varchar(100)   ,
	last_update          timestamp NOT NULL default current_timestamp,
	active               boolean NOT NULL default true,
	CONSTRAINT pk_conversations_conversation_id PRIMARY KEY ( conversation_id )
 );

CREATE  TABLE users ( 
	user_id              int  NOT NULL default nextval('user_id'),
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
	conversation_id      integer  NOT NULL ,
	CONSTRAINT unq_membership UNIQUE ( user_id, conversation_id )
 );

CREATE  TABLE messages ( 
	user_id              integer  NOT NULL ,
	conversation_id      integer  NOT NULL ,
	send_time            timestamp DEFAULT current_timestamp NOT NULL ,
	content              text  NOT NULL ,
	type                 char(1) NOT NULL
 );

ALTER TABLE membership ADD CONSTRAINT fk_membership_users FOREIGN KEY ( user_id ) REFERENCES users( user_id );

ALTER TABLE membership ADD CONSTRAINT fk_membership_conversations FOREIGN KEY ( conversation_id ) REFERENCES conversations( conversation_id );

ALTER TABLE messages ADD CONSTRAINT fk_messages_users FOREIGN KEY ( user_id ) REFERENCES users( user_id );

ALTER TABLE messages ADD CONSTRAINT fk_messages_conversations FOREIGN KEY ( conversation_id ) REFERENCES conversations( conversation_id );

CREATE FUNCTION last_update() returns trigger as $$
declare
	conv_id int;
begin
	if new is not null then
		conv_id = new.conversation_id;
	else
		conv_id = old.conversation_id;
	end if;

	update conversations set last_update = current_timestamp where conversation_id = conv_id;
	return new;
end;
$$ language plpgsql;


CREATE FUNCTION last_update_conversations() returns trigger as $$
declare
	conv_id int;
begin
	new.last_update = current_timestamp;
	return new;
end;
$$ language plpgsql;

CREATE TRIGGER last_update AFTER INSERT OR UPDATE ON messages
for each row execute procedure last_update();

CREATE TRIGGER last_update AFTER INSERT OR UPDATE OR DELETE ON membership
for each row execute procedure last_update();

CREATE TRIGGER last_update AFTER INSERT OR UPDATE ON conversations
for each row execute procedure last_update_conversations();
