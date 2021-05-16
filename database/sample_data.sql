insert into users values (1, 'Obi-Wan Kenobi', 'obiwan', 'obiwan@jedi.org', decode('86f6dd6a13336803807e59563860f2dae0b683646de45ce87165d65053ec0c79', 'hex'), decode('baf43f0c7876e669398b09d6051c4877', 'hex'), true, null); -- password = 'password'
insert into users values (2, 'Chancellor Palpatine', 'sith', 'senat@gov.org', decode('2ef9fa00636f4ac3e3a55b4b60c4ace01b8ce5d19504d88cc73537eba0d19f4a', 'hex'), decode('5fcf65256dfb18f364392e1ff410065d', 'hex'), true, null); -- password = 'abc123'
insert into users values (3, 'Anakin Skywalker', 'asky', 'asky@jedi.org', decode('9ab2c7546e46676c7a62ba4ea45d2f80ca4ed7b0d03139c805091ce1304aea98', 'hex'), decode('c2eaf428d182a1f89e1d09635c0d9a48', 'hex'), true, null); -- password = 'password123'
insert into users values (4, 'Count Dooku', 'dook', 'dook@gov.org', decode('9ab2c7546e46676c7a62ba4ea45d2f80ca4ed7b0d03139c805091ce1304aea98', 'hex'), decode('c2eaf428d182a1f89e1d09635c0d9a48', 'hex'), true, null); -- password = 'password123'

insert into conversations (conversation_id, name) values (1, 'Duel on Mustafar');
insert into membership (conversation_id, user_id) values (1, 1);
insert into membership (conversation_id, user_id) values (1, 3);

insert into conversations (conversation_id, name) values (2, null);
insert into membership (conversation_id, user_id) values (2, 1);
insert into membership (conversation_id, user_id) values (2, 3);

insert into conversations (conversation_id, name) values (3, null);
insert into membership (conversation_id, user_id) values (3, 1);
insert into membership (conversation_id, user_id) values (3, 2);
insert into membership (conversation_id, user_id) values (3, 3);
insert into membership (conversation_id, user_id) values (3, 4);

insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:00:00', 'You turned her against me!');
insert into messages (user_id, conversation_id, send_time, content) values (1, 1, '1970-01-01 00:00:10', 'You have done that yourself.');
insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:00:20', 'You will not take her from me!');
insert into messages (user_id, conversation_id, send_time, content) values (1, 1, '1970-01-01 00:00:30', 'Your anger and your lust for power have already done that. You have allowed this dark lord to twist your mind, until now... until now you have become the very thing you swore to destroy.');
insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:00:40', 'Don''t lecture me, Obi-Wan. I see through the lies of the Jedi. I do not fear the dark side as you do. I have brought peace, freedom, justice and security to my new Empire!');
insert into messages (user_id, conversation_id, send_time, content) values (1, 1, '1970-01-01 00:00:50', 'Your new Empire?');
insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:01:00', 'Don''t make me kill you.');
insert into messages (user_id, conversation_id, send_time, content) values (1, 1, '1970-01-01 00:01:10', 'Anakin, my allegiance is to the Republic, to Democracy!');
insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:01:20', 'If you''re not with me, then you''re my enemy.');
insert into messages (user_id, conversation_id, send_time, content) values (1, 1, '1970-01-01 00:01:30', 'Only a Sith deals in absolutes. I will do what I must');
insert into messages (user_id, conversation_id, send_time, content) values (3, 1, '1970-01-01 00:01:40', 'You will try.');

