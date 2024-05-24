-- 定时发送syslog 发送周期（DAY 每天、MINUTE 按分钟间隔）
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
( '13f60a6a-d7aa-40b9-bd6d-d8ec34d36a9c', 'send_syslog_schedule_type', 'DAY', 'DAY', now(), now(), 0 );
-- 定时发送syslog 间隔发送长度（分钟）
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
( '13f60a6a-d7aa-40b9-bd6d-d8ec34d36a9b', 'send_syslog_schedule_interval_minute', '1', '1', now(), now(), 0 );

--已经添加域的云桌面标识
ALTER TABLE t_rco_user_desktop ADD COLUMN IF NOT EXISTS has_auto_join_domain bool DEFAULT FALSE;
COMMENT ON COLUMN t_rco_user_desktop.has_auto_join_domain IS '是否加入域';

-- 定时发送syslog 发送详细配置 {"intervalMinute":1,"scheduleCron":"0 0 3 * * ?", "sendCycleType":"DAY"}
-- 判断key是否存在 存在不插入
INSERT INTO t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time",
                                   "version")
VALUES ('7cb7483f-1052-470c-bfb5-25c993c8d243',
        'send_syslog_schedule_config',
        (SELECT '{"intervalMinute":1,"scheduleCron":"' || (SELECT param_value FROM t_rco_global_parameter WHERE param_key = 'send_syslog_schedule_cron') || (SELECT '","sendCycleType":"DAY"}')),
        (SELECT '{"intervalMinute":1,"scheduleCron":"' || (SELECT default_value FROM t_rco_global_parameter WHERE param_key = 'send_syslog_schedule_cron') || (SELECT '","sendCycleType":"DAY"}')),
        now(),
        now(),
        0)
ON CONFLICT(param_key) DO NOTHING;

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