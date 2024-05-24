--- 3.1.54 START --
--/**rco管控软件分组表*/
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


-- /**rco管控软件表*/
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
--- 3.1.54 END --
--- 3.1.90 START --
alter table t_rco_open_api_task_info add COLUMN IF NOT EXISTS task_item_list varchar;
alter table t_rco_open_api_task_info add COLUMN IF NOT EXISTS  task_result varchar;
alter table t_rco_open_api_task_info alter column exception_message type varchar using exception_message::varchar;
comment on column t_rco_open_api_task_info.task_item_list is '子任务列表';
comment on column t_rco_open_api_task_info.task_result is '任务结果';


-- 终端镜像下载状态表
CREATE TABLE IF NOT EXISTS "t_rco_image_download_state" (
  "id" uuid NOT NULL,
  "terminal_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "image_id" uuid,
  "download_state" varchar(32) COLLATE "pg_catalog"."default",
  "fail_code" int4,
  "download_finish_time" timestamp(6),
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "version" int4,
  CONSTRAINT "t_rco_image_download_state_pkey" PRIMARY KEY ("id")
);

-- 创建终端镜像下载状态表索引
CREATE INDEX IF NOT EXISTS idx_image_download_state ON t_rco_image_download_state (terminal_id);

COMMENT ON COLUMN "t_rco_image_download_state"."id" IS 'id';
COMMENT ON COLUMN "t_rco_image_download_state"."terminal_id" IS '终端id';
COMMENT ON COLUMN "t_rco_image_download_state"."image_id" IS '镜像模板id';
COMMENT ON COLUMN "t_rco_image_download_state"."download_state" IS '下发状态';
COMMENT ON COLUMN "t_rco_image_download_state"."fail_code" IS '错误码';
COMMENT ON COLUMN "t_rco_image_download_state"."download_finish_time" IS '下发成功的时间';
COMMENT ON COLUMN "t_rco_image_download_state"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_rco_image_download_state"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_rco_image_download_state"."version" IS '乐观锁字段';
--- 3.1.90 END --
--- 3.1.93 START --
/* 外系统映射表 */
DROP TABLE IF EXISTS t_rco_system_business_mapping;
CREATE TABLE IF NOT EXISTS t_rco_system_business_mapping
(
    id            uuid           NOT NULL,
    system_type   text           NOT NULL,
    business_type text           NOT NULL,
    src_id        text           NOT NULL,
    dest_id       text           NULL,
    context       text           NULL,
    create_date   timestamp(6)   NOT NULL,
    update_date   timestamp(6)   NOT NULL,
    version       int4 DEFAULT 0 NOT NULL,
    CONSTRAINT "t_rco_system_business_mapping_pkey" PRIMARY KEY ("id")
);

create index if not exists index_t_rco_system_business_mapping_1 on t_rco_system_business_mapping (system_type, business_type, src_id);

COMMENT ON COLUMN t_rco_system_business_mapping.id IS '主键标识';
COMMENT ON COLUMN t_rco_system_business_mapping.system_type IS '系统类型';
COMMENT ON COLUMN t_rco_system_business_mapping.business_type IS '业务类型';
COMMENT ON COLUMN t_rco_system_business_mapping.src_id IS '源业务标识';
COMMENT ON COLUMN t_rco_system_business_mapping.dest_id IS '目标业务标识';
COMMENT ON COLUMN t_rco_system_business_mapping.create_date IS '创建时间';
COMMENT ON COLUMN t_rco_system_business_mapping.update_date IS '更新时间';
COMMENT ON COLUMN t_rco_system_business_mapping.version IS '版本号';


