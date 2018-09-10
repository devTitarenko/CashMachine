create table card
(
	id bigint auto_increment primary key,
	amount bigint not null,
	isActive bit not null,
	password varchar(255) not null
)
;

create table operation
(
	id bigint auto_increment primary key,
	amount bigint not null,
	date datetime not null,
	rest bigint not null,
	FK_CARD_ID varchar(255) null,
	constraint FK_CARD_OPERATION foreign key (FK_CARD_ID) references card (id)
)
;

INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(1111111111111111,477,true ,'caf1a3dfb505ffed0d024130f58c5cfa');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(4568541320321810,260,true ,'202cb962ac59075b964b07152d234b70');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(4568547501004810,111,false ,'202cb962ac59075b964b07152d234b70');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(4621874445630214,16,true ,'202cb962ac59075b964b07152d234b70');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(4851300015430214,984,false ,'caf1a3dfb505ffed0d024130f58c5cfa');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(4851300102799214,777,true ,'caf1a3dfb505ffed0d024130f58c5cfa');
INSERT INTO `cash_machine`.`card`(`id`,`amount`,`isActive`,`password`)
VALUES(9874215101004810,450,true ,'202cb962ac59075b964b07152d234b70');