insert into messages (user_id, conversation_id, content) values (3, 2, 'This is where the fun begins. Ten Vulture Droids straight ahead, coming down the left side.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Add five Tri-fighters on the right...');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m going head to head. See you.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Take it easy, Anakin.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Incoming!!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Five more on the right!');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Here we go.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'I''m going high and right.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Hang on. There are four more of them.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Stay with me... swing back and right... help me engage. Back off... Let them pass between us.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m coming around. I''m coming around on your tail.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'All right, engage... and hurry. These droids are all over me like a rash.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'How many back there. Artoo? insert into messages (user_id, conversation_id, content) values (Artoo beeps) Three... Four... that''s not good.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Anakin, you have four on your tail.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I know. I know!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Four more closing from your left.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I know. I know!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Break right and go high.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m going low and left.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'insert into messages (user_id, conversation_id, content) values (to himself) He still has much to learn.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Hang on, Artoo. Obi-Wan, do you copy? I''m going to pull them through the needle...');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Too dangerous. First Jedi rule: "Survive."');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Sorry, no choice. Come down here and thin them out a little.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Just keep me steady... hold on... not yet... now break left.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'You''ll never get through there, Anakin. It''s too tight.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Easy, Artoo... we''ve done this before.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Use the Force, think yourself through, the ship will follow.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Wrong thought, Artoo.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m through.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m going to go help them out!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'No, no! They are doing their job so we can do ours. Head for the Command Ship!');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Missiles! Pull up!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'They overshot us...');
insert into messages (user_id, conversation_id, content) values (3, 2, 'They''re coming around!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'All right, Arfour. No, no. Nothing too fancy.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Surge all power units. Artoo! Stand by the reverse thrusters.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'We got ''em. Artoo!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Flying is for droids.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'I''m hit! Anakin?');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I see them... Buzz Droids.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Arfour, be careful. You have one...');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Oh dear. They''re shutting down all the controls.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Move to the right so I can get a clear shot at them.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'The mission. Get to the Command Ship. Get the Chancellor! I''m running out of tricks here.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'In the name of...');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Steady... steady...');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Anakin, hold your fire... hold your fire. You''re not helping here.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I agree, bad idea. Swing right... ease over... steady...');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Wait... wait... I can''t see a thing! My cockpit''s fogging. They''re all over me, Anakin.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Move to the right.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Hold on, Anakin. You''re going to get us both killed! Get out of here. There''s nothing more you can do.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'I''m not leaving without you, Master.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Blast it... I can''t see... my controls are gone.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Get ''em, Artoo. Watch out!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Artoo, hit the buzz droid''s center eye.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Yeah, you got him!');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Great, Artoo.');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Stay on my wing... the General''s Command Ship is dead ahead. Easy... pull up... Head for the hangar.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Have you noticed the shields are still up?');
insert into messages (user_id, conversation_id, content) values (3, 2, 'Oh?!? Sorry, Master.');
insert into messages (user_id, conversation_id, content) values (1, 2, 'Oh, I have a bad feeling about this.');

insert into messages (user_id, conversation_id, content) values (1, 3, 'Chancellor.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'Are you all right?');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Count Dooku.');
insert into messages (user_id, conversation_id, content) values (1, 3, 'This time we will do it together.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'I was about to say that.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Get help! You''re no match for him. He''s a Sith Lord.');
insert into messages (user_id, conversation_id, content) values (1, 3, 'Chancellor Palpatine, Sith Lords are our specialty.');
insert into messages (user_id, conversation_id, content) values (4, 3, 'Your swords, please, Master Jedi. We don''t want to make a mess of things in front of the Chancellor.');
insert into messages (user_id, conversation_id, content) values (1, 3, 'You won''t get away this time, Dooku.');
insert into messages (user_id, conversation_id, content) values (4, 3, 'I''ve been looking forward to this.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'My powers have doubled since the last time we met, Count.');
insert into messages (user_id, conversation_id, content) values (4, 3, 'Good. Twice the pride, double the fall.');
insert into messages (user_id, conversation_id, content) values (4, 3, 'Your moves are clumsy, Kenobi... too predictable. You''ll have to do better.');
insert into messages (user_id, conversation_id, content) values (4, 3, 'I sense great fear in you, Skywalker. You have hate, you have anger, but you don''t use them.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Good, Anakin, good. I knew you could do it. Kill him. Kill him now!');
insert into messages (user_id, conversation_id, content) values (3, 3, 'I shouldn''t...');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Do it!!');
insert into messages (user_id, conversation_id, content) values (3, 3, 'I couldn''t stop myself.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'You did well, Anakin. He was too dangerous to be kept alive.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'Yes, but he was an unarmed prisoner.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'I shouldn''t have done that, Chancellor. It''s not the Jedi way.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'It is only natural. He cut off your arm, and you wanted revenge. It wasn''t the first time, Anakin. Remember what you told me about your mother and the Sand People. Now, we must leave before more security droids arrive.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Anakin, there is no time. We must get off the ship before it''s too late.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'He seems to be all right. No broken bones, breathing''s all right.');
insert into messages (user_id, conversation_id, content) values (2, 3, 'Leave him, or we''ll never make it.');
insert into messages (user_id, conversation_id, content) values (3, 3, 'His fate will be the same as ours.');
