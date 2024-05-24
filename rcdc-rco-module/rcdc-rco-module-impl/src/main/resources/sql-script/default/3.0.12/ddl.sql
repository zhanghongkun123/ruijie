-- 特种终端驱动安装适配
CREATE TABLE t_rco_terminal_driver_config (
id uuid NOT NULL,
product_model varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
product_id varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
driver_type varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
create_time timestamp(6),
update_time timestamp(6),
version int4,
CONSTRAINT t_rco_terminal_driver_config_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."id" IS '配置id，主键';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."product_model" IS '终端模型';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."product_id" IS '产品ID';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."driver_type" IS '驱动类型';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."t_rco_terminal_driver_config"."version" IS '版本号';


-- 镜像应用软件版本关联表修订，增加字段
ALTER TABLE t_rco_image_template_app_version ADD last_app_version VARCHAR(16);

