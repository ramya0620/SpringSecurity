create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

INSERT IGNORE INTO `users` VALUES ('user','{noop}12345',1);
INSERT IGNORE INTO `authorities` VALUES ('user','read');

INSERT IGNORE INTO`users` VALUES ('admin','{bcrypt}$2a$12$0G60.9OXIddqXMO.1oBauOFSbOZUG/55VUyI50uq.T1m5II5P3Diu',1);
INSERT IGNORE INTO  `authorities`  VALUES ('admin','admin');


INSERT  INTO `CUSTOMERS` (`email`,`pwd`,`role`) VALUES ('admin@gmail.com','{bcrypt}$2a$12$0G60.9OXIddqXMO.1oBauOFSbOZUG/55VUyI50uq.T1m5II5P3Diu','admin');
INSERT  INTO  `CUSTOMERS` (`email`,`pwd`,`role`)  VALUES ('user@gmail.com','{noop}12345','read');

select * from CUSTOMERS LIMIT 100;
