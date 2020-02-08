-- criando db e usuário
create schema jis2011;
use jis2011;
create database jis2011;
use jis2011;
create user 'jis2011'@'localhost' identified by 'jis2011';
grant all privileges on jis2011 to jis2011;
-- ******************************************
-- criando tabelas
create table if not exists resultado_memoria (
	id INT NOT NULL AUTO_INCREMENT,
	processador varchar(200),
    tipono varchar(200),
    dir_base varchar(200),
    arq_consulta varchar(200),
    quant_consulta int,
    quant_pilhas int,
    PRIMARY KEY (id)
)DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
commit;
