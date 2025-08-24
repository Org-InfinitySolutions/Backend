SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "-03:00"; -- Timezone Brasil

-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS novalocacoes
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE novalocacoes;

create table if not exists cargo
(
    id        int auto_increment
        primary key,
    descricao varchar(255)                                              not null,
    nome      enum ('ADMIN', 'FUNCIONARIO', 'USUARIO_PF', 'USUARIO_PJ') not null,
    constraint UK91fcfd99kg1af67rjv08heu1q
        unique (nome)
);

create table if not exists categorias
(
    id       int auto_increment
        primary key,
    is_ativo bit          not null,
    nome     varchar(255) null
);

create table if not exists credencial
(
    fk_usuario   binary(16)   not null
        primary key,
    ativo        bit          not null,
    email        varchar(255) not null,
    hash_senha   varchar(255) not null,
    ultimo_login datetime(6)  null,
    constraint UKi55jg4ttof7s2x6rfyopxxo6l
        unique (email)
);

create table if not exists credencial_cargo
(
    fk_usuario binary(16) not null,
    cargo_id   int        not null,
    primary key (fk_usuario, cargo_id),
    constraint FKbgafdyldqm2ddluvtidbrkgij
        foreign key (fk_usuario) references credencial (fk_usuario),
    constraint FKmpiqepm3rf73uo4o1v497hjif
        foreign key (cargo_id) references cargo (id)
);

create table if not exists endereco
(
    id_endereco int auto_increment
        primary key,
    bairro      varchar(255) not null,
    cep         varchar(10)  not null,
    cidade      varchar(255) not null,
    complemento varchar(255) null,
    estado      varchar(2)   not null,
    logradouro  varchar(255) not null,
    numero      varchar(20)  not null
);

create table if not exists produtos
(
    id             int auto_increment
        primary key,
    descricao      varchar(255) null,
    is_ativo       bit          not null,
    marca          varchar(255) not null,
    modelo         varchar(255) not null,
    qtd_estoque    int          null,
    url_fabricante varchar(255) null,
    categoria_id   int          null,
    constraint FK8rqw0ljwdaom34jr2t46bjtrn
        foreign key (categoria_id) references categorias (id)
);

create table if not exists usuario
(
    id_usuario               binary(16)   not null
        primary key,
    comprovante_data_upload  datetime(6)  null,
    comprovante_endereco     tinyblob     null,
    comprovante_nome_arquivo varchar(255) null,
    comprovante_tipo_arquivo varchar(255) null,
    data_atualizacao         datetime(6)  not null,
    data_criacao             datetime(6)  not null,
    is_ativo                 bit          not null,
    nome                     varchar(255) not null,
    telefone_celular         varchar(20)  not null,
    fk_endereco              int          not null,
    constraint FKrcwxybu0rqr9nknjgggvw1cec
        foreign key (fk_endereco) references endereco (id_endereco)
);

create table if not exists pedidos
(
    id                  int auto_increment
        primary key,
    data_atualizacao    datetime(6)                                                             not null,
    data_criacao        datetime(6)                                                             not null,
    data_entrega        datetime(6)                                                             null,
    data_retirada       datetime(6)                                                             null,
    descricao           varchar(255)                                                            null,
    situacao            enum ('APROVADO', 'CANCELADO', 'EM_ANALISE', 'EM_EVENTO', 'FINALIZADO') not null,
    tipo                enum ('INDOOR', 'OUTDOOR')                                              not null,
    fk_endereco_entrega int                                                                     null,
    fk_usuario          binary(16)                                                              not null,
    constraint FKdb6m2f8k1qnkvlr2s0cyt79gd
        foreign key (fk_endereco_entrega) references endereco (id_endereco),
    constraint FKrtohqha6j8ia2bsvmv8sg8gre
        foreign key (fk_usuario) references usuario (id_usuario)
);

create table if not exists arquivo_metadados
(
    id                bigint auto_increment
        primary key,
    blob_name         varchar(255)  not null,
    blob_url          varchar(1024) not null,
    file_size_bytes   bigint        not null,
    mime_type         varchar(100)  not null,
    original_filename varchar(255)  not null,
    tipo_anexo        tinyint       not null,
    uploaded_at       datetime(6)   not null,
    pedido_id         int           null,
    produto_id        int           null,
    usuario_id        binary(16)    null,
    constraint UKadyx6xdel64h068tx82alilv8
        unique (blob_name),
    constraint FK3xxajhh46hv0ne0b4gy6m1f4f
        foreign key (produto_id) references produtos (id),
    constraint FK97d2yh3627mgcfuh0glg842lh
        foreign key (usuario_id) references usuario (id_usuario),
    constraint FK9hpy731x0m86nwog067bxi2wv
        foreign key (pedido_id) references pedidos (id),
    check (`tipo_anexo` between 0 and 5)
);

create table if not exists pessoa_fisica
(
    fk_usuario binary(16)  not null
        primary key,
    copia_rg   tinyblob    null,
    cpf        varchar(11) not null,
    rg         varchar(20) not null,
    constraint UKg3ce8301m7h9hv5rfjtvv3kcy
        unique (rg),
    constraint UKp3d8co8s4y5h7y18fpqco1wv6
        unique (cpf),
    constraint FKbdfjfbcnjrg62bjxrtpefaid9
        foreign key (fk_usuario) references usuario (id_usuario)
);

create table if not exists pessoa_juridica
(
    fk_usuario           binary(16)   not null
        primary key,
    cartao_cnpj          tinyblob     null,
    cnpj                 varchar(14)  not null,
    contrato_social      tinyblob     null,
    razao_social         varchar(255) not null,
    telefone_residencial varchar(20)  not null,
    constraint UK3h78rtw3ei11cb43k77af5nhl
        unique (cnpj),
    constraint FKruqua5fff87cspgfg1rn4pq0
        foreign key (fk_usuario) references usuario (id_usuario)
);

create index idx_pj_telefone_residencial
    on pessoa_juridica (telefone_residencial);

create table if not exists produto_pedido
(
    id         int auto_increment
        primary key,
    qtd        int null,
    pedido_id  int null,
    produto_id int null,
    constraint FK3bs4r4l15xg41kh4fwj5nwoqb
        foreign key (pedido_id) references pedidos (id),
    constraint FK8kkew2u78bwq2d6cwbll9hx0g
        foreign key (produto_id) references produtos (id)
);

create index idx_usuario_celular
    on usuario (telefone_celular);