DROP INDEX IF EXISTS t_cbb_desk_strategy_query_page_idx;
CREATE INDEX IF NOT EXISTS "t_cbb_desk_strategy_query_page_idx" ON "public"."t_cbb_desk_strategy" USING btree (
  "pattern" ,
  "system_size" ,
  "is_allow_local_disk" ,
  "strategy_type",
  "name"
);
--- 3.1.93 END --
--- 3.1.95 START --
-- 增加目标业务标识索引
create index if not exists index_t_rco_system_business_mapping_2 on t_rco_system_business_mapping (system_type, business_type, dest_id);
--- 3.1.95 END --
--- 3.1.96 START --
-- 增加云桌面策略、网络策略的自增序列
create sequence if not exists sequence_global_business_increment increment 1 minvalue 1 maxvalue 99999999 start 1 cache 1 cycle;
CREATE INDEX IF NOT EXISTS "index_t_rco_system_business_mapping_create_date" ON "t_rco_system_business_mapping" ("create_date" ASC);
--- 3.1.96 END --
--- 3.1.130 START--
--已经添加域的云桌面标识
ALTER TABLE t_rco_user_desktop ADD COLUMN IF NOT EXISTS has_auto_join_domain bool DEFAULT FALSE;
COMMENT ON COLUMN t_rco_user_desktop.has_auto_join_domain IS '是否加入域';
-- 增加是否允许开启系统盘自动扩容
ALTER TABLE t_rco_user_desktop ADD COLUMN IF NOT EXISTS "enable_full_system_disk" BOOLEAN DEFAULT FALSE NOT NULL;
COMMENT ON COLUMN t_rco_user_desktop.enable_full_system_disk IS '是否开启系统盘自动扩容，开启后终端磁盘将全部作为系统盘';

-- 软件表增加黑名单标识字段
alter table t_rco_software add COLUMN IF NOT EXISTS "digital_sign_black_flag" bool DEFAULT false;
COMMENT ON COLUMN "public"."t_rco_software"."digital_sign_black_flag" IS '黑名单厂商数字签名 是否为空标志';

alter table t_rco_software add COLUMN IF NOT EXISTS "product_name_black_flag" bool DEFAULT false;
COMMENT ON COLUMN "public"."t_rco_software"."product_name_black_flag" IS '黑名单产品名称 是否为空标志';

alter table t_rco_software add COLUMN IF NOT EXISTS "process_name_black_flag" bool DEFAULT false;
COMMENT ON COLUMN "public"."t_rco_software"."process_name_black_flag" IS '黑名单进程名 是否为空标志';

alter table t_rco_software add COLUMN IF NOT EXISTS "original_file_name_black_flag" bool DEFAULT false;
COMMENT ON COLUMN "public"."t_rco_software"."original_file_name_black_flag" IS '黑名单原始文件名 是否为空标志';

alter table t_rco_software add COLUMN IF NOT EXISTS "file_custom_md5_black_flag" bool DEFAULT false;
COMMENT ON COLUMN "public"."t_rco_software"."file_custom_md5_black_flag" IS '黑名单文件自定义md5信息 是否为空标志';


