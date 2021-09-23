GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO bidontaskwriter;
--
GRANT INSERT ON ALL TABLES IN SCHEMA public TO bidontaskwriter;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO bidontaskwriter;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO bidontaskwriter;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO bidontaskwriter;
grant postgres to bidontaskwriter;

START TRANSACTION;


-- DROP TYPE IF EXISTS public.auth_provider;
CREATE TYPE public.auth_provider AS ENUM (
    'local',
    'facebook',
    'google'
    );
ALTER TYPE public.auth_provider OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.award_type;
CREATE TYPE public.award_type AS ENUM (
    'STARTER',
    'EARLY_USER',
    'TEN_TASKS_PERFORMED',
    'TEN_TASKS_BIDDER',
    'FIFTY_TASKS_PERFORMED',
    'FIFTY_TASKS_BIDDER',
    'HUNDRED_TASKS_PERFORMED',
    'HUNDRED_TASKS_BIDDER',
    'SPENT_THOUSAND_DOLLARS',
    'EARNED_THOUSAND_DOLLARS',
    'SPENT_TEN_THOUSANDS_DOLLARS',
    'EARNED_TEN_THOUSANDS_DOLLARS',
    'SPENT_MILLION',
    'EARNED_MILLION'
    );
ALTER TYPE public.award_type OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.card_brand;
CREATE TYPE public.card_brand AS ENUM (
    'VISA',
    'MASTERCARD',
    'UNIONPAY',
    'AMERICANEXPRESS'
    );
ALTER TYPE public.card_brand OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.card_type;
CREATE TYPE public.card_type AS ENUM (
    'CREDIT',
    'DEBIT',
    'ATM'
    );
ALTER TYPE public.card_type OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.skill_proficiency;
CREATE TYPE public.skill_proficiency AS ENUM (
    'BASIC',
    'MID_LEVEL',
    'AWESOME',
    'EXPERT'
    );
ALTER TYPE public.skill_proficiency OWNER TO bidontaskwriter;


-- DROP TYPE IF EXISTS public.payment_type;
CREATE TYPE public.payment_type AS ENUM (
    'FIXED',
    'HOURLY',
    'MILESTONE'
    );
ALTER TYPE public.payment_type OWNER TO bidontaskwriter;

DROP TYPE IF EXISTS public.task_repeat;
CREATE TYPE public.task_repeat AS ENUM (
    'DAILY',
    'WEEKLY',
    'MONTHLY'
    );
ALTER TYPE public.task_repeat OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.transaction_type;
CREATE TYPE public.transaction_type AS ENUM (
    'CREDIT',
    'DEBIT',
    'ESCROW'
    );
ALTER TYPE public.transaction_type OWNER TO bidontaskwriter;

-- DROP TYPE IF EXISTS public.mediums;
CREATE TYPE public.mediums AS ENUM (
    'WEBSITE', 'ANDROID', 'IOS', 'PORTAL'
    );
ALTER TYPE public.mediums OWNER TO bidontaskwriter;



-- DROP SEQUENCE IF EXISTS public.hibernate_sequence;
CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.hibernate_sequence OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.awards;
CREATE TABLE IF NOT EXISTS public.awards (
                                             id BIGSERIAL NOT NULL,
                                             create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                             update_date_time timestamp with time zone,
                                             award_icon character varying(255) NOT NULL,
                                             award_type public.award_type NOT NULL,
                                             description text NOT NULL,
                                             public_id bigint NOT NULL,
                                             title character varying(255) NOT NULL
);
ALTER TABLE public.awards OWNER TO bidontaskwriter;


DROP TABLE IF EXISTS public.reviews;
CREATE TABLE IF NOT EXISTS public.reviews (
                                              id BIGSERIAL NOT NULL,
                                              create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                              update_date_time timestamp with time zone,
                                              is_deleted_by_poster boolean DEFAULT false,
                                              is_deleted_by_tasker boolean DEFAULT false,
                                              is_removed boolean DEFAULT false,
                                              is_reported_by_poster boolean DEFAULT false,
                                              is_reported_by_tasker boolean DEFAULT false,
                                              is_reviewed_poster boolean DEFAULT false,
                                              is_reviewed_tasker boolean DEFAULT false,
                                              poster_image text,
                                              poster_name character varying(255),
                                              poster_review text,
                                              poster_stars integer,
                                              public_id bigint NOT NULL,
                                              tasker_image text,
                                              tasker_name character varying(255),
                                              tasker_review text,
                                              tasker_stars integer,
                                              poster_public_id bigint,
                                              task_public_id bigint,
                                              tasker_public_id bigint
);
ALTER TABLE public.reviews OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.skills;
CREATE TABLE IF NOT EXISTS public.skills (
                                             id BIGSERIAL NOT NULL,
                                             create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                             update_date_time timestamp with time zone,
                                             is_approved boolean DEFAULT true NOT NULL,
                                             name character varying(255) NOT NULL,
                                             public_id bigint NOT NULL,
                                             skill_icon character varying(255)
);
ALTER TABLE public.skills OWNER TO bidontaskwriter;


