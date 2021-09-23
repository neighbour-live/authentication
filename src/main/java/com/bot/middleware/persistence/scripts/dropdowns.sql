--
-- Name: awards id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.awards ALTER COLUMN id SET DEFAULT nextval('public.awards_id_seq'::regclass);


--
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- Name: skills id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skills ALTER COLUMN id SET DEFAULT nextval('public.skills_id_seq'::regclass);


--
-- Name: tasks id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks ALTER COLUMN id SET DEFAULT nextval('public.tasks_id_seq'::regclass);


--
-- Name: user_addresses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_addresses ALTER COLUMN id SET DEFAULT nextval('public.user_addresses_id_seq'::regclass);


--
-- Name: user_awards id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_awards ALTER COLUMN id SET DEFAULT nextval('public.user_awards_id_seq'::regclass);


--
-- Name: user_certifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_certifications ALTER COLUMN id SET DEFAULT nextval('public.user_certifications_id_seq'::regclass);


--
-- Name: user_payment_cards id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_payment_cards ALTER COLUMN id SET DEFAULT nextval('public.user_payment_cards_id_seq'::regclass);


--
-- Name: user_skills id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_skills ALTER COLUMN id SET DEFAULT nextval('public.user_skills_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: awards; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: skills; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES (1, '2020-03-13 00:00:20.679053+05', '2020-03-15 21:16:03.343276+05', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTg0Mjg4OTYzLCJleHAiOjE1ODUxNTI5NjN9.f_KBKb6PIDLu65-YtJR1CE1JcDuqwnPfztsg-6in2aGONzRAZAPQ9ugzUkd0XgNdl5ogs3UyhA9-11008Yi8_Q', 'PERMANENT', 'Korangi 3, Karachi', false, 0, 'engr_sami@outlook.com', NULL, false, 'Abdul Sami', 'https://www.google.com', false, false, false, 'Haroon', '$2a$10$5FfQAZujrXbT8Sqo1u28yuO4BiDNW23D6K./8SZjmHTCciDJU2JRS', '+14162742988', NULL, NULL, false, 'local', NULL, 6643943709125159927, false, 'House L-20, Sector 34/1', 'cus_GtjjC6tl6Uecm4');




--
-- Data for Name: user_addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_addresses VALUES (2, '2020-03-13 00:00:20.696055+05', '2020-03-14 22:59:19.000767+05', 'PERMANENT', 'Korangi 3, Karachi', false, 6643943715097848824, 'House L-20, Sector 34/1', 6643943709125159927);


--
-- Data for Name: user_awards; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.skills VALUES (8, '2020-03-15 21:36:47.665115+05', '2020-03-15 21:36:47.665115+05', 6644994755076940821, true, 'Time Management', 'https://cdn0.iconfinder.com/data/icons/freelancer/512/time-management-skills-freelance-512.png');
INSERT INTO public.skills VALUES (9, '2020-03-15 22:03:46.077485+05', '2020-03-15 22:03:46.077485+05', 6645001543281140758, true, 'Cognitive Ability',  'https://cdn0.iconfinder.com/data/icons/business-motivation-10/512/skills-brain-ability-cognitive-512.png');
INSERT INTO public.skills VALUES (10, '2020-03-15 22:04:10.757894+05', '2020-03-15 22:04:10.757894+05', 6645001646796563479,  true, 'Athletics',  'https://cdn1.iconfinder.com/data/icons/circus-2-1/128/57-512.png');
INSERT INTO public.skills VALUES (11, '2020-03-15 22:05:04.18011+05', '2020-03-15 22:05:04.18011+05', 6645001870860477464, true, 'Teaching', 'https://cdn0.iconfinder.com/data/icons/business-theme-3/193/cc90-512.png');
INSERT INTO public.skills VALUES (12, '2020-03-15 22:06:28.74722+05', '2020-03-15 22:06:28.74722+05', 6645002225564378137, true, 'Presentation Skills',  'https://cdn3.iconfinder.com/data/icons/banking-and-finance-3-6/48/125-512.png');
INSERT INTO public.skills VALUES (13, '2020-03-15 22:07:03.085362+05', '2020-03-15 22:07:03.085362+05', 6645002369592583194, true, 'Growth Hacker', 'https://cdn2.iconfinder.com/data/icons/data-and-growth-1/90/34-512.png');

--
-- Data for Name: user_certifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_certifications VALUES (24, '2020-03-16 00:07:37.912785+05', '2020-03-16 00:07:37.912785+05', 'https://cdn2.iconfinder.com/data/icons/data-and-growth-1/90/34-512.png', NULL, true, false, '2020-01-01', 'Google', 6645032714482916768, 'Athletics', 'My Fitness is superb, I can guide you about how to do your work efficiently',6643943709125159927, 6644994755076940821);
INSERT INTO public.user_certifications VALUES (25, '2020-03-16 00:09:58.554447+05', '2020-03-16 00:09:58.554447+05', 'https://cdn2.iconfinder.com/data/icons/data-and-growth-1/90/34-512.png', NULL, true, false, '2020-01-01', 'Google', 6645033304545992097, 'Athletics', 'My Fitness is superb, I can guide you about how to do your work efficiently', 6643943709125159927, 6645001543281140758);
INSERT INTO public.user_certifications VALUES (26, '2020-03-16 00:21:15.419213+05', '2020-03-16 00:28:34.401866+05', 'https://cdn2.iconfinder.com/data/icons/data-and-growth-1/90/34-512.png', NULL, true, false, '2020-01-01', 'Amazon', 6645036143448070157, 'Athletics', 'Sami is superb, I can guide you about how to do your work efficiently', 6643943709125159927, 6645001646796563479);


--
-- Data for Name: user_payment_cards; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: user_skills; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_skills VALUES (18, '2020-03-15 23:22:23.106186+05', '2020-03-15 23:22:23.106186+05', false, 6645021315769737174, 'Time Management', true, 'MID_LEVEL', 1, 8, 'My time management is superb, I can guide you about how to do your work efficiently');
INSERT INTO public.user_skills VALUES (19, '2020-03-15 23:26:36.365614+05', '2020-03-15 23:26:36.365614+05', false, 6645021884739657412, 'Cognitive Ability', true, 'MID_LEVEL', 1, 9, 'My Cognitive Ability is superb, I can guide you about how to do your work efficiently');
INSERT INTO public.user_skills VALUES (20, '2020-03-15 23:31:19.446541+05', '2020-03-15 23:33:18.575827+05', true, 6645023564147364272, 'Athletics edited', true, 'EXPERT', 1, 10, 'My Fitness ability is great, I can guide you about how to do your work efficiently');



--
-- Name: awards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.awards_id_seq', 1, false);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 26, true);


--
-- Name: reviews_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reviews_id_seq', 1, false);


--
-- Name: skills_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.skills_id_seq', 1, false);


--
-- Name: tasks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tasks_id_seq', 1, false);


--
-- Name: user_addresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_addresses_id_seq', 1, false);


--
-- Name: user_awards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_awards_id_seq', 1, false);


--
-- Name: user_certifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_certifications_id_seq', 1, false);


--
-- Name: user_payment_cards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_payment_cards_id_seq', 1, false);


--
-- Name: user_skills_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_skills_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- Name: awards awards_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--