insert into
	users (name, login, password)
	values ('Obi-Wan Kenobi', 'obiwan', decode('5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'hex')); -- password = 'password'

insert into
	users (name, login, password, salt)
	values ('Ben Kenobi', 'ben', decode('8c9777dfec582cfa4b3fd495e79dcf5625354955b086bc4d24398f70b93d2fb6', 'hex'), decode('31cf31948826b69877029556043ed782', 'hex')); -- password = 'password123'

insert into
	users (name, login, password, salt)
	values ('Darth Vader', 'dthv', decode('2ef9fa00636f4ac3e3a55b4b60c4ace01b8ce5d19504d88cc73537eba0d19f4a', 'hex'), decode('5fcf65256dfb18f364392e1ff410065d', 'hex')); -- password = 'abc123'

insert into
	conversation (name)
	values ('Duel on Mustafar');

insert into
	messages (user_id, conversation_id, send_time, content)
	values (8, 1, '1970-01-01 00:00:00', 'You turned her against me!'),
	(6, 1, '1970-01-01 00:00:10', 'ou have done that yourself.'),
	(8, 1, '1970-01-01 00:00:20', 'You will not take her from me!'),
	(6, 1, '1970-01-01 00:00:30', 'Your anger and your lust for power have already done that. You have allowed this dark lord to twist your mind, until now... until now you have become the very thing you swore to destroy.'),
	(8, 1, '1970-01-01 00:00:40', 'Don''t lecture me, Obi-Wan. I see through the lies of the Jedi. I do not fear the dark side as you do. I have brought peace, freedom, justice and security to my new Empire!'),
	(6, 1, '1970-01-01 00:00:50', 'Your new Empire?'),
	(8, 1, '1970-01-01 00:01:00', 'Don''t make me kill you.'),
	(6, 1, '1970-01-01 00:01:10', 'Anakin, my allegiance is to the Republic, to Democracy!'),
	(8, 1, '1970-01-01 00:01:20', 'If you''re not with me, then you''re my enemy.'),
	(6, 1, '1970-01-01 00:01:30', 'Only a Sith deals in absolutes. I will do what I must'),
	(8, 1, '1970-01-01 00:01:40', 'You will try.');
