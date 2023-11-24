

CREATE TABLE IF NOT EXISTS public.tb_account
(
    account_id serial NOT NULL,
    balance real NOT NULL,
    holder_name character varying(42) NOT NULL,
    CONSTRAINT tb_account_pkey PRIMARY KEY (account_id)
);

CREATE TABLE IF NOT EXISTS public.tb_recurrence
(
    account_destination_id integer,
    account_id integer,
    recurrence_date date NOT NULL,
    recurrence_duration integer NOT NULL,
    status smallint NOT NULL,
    value real NOT NULL,
    recurrence_id uuid NOT NULL,
    recurrence_name character varying(32) NOT NULL,
    CONSTRAINT tb_recurrence_pkey PRIMARY KEY (recurrence_id),
    CONSTRAINT FK_recurrence_account FOREIGN KEY (account_id)
    REFERENCES public.tb_account (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT FK_recurrence_account_destination FOREIGN KEY (account_destination_id)
    REFERENCES public.tb_account (account_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT tb_recurrence_status_check CHECK (status >= 0 AND status <= 2)
);

CREATE TABLE IF NOT EXISTS public.tb_entry
(
    account_id integer,
    operation_type smallint NOT NULL,
    origin_id integer,
    status smallint NOT NULL,
    value real NOT NULL,
    entry_date timestamp(6) without time zone NOT NULL,
    entry_id uuid NOT NULL,
    recurrence_id uuid,
    CONSTRAINT tb_entry_pkey PRIMARY KEY (entry_id),
    CONSTRAINT FK_entry_account_origin FOREIGN KEY (origin_id)
    REFERENCES public.tb_account (account_id) MATCH SIMPLE
                            ON UPDATE NO ACTION
                            ON DELETE NO ACTION,
    CONSTRAINT FK_entry_account FOREIGN KEY (account_id)
    REFERENCES public.tb_account (account_id) MATCH SIMPLE
                            ON UPDATE NO ACTION
                            ON DELETE NO ACTION,
    CONSTRAINT FK_entry_recurrence FOREIGN KEY (recurrence_id)
    REFERENCES public.tb_recurrence (recurrence_id) MATCH SIMPLE
                            ON UPDATE NO ACTION
                            ON DELETE NO ACTION,
    CONSTRAINT tb_entry_operation_type_check CHECK (operation_type >= 0 AND operation_type <= 1),
    CONSTRAINT tb_entry_status_check CHECK (status >= 0 AND status <= 2)
);

INSERT INTO tb_account VALUES ( 1, 10, 'TEST NAME' )