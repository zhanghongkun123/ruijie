-- 智慧教室 RG-OPS 终端模糊接入
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('fdb1b781-30eb-4ae2-b006-e408b5f2e051', 'RG-OPS', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-OPS',null);