DROP TABLE IF EXISTS public.tasks;
CREATE TABLE IF NOT EXISTS public.tasks (
                                            id BIGSERIAL NOT NULL,
                                            public_id bigint NOT NULL,
                                            create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                            update_date_time timestamp with time zone,
                                            description text NOT NULL,
                                            is_approved boolean DEFAULT true NOT NULL,
                                            is_assigned boolean DEFAULT false NOT NULL,
                                            is_completed boolean DEFAULT false NOT NULL,
                                            is_deleted boolean DEFAULT false NOT NULL,
                                            is_pending boolean DEFAULT true NOT NULL,
                                            title character varying(255) NOT NULL,
                                            task_repeat character varying(255) DEFAULT NULL,
                                            payment_type character varying(255) NOT NULL,
                                            budget decimal NOT NULL,
                                            task_time float DEFAULT NULL,
                                            hourly_rate float DEFAULT NULL,
                                            milestone_rate float DEFAULT NULL,
                                            task_media text DEFAULT '',
                                            task_status character varying(255) DEFAULT 'APPROVED',
                                            start_date_time timestamp with time zone NOT NULL,
                                            user_address_public_id bigint,
                                            task_category_public_id bigint,
                                            task_transaction bigint,
                                            blocked_by bigint,
                                            supplier_public_id bigint,
                                            buyer_public_id bigint
);
ALTER TABLE public.tasks OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_addresses;
CREATE TABLE IF NOT EXISTS public.user_addresses (
                                                     id BIGSERIAL NOT NULL,
                                                     create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                     update_date_time timestamp with time zone,
                                                     address_type character varying(25) NOT NULL,
                                                     apartment_address character varying(255),
                                                     is_deleted boolean DEFAULT false NOT NULL,
                                                     public_id bigint NOT NULL,
                                                     address_line character varying(255) NOT NULL,
                                                     country character varying(255) NOT NULL,
                                                     state character varying(255) NOT NULL,
                                                     city character varying(255) NOT NULL,
                                                     postal_code character varying(255) NOT NULL,
                                                     lat float,
                                                     lng float,
                                                     user_id bigint
);
ALTER TABLE public.user_addresses OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_awards;
CREATE TABLE IF NOT EXISTS public.user_awards (
                                                  id BIGSERIAL NOT NULL,
                                                  create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                  update_date_time timestamp with time zone,
                                                  is_active boolean DEFAULT true NOT NULL,
                                                  is_unlocked boolean DEFAULT false NOT NULL,
                                                  progress integer NOT NULL,
                                                  public_id bigint NOT NULL,
                                                  award_id bigint,
                                                  user_id bigint
);
ALTER TABLE public.user_awards OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_payment_cards;
CREATE TABLE IF NOT EXISTS public.user_payment_cards (
                                                         id BIGSERIAL NOT NULL,
                                                         create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                         update_date_time timestamp with time zone,
                                                         card_brand character varying(255),
                                                         card_expiry_date character varying(255) NOT NULL,
                                                         card_number character varying(15) NOT NULL,
                                                         card_type character varying(255),
                                                         card_verified boolean DEFAULT false NOT NULL,
                                                         cardholder_name character varying(255) NOT NULL,
                                                         is_active boolean DEFAULT false NOT NULL,
                                                         is_deleted boolean DEFAULT false NOT NULL,
                                                         is_default boolean DEFAULT false NOT NULL,
                                                         public_id bigint NOT NULL,
                                                         user_id bigint,
                                                         stripe_source_id character varying(255) NOT NULL,
                                                         stripe_source_object text
);
ALTER TABLE public.user_payment_cards OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_skills;
CREATE TABLE IF NOT EXISTS public.user_skills (
                                                  id BIGSERIAL NOT NULL,
                                                  create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                  update_date_time timestamp with time zone,
                                                  public_id bigint NOT NULL,
                                                  title character varying(255) NOT NULL,
                                                  description  character varying(255) NOT NULL,
                                                  is_active boolean DEFAULT false NOT NULL,
                                                  is_deleted boolean DEFAULT false NOT NULL,
                                                  skill_proficiency character varying(255) NOT NULL,
                                                  user_id bigint,
                                                  skill_id bigint
);
ALTER TABLE public.user_skills OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_certifications;
CREATE TABLE IF NOT EXISTS public.user_certifications (
                                                          id BIGSERIAL NOT NULL,
                                                          create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                          update_date_time timestamp with time zone,
                                                          certification_url text,
                                                          expiry_date character varying(255),
                                                          is_approved boolean DEFAULT false NOT NULL,
                                                          is_deleted boolean DEFAULT false NOT NULL,
                                                          issuing_date character varying(255) NOT NULL,
                                                          issuing_institution character varying(255) NOT NULL,
                                                          public_id bigint NOT NULL,
                                                          title character varying(255) NOT NULL,
                                                          description character varying(255) NOT NULL,
                                                          user_id bigint,
                                                          skill_id bigint
);
ALTER TABLE public.user_certifications OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.users;
CREATE TABLE IF NOT EXISTS public.users (
                                            id BIGSERIAL NOT NULL,
                                            create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                            update_date_time timestamp with time zone,
                                            access_token character varying(255),
                                            address_type character varying(25),
                                            apartment_address character varying(255),
                                            card_verified boolean,
                                            credentials_try_count integer,
                                            email character varying(255),
                                            email_verification_token character varying(255),
                                            email_verified boolean,
                                            first_name character varying(255),
                                            image_url character varying(255),
                                            is_blocked boolean,
                                            is_deleted boolean,
                                            is_suspended boolean,
                                            last_name character varying(255),
                                            password character varying(255),
                                            phone_number character varying(255),
                                            user_tagline character varying(255),
                                            user_bio character varying(255),
                                            phone_verification_otp character varying(255),
                                            phone_verification_token character varying(255),
                                            phone_verified boolean,
                                            provider character varying(255),
                                            provider_id character varying(255),
                                            public_id bigint NOT NULL,
                                            poster_rating_avg double precision DEFAULT 0.00,
                                            tasker_rating_avg double precision DEFAULT 0.00,
                                            sterling_background_verified boolean,
                                            address_line character varying(255),
                                            postal_code character varying(25),
                                            city character varying(50),
                                            state character varying(50),
                                            country character varying(50),
                                            country_short character varying(50),
                                            currency character varying(50),
                                            ip character varying(15),
                                            dob character varying(10),
                                            lat float,
                                            lng float,
                                            stripe_id character varying(255),
                                            connect_id character varying(255)
);
ALTER TABLE public.users OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_transactions;
CREATE TABLE IF NOT EXISTS public.user_transactions (
                                                        id BIGSERIAL NOT NULL,
                                                        public_id bigint NOT NULL,
                                                        create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                        update_date_time timestamp with time zone,
                                                        transaction_type character varying(255) NOT NULL,
                                                        amount decimal NOT NULL,
                                                        purpose character varying(255),
                                                        description text,
                                                        category character varying(255),
                                                        payment_id character varying(255) NOT NULL,
                                                        user_id bigint
);
ALTER TABLE public.user_transactions OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_bank_account;
CREATE TABLE IF NOT EXISTS public.user_bank_account (
                                                        id BIGSERIAL NOT NULL,
                                                        public_id bigint NOT NULL,
                                                        create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                        update_date_time timestamp with time zone,
                                                        account_holder_name character varying(255) NOT NULL,
                                                        bank_name character varying(255) NOT NULL,
                                                        transit_number character varying(5) NOT NULL,
                                                        financial_institution_number character varying(3) NOT NULL,
                                                        account_number character varying(12) NOT NULL,
                                                        is_verified boolean DEFAULT false NOT NULL,
                                                        is_active boolean DEFAULT false NOT NULL,
                                                        is_deleted boolean DEFAULT false NOT NULL,
                                                        is_default boolean DEFAULT false NOT NULL,
                                                        user_id bigint,
                                                        stripe_source_id character varying(255) NOT NULL,
                                                        stripe_source_object text
);
ALTER TABLE public.user_bank_account OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_wallet;
CREATE TABLE IF NOT EXISTS public.user_wallet (
                                                  id BIGSERIAL NOT NULL,
                                                  public_id bigint NOT NULL,
                                                  create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                  update_date_time timestamp with time zone,
                                                  currency character varying(255) NOT NULL,
                                                  amount decimal NOT NULL,
                                                  user_id bigint

);
ALTER TABLE public.user_wallet OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.report_user;
CREATE TABLE IF NOT EXISTS public.report_user (
                                                  id BIGSERIAL NOT NULL,
                                                  public_id bigint NOT NULL,
                                                  create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                  update_date_time timestamp with time zone,
                                                  is_resolved boolean DEFAULT false NOT NULL,
                                                  subject character varying(255) NOT NULL,
                                                  issue text NOT NULL,
                                                  reporter_public_id bigint NOT NULL,
                                                  reported_public_id bigint NOT NULL

);
ALTER TABLE public.report_user OWNER TO bidontaskwriter;


