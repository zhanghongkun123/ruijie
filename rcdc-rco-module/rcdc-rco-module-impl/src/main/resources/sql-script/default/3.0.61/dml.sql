
-- VOI终端临时授权标记 --
INSERT INTO t_rco_global_parameter (id,param_key, param_value, default_value, create_time, update_time,version)
VALUES ('e072f519-c4a3-4c76-8e61-5ed43ceb38a1','voi_auto_trial_license', 'can_license', '', now(), now(), 0);


-- 智慧教室终端类型支持 --
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('c37e64fc-d7fc-4a6c-ab86-2b0ba2c8606f', 'RG-OPS-V-i5V2', 'IDV,VOI', 'product', true, now(), now(), 0, null, null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('fcb35239-f5d2-4d8a-a370-5dbd9244911f', 'RG-OPS-V-i5V5', 'IDV,VOI', 'product', true, now(), now(), 0, null, null);