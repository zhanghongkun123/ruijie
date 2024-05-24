-- 记录用户登录登出云桌面虚机日志，如果已经存在，则不再创建
CREATE TABLE IF NOT EXISTS "t_rco_desktop_online_log" (
  "id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "user_name" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "desktop_id" uuid NOT NULL,
  "desktop_name" varchar(255),
  "desktop_ip" varchar(64),
  "operation_type" varchar(32) NOT NULL,
  "operation_time" timestamp(6) NOT NULL,
  "version" int4 NOT NULL,
  CONSTRAINT "t_rco_desktop_online_log_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "t_rco_desktop_online_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "t_rco_desktop_online_log"."user_name" IS '用户名称';
COMMENT ON COLUMN "t_rco_desktop_online_log"."desktop_id" IS '云桌面ID';
COMMENT ON COLUMN "t_rco_desktop_online_log"."desktop_name" IS '云桌面名称';
COMMENT ON COLUMN "t_rco_desktop_online_log"."desktop_ip" IS '云桌面IP';
COMMENT ON COLUMN "t_rco_desktop_online_log"."operation_type" IS '动作类型:SHUTDOWN     - 虚拟机系统正常关机后抛出此事件
DESTROY      - 虚拟机被强制关机后抛出此事件
CRASH        - QEMU执行异常后抛出此事件
SUSPEND_DISK - 虚拟机系统休眠后抛出此事件
EST_LOGIN    - EST客户端断开后抛出此事件
EST_LOGOUT   - EST客户端连接后抛出此事件';
COMMENT ON COLUMN "t_rco_desktop_online_log"."operation_time" IS '动作执行时间';


-- 用户锁定信息表，如果已经存在，则不再创建
CREATE TABLE IF NOT EXISTS "t_rco_user_authentication" (
  "id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "is_lock" bool NOT NULL,
  "lock_time" timestamp(6),
  "unlock_time" timestamp(6),
  "pwd_error_times" int4 DEFAULT 0,
  "last_login_time" timestamp(6),
  "update_password_time" timestamp(6),
  "version" int4,
  CONSTRAINT "t_rco_user_authentication_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "t_rco_user_authentication_user_id_key" UNIQUE ("user_id")
);

COMMENT ON COLUMN "t_rco_user_authentication"."is_lock" IS '是否锁定，默认false';
COMMENT ON COLUMN "t_rco_user_authentication"."lock_time" IS '锁定时间';
COMMENT ON COLUMN "t_rco_user_authentication"."unlock_time" IS '解锁时间';
COMMENT ON COLUMN "t_rco_user_authentication"."pwd_error_times" IS '密码输错次数';
COMMENT ON COLUMN "t_rco_user_authentication"."last_login_time" IS '上一次登录时间';
COMMENT ON COLUMN "t_rco_user_authentication"."update_password_time" IS '密码修改时间';
