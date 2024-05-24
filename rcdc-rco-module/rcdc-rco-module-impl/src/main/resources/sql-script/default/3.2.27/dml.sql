-- 是否需要处理sysadmin权限（修订bug: 659024）
INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, "version")
VALUES('9df121b5-6833-499c-87e4-b2f580a7007d', 'need_attach_sysadmin_permission', 'true', 'true', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

-- 智慧教室存量终端类型支持
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('b6166a4d-d441-4b9c-8ac2-8466450c0f25', 'RG-OPS-X501', 'IDV,VOI', 'product', true, now(), now(), 0, null, null) ON CONFLICT(id) DO NOTHING;

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('e2612a52-e431-46f4-b87d-da6040be0898', 'RG-OPS-X502', 'IDV,VOI', 'product', true, now(), now(), 0, null, null) ON CONFLICT(id) DO NOTHING;

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('89cd74fa-960b-4108-b241-1729aead9b16', 'RG-OPS-X512', 'IDV,VOI', 'product', true, now(), now(), 0, null, null) ON CONFLICT(id) DO NOTHING;

-- 智慧教室 RG-OPS 终端模糊匹配规则修改为仅支持TCI
update t_rco_terminal_work_mode_mapping set working_mode = 'VOI' where id = 'fdb1b781-30eb-4ae2-b006-e408b5f2e051';