-- 支持RG-CT5300C作为IDV/TCI使用
insert into t_rco_terminal_work_mode_mapping
    (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version, match_rule, sub_match_rule)
values
    ('93ab8ff8-1f6e-62f1-6e54-b153dcbaf798', 'RG-CT5300C', 'IDV,VOI', 'product', true, now(), now(), 0, 'RG-CT5', null);
-- 支持RG-CT5502C-G3作为IDV/TCI使用
insert into t_rco_terminal_work_mode_mapping
    (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version, match_rule, sub_match_rule)
values
    ('566b9d3f-3251-7c60-ab0d-fd4d6d545d04', 'RG-CT5502C-G3', 'IDV,VOI', 'product', true, now(), now(), 0, 'RG-CT5', null);