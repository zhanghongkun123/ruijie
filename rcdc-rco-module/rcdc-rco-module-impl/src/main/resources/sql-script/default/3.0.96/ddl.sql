-- 记录用户登录登出云桌面虚机日志
CREATE TABLE "t_rco_desktop_online_log" (
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