-- 增加软控策略id
alter table  t_rco_user_terminal_group_desk_config add COLUMN IF NOT EXISTS software_strategy_id uuid;
--- 3.1.130 END --
--- 3.1.150 START --
/* 桌面池与用户关系表 */
-- 池与用户关系
CREATE TABLE IF NOT EXISTS t_rco_desktop_pool_user (
                                         id uuid NOT NULL,
                                         desktop_pool_id uuid NOT NULL,
                                         related_id uuid NOT NULL,
                                         related_type varchar(64) NOT NULL,
                                         create_time timestamp,
                                         version int4 DEFAULT 0 NOT NULL,
                                         CONSTRAINT t_rco_desktop_pool_user_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE t_rco_desktop_pool_user IS '桌面池与用户关系表';
COMMENT ON COLUMN t_rco_desktop_pool_user.id IS '桌面池与用户关系表id，主键';
COMMENT ON COLUMN t_rco_desktop_pool_user.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_desktop_pool_user.related_id IS '关联对象id';
COMMENT ON COLUMN t_rco_desktop_pool_user.related_type IS '关联对象类型：;USER：用户;USER_GROUP：用户组';
COMMENT ON COLUMN t_rco_desktop_pool_user.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_pool_user.version IS '版本号';

CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_index" ON "t_rco_desktop_pool_user" ("related_id");
CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_type_index" ON "t_rco_desktop_pool_user" ("related_id", "related_type");
CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_pool_id_index" ON "t_rco_desktop_pool_user" ("desktop_pool_id", "related_id");

CREATE TABLE IF NOT EXISTS t_rco_desktop_pool_config (
    id uuid NOT NULL,
    desktop_pool_id uuid NOT NULL,
    software_strategy_id uuid,
    user_profile_strategy_id uuid,
    create_time timestamp(6) NOT NULL,
    update_time timestamp(6),
    version int4 NOT NULL DEFAULT 0,
    CONSTRAINT t_rco_desktop_pool_config_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS "uniq_desktop_pool_id" ON "t_rco_desktop_pool_config" USING btree (
  "desktop_pool_id" "pg_catalog"."uuid_ops" ASC NULLS LAST
);

COMMENT ON COLUMN t_rco_desktop_pool_config.id IS '主键';
COMMENT ON COLUMN t_rco_desktop_pool_config.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_desktop_pool_config.software_strategy_id IS '软件管控策略id';
COMMENT ON COLUMN t_rco_desktop_pool_config.user_profile_strategy_id IS 'UPM策略id';
COMMENT ON COLUMN t_rco_desktop_pool_config.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_pool_config.update_time IS '修改时间';
COMMENT ON COLUMN t_rco_desktop_pool_config.version IS '版本号';
COMMENT ON TABLE t_rco_desktop_pool_config IS '桌面池的业务层配置信息';
--- 3.1.150 END --
--- 3.1.151 START--
/* 磁盘-用户关联表 */
CREATE TABLE IF NOT EXISTS t_rco_user_disk (
    id uuid NOT NULL,
    disk_id uuid NOT NULL,
    user_id uuid,
    create_time timestamp(6),
    latest_use_time timestamp(6),
    version  int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_disk_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_disk.id IS '关联ID';
COMMENT ON COLUMN t_rco_user_disk.disk_id IS '磁盘ID';
COMMENT ON COLUMN t_rco_user_disk.user_id IS '用户ID';
COMMENT ON COLUMN t_rco_user_disk.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_user_disk.latest_use_time IS '最新使用时间';
COMMENT ON COLUMN t_rco_user_disk.version IS '版本号';

/* 桌面池与用户关系表 */
-- 池与用户关系
CREATE TABLE IF NOT EXISTS t_rco_disk_pool_user (
    id uuid NOT NULL,
    disk_pool_id uuid NOT NULL,
    related_id uuid NOT NULL,
    related_type varchar(64) NOT NULL,
    create_time timestamp,
    version int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_disk_pool_user_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE  t_rco_disk_pool_user IS '磁盘池与用户关系表';
COMMENT ON COLUMN t_rco_disk_pool_user.id IS '磁盘池与用户关系表id，主键';
COMMENT ON COLUMN t_rco_disk_pool_user.disk_pool_id IS '磁盘池id';
COMMENT ON COLUMN t_rco_disk_pool_user.related_id IS '关联对象id';
COMMENT ON COLUMN t_rco_disk_pool_user.related_type IS '关联对象类型：;USER：用户;USER_GROUP：用户组';
COMMENT ON COLUMN t_rco_disk_pool_user.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_disk_pool_user.version IS '版本号';

CREATE INDEX IF NOT EXISTS "t_rco_disk_pool_user_related_id_index" ON "t_rco_disk_pool_user" ("related_id");
CREATE INDEX IF NOT EXISTS "t_rco_disk_pool_user_related_id_type_index" ON "t_rco_disk_pool_user" ("related_id", "related_type");
CREATE INDEX IF NOT EXISTS "t_rco_disk_pool_user_related_id_pool_id_index" ON "t_rco_disk_pool_user" ("disk_pool_id", "related_id");

-- 云桌面会话使用记录表
CREATE TABLE IF NOT EXISTS "public"."t_rco_desktop_session_log" (
  "id" uuid NOT NULL,
  "desktop_id" uuid NOT NULL,
  "desktop_name" varchar(64) COLLATE "pg_catalog"."default",
  "desktop_pool_type" varchar(255) COLLATE "pg_catalog"."default",
  "login_time" timestamp(6),
  "logout_time" timestamp(6),
  "user_group_id" uuid,
  "user_id" uuid,
  "user_name" varchar(256) COLLATE "pg_catalog"."default",
  "user_group_name" varchar(256) COLLATE "pg_catalog"."default",
  "version" int4 NOT NULL DEFAULT 0,
  "create_time" timestamp(6),
  "desktop_pool_name" varchar(64) COLLATE "pg_catalog"."default",
  "related_id" uuid,
  CONSTRAINT "t_rco_desktop_session_log_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."id" IS 'id';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."desktop_id" IS '桌面id';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."desktop_name" IS '桌面名称';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."desktop_pool_type" IS '桌面类型：普通桌面、静态池桌面、动态池桌面、多会话桌面';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."login_time" IS '会话登录时间';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."logout_time" IS '会话退出时间';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."user_group_id" IS '用户组id';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."user_id" IS '用户id';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."user_name" IS '用户名称';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."user_group_name" IS '用户组名称';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."version" IS '版本号';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."desktop_pool_name" IS '桌面池名称';

COMMENT ON COLUMN "public"."t_rco_desktop_session_log"."related_id" IS '桌面池id';

-- 以小时为单位的云桌面会话使用记录表
CREATE TABLE IF NOT EXISTS "public"."t_desktop_session_log_hour_record" (
  "id" uuid NOT NULL,
  "day_key" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "hour_key" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "count" numeric(32,2),
  "related_type" varchar(64) COLLATE "pg_catalog"."default",
  "related_id" uuid,
  "type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  CONSTRAINT "t_desktop_session_log_hour_record_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."id" IS 'id';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."day_key" IS '天key';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."hour_key" IS '小时key';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."version" IS '版本号';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."count" IS '数量';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."related_type" IS '关联类型：桌面池';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."related_id" IS '关联id';

COMMENT ON COLUMN "public"."t_desktop_session_log_hour_record"."type" IS '类型：使用率，连接失败数';

-- t_desktop_session_log_day_record
CREATE TABLE IF NOT EXISTS "public"."t_desktop_session_log_day_record" (
  "id" uuid NOT NULL,
  "day_key" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "month_key" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "related_type" varchar(64) COLLATE "pg_catalog"."default",
  "related_id" uuid,
  "create_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "count" numeric(32,2),
  "type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  CONSTRAINT "t_desktop_session_log_day_record_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."id" IS 'id';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."day_key" IS '天key';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."month_key" IS '月key';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."related_type" IS '关联类型：桌面池';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."related_id" IS '关联id';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."version" IS '版本号';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."count" IS '数量';

COMMENT ON COLUMN "public"."t_desktop_session_log_day_record"."type" IS '类型：使用率，连接失败数';

-- 分配云桌面失败记录表
CREATE TABLE IF NOT EXISTS "public"."t_rco_user_connect_desktop_fault_log" (
  "id" uuid NOT NULL,
  "user_id" uuid,
  "user_name" varchar(256) COLLATE "pg_catalog"."default",
  "related_type" varchar(64) COLLATE "pg_catalog"."default",
  "related_id" uuid,
  "fault_type" varchar(64) COLLATE "pg_catalog"."default",
  "fault_desc" text COLLATE "pg_catalog"."default",
  "fault_time" timestamp(6),
  "user_group_id" uuid,
  "user_group_name" varchar(256) COLLATE "pg_catalog"."default",
  "version" int4 NOT NULL DEFAULT 0,
  "desktop_pool_name" varchar(64) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_rco_user_connect_desktop_fault_log_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."id" IS 'id';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."user_id" IS '用户id';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."user_name" IS '用户名';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."related_type" IS '关联类型：桌面池';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."related_id" IS '桌面id、桌面池id';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."fault_type" IS '故障类型：资源不足、其它';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."fault_desc" IS '故障描述';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."fault_time" IS '故障时间';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."user_group_id" IS '用户组id';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."user_group_name" IS '用户组名称';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."version" IS '版本号';

COMMENT ON COLUMN "public"."t_rco_user_connect_desktop_fault_log"."desktop_pool_name" IS '桌面池名称';
--- 3.1.151 END --
--- 3.1.152 START --
/**rco用户配置路径表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_path
(
    id          uuid           NOT NULL,
    group_id    uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    description varchar(128),
    import_user_profile_path_type varchar(16),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    creator_user_name varchar(64),
    is_default BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT t_rco_user_profile_path_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path.id IS '用户配置路径ID';
COMMENT ON COLUMN t_rco_user_profile_path.group_id IS '用户配置路径组ID';
COMMENT ON COLUMN t_rco_user_profile_path.name IS '路径名称';
COMMENT ON COLUMN t_rco_user_profile_path.description IS '描述';
COMMENT ON COLUMN t_rco_user_profile_path.creator_user_name IS '创建人名称';
COMMENT ON COLUMN t_rco_user_profile_path.is_default IS '是否系统自带';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_path_id_index ON t_rco_user_profile_path USING btree (id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_path_group_id_index ON t_rco_user_profile_path USING btree (group_id);


/**rco用户配置路径组表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_path_group
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    description varchar(128),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_path_group_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path_group.id IS '用户配置路径组ID';
COMMENT ON COLUMN t_rco_user_profile_path_group.name IS '组名称';
COMMENT ON COLUMN t_rco_user_profile_path_group.description IS '组描述';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_group_id_index ON t_rco_user_profile_path_group USING btree (id);
CREATE unique INDEX IF NOT EXISTS t_rco_user_profile_path_group_name_index ON t_rco_user_profile_path_group USING btree (name);


/**rco用户配置策略表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_strategy
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    storage_type varchar(16)   NOT NULL,
    disk_path    text,
    disk_size    int2,
    description varchar(128),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    creator_user_name varchar(64),
    CONSTRAINT t_rco_user_profile_strategy_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_strategy.id IS '用户配置策略ID';
COMMENT ON COLUMN t_rco_user_profile_strategy.name IS '策略名称';
COMMENT ON COLUMN t_rco_user_profile_strategy.storage_type IS '存储位置(本地/UNC路径)';
COMMENT ON COLUMN t_rco_user_profile_strategy.disk_path IS '磁盘路径';
COMMENT ON COLUMN t_rco_user_profile_strategy.disk_size IS '磁盘容量 单位:GB';
COMMENT ON COLUMN t_rco_user_profile_strategy.description IS '描述';
COMMENT ON COLUMN t_rco_user_profile_strategy.creator_user_name IS '创建人名称';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_id_index ON t_rco_user_profile_strategy USING btree (id);


/**rco用户配置策略与路径关联表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_strategy_related
(
    id          uuid           NOT NULL,
    strategy_id uuid           NOT NULL,
    related_id  uuid           NOT NULL,
    related_type varchar(32)   NOT NULL,
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_strategy_related_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_strategy_related.id IS '用户配置策略与路径关联ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.strategy_id IS '用户配置策略ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.related_id IS '关联对象ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.related_type IS '关联对象类型（GROUP组、PATH路径）';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_id_index ON t_rco_user_profile_strategy_related USING btree (id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_strategy_id_index ON t_rco_user_profile_strategy_related USING btree (strategy_id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_related_union_index ON t_rco_user_profile_strategy_related USING btree (related_id,related_type);

/**rco云桌面信息与策略关联*/
ALTER TABLE t_rco_desk_info ADD column if not exists user_profile_strategy_id uuid;
ALTER TABLE t_rco_desk_info ADD column if not exists update_time timestamp(6);
ALTER TABLE t_rco_desk_info ADD column if not exists create_time timestamp(6);

/**rco用户桌面信息配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_desktop_config ADD column if not exists user_profile_strategy_id uuid;
/**rco用户组桌面信息配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_group_desktop_config ADD column if not exists user_profile_strategy_id uuid;
/**rco访客桌面配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_desktop_visitor_config ADD column if not exists user_profile_strategy_id uuid;
/**rco终端组桌面信息配置表增加用户配置策略ID*/
alter table t_rco_user_terminal_group_desk_config ADD column if not exists user_profile_strategy_id uuid;
--- 3.1.152 END --
--- 3.1.153 START--
/**rco用户配置子路径表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_child_path
(
    id          uuid           NOT NULL,
    mode        varchar(16)    NOT NULL,
    type        varchar(16)    NOT NULL,
    user_profile_path_id uuid  NOT NULL,
    index       int4           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_child_path_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_child_path.id IS '子路径类型ID';
COMMENT ON COLUMN t_rco_user_profile_child_path.mode IS '配置方式(同步/排除)';
COMMENT ON COLUMN t_rco_user_profile_child_path.type IS '类型(文件夹/文件/注册表)';
COMMENT ON COLUMN t_rco_user_profile_child_path.user_profile_path_id IS '所属路径配置ID';
COMMENT ON COLUMN t_rco_user_profile_child_path.index  IS '序号';

CREATE INDEX if NOT EXISTS t_rco_user_profile_child_path_id_index ON t_rco_user_profile_child_path USING btree (id);

/**子路径表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_path_detail
(
    id          uuid           NOT NULL,
    path        text           NOT NULL,
    user_profile_child_path_id uuid  NOT NULL,
    user_profile_path_id uuid  NOT NULL,
    index       int4           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_path_detail_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path_detail.id IS '子路径ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.path IS '具体路径';
COMMENT ON COLUMN t_rco_user_profile_path_detail.user_profile_child_path_id IS '所属子路径类型ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.user_profile_path_id IS '所属路径配置ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.index  IS '序号';

CREATE INDEX if NOT EXISTS t_rco_user_profile_path_detail_id_index  ON t_rco_user_profile_path_detail USING btree (id);

-- 桌面池会话连接记录表索引
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_create_time ON t_rco_desktop_session_log USING btree (create_time);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_user_name ON t_rco_desktop_session_log USING btree (user_name);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_user_group_name ON t_rco_desktop_session_log USING btree (user_group_name);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_related_id ON t_rco_desktop_session_log USING btree (related_id);

-- 用户分配桌面池失败表所索引
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_fault_time ON t_rco_user_connect_desktop_fault_log USING btree (fault_time);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_user_name ON t_rco_user_connect_desktop_fault_log USING btree (user_name);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_user_group_name ON t_rco_user_connect_desktop_fault_log USING btree (user_group_name);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_related_id ON t_rco_user_connect_desktop_fault_log USING btree (related_id);
--- 3.1.153 END --
--- 3.1.154 START --
/**用户配置特殊配置表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_special_config (
    id uuid NOT NULL,
    config_version int8 NULL,
    config_content text NOT NULL,
    config_md5 varchar(256) NOT NULL,
    file_name varchar(1024) NULL,
    create_time timestamp(6) NULL,
    version int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_special_config_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_special_config.id IS '用户特殊配置ID';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_version IS '特殊配置版本号)';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_content IS '特殊配置表内容';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_md5 IS '针对content的md5值';
COMMENT ON COLUMN t_rco_user_profile_special_config.file_name IS '配置文件名';
COMMENT ON COLUMN t_rco_user_profile_special_config.create_time IS '创建时间';

CREATE INDEX if NOT EXISTS t_rco_user_profile_special_config_id_index ON t_rco_user_profile_special_config USING btree (id);

/**缓存下发失败的清理路径请求*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_fail_clean_request
(
    id          uuid           NOT NULL,
    desktop_id     uuid           NOT NULL,
    type        varchar(16)    NOT NULL,
    path        text           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_fail_clean_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.id IS '失败请求ID';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.desktop_id IS '云桌面ID';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.path IS '路径';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.type IS '类型(文件夹/文件/注册表)';

CREATE INDEX if NOT EXISTS t_rco_user_profile_fail_clean_request_id_index ON t_rco_user_profile_fail_clean_request USING btree (id);
--- 3.1.154 END --
--- 3.1.155 START --
-- 桌面池会话列表增加会话id字段
ALTER TABLE t_rco_desktop_session_log ADD COLUMN IF NOT EXISTS "connection_id" int8;
COMMENT ON COLUMN t_rco_desktop_session_log.connection_id IS '会话id';
--- 3.1.155 END -

ALTER TABLE t_rco_user_terminal alter COLUMN  bind_desk_id type uuid USING(bind_desk_id::uuid);
COMMENT ON COLUMN t_rco_user_terminal.bind_desk_id IS '终端绑定云桌面id';
