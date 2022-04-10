create table admin
(
    id            int auto_increment
        primary key,
    username      varchar(40)                          not null,
    name          varchar(20)                          null,
    email         varchar(40)                          null,
    phone         varchar(20)                          null,
    department    varchar(20)                          null,
    totp          varchar(50)                          null,
    enable_totp   tinyint(1) default 0                 not null,
    password_hash varchar(80)                          not null,
    permission    text                                 null,
    create_time   timestamp  default CURRENT_TIMESTAMP not null,
    constraint admin_username_uindex
        unique (username)
);

create table news
(
    id           int auto_increment
        primary key,
    title        varchar(40)                          not null,
    content      text                                 not null,
    use_markdown tinyint(1) default 0                 not null,
    checked      tinyint(1) default 0                 not null,
    count        int        default 0                 not null,
    release_time timestamp  default CURRENT_TIMESTAMP not null,
    author_id    int                                  not null,
    constraint news_admin_id_fk
        foreign key (author_id) references admin (id)
);

create index news_title_index
    on news (title);

create table tag
(
    id       int auto_increment,
    tag_name varchar(20)   not null,
    `desc`   varchar(20)   not null,
    trend    int default 1 not null,
    primary key (id, tag_name)
);

create table news_tags
(
    id       int auto_increment
        primary key,
    news_id  int         not null,
    tag_id   int         not null,
    tag_name varchar(20) not null,
    constraint news_tags_news_id_fk
        foreign key (news_id) references news (id),
    constraint news_tags_tag_id_tag_name_fk
        foreign key (tag_id, tag_name) references tag (id, tag_name)
)
    comment 'news和tag关系表';

create index news_tags_tag_name_index
    on news_tags (tag_name);