DROP TABLE IF EXISTS public.support_user;
CREATE TABLE IF NOT EXISTS public.support_user (
                                                   id BIGSERIAL NOT NULL,
                                                   public_id bigint NOT NULL,
                                                   create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                   update_date_time timestamp with time zone,
                                                   is_resolved boolean DEFAULT false NOT NULL,
                                                   related_to character varying(255) NOT NULL,
                                                   description character varying(255) NOT NULL,
                                                   user_public_id bigint NOT NULL

);
ALTER TABLE public.support_user OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.task_categories;
CREATE TABLE IF NOT EXISTS public.task_categories (
                                                      id BIGSERIAL NOT NULL,
                                                      public_id bigint NOT NULL,
                                                      create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                      update_date_time timestamp with time zone,
                                                      name character varying(255) NOT NULL,
                                                      icon_url character varying(255) NOT NULL

);
ALTER TABLE public.task_categories OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_bids;
CREATE TABLE IF NOT EXISTS public.user_bids (
                                                id BIGSERIAL NOT NULL,
                                                public_id bigint NOT NULL,
                                                create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                update_date_time timestamp with time zone,
                                                is_deleted boolean default false,
                                                is_active boolean default true,
                                                hours int default 0 NOT NULL,
                                                hourly_rate double precision default 0.0 NOT NULL,
                                                budget double precision default 0.0 NOT NULL,
                                                other_costs double precision default 0.0 NOT NULL,
                                                description text,
                                                other_costs_explanation text,
                                                time_utilization_explanation text,
                                                status character varying(25) DEFAULT NULL,
                                                image_url text,
                                                user_id bigint,
                                                task_id bigint
);
ALTER TABLE public.user_bids OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.logs;
CREATE TABLE IF NOT EXISTS public.logs (
                                           id BIGSERIAL NOT NULL,
                                           request text,
                                           response TEXT,
                                           is_deleted boolean DEFAULT false NOT NULL,
                                           ip_address Character Varying(15) NOT NULL DEFAULT '120.0.0.1',
                                           user_public_id bigint NOT NULL,
                                           create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                           update_date_time timestamp with time zone
);
ALTER TABLE public.logs OWNER TO bidontaskwriter;

