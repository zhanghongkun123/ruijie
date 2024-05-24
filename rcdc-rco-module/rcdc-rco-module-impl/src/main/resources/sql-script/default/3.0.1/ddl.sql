-- 3V合一
CREATE TABLE t_rco_terminal_work_mode_mapping (
id uuid NOT NULL,
support_mode varchar(32),
working_mode varchar(32),
judge_basis varchar(16),
enable_state bool DEFAULT true NOT NULL,
create_time timestamp(6),
update_time timestamp(6),
version int4,
CONSTRAINT t_rco_terminal_work_mode_mapping_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."id" IS '配置id，主键';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."support_mode" IS '终端支持的工作模式';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."working_mode" IS '使用工作模式';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."judge_basis" IS '判断维度（mode-终端模式/product-终端型号）';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."enable_state" IS '是否启用';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."t_rco_terminal_work_mode_mapping"."version" IS '版本号';

--  PC小助手远程协助修订
ALTER TABLE t_rco_computer ALTER COLUMN assist_pwd TYPE VARCHAR(64);
