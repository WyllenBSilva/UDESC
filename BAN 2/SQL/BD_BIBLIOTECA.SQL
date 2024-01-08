create table assistente(

	cod_a integer,
	nome character varying(50),
	primary key (cod_a)
);

create table bibliotecario(

	cod_b integer,
	nome character varying(50),
	cod_a integer,
	primary key (cod_b),
	foreign key (cod_a) references assistente

);

create table livros(
	
	id integer,
	ISBN char(13),
	titulo character varying(100),
	autor character varying(50),
	editora character varying(100),
	colecao character varying(100),
	primary key (id)

);

create table exemplares(

	id integer,
	id_livro integer,
	id_assistente integer,
	localizacao character varying(150),
	primary key(id),
	foreign key (id_livro) references livros,
	foreign key (id_assistente) references assistente
	
);

create table usuarios(

	id integer,
	nome character varying(50),
	endereco character varying(500),
	telefone character varying(100),
	id_assistente integer,
	data_dev date,
	categoria character varying(100),
	periodo character varying(100),
	primary key(id),
	foreign key (id_assistente) references assistente

);

create table reservas(

	id integer,
	id_user integer,
	id_exemplar integer,
	data date,
	hora character varying(50),
	primary key(id),
	foreign key (id_user) references usuarios,
	foreign key (id_exemplar) references exemplares
	
);


create table emprestimo(

	id integer,
	id_user integer,
	id_exemplar integer,
	data date,
	multas float,
	data_dev date,
	primary key(id),
	foreign key (id_user) references usuarios,
	foreign key (id_exemplar) references exemplares

);