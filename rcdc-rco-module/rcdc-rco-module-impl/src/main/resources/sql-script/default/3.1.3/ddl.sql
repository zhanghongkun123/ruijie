-- 增加磁盘映射字段
ALTER TABLE "public"."t_rco_user_desk_strategy_recommend" ADD COLUMN "enable_disk_mapping" bool DEFAULT FALSE,
ADD COLUMN "enable_disk_mapping_writeable" bool DEFAULT FALSE;
COMMENT ON COLUMN "public"."t_rco_user_desk_strategy_recommend"."enable_disk_mapping" IS '开启磁盘映射';
COMMENT ON COLUMN "public"."t_rco_user_desk_strategy_recommend"."enable_disk_mapping_writeable" IS '开启磁盘映射允许写入';


-- 增加局域网自动检测字段
ALTER TABLE t_rco_user_desk_strategy_recommend ADD COLUMN IF NOT EXISTS enable_lan_auto_detection bool DEFAULT 'f';
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.enable_lan_auto_detection IS '开启局域网自动检测';

-- 身份验证配置表（t_rco_user_identity_config）修改
ALTER TABLE "public"."t_rco_user_identity_config"
    ADD COLUMN "open_cas_certification" bool default false,
    ADD COLUMN "open_account_password_certification" bool default true;
COMMENT ON COLUMN "public"."t_rco_user_identity_config"."open_cas_certification" IS '开启CAS认证：默认关闭';
COMMENT ON COLUMN "public"."t_rco_user_identity_config"."open_account_password_certification" IS '开启账号密码认证：默认开启';

-- open api增加任务异常信息保存表
CREATE TABLE "public"."t_rco_open_api_task_info" (
  "id" uuid NOT NULL,
  "task_id" uuid NOT NULL,
  "action" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "exception_name" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "exception_message" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "version" int4 NOT NULL,
  CONSTRAINT "t_rco_open_api_task_info_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."t_rco_open_api_task_info"
  OWNER TO "rcdcuser";

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."id" IS '主键id';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."task_id" IS '任务id';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."action" IS '任务类型';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."exception_name" IS '异常信息名称';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."exception_message" IS '异常信息';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."version" IS '版本号';