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

COMMENT ON TABLE t_rco_disk_pool_user IS '磁盘池与用户关系表';
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
CREATE TABLE "public"."t_rco_desktop_session_log" (
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
CREATE TABLE "public"."t_desktop_session_log_hour_record" (
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
CREATE TABLE "public"."t_desktop_session_log_day_record" (
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
CREATE TABLE "public"."t_rco_user_connect_desktop_fault_log" (
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


----------------------------development-ningbo-first-hospital版本3.1.150文件冲突，升级不上来，迁移脚本
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