DROP TABLE IF EXISTS public.user_temporary;
CREATE TABLE IF NOT EXISTS public.user_temporary (
                                                     id BIGSERIAL NOT NULL,
                                                     email character varying(255),
                                                     password character varying(255),
                                                     phone_number character varying(255),
                                                     user_public_id bigint NOT NULL,
                                                     create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                     update_date_time timestamp with time zone
);
ALTER TABLE public.user_temporary OWNER TO bidontaskwriter;


alter table public.tasks
    add task_completed_as_tasker int default 0;

alter table public.tasks
    add task_completed_as_poster int default 0;


ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.awards
    ADD CONSTRAINT awards_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.skills
    ADD CONSTRAINT skills_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT user_addresses_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_awards
    ADD CONSTRAINT user_awards_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_payment_cards
    ADD CONSTRAINT user_payment_cards_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_skills
    ADD CONSTRAINT user_skills_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_certifications
    ADD CONSTRAINT user_certifications_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_transactions
    ADD CONSTRAINT user_transactions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_bank_account
    ADD CONSTRAINT user_bank_account_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_wallet
    ADD CONSTRAINT user_wallet_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.report_user
    ADD CONSTRAINT report_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.support_user
    ADD CONSTRAINT support_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.task_categories
    ADD CONSTRAINT task_categories_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_bids
    ADD CONSTRAINT user_bids_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.logs
    ADD CONSTRAINT logs_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_temporary
    ADD CONSTRAINT user_temporary_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_access_token_users UNIQUE (access_token);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_phone_number_users UNIQUE (phone_number);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_stripe_id_users UNIQUE (stripe_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_email_users UNIQUE (email);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT uk_public_id_tasks UNIQUE (public_id);

ALTER TABLE ONLY public.awards
    ADD CONSTRAINT uk_public_id_awards UNIQUE (public_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_email_verification_token_users UNIQUE (email_verification_token);

ALTER TABLE ONLY public.skills
    ADD CONSTRAINT uk_public_id_skills UNIQUE (public_id);

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT uk_public_id_reviews UNIQUE (public_id);

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT uk_public_id_user_addresses UNIQUE (public_id);

ALTER TABLE ONLY public.user_skills
    ADD CONSTRAINT uk_public_id_user_skills UNIQUE (public_id);

ALTER TABLE ONLY public.user_payment_cards
    ADD CONSTRAINT uk_public_id_user_payment_cards UNIQUE (public_id);

ALTER TABLE ONLY public.user_payment_cards
    ADD CONSTRAINT uk_card_number_user_payment_cards UNIQUE (card_number);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_phone_verification_token_users UNIQUE (phone_verification_token);

ALTER TABLE ONLY public.user_awards
    ADD CONSTRAINT uk_public_id_user_awards UNIQUE (public_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_public_id_users UNIQUE (public_id);

ALTER TABLE ONLY public.user_bank_account
    ADD CONSTRAINT uk_public_id_user_bank_account UNIQUE (public_id);

ALTER TABLE ONLY public.user_bank_account
    ADD CONSTRAINT uk_account_number_user_bank_account UNIQUE (account_number);

ALTER TABLE ONLY public.user_transactions
    ADD CONSTRAINT uk_public_id_user_transactions UNIQUE (public_id);

ALTER TABLE ONLY public.user_transactions
    ADD CONSTRAINT uk_payment_id_user_transactions UNIQUE (payment_id);

ALTER TABLE ONLY public.user_wallet
    ADD CONSTRAINT uk_public_id_user_wallet UNIQUE (public_id);

ALTER TABLE ONLY public.support_user
    ADD CONSTRAINT uk_public_id_support_user UNIQUE (public_id);

ALTER TABLE ONLY public.report_user
    ADD CONSTRAINT uk_public_id_report_user UNIQUE (public_id);

ALTER TABLE ONLY public.report_user
    ADD CONSTRAINT uk_reporter_and_reported_user_cannot_be_same CHECK (reported_public_id != reporter_public_id);

ALTER TABLE ONLY public.task_categories
    ADD CONSTRAINT uk_public_id_task_categories UNIQUE (public_id);

ALTER TABLE ONLY public.user_bids
    ADD CONSTRAINT uk_public_id_user_bids UNIQUE (public_id);

ALTER TABLE ONLY public.user_temporary
    ADD CONSTRAINT uk_email_user_temporary UNIQUE (email);

ALTER TABLE ONLY public.user_temporary
    ADD CONSTRAINT uk_phone_numbers_user_temporary UNIQUE (phone_number);



ALTER TABLE ONLY public.user_awards
    ADD CONSTRAINT fk_award_id_user_awards FOREIGN KEY (award_id) REFERENCES public.awards(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_blocked_by_tasks FOREIGN KEY (blocked_by) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk_tasker_public_id_reviews FOREIGN KEY (tasker_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_payment_cards
    ADD CONSTRAINT fk_user_id_user_payment_cards FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_supplier_public_id_tasks FOREIGN KEY (supplier_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_user_address_tasks FOREIGN KEY (user_address_public_id) REFERENCES public.user_addresses(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_task_category_tasks FOREIGN KEY (task_category_public_id) REFERENCES public.task_categories(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_task_transaction_tasks FOREIGN KEY (task_transaction) REFERENCES public.user_transactions(public_id);

ALTER TABLE ONLY public.user_bank_account
    ADD CONSTRAINT fk_user_id_user_bank_account FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_wallet
    ADD CONSTRAINT fk_user_public_id_user_wallet FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_transactions
    ADD CONSTRAINT fk_user_public_id_user_transactions FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk_poster_public_id_reviews FOREIGN KEY (poster_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk_task_public_id_reviews FOREIGN KEY (task_public_id) REFERENCES public.tasks(public_id);

ALTER TABLE ONLY public.user_skills
    ADD CONSTRAINT fk_skill_id_user_skills FOREIGN KEY (skill_id) REFERENCES public.skills(public_id);

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT fk_user_id_user_addresses FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_skills
    ADD CONSTRAINT fk_user_id_user_skills FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_awards
    ADD CONSTRAINT fk_user_id_user_awards FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_buyer_public_id_tasks FOREIGN KEY (buyer_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_certifications
    ADD CONSTRAINT fk_user_id_user_certifications FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.support_user
    ADD CONSTRAINT fk_user_id_support_user FOREIGN KEY (user_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.report_user
    ADD CONSTRAINT fk_reporter_user_id_report_user FOREIGN KEY (reporter_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.report_user
    ADD CONSTRAINT fk_reported_user_id_report_user FOREIGN KEY (reported_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_bids
    ADD CONSTRAINT fk_task_id_user_bids FOREIGN KEY (task_id) REFERENCES public.tasks(public_id);

ALTER TABLE ONLY public.user_bids
    ADD CONSTRAINT fk_user_id_user_bids FOREIGN KEY (user_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_temporary
    ADD CONSTRAINT fk_user_id_user_temporary FOREIGN KEY (user_public_id) REFERENCES public.users(public_id);

COMMIT;



-- //DROP DOWNS
START TRANSACTION;

INSERT INTO "public"."skills" ( "id", "create_date_time", "update_date_time", "public_id","is_approved", "name", "skill_icon")
VALUES
(1, '2020-03-15 21:36:47.665115+05', '2020-03-15 21:36:47.665115+05', 6649001526066056399, true, 'Time Management', 'https://cdn0.iconfinder.com/data/icons/freelancer/512/time-management-skills-freelance-512.png' ),
(2, '2020-03-15 22:03:46.077485+05', '2020-03-15 22:03:46.077485+05', 6649001526066056398, true, 'Cognitive Ability',  'https://cdn0.iconfinder.com/data/icons/business-motivation-10/512/skills-brain-ability-cognitive-512.png' ),
(3, '2020-03-15 22:04:10.757894+05', '2020-03-15 22:04:10.757894+05', 6649001526066056397,  true, 'Athletics',  'https://cdn1.iconfinder.com/data/icons/circus-2-1/128/57-512.png' ),
(4, '2020-03-15 22:05:04.18011+05', '2020-03-15 22:05:04.18011+05', 6649001526066056396, true, 'Teaching', 'https://cdn0.iconfinder.com/data/icons/business-theme-3/193/cc90-512.png'),
(5, '2020-03-15 22:06:28.74722+05', '2020-03-15 22:06:28.74722+05', 6649001526066056395, true, 'Presentation Skills',  'https://cdn3.iconfinder.com/data/icons/banking-and-finance-3-6/48/125-512.png'),
(6, '2020-03-15 22:07:03.085362+05', '2020-03-15 22:07:03.085362+05', 6649001526066056394, true, 'Growth Hacker', 'https://cdn2.iconfinder.com/data/icons/data-and-growth-1/90/34-512.png');


INSERT INTO "public"."awards" ("id", "create_date_time", "update_date_time","award_icon","award_type","description","public_id","title")
VALUES
(2, '2020-04-12 16:42:47.202083+05', '2020-04-12 16:42:47.202083+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'TEN_TASKS_PERFORMED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067625941148289, '10 Tasks Performed'),
(3, '2020-04-12 16:43:01.564268+05', '2020-04-12 16:43:01.564268+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'FIFTY_TASKS_PERFORMED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067686175548034, '50 Tasks Performed'),
(4, '2020-04-12 16:43:21.797073+05', '2020-04-12 16:43:21.797073+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'HUNDRED_TASKS_PERFORMED', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067771043095171, '100 Tasks Performed'),
(5, '2020-04-12 16:43:48.380777+05', '2020-04-12 16:43:48.381732+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'TEN_TASKS_BIDDER', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067882540278404, '10 Tasks Bidder'),
(6, '2020-04-12 16:43:57.779547+05', '2020-04-12 16:43:57.779547+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'FIFTY_TASKS_BIDDER', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067921958347397, '50 Tasks Bidder'),
(7, '2020-04-12 16:44:08.548003+05', '2020-04-12 16:44:08.548003+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'HUNDRED_TASKS_BIDDER', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067967126807174, '100 Tasks Bidder'),
(8, '2020-04-12 16:44:47.261977+05', '2020-04-12 16:44:47.261977+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'SPENT_THOUSAND_DOLLARS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655068129505092231, 'Spent 1000 Dollars'),
(9, '2020-04-12 16:45:01.493593+05', '2020-04-12 16:45:01.493593+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'SPENT_TEN_THOUSANDS_DOLLARS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655068189198426760, 'Spent 10,000 Dollars'),
(10, '2020-04-12 16:45:40.800758+05', '2020-04-12 16:45:40.800758+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'SPENT_MILLION', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655068354068128393, 'Spent Million Dollars'),
(11, '2020-04-12 16:45:51.264788+05', '2020-04-12 16:45:51.264788+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'EARNED_MILLION', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655068397953131146, 'Earned Million Dollars'),
(12, '2020-04-12 16:46:00.000134+05', '2020-04-12 16:46:00.000134+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'EARNED_TEN_THOUSANDS_DOLLARS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655068434590376587, 'Earned 10,000 Dollars'),
(13, '2020-04-12 16:50:23.743629+05', '2020-04-12 16:50:23.744628+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'EARNED_THOUSAND_DOLLARS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655069540812890765, 'Earned 1000 Dollars'),
(14, '2020-04-12 16:42:47.202083+05', '2020-04-12 16:42:47.202083+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'STARTER', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067625941148290, 'Starter'),
(15, '2020-04-12 16:43:01.564268+05', '2020-04-12 16:43:01.564268+05', 'https://cdn0.iconfinder.com/data/icons/iconika-awards/512/16-512.png', 'EARLY_USER', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 6655067686175548091, 'Early User');



INSERT INTO "public"."task_categories" ("id", "name", "public_id", "icon_url", "create_date_time","update_date_time")
VALUES
(1, 'Gardening', 6649001526066100001, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F001-gardening.svg?alt=media&token=7145f45a-ab9c-48ad-86b0-09e2052bb576', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(2, 'Laundry', 6649001526066100002, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F002-laundry.svg?alt=media&token=d8f81c1c-9e2f-4fdb-b794-58a5afaed6df', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(3, 'Carpet Flooring', 6649001526066100003, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F003-carpet-flooring.svg?alt=media&token=d65e924c-2655-4923-8ee8-b58307779102', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(4, 'Painting', 6649001526066100004, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F004-painting.svg?alt=media&token=ea795bc0-d5b2-4f4a-a63e-8d227001f2a3', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(5, 'Cleaning', 6649001526066100005, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F005-cleaning.svg?alt=media&token=70cbec65-b21a-4dd3-98e9-ede130cb56e2', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(6, 'Baby Sitting', 6649001526066100006, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F006-baby-sitting.svg?alt=media&token=922d7c35-ff34-4344-a4ff-26ed5e39cc22', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(7, 'Dishwashing', 6649001526066100007, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F007-dish-washing.svg?alt=media&token=fe38e864-0625-46be-8715-3ec5524ebc58', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(8, 'Moving', 6649001526066100008, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F008-moving.svg?alt=media&token=c0ced162-2c1c-4a1e-93eb-28b945496386', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(9, 'Sewing', 6649001526066100009, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F009-sewing.svg?alt=media&token=835fbdd1-ffad-46d4-ac74-d46ba4379d96', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(10, 'Fitness', 6649001526066100010, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F010-fitness.svg?alt=media&token=bc285e68-bbd7-4f11-8453-0a8ab18e7f6a', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(11, 'Bakery', 6649001526066100011, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F011-bakery.svg?alt=media&token=9295bbbc-4de3-4481-966c-9ad4fb518829', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(12, 'Dog Walker', 6649001526066100012, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F012-dog-walker.svg?alt=media&token=cf042be5-aa50-4abe-9f26-4a1e4a0cd105', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),

(13, 'Air-condition Repair', 6649001526066100013, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F013-aircondition-repair.svg?alt=media&token=b452777d-777b-4c3a-94b3-1740db13f88a', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(14, 'Healthcare', 6649001526066100014, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F014-health-care.svg?alt=media&token=eda37020-cc24-428c-90d2-009cc6b0177d', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(15, 'Dancing', 6649001526066100015, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F015-dancing.svg?alt=media&token=8fa68b7e-55fd-4963-859e-76fb65443d72', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(16, 'Cooking', 6649001526066100016, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F016-cooking.svg?alt=media&token=3780b5d2-c16d-423a-8363-efacd09d7f4e', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(17, 'Repair', 6649001526066100017, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F018-pest-control.svg?alt=media&token=0af2588a-477f-4f47-a0e1-ac5d0a93631a', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(18, 'Pest Control', 6649001526066100018, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F018-pest-control.svg?alt=media&token=0af2588a-477f-4f47-a0e1-ac5d0a93631a', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(19, 'Electrician', 6649001526066100019, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F019-electrician.svg?alt=media&token=83091f3a-70da-4da3-8b16-95678dc54cf7', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(20, 'Carpenter', 6649001526066100020, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F020-carpenter.svg?alt=media&token=5b2e37bb-20d1-4863-876b-f2a25b7e04c8', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(21, 'Computer Repair', 6649001526066100021, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F021-computer-repair.svg?alt=media&token=dd1802f8-24d3-4729-9989-603dd6bb2fdc', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(22, 'Technical Support', 6649001526066100022, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F022-technical-support.svg?alt=media&token=b1c0d8a7-d409-4770-9bd7-e0a54e61039d', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(23, 'Car Service', 6649001526066100023, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F023-car-service.svg?alt=media&token=2e7aa675-9161-4199-afce-778c059e1362', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(24, 'Mechanic', 6649001526066100024, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F023-car-service.svg?alt=media&token=2e7aa675-9161-4199-afce-778c059e1362', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),

(25, 'Virtual Assistant', 6649001526066100025, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F025-virtual.svg?alt=media&token=27dd31c4-b985-469e-9e57-9458687db6da', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(26, 'Administrator', 6649001526066100026, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F026-administrator.svg?alt=media&token=0c88b9fc-001f-4dce-9d7b-f7838d3b7c2d', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(27, 'Appliances Repair', 6649001526066100027, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F027-appliances-repair.svg?alt=media&token=26756e6e-91f8-4077-a18f-c5dc6da567ae', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(28, 'House Maintenance', 6649001526066100028, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F028-house-maintenance.svg?alt=media&token=844a39dc-4d12-4d43-929e-87272ea0ca1c', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(29, 'Car Wash', 6649001526066100029, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F029-car-wash.svg?alt=media&token=68bc2367-df55-4375-a595-03725d73e543', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(30, 'Catering', 6649001526066100030, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F030-catering.svg?alt=media&token=b00bc35e-dd74-44c1-a8f5-c339c64cae92', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(31, 'Plumbing', 6649001526066100031, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F031-plumbing.svg?alt=media&token=ccad8811-d608-4dde-9fc7-397a3a18faa0', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(32, 'Snow Shoveling', 6649001526066100032, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F032-snow-shoveling.svg?alt=media&token=92118e06-3d6a-4773-afda-14e535a1544f', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(33, 'Manicure', 6649001526066100033, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F033-manicure.svg?alt=media&token=0595f2f6-103d-45cc-a3ed-504de1efc1c4', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(34, 'Pedicure', 6649001526066100034, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F034-pedicure.svg?alt=media&token=21ca46b2-cf4a-43d0-a598-f21b58dfd65f', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(35, 'Lawn Moving', 6649001526066100035, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F035-lawn-mowing.svg?alt=media&token=bbd99f3d-d0eb-4573-8c11-8805b75b09c7', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(36, 'Fall Cleaning', 6649001526066100036, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F036-fall-cleaning.svg?alt=media&token=450383b3-7a9b-46a6-9b5a-4b2145d04856', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),

(37, 'Accounting', 6649001526066100037, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F037-accounting.svg?alt=media&token=34e85f73-7428-4030-8fc3-c19833cd95e6', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(38, 'COVID-19', 6649001526066100038, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F038-covid-19.svg?alt=media&token=5da10aee-bd60-48ce-ab4c-6d7c00d8f9ed', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(39, 'Sanitize', 6649001526066100039, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F039-sanitize.svg?alt=media&token=57c856c5-bfad-4e89-8af6-0e378204bffd', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(40, 'Packing', 6649001526066100040, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F040-packing.svg?alt=media&token=94cc5cf0-7df8-4aea-9602-e14e25a36a4a', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(41, 'Hair Stylist', 6649001526066100041, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F041-hair-stylist.svg?alt=media&token=e984e79d-64d7-42ad-ad3b-2f860495b6d0', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05'),
(42, 'Roofing', 6649001526066100042, 'https://firebasestorage.googleapis.com/v0/b/bidontask.appspot.com/o/assets%2Fcategories-icons%2F042-roofing.svg?alt=media&token=ee72151f-1cfd-466a-9a01-1b1a8193f972', '2020-03-26 22:58:23.068854+05', '2020-03-26 23:09:22.667395+05');

COMMIT;


--  Conversation and Chatting Start
START TRANSACTION;

-- Queries for Conversation Table
CREATE TABLE IF NOT EXISTS public.conversations (
                                                    id BIGSERIAL NOT NULL,
                                                    public_id bigint NOT NULL,
                                                    description text,
                                                    title character varying(255),
                                                    is_deleted boolean DEFAULT false NOT NULL,
                                                    is_active boolean DEFAULT true NOT NULL,

                                                    create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                    update_date_time timestamp with time zone,

                                                    tasker_public_id bigint NOT NULL,
                                                    poster_public_id bigint NOT NULL,
                                                    task_public_id bigint NOT NULL
);
ALTER TABLE public.conversations OWNER TO postgres;

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT conversations_pKey PRIMARY KEY (id);

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT uk_public_id_conversations UNIQUE (public_id);

ALTER TABLE public.conversations
    ADD CONSTRAINT uk_unique_conversations UNIQUE (tasker_public_id, poster_public_id, task_public_id);

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk_conversation_tasker FOREIGN KEY (tasker_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk_conversation_poster FOREIGN KEY (poster_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk_conversation_task FOREIGN KEY (task_public_id) REFERENCES public.tasks(public_id);


-- Queries for Chats Table

CREATE TABLE IF NOT EXISTS public.user_chats (
                                                 id BIGSERIAL NOT NULL,
                                                 public_id bigint NOT NULL,
                                                 message text,
                                                 message_attributes jsonb DEFAULT '{}',
                                                 is_deleted boolean DEFAULT false NOT NULL,
                                                 is_active boolean DEFAULT true NOT NULL,

                                                 create_date_time timestamp with time zone DEFAULT now() NOT NULL,
                                                 update_date_time timestamp with time zone,

                                                 sender_public_id bigint NOT NULL,
                                                 receiver_public_id bigint NOT NULL,
                                                 task_public_id bigint NOT NULL
);
ALTER TABLE public.user_chats OWNER TO postgres;
alter table public.user_chats alter column message_attributes type text;

ALTER TABLE ONLY public.user_chats
    ADD CONSTRAINT user_chats_pKey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_chats
    ADD CONSTRAINT uk_public_id_user_chats UNIQUE (public_id);

ALTER TABLE ONLY public.user_chats
    ADD CONSTRAINT fk_user_chats_sender FOREIGN KEY (sender_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_chats
    ADD CONSTRAINT fk_user_chats_receiver FOREIGN KEY (receiver_public_id) REFERENCES public.users(public_id);

ALTER TABLE ONLY public.user_chats
    ADD CONSTRAINT fk_user_chats_task FOREIGN KEY (task_public_id) REFERENCES public.tasks(public_id);


COMMIT;


GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO postgres;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO postgres;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO postgres;
grant postgres to postgres;