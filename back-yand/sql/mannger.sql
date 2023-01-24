create table mannger.article
(
    id            varchar(58)                       not null comment 'id'
        primary key,
    userId        bigint                             not null comment '所属用户ID',
    userArticleid bigint   default 0                 not null comment '用户文章ID',
    articleTitle  varchar(256)                       null,
    articleUrl    varchar(256)                       not null comment '文章地址',
    type          tinyint  default 0                 null comment '文章分类',
    isPublic      tinyint  default 0                 not null comment '是否公开（0为否，1为公开）',
    viewNum       bigint   default 0                 not null comment '浏览量',
    IP            varchar(256)                       not null comment 'IP地址',
    thumbNum      bigint   default 0                 not null comment '点赞量',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '注册时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除（0否，1是）'
)
    comment '文章表';

create table mannger.articlehistory
(
    id         varchar(256)                       not null comment '文章id',
    articleUrl varchar(256)                       not null comment '文章地址',
    ip         varchar(256)                       not null comment 'ip地址',
    version    bigint                             not null comment '文章',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除（0否，1是）'
)
    comment '文章历史';

create table mannger.articlethumbrecords
(
    id        int auto_increment
        primary key,
    thumbTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '点赞时间',
    userId    bigint                             not null comment '用户ID',
    articleId varchar(256)                       not null comment '文章ID',
    constraint id
        unique (id)
)
    comment '文章点赞记录表';

create table mannger.common
(
    attribute   varchar(256)                         not null comment '属性'
        primary key,
    value       varchar(256)                         null comment '值',
    comment     varchar(256)                         null,
    type        tinyint(1) default 0                 not null comment '''0 文本型 1 文件上传型',
    createdTime datetime   default CURRENT_TIMESTAMP null comment '创建或更新事件',
    constraint attribute
        unique (attribute)
)
    comment '普通配置表';

create table mannger.type
(
    id       tinyint auto_increment comment '''ID'''
        primary key,
    fId      tinyint     default 0        null comment '''父类型''',
    typeName varchar(52) default '未分类' null comment '类型名',
    constraint id
        unique (id),
    constraint id_2
        unique (id)
);

create table mannger.user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(256)                          null comment '用户头像',
    userMail     varchar(256)                           null comment '用户邮箱',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(256)                           not null comment '密码',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount),
    constraint user_userMail_uindex
        unique (userMail)
)
    comment '用户';

