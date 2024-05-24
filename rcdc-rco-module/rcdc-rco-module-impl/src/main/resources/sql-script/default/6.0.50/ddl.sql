
-- 软件安装信息表
drop table IF EXISTS t_rco_software_reg;
CREATE TABLE IF NOT EXISTS t_rco_software_reg
(
    "id"                      uuid PRIMARY KEY,
    "file_custom_md5"         text NOT NULL,
    "reg_path"                text NOT NULL,
    "create_time"             TIMESTAMP(6),
    "update_time"             TIMESTAMP(6),
    "version"                 int8 NOT NULL DEFAULT 0
);

COMMENT ON COLUMN "public"."t_rco_software_reg"."id" IS 'id';
COMMENT ON COLUMN "public"."t_rco_software_reg"."file_custom_md5" IS '文件特征码';
COMMENT ON COLUMN "public"."t_rco_software_reg"."reg_path" IS '注册表路径';
COMMENT ON COLUMN "public"."t_rco_software_reg"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_rco_software_reg"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."t_rco_software_reg"."version" IS '版本号';

-- 用户组桌面信息配置表增加云平台标识
ALTER TABLE t_rco_user_group_desktop_config ADD COLUMN IF NOT EXISTS platform_id uuid DEFAULT '00000000-0000-0000-0000-000000000000';
COMMENT ON COLUMN "public"."t_rco_user_group_desktop_config"."platform_id" IS '云平台标识';