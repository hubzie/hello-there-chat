create table users (
	user_id serial,
	name varchar(40) not null,
	login varchar(40) not null,
	password bytea not null,
	salt bytea,

	primary key(user_id)
);

create table conversation (
	conversation_id serial,
	name varchar(100),

	primary key(conversation_id)
);

create table membership (
	user_id serial not null,
	conversation_id serial not null
);

create table messages (
	user_id serial not null,
	conversation_id serial not null,
	send_time date not null,
	content varchar(250)
);
