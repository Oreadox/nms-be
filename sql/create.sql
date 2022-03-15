create table admin
(
    id            int auto_increment,
    username      varchar(40)            not null,
    name          varchar(20)            null,
    totp          varchar(20)            null,
    enable_totp   bool     default false not null,
    password_hash varchar(80)            not null,
    permission    text                   null,
    create_time   datetime default now() not null,
    constraint admin_pk
        primary key (id)
);

create unique index admin_username_uindex
    on admin (username);

create table tag
(
    id       int auto_increment,
    tag_name varchar(20)   not null,
    trend    int default 1 not null,
    constraint tag_pk
        primary key (id, tag_name)
);


create table news
(
    id           int auto_increment,
    title        varchar(40)            not null,
    content      text                   not null,
    use_markdown bool     default false not null,
    checked      bool     default false not null,
    author_id    int                    not null,
    release_time datetime default now() not null,
    constraint news_pk
        primary key (id),
    constraint news_admin_id_fk
        foreign key (author_id) references admin (id)
);

create index news_title_index
    on news (title);

create table news_tags
(
    id       int auto_increment,
    news_id  int         not null,
    tag_id   int         not null,
    tag_name varchar(20) not null,
    constraint news_tags_pk
        primary key (id),
    constraint news_tags_news_id_fk
        foreign key (news_id) references news (id),
    constraint news_tags_tag_id_tag_name_fk
        foreign key (tag_id, tag_name) references tag (id, tag_name)
)
    comment 'news和tag关系表';

create index news_tags_tag_name_index
    on news_tags (tag_name);





