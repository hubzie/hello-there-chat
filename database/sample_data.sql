insert into
	users (name, login, password)
	values ('Obi-Wan Kenobi', 'obiwan', decode('5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'hex')); -- password = 'password'

insert into
	users (name, login, password, salt)
	values ('Ben Kenobi', 'ben', decode('8c9777dfec582cfa4b3fd495e79dcf5625354955b086bc4d24398f70b93d2fb6', 'hex'), decode('31cf31948826b69877029556043ed782', 'hex')); -- password = 'password123'
