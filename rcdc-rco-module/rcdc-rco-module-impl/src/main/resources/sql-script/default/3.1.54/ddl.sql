/**rco管控软件分组表*/
CREATE TABLE IF NOT EXISTS t_rco_software_group
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    group_type  varchar(32)    NOT NULL,
    description varchar(128),
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_software_group_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_software_group.id IS '软件分组ID';
COMMENT ON COLUMN t_rco_software_group.name IS '软件分组名称';
COMMENT ON COLUMN t_rco_software_group.group_type IS '软件分组类型（内置DEFAULT、自定义CUSTOM）';
COMMENT ON COLUMN t_rco_software_group.description IS '软件分组描述';

CREATE INDEX IF NOT EXISTS t_rco_software_group_id_index ON t_rco_software_group USING btree (id);


/**rco管控软件表*/
CREATE TABLE IF NOT EXISTS t_rco_software
(
    id          uuid           NOT NULL,
    group_id   uuid            NOT NULL,
    name        varchar(64)    NOT NULL,
    name_search      varchar(64)    NOT NULL,
    description varchar(128),
    digital_sign varchar(260),
    digital_sign_flag bool default false,
    install_path varchar(260),
    install_path_flag bool default false,
    product_name varchar(260),
    product_name_flag bool default false,
    process_name varchar(260),
    process_name_flag bool default false,
    original_file_name varchar(260),
    original_file_name_flag bool default false,
    file_custom_md5 varchar(32),
    file_custom_md5_flag bool default false,
    parent_id uuid,
    directory_flag bool default false,
    top_level_file bool default false,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_software_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_software.id IS '软件ID';
COMMENT ON COLUMN t_rco_software.group_id IS '软件分组ID';
COMMENT ON COLUMN t_rco_software.name IS '软件名称';
COMMENT ON COLUMN t_rco_software.name_search IS '软件名称-用于搜索';
COMMENT ON COLUMN t_rco_software.description IS '软件描述';
COMMENT ON COLUMN t_rco_software.digital_sign IS '厂商数字签名(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.digital_sign_flag IS '厂商数字签名 是否为空标志';
COMMENT ON COLUMN t_rco_software.install_path IS '安装路径(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.install_path_flag IS '安装路径 是否为空标志';
COMMENT ON COLUMN t_rco_software.product_name IS '产品名称(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.product_name_flag IS '产品名称 是否为空标志';
COMMENT ON COLUMN t_rco_software.process_name IS '进程名(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.process_name_flag IS '进程名 是否为空标志';
COMMENT ON COLUMN t_rco_software.original_file_name IS '原始文件名(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.original_file_name_flag IS '原始文件名 是否为空标志';
COMMENT ON COLUMN t_rco_software.file_custom_md5 IS '文件自定义md5信息(长度与GT保持一致)';
COMMENT ON COLUMN t_rco_software.file_custom_md5_flag IS '文件自定义md5信息 是否为空标志';
COMMENT ON COLUMN t_rco_software.parent_id IS '绿色软件下文件对应的目录';
COMMENT ON COLUMN t_rco_software.directory_flag IS '是否为文件夹';
COMMENT ON COLUMN t_rco_software.top_level_file IS '是否最上层文件';

CREATE INDEX IF NOT EXISTS t_rco_software_id_index ON t_rco_software USING btree (id);
CREATE INDEX IF NOT EXISTS trs_group_id_index ON t_rco_software USING btree (group_id);
CREATE INDEX IF NOT EXISTS trs_name_index ON t_rco_software USING btree (name, top_level_file);
CREATE INDEX IF NOT EXISTS trs_file_custom_md5_index ON t_rco_software USING btree (file_custom_md5, top_level_file);


/**rco管控软件策略表*/
CREATE TABLE IF NOT EXISTS t_rco_software_strategy
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    description varchar(128),
    is_whitelist_mode bool,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_software_strategy_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_software_strategy.id IS '软件策略分组ID';
COMMENT ON COLUMN t_rco_software_strategy.name IS '软件策略分组名称';
COMMENT ON COLUMN t_rco_software_strategy.is_whitelist_mode IS '是否白名单运行模式';
COMMENT ON COLUMN t_rco_software_strategy.description IS '软件策略分组描述';

CREATE INDEX IF NOT EXISTS t_rco_software_strategy_id_index ON t_rco_software_strategy USING btree (id);

/**rco管控软件策略关联信息表*/
CREATE TABLE IF NOT EXISTS t_rco_software_strategy_detail
(
    id          uuid           NOT NULL,
    strategy_id uuid           NOT NULL,
    related_id  uuid           NOT NULL,
    related_type varchar(32)   NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_software_strategy_detail_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_software_strategy_detail.id IS '软件策略分组关联信息ID';
COMMENT ON COLUMN t_rco_software_strategy_detail.strategy_id IS '软件策略分组ID';
COMMENT ON COLUMN t_rco_software_strategy_detail.related_id IS '关联对象ID';
COMMENT ON COLUMN t_rco_software_strategy_detail.related_type IS '关联对象类型（GROUP组、SOFTWARE软件）';

CREATE INDEX IF NOT EXISTS t_rco_software_strategy_detail_id_index ON t_rco_software_strategy_detail USING btree (id);
CREATE INDEX IF NOT EXISTS t_rco_software_strategy_relate_index ON t_rco_software_strategy_detail USING btree (related_id,related_type);
CREATE INDEX IF NOT EXISTS t_rco_software_strategy_strategy_id_index ON t_rco_software_strategy_detail USING btree (strategy_id);

/**cbb云桌面信息表增加软件管控策略ID*/
CREATE TABLE IF NOT EXISTS t_rco_desk_info
(
    desk_id uuid           NOT NULL,
    software_strategy_id uuid,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_desk_info_pkey PRIMARY KEY (desk_id)
);

COMMENT ON COLUMN t_rco_desk_info.desk_id IS '云桌面id';
COMMENT ON COLUMN t_rco_desk_info.software_strategy_id IS '软件管控策略id';

CREATE INDEX IF NOT EXISTS t_rco_desk_info_desk_id_index ON t_rco_desk_info USING btree (desk_id);
CREATE INDEX IF NOT EXISTS t_rco_desk_info_software_strategy_id_index ON t_rco_desk_info USING btree (software_strategy_id);

/**rco用户桌面信息配置表增加软件管控策略ID*/
ALTER TABLE t_rco_user_desktop_config ADD IF NOT EXISTS software_strategy_id uuid;
/**cbb用户组桌面信息配置表增加软件管控策略ID*/
ALTER TABLE t_rco_user_group_desktop_config ADD IF NOT EXISTS software_strategy_id uuid;
/**访客桌面配置表增加软件管控策略ID*/
ALTER TABLE t_rco_user_desktop_visitor_config add IF NOT EXISTS software_strategy_id uuid;


--用户云桌面表 增加字段
ALTER TABLE t_rco_user_desktop ADD column IF NOT EXISTS  connect_closed_time timestamp(6);
COMMENT ON COLUMN "public"."t_rco_user_desktop"."connect_closed_time" IS '连接关闭时间';

-- 软件管控策略与其他对象，如桌面池等的绑定关系
CREATE TABLE IF NOT EXISTS "t_rco_software_strategy_relation" (
    "id" uuid NOT NULL,
    "software_strategy_id" uuid NOT NULL,
    "relation_type" varchar(20) NOT NULL,
    "relation_id" uuid NOT NULL,
    "create_time" timestamp,
    "update_time" timestamp,
    "version" int4 NOT NULL,
    CONSTRAINT "t_rco_software_strategy_relation_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "t_rco_software_strategy_relation"."software_strategy_id" IS '软件管控策略ID';
COMMENT ON COLUMN "t_rco_software_strategy_relation"."relation_type" IS '绑定对象类型：DESKTOP_POOL桌面池，其他待增加';
COMMENT ON COLUMN "t_rco_software_strategy_relation"."relation_id" IS '绑定对象ID';
COMMENT ON COLUMN "t_rco_software_strategy_relation"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_rco_software_strategy_relation"."update_time" IS '修改时间';
COMMENT ON TABLE "t_rco_software_strategy_relation" IS '软件管控策略与其他对象，如桌面池等的绑定关系';

CREATE INDEX IF NOT EXISTS "idx_software_strategy_relation_id" ON "t_rco_software_strategy_relation" USING btree (
  "relation_type",
  "relation_id"
);
CREATE INDEX IF NOT EXISTS "idx_software_strategy_relation_strategy_id" ON "t_rco_software_strategy_relation" USING btree (
  "software_strategy_id"
);
