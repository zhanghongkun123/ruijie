
--修改终端型号映射规则--
ALTER TABLE t_rco_terminal_work_mode_mapping ADD COLUMN match_rule varchar(32);
comment ON COLUMN t_rco_terminal_work_mode_mapping.match_rule IS '匹配规则';

ALTER TABLE t_rco_terminal_work_mode_mapping ADD COLUMN sub_match_rule varchar(32);
comment ON COLUMN t_rco_terminal_work_mode_mapping.sub_match_rule IS '二级匹配规则';

-- 修改终端组配置表  新增终端组类型(desk_type) :IDV|VDI|VOI
ALTER TABLE t_rco_user_terminal_group_desk_config
ADD desk_type varchar(32) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN t_rco_user_terminal_group_desk_config.desk_type IS '终端组类型:IDV|VDI|VOI';

-- 添加ID字段
ALTER TABLE t_rco_user_terminal_group_desk_config ADD  id uuid  ;

-- 将原有cbb_terminal_group_id主键赋值给新的id
UPDATE t_rco_user_terminal_group_desk_config SET id=cbb_terminal_group_id;

--  删除原有主键
ALTER TABLE t_rco_user_terminal_group_desk_config DROP CONSTRAINT t_rco_user_terminal_group_desk_config_pkey;

-- 新增主键为ID
ALTER TABLE t_rco_user_terminal_group_desk_config  ADD CONSTRAINT "t_rco_user_terminal_group_desk_config_pkey" PRIMARY KEY ("id");
