drop rule if exists r_insert_ignore on t_rco_terminal_work_mode_mapping;
create rule r_insert_ignore as on insert to t_rco_terminal_work_mode_mapping where exists (select 1 from t_rco_terminal_work_mode_mapping where id = new.id) do instead nothing;
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('9123c51b-e925-4a0a-a4a6-6a4fe762446e', 'RG-CT3100L-G2', 'VOI,VDI', 'product', true, now(), now(),0, 'RG-CT3',null);
update t_rco_terminal_work_mode_mapping set working_mode = 'VOI,VDI' where match_rule = 'RG-CT3';
update t_rco_terminal_work_mode_mapping set working_mode = 'IDV,VOI' where support_mode = 'RG-CT5702C-G3';
