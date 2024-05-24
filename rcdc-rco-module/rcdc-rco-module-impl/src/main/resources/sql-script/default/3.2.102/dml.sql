update
	t_rco_terminal_work_mode_mapping
set
	working_mode = 'IDV,VOI'
where
	id in ('603184f4-9422-46ac-86a3-43dcdde60d8e');

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('8e219bb0-7555-40f8-a1fb-fa594aee25df', 'RG-CT7800-1000', 'IDV,VOI', 'product', true, now(), now(),0, null,null) ON CONFLICT(id) DO NOTHING;

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('4f2ce305-d4ca-4272-b184-2ef38c27fae1', 'RG-CT7800-2000', 'IDV,VOI', 'product', true, now(), now(),0, null,null) ON CONFLICT(id) DO NOTHING;

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('474a6767-54f5-4790-b388-cfc5474650b8', 'RG-CT7900 V3.00', 'IDV,VOI', 'product', true, now(), now(),0, null,null) ON CONFLICT(id) DO NOTHING;