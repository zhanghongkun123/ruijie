-- 云桌面策略推荐表 新增支持USB存储设备映射模式字段
ALTER TABLE t_rco_user_desk_strategy_recommend ADD COLUMN IF NOT EXISTS usb_storage_device_mapping_mode varchar(64) DEFAULT 'CLOSED';
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.usb_storage_device_mapping_mode IS 'USB存储设备映射：CLOSED-关闭，READ_ONLY-只读，READ_WRITE-读写';
alter table t_rco_user_identity_config add column if not exists "open_radius_certification" bool;

-- 分配云桌面失败记录表，因为之前版本只统计了动态池，所以默认指为DYNAMIC
ALTER TABLE t_rco_user_connect_desktop_fault_log ADD COLUMN IF NOT EXISTS desktop_pool_type varchar(64) DEFAULT 'DYNAMIC' NOT NULL;
COMMENT ON COLUMN t_rco_user_connect_desktop_fault_log.desktop_pool_type IS '桌面池类型';

-- 删除S金融版本中桌面分组表，使用静态桌面池替代
DROP TABLE IF EXISTS t_rco_user_desktop_group;
-- 删除S金融版本中桌面分组桌面策略表，使用静态桌面池替代
DROP TABLE IF EXISTS t_rco_user_desktop_group_strategy;
-- 新增 用户和用户组同步日志
CREATE TABLE "t_rco_data_sync_log" (
  "id" uuid NOT NULL,
  "content" text,
  "version" int4 NOT NULL,
  "create_time" timestamp(6) NOT NULL
);
COMMENT ON COLUMN "t_rco_data_sync_log"."id" IS '同步日志id';
COMMENT ON COLUMN "t_rco_data_sync_log"."content" IS '操作内容';
COMMENT ON COLUMN "t_rco_data_sync_log"."create_time" IS '操作时间';
COMMENT ON COLUMN "t_rco_data_sync_log"."version" IS '描述';

ALTER TABLE "t_rco_data_sync_log" ADD CONSTRAINT "t_rco_data_sync_log_pkey" PRIMARY KEY ("id");

-- 用户使用记录表
CREATE TABLE IF NOT EXISTS "public"."t_cbb_user_login_record" (
	"id" UUID NOT NULL PRIMARY KEY,
	"version" int4 NOT NULL,
	"user_id" VARCHAR(256) NOT NULL,
	"user_name" VARCHAR(256) NOT NULL,
	"user_group_id" VARCHAR(64) NOT NULL,
	"user_group_name" VARCHAR(64) NOT NULL,
	"terminal_id" VARCHAR(64) NOT NULL,
	"terminal_ip" VARCHAR(64),
	"terminal_mac" VARCHAR(64) NOT NULL,
	"terminal_name" VARCHAR(64) NOT NULL,
	"desk_type" VARCHAR(64) NOT NULL,
	"desk_strategy_pattern" VARCHAR(64) NOT NULL,
	"desk_id" VARCHAR(64) NOT NULL,
	"desk_name" VARCHAR(64) NOT NULL,
	"computer_name" VARCHAR(64) NOT NULL,
	"desk_ip" VARCHAR(64) NOT NULL,
	"desk_mac" VARCHAR(64) NOT NULL,
	"desk_system" VARCHAR(64) NOT NULL,
	"desk_image" VARCHAR(64) NOT NULL,
	"desk_strategy" VARCHAR(64) NOT NULL,
	"session_state" VARCHAR(64) NOT NULL,
	"create_time" TIMESTAMP(6),
	"login_time" TIMESTAMP(6),
	"auth_duration" int8 NOT NULL,
	"user_type" VARCHAR(64) NOT NULL,
	"connect_time" TIMESTAMP(6),
	"connect_duration" int8 NOT NULL,
	"logout_time" TIMESTAMP(6),
	"use_duration" int8 NOT NULL
);

ALTER TABLE "public"."t_cbb_user_login_record"
  OWNER TO "rcdcuser";

COMMENT ON COLUMN "public"."t_cbb_user_login_record"."id" IS '主键ID';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."version" IS '版本号';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."user_name" IS '用户名';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."user_group_id" IS '用户分组id';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."user_group_name" IS '用户分组名';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_id" IS '云桌面id';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_name" IS '云桌面名称';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."terminal_id" IS '终端id';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."terminal_ip" IS '终端ip';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."terminal_mac" IS '终端mac';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."terminal_name" IS '终端名称';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_type" IS '云桌面类型';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_strategy_pattern" IS '云桌面策略类型';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."computer_name" IS '计算机名称';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_ip" IS '云桌面ip';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_mac" IS '云桌面mac';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_system" IS '操作系统';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_image" IS '镜像名称';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."desk_strategy" IS '云桌面策略';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."session_state" IS '会话状态';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."login_time" IS '登录时间';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."auth_duration" IS '认证耗时';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."user_type" IS '用户类型';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."connect_time" IS '连接时间';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."connect_duration" IS '连接耗时';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."logout_time" IS '注销时间';
COMMENT ON COLUMN "public"."t_cbb_user_login_record"."use_duration" IS '使用时长';

CREATE INDEX IF NOT EXISTS "user_name" ON "t_cbb_user_login_record" ("user_name");
CREATE INDEX IF NOT EXISTS "user_group_name" ON "t_cbb_user_login_record" ("user_group_name");

-- 桌面池会话列表增加会话id字段
ALTER TABLE t_cbb_user_login_record ADD COLUMN IF NOT EXISTS "connection_id" int8;
COMMENT ON COLUMN t_cbb_user_login_record.connection_id IS '会话id';

--ddl 桌面策略新增用户自助快照字段
alter table t_cbb_desk_strategy add column if not exists enable_user_snapshot bool default false;
comment on column t_cbb_desk_strategy.enable_user_snapshot is '是否开启用户自助快照';

-- 第三方用户角色
ALTER TABLE t_rco_user_identity_config add COLUMN IF NOT EXISTS  "open_third_party_certification" BOOL DEFAULT false;
COMMENT ON COLUMN "t_rco_user_identity_config"."open_third_party_certification" IS '是否失效';

-- 解决兼容企金2.0刷机场景，用户相关表迁移到身份中心
do $$
begin
    if exists (SELECT 1 FROM information_schema.tables WHERE table_name = 't_cbb_user_group')
    then
        -- 添加是否为第三方用户组
        ALTER TABLE t_cbb_user_group ADD COLUMN IF NOT EXISTS is_third_party_group bool default false;
        COMMENT ON COLUMN "t_cbb_user_group"."is_third_party_group" IS '是否为第三方用户组';

        -- 添加用户组 更新时间
        ALTER TABLE t_cbb_user_group ADD IF NOT EXISTS  "update_time" TIMESTAMP;
        COMMENT ON COLUMN "t_cbb_user_group"."update_time" IS '更新时间';
    else
        RAISE NOTICE 't_cbb_user_group does not exist,skip';
end if;
end $$
;
