CREATE OR REPLACE FUNCTION create_account_number ()
RETURNS TRIGGER AS $$
DECLARE
    acc_num_temp BIGINT;
    row_id BIGINT;
    acc_num VARCHAR(16);
BEGIN
    acc_num_temp := 1000000000000000 + NEW.row_id
    acc_num := acc_num_temp::VARCHACR(16)
    NEW.account_number := acc_num;
    RETURN NEW;

END;



CREATE OR REPLACE FUNCTION create_username ()
RETURNS TRIGGER AS $$
DECLARE
    username TEXT;
BEGIN
    username = CONCAT(NEW.first_name, NEW.last_name, NEW.row_id);
    NEW.username := username;
    RETURN NEW;

END;


CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE FUNCTION create_hash_password() 
RETURNS TRIGGER AS $$
DECLARE
    hashed_pass TEXT;
BEGIN
    hashed_pass := crypt(NEW.password, gen_salt('md5'));
    NEW.password := hashed_pass;
    RETURN NEW;
END;





CREATE OR REPLACE PROCEDURE sign_up(IN in_password text, IN in_first_name text, IN in_last_name text, IN in_national_id character[], IN in_dob text, IN in_type text, IN in_interest_rate double precision)
AS $$
BEGIN
    INSERT INTO account (password, first_name, last_name, national_id, date_of_birth, type, interest_rate) VALUES (in_password, in_first_name, in_last_name, in_national_id, in_dob, in_type, in_interest_rate);
END
$$ LANGUAGE plpgsql;

CREATE FUNCTION public.set_zero_balance()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST null
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
  INSERT INTO latest_balance (accountNumber, amount) VALUES (NEW.account_number, 0.0);
  RETURN NEW;
END;
$BODY$;




CREATE PROCEDURE login(IN inp_username text, IN unhashed_paasword text) AS $$
DECLARE
    logged_username TEXT;
BEGIN
      SELECT username FROM account WHERE username = inp_username AND password = crypt(unhashed_password, password) INTO logged_username;
      INSERT INTO login_log(username, login_time) VALUES (logged_username, NOW());
END;
$$ LANGUAGE plpgsql;





CREATE OR REPLACE PROCEDURE check_balance (OUT balance FLOAT) AS $$
DECLARE
    last_user TEXT;
BEGIN
    SELECT username INTO last_user FROM login_log ORDER BY login_time DESC LIMIT 1;
    SELECT amount INTO balance FROM latest_balance WHERE accountNumber = last_user;
END;
$$ LANGUAGE plpgsql;




CREATE OR REPLACE PROCEDURE deposit(IN deposit_amount FLOAT)
LANGUAGE plpgsql
AS $$
DECLARE user TEXT;
BEGIN
  SELECT username INTO user FROM login_log ORDER BY login_time DESC LIMIT 1;
  INSERT INTO transaction ("type", "from", "to", amount)
  VALUES ('deposit', NULL, latest_user, deposit_amount);
END;
$$;




CREATE OR REPLACE PROCEDURE withdraw(IN withdraw_amount FLOAT)
LANGUAGE plpgsql
AS $$
DECLARE latest_user TEXT;
BEGIN

  SELECT username INTO latest_user FROM login_log ORDER BY login_time DESC LIMIT 1;
  INSERT INTO transaction ("type", "from", "to", amount)
  VALUES ('withdraw', latest_user, NULL, wothdraw_amount);
END;
$$;





CREATE OR REPLACE PROCEDURE transfer(IN destination TEXT, IN transfer_amount FLOAT)
LANGUAGE plpgsql
AS $$
DECLARE
    last_user TEXT;
BEGIN
    SELECT username INTO last_user FROM login_log ORDER BY login_time DESC LIMIT 1;
    INSERT INTO transaction (type, "from", "to", amount)
    VALUES ('transfer', last_user, destination, u_amount);
END;
$$;





CREATE OR REPLACE PROCEDURE interest_payment() AS $$
DECLARE
    inp_username TEXT;
    inp_account_number TEXT;
BEGIN
    SELECT inp_username INTO last_user FROM login_log ORDER BY login_time DESC LIMIT 1;
    SELECT account_number INTO inp_account_number FROM account WHERE account.username = inp_username;
    INSERT INTO transaction (type, "from", "to", amount)
    VALUES ('interest', NULL, inp_username, (SELECT interest_rate FROM account WHERE account.username = inp_username)  * (SELECT amount FROM latest_balance WHERE account_number = inp_account_number));
END;
$$ LANGUAGE plpgsql;






