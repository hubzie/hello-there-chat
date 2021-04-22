create table users (
	user_id serial,
	name varchar(40)
		not null,
	login varchar(40)
		not null,
	password bytea
		not null,
	salt bytea,

	primary key(user_id)
);

create table conversations (
	conversation_id serial,
	name varchar(100),

	primary key(conversation_id)
);

create table membership (
	user_id serial
		not null
		references users,
	conversation_id serial
		not null
		references conversations
);

create table messages (
	user_id serial
		not null
		references users,
	conversation_id serial
		not null
		references conversations,
	send_time timestamp
		not null
		default now(),
	content varchar(250)
);
