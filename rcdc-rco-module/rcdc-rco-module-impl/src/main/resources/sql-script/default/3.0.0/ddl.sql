CREATE TABLE t_rco_cloud_desk_computer_name (
id uuid NOT NULL,
desk_strategy_id uuid NOT NULL,
computer_name varchar(16),
update_time timestamp(6),
version int4,
CONSTRAINT t_rco_cloud_desk_computer_name_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_cloud_desk_computer_name.id IS '主键id';
COMMENT ON COLUMN t_rco_cloud_desk_computer_name.desk_strategy_id IS '云桌面策略id';
COMMENT ON COLUMN t_rco_cloud_desk_computer_name.computer_name IS '计算机名';
COMMENT ON COLUMN t_rco_cloud_desk_computer_name.update_time IS '更新时间';
COMMENT ON COLUMN t_rco_cloud_desk_computer_name.version IS '版本号';


CREATE TABLE t_rco_wifi_whitelist (
  id uuid NOT NULL,
  ssid varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  terminal_group_id uuid NOT NULL,
  index int2 NOT NULL,
  create_time timestamp(6) NOT NULL,
  version int4 NOT NULL,
  CONSTRAINT "t_rco_wifi_whitelist_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN t_rco_wifi_whitelist.id IS '主键id';

COMMENT ON COLUMN t_rco_wifi_whitelist.ssid IS 'ssid';

COMMENT ON COLUMN t_rco_wifi_whitelist.terminal_group_id IS '管理终端id';

COMMENT ON COLUMN t_rco_wifi_whitelist.index IS 'SSID序号';

COMMENT ON COLUMN t_rco_wifi_whitelist.create_time IS '创建时间';

COMMENT ON COLUMN t_rco_wifi_whitelist.version IS '版本号';


-- 打印机配置功能

-- 打印机配置表
CREATE TABLE "public"."t_rco_printer_manage" (
  "id" uuid NOT NULL,
  "config_serial" int4,
  "config_detail" text COLLATE "pg_catalog"."default",
  "config_md5" varchar(64) COLLATE "pg_catalog"."default",
  "config_enable_covered" bool DEFAULT false,
  "config_name" varchar(259) COLLATE "pg_catalog"."default" NOT NULL,
  "config_support_os" varchar(128) COLLATE "pg_catalog"."default" DEFAULT '["Windows XP","Windows 7","Windows 10"]'::character varying,
  "config_description" varchar(500) COLLATE "pg_catalog"."default",
  "printer_connect_type" varchar(64) COLLATE "pg_catalog"."default" DEFAULT 'L'::character varying,
  "printer_model" varchar(259) COLLATE "pg_catalog"."default" NOT NULL,
  "printer_name" varchar(259) COLLATE "pg_catalog"."default" NOT NULL,
  "printer_port" varchar(64) COLLATE "pg_catalog"."default",
  "printer_ip" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "create_time" timestamp(6),
  "version" int4
)
;
COMMENT ON COLUMN "public"."t_rco_printer_manage"."id" IS '配置id，主键';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_serial" IS '配置流水号';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_detail" IS '打印机配置详细信息';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_md5" IS '打印机配置md5';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_enable_covered" IS '是否覆盖当前数据库记录';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_support_os" IS '打印机配置支持的操作系统';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."config_description" IS '打印机描述';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."printer_connect_type" IS '打印机类型：共享打印机（S）、网络打印机（N）、本地打印机-不共享（L）、本地打印机-共享（M） ';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."printer_model" IS '打印机型号';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."printer_name" IS '打印机名';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."printer_port" IS '打印机端口';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."printer_ip" IS '打印机ip';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."t_rco_printer_manage"."create_time" IS '创建时间';

-- ----------------------------
-- Uniques structure for table t_rco_printer_manage
-- ----------------------------
ALTER TABLE "public"."t_rco_printer_manage" ADD CONSTRAINT "unique_key_config_name" UNIQUE ("config_name");
COMMENT ON CONSTRAINT "unique_key_config_name" ON "public"."t_rco_printer_manage" IS '配置名唯一健约束';

-- ----------------------------
-- Primary Key structure for table t_rco_printer_manage
-- ----------------------------
ALTER TABLE "public"."t_rco_printer_manage" ADD CONSTRAINT "t_rco_printer_manage_pkey" PRIMARY KEY ("id");

--  打印机特殊配置表
CREATE TABLE "public"."t_rco_printer_special_config" (
  "id" uuid NOT NULL,
  "config_version" int8,
  "config_content" text COLLATE "pg_catalog"."default" NOT NULL,
  "config_md5" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "version" int4,
  "file_name" varchar(1024) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  CONSTRAINT "t_rco_printer_special_config_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."t_rco_printer_special_config"."id" IS '配置id，主键';

COMMENT ON COLUMN "public"."t_rco_printer_special_config"."config_version" IS '配置版本号';

COMMENT ON COLUMN "public"."t_rco_printer_special_config"."config_content" IS '特殊配置表内容';

COMMENT ON COLUMN "public"."t_rco_printer_special_config"."config_md5" IS '针对content内容再加盐后的md5值';

COMMENT ON COLUMN "public"."t_rco_printer_special_config"."file_name" IS '特殊配置文件名';


-- 创建终端在线记录小时表
CREATE TABLE t_terminal_online_situation_hour_record (
id uuid NOT NULL,
terminal_id varchar(64) NOT NULL,
create_time timestamp(6) NOT NULL,
platform varchar(64) NOT NULL,
day_key varchar(64) NOT NULL,
hour_key varchar(64) NOT NULL,
version int4,
CONSTRAINT t_terminal_online_situation_hour_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_terminal_online_situation_hour_record.id IS '主键id';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.terminal_id IS '终端id或PC终端mac';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.create_time IS '创建时间';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.platform IS '终端类型';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.day_key IS '按日搜索关键词';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.hour_key IS '按小时搜索关键词';
COMMENT ON COLUMN t_terminal_online_situation_hour_record.version IS '版本号';

-- 创建终端在线记录日表
CREATE TABLE t_terminal_online_situation_day_record (
id uuid NOT NULL,
terminal_id varchar(64) NOT NULL,
create_time timestamp(6) NOT NULL,
platform varchar(64) NOT NULL,
month_key varchar(64) NOT NULL,
day_key varchar(64) NOT NULL,
version int4,
CONSTRAINT t_terminal_online_situation_day_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_terminal_online_situation_day_record.id IS '主键id';
COMMENT ON COLUMN t_terminal_online_situation_day_record.terminal_id IS '终端id或PC终端mac';
COMMENT ON COLUMN t_terminal_online_situation_day_record.create_time IS '创建时间';
COMMENT ON COLUMN t_terminal_online_situation_day_record.platform IS '终端类型';
COMMENT ON COLUMN t_terminal_online_situation_day_record.month_key IS '按月搜索关键词';
COMMENT ON COLUMN t_terminal_online_situation_day_record.day_key IS '按日搜索关键词';
COMMENT ON COLUMN t_terminal_online_situation_day_record.version IS '版本号';

-- 创建镜像应用软件版本关联表
CREATE TABLE t_rco_image_template_app_version (
id uuid NOT NULL,
image_id uuid NOT NULL,
app_version varchar(16),
app_type varchar(64) NOT NULL,
update_time timestamp(6),
version int4,
CONSTRAINT t_rco_image_template_app_version_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_image_template_app_version.id IS '主键id';
COMMENT ON COLUMN t_rco_image_template_app_version.image_id IS '镜像id';
COMMENT ON COLUMN t_rco_image_template_app_version.app_version IS '应用软件版本号';
COMMENT ON COLUMN t_rco_image_template_app_version.app_type IS '应用软件类型';
COMMENT ON COLUMN t_rco_image_template_app_version.update_time IS '更新时间';
COMMENT ON COLUMN t_rco_image_template_app_version.version IS '版本号';

-- 创建云桌面应用ISO挂载信息关联表
CREATE TABLE t_rco_cloud_desk_app_config (
id uuid NOT NULL,
desk_id uuid NOT NULL,
app_iso_id uuid NOT NULL,
app_type varchar(64) NOT NULL,
update_time timestamp(6),
version int4,
CONSTRAINT t_rco_cloud_desk_app_config_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_cloud_desk_app_config.id IS '主键id';
COMMENT ON COLUMN t_rco_cloud_desk_app_config.desk_id IS '云桌面id';
COMMENT ON COLUMN t_rco_cloud_desk_app_config.app_iso_id IS '应用ISO挂载id';
COMMENT ON COLUMN t_rco_cloud_desk_app_config.app_type IS '应用软件类型';
COMMENT ON COLUMN t_rco_cloud_desk_app_config.update_time IS '更新时间';
COMMENT ON COLUMN t_rco_cloud_desk_app_config.version IS '版本号';
