# Event-driven-Bank

This is an Event driven banking system coded in JavaFX (ui) and SQL (postgres) for its database which is a Docker container
With Salt for Hashing Passwords


## Docker Setup

![image](https://github.com/Shayan2781/Event-driven-Bank/assets/99325811/66faa5d2-3aef-43e6-9ade-b78205985556)


```
docker run --hostname=1348385104c3 --mac-address=02:42:ac:11:00:02 --env=POSTGRES_PASSWORD=docker --env=PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/postgresql/15/bin --env=GOSU_VERSION=1.16 --env=LANG=en_US.utf8 --env=PG_MAJOR=15 --env=PG_VERSION=15.2-1.pgdg110+1 --env=PGDATA=/var/lib/postgresql/data --volume=/var/lib/postgresql/data -p 5432:5432 --restart=no --runtime=runc -d postgres
```

## Salt Hashing

### Signup and generate hash
```
CREATE OR REPLACE FUNCTION create_hash_password() 
RETURNS TRIGGER AS $$
DECLARE
    hashed_pass TEXT;
BEGIN
    hashed_pass = crypt(NEW.password, gen_salt('md5'));
    NEW.password = hashed_pass;
    RETURN NEW;
END;
```

### Login and compare hash
```
CREATE PROCEDURE login(IN inp_username text, IN unhashed_paasword text) AS $$
DECLARE
    logged_username TEXT;
BEGIN
      SELECT username FROM account WHERE username = inp_username AND password = crypt(unhashed_password, password) INTO logged_username;
      INSERT INTO login_log(username, login_time) VALUES (logged_username, NOW());
END;
$$ LANGUAGE plpgsql;
```

## UI

![image](https://github.com/Shayan2781/Event-driven-Bank/assets/99325811/8aab4898-2ce7-4c06-a7a2-176717b53838)

![image](https://github.com/Shayan2781/Event-driven-Bank/assets/99325811/7bf83717-363e-4020-8d58-685370df7755)

![image](https://github.com/Shayan2781/Event-driven-Bank/assets/99325811/37e54fb1-4411-481f-a789-a6a827b5de18)

![image](https://github.com/Shayan2781/Event-driven-Bank/assets/99325811/0b77206f-8f11-4bb2-bc02-67bc49b77536)


