

CREATE TABLE IF NOT EXISTS public.tb_account
(
    account_id serial NOT NULL,
    aggregated_balance real NOT NULL,
    aggregation_date_time timestamp(6) without time zone NOT NULL,
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

INSERT INTO tb_account VALUES ( 1, 100, '2020-11-25', 'TEST1 NAME1' );
INSERT INTO tb_account VALUES ( 2, 200, '2020-11-25', 'TEST2 NAME2' );
INSERT INTO tb_recurrence VALUES ( 1, 2, '2023-12-12T00:00', 10, 1, 22, '18404c10-7edc-4c21-b606-5193aab6a342',  'TEST RECURRENCE 1' );
INSERT INTO tb_entry VALUES ( 1, 0, 2, 1, 9, '2023-12-12T00:00', '3fa85f64-5717-4562-b3fc-2c963f66afa6','18404c10-7edc-4c21-b606-5193aab6a342' );
INSERT INTO tb_entry VALUES ( 2, 1, 1, 1, 9, '2023-12-12T00:00', 'faed97ca-3bc2-48a5-903a-12d3745e5da6','18404c10-7edc-4c21-b606-5193aab6a342' );