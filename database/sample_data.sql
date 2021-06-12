COPY conversations (conversation_id, name) FROM stdin;
1	Duel on Mustafar
2	\N
3	\N
\.

COPY users (user_id, name, login, email, password, salt, active, activation_token) FROM stdin;
1	Obi-Wan Kenobi	obiwan	obiwan@jedi.org	\\x86f6dd6a13336803807e59563860f2dae0b683646de45ce87165d65053ec0c79	\\xbaf43f0c7876e669398b09d6051c4877	t	\N
2	Chancellor Palpatine	sith	senat@gov.org	\\x2ef9fa00636f4ac3e3a55b4b60c4ace01b8ce5d19504d88cc73537eba0d19f4a	\\x5fcf65256dfb18f364392e1ff410065d	t	\N
3	Anakin Skywalker	asky	asky@jedi.org	\\x9ab2c7546e46676c7a62ba4ea45d2f80ca4ed7b0d03139c805091ce1304aea98	\\xc2eaf428d182a1f89e1d09635c0d9a48	t	\N
4	Count Dooku	dook	dook@gov.org	\\x9ab2c7546e46676c7a62ba4ea45d2f80ca4ed7b0d03139c805091ce1304aea98	\\xc2eaf428d182a1f89e1d09635c0d9a48	t	\N
\.

COPY membership (user_id, conversation_id) FROM stdin;
1	1
3	1
1	2
3	2
1	3
2	3
3	3
4	3
\.

COPY messages (user_id, conversation_id, send_time, content, type) FROM stdin;
1	1	1970-01-01 00:00:50	Your new Empire?	T
3	1	1970-01-01 00:00:00	You turned her against me!	T
1	1	1970-01-01 00:00:10	You have done that yourself.	T
3	1	1970-01-01 00:00:20	You will not take her from me!	T
1	1	1970-01-01 00:00:30	Your anger and your lust for power have already done that. You have allowed this dark lord to twist your mind, until now... until now you have become the very thing you swore to destroy.	T
3	1	1970-01-01 00:00:40	Don't lecture me, Obi-Wan. I see through the lies of the Jedi. I do not fear the dark side as you do. I have brought peace, freedom, justice and security to my new Empire!	T
3	1	1970-01-01 00:01:00	Don't make me kill you.	T
1	1	1970-01-01 00:01:10	Anakin, my allegiance is to the Republic, to Democracy!	T
3	1	1970-01-01 00:01:20	If you're not with me, then you're my enemy.	T
1	1	1970-01-01 00:01:30	Only a Sith deals in absolutes. I will do what I must	T
3	1	1970-01-01 00:01:40	You will try.	T
3	2	2021-05-26 18:37:26.534139	This is where the fun begins. Ten Vulture Droids straight ahead, coming down the left side.	T
1	2	2021-05-26 18:37:26.535544	Add five Tri-fighters on the right...	T
3	2	2021-05-26 18:37:26.537188	I'm going head to head. See you.	T
1	2	2021-05-26 18:37:26.538592	Take it easy, Anakin.	T
3	2	2021-05-26 18:37:26.539907	Incoming!!	T
1	2	2021-05-26 18:37:26.541225	Five more on the right!	T
3	2	2021-05-26 18:37:26.542526	Here we go.	T
1	2	2021-05-26 18:37:26.54373	I'm going high and right.	T
3	2	2021-05-26 18:37:26.544872	Hang on. There are four more of them.	T
1	2	2021-05-26 18:37:26.545887	Stay with me... swing back and right... help me engage. Back off... Let them pass between us.	T
3	2	2021-05-26 18:37:26.546908	I'm coming around. I'm coming around on your tail.	T
1	2	2021-05-26 18:37:26.548018	All right, engage... and hurry. These droids are all over me like a rash.	T
3	2	2021-05-26 18:37:26.549057	How many back there. Artoo? insert into messages (user_id, conversation_id, content) values (Artoo beeps) Three... Four... that's not good.	T
1	2	2021-05-26 18:37:26.550198	Anakin, you have four on your tail.	T
3	2	2021-05-26 18:37:26.55149	I know. I know!	T
1	2	2021-05-26 18:37:26.552837	Four more closing from your left.	T
3	2	2021-05-26 18:37:26.554078	I know. I know!	T
1	2	2021-05-26 18:37:26.555654	Break right and go high.	T
3	2	2021-05-26 18:37:26.556971	I'm going low and left.	T
1	2	2021-05-26 18:37:26.558208	insert into messages (user_id, conversation_id, content) values (to himself) He still has much to learn.	T
3	2	2021-05-26 18:37:26.559418	Hang on, Artoo. Obi-Wan, do you copy? I'm going to pull them through the needle...	T
1	2	2021-05-26 18:37:26.561214	Too dangerous. First Jedi rule: "Survive."	T
3	2	2021-05-26 18:37:26.562395	Sorry, no choice. Come down here and thin them out a little.	T
1	2	2021-05-26 18:37:26.563463	Just keep me steady... hold on... not yet... now break left.	T
1	2	2021-05-26 18:37:26.564727	You'll never get through there, Anakin. It's too tight.	T
3	2	2021-05-26 18:37:26.565961	Easy, Artoo... we've done this before.	T
1	2	2021-05-26 18:37:26.567165	Use the Force, think yourself through, the ship will follow.	T
3	2	2021-05-26 18:37:26.568368	Wrong thought, Artoo.	T
3	2	2021-05-26 18:37:26.569469	I'm through.	T
3	2	2021-05-26 18:37:26.570573	I'm going to go help them out!	T
1	2	2021-05-26 18:37:26.571957	No, no! They are doing their job so we can do ours. Head for the Command Ship!	T
3	2	2021-05-26 18:37:26.573256	Missiles! Pull up!	T
1	2	2021-05-26 18:37:26.57451	They overshot us...	T
3	2	2021-05-26 18:37:26.575735	They're coming around!	T
1	2	2021-05-26 18:37:26.576811	All right, Arfour. No, no. Nothing too fancy.	T
3	2	2021-05-26 18:37:26.577882	Surge all power units. Artoo! Stand by the reverse thrusters.	T
3	2	2021-05-26 18:37:26.578889	We got 'em. Artoo!	T
1	2	2021-05-26 18:37:26.579965	Flying is for droids.	T
1	2	2021-05-26 18:37:26.581121	I'm hit! Anakin?	T
3	2	2021-05-26 18:37:26.582074	I see them... Buzz Droids.	T
1	2	2021-05-26 18:37:26.583199	Arfour, be careful. You have one...	T
1	2	2021-05-26 18:37:26.58449	Oh dear. They're shutting down all the controls.	T
3	2	2021-05-26 18:37:26.585797	Move to the right so I can get a clear shot at them.	T
1	2	2021-05-26 18:37:26.586921	The mission. Get to the Command Ship. Get the Chancellor! I'm running out of tricks here.	T
1	2	2021-05-26 18:37:26.587987	In the name of...	T
3	2	2021-05-26 18:37:26.589041	Steady... steady...	T
1	2	2021-05-26 18:37:26.590192	Anakin, hold your fire... hold your fire. You're not helping here.	T
3	2	2021-05-26 18:37:26.59126	I agree, bad idea. Swing right... ease over... steady...	T
1	2	2021-05-26 18:37:26.592387	Wait... wait... I can't see a thing! My cockpit's fogging. They're all over me, Anakin.	T
3	2	2021-05-26 18:37:26.593618	Move to the right.	T
1	2	2021-05-26 18:37:26.594713	Hold on, Anakin. You're going to get us both killed! Get out of here. There's nothing more you can do.	T
3	2	2021-05-26 18:37:26.595773	I'm not leaving without you, Master.	T
1	2	2021-05-26 18:37:26.59699	Blast it... I can't see... my controls are gone.	T
3	2	2021-05-26 18:37:26.59824	Get 'em, Artoo. Watch out!	T
1	2	2021-05-26 18:37:26.599463	Artoo, hit the buzz droid's center eye.	T
3	2	2021-05-26 18:37:26.600896	Yeah, you got him!	T
1	2	2021-05-26 18:37:26.602256	Great, Artoo.	T
3	2	2021-05-26 18:37:26.604079	Stay on my wing... the General's Command Ship is dead ahead. Easy... pull up... Head for the hangar.	T
1	2	2021-05-26 18:37:26.605534	Have you noticed the shields are still up?	T
3	2	2021-05-26 18:37:26.606658	Oh?!? Sorry, Master.	T
1	2	2021-05-26 18:37:26.607646	Oh, I have a bad feeling about this.	T
1	3	2021-05-26 18:37:26.608676	Chancellor.	T
3	3	2021-05-26 18:37:26.609641	Are you all right?	T
2	3	2021-05-26 18:37:26.610759	Count Dooku.	T
1	3	2021-05-26 18:37:26.61187	This time we will do it together.	T
3	3	2021-05-26 18:37:26.613253	I was about to say that.	T
2	3	2021-05-26 18:37:26.615119	Get help! You're no match for him. He's a Sith Lord.	T
1	3	2021-05-26 18:37:26.61719	Chancellor Palpatine, Sith Lords are our specialty.	T
4	3	2021-05-26 18:37:26.619026	Your swords, please, Master Jedi. We don't want to make a mess of things in front of the Chancellor.	T
1	3	2021-05-26 18:37:26.620931	You won't get away this time, Dooku.	T
4	3	2021-05-26 18:37:26.622629	I've been looking forward to this.	T
3	3	2021-05-26 18:37:26.624636	My powers have doubled since the last time we met, Count.	T
4	3	2021-05-26 18:37:26.626711	Good. Twice the pride, double the fall.	T
4	3	2021-05-26 18:37:26.628176	Your moves are clumsy, Kenobi... too predictable. You'll have to do better.	T
4	3	2021-05-26 18:37:26.629616	I sense great fear in you, Skywalker. You have hate, you have anger, but you don't use them.	T
2	3	2021-05-26 18:37:26.630926	Good, Anakin, good. I knew you could do it. Kill him. Kill him now!	T
3	3	2021-05-26 18:37:26.632604	I shouldn't...	T
2	3	2021-05-26 18:37:26.63391	Do it!!	T
3	3	2021-05-26 18:37:26.635308	I couldn't stop myself.	T
2	3	2021-05-26 18:37:26.636548	You did well, Anakin. He was too dangerous to be kept alive.	T
3	3	2021-05-26 18:37:26.637624	Yes, but he was an unarmed prisoner.	T
3	3	2021-05-26 18:37:26.63878	I shouldn't have done that, Chancellor. It's not the Jedi way.	T
2	3	2021-05-26 18:37:26.640071	It is only natural. He cut off your arm, and you wanted revenge. It wasn't the first time, Anakin. Remember what you told me about your mother and the Sand People. Now, we must leave before more security droids arrive.	T
2	3	2021-05-26 18:37:26.64151	Anakin, there is no time. We must get off the ship before it's too late.	T
3	3	2021-05-26 18:37:26.642733	He seems to be all right. No broken bones, breathing's all right.	T
2	3	2021-05-26 18:37:26.644096	Leave him, or we'll never make it.	T
3	3	2021-05-26 18:37:26.645431	His fate will be the same as ours.	T
\.

SELECT pg_catalog.setval('conversation_id', 4, true);


SELECT pg_catalog.setval('user_id', 5, true);
