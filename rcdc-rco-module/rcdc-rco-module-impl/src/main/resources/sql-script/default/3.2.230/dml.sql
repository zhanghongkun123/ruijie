--- 3.2.5 START --
-- S4版本中存在assign_storage_pool_id的字段，而企金1.0存在storage
do $$
begin
    if exists (select * from information_schema.columns where column_name='assign_storage_pool_id' and table_name = 't_rco_user_group_desktop_config')
    then
            update t_rco_user_group_desktop_config set storage_pool_id = (assign_storage_pool_id::uuid) where storage_pool_id is null;
    end if;
end $$
;
--- 3.2.5 END --



INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('bf964032-4937-4a23-a853-76dfb7c706cf', 'config_one_click_after_vip_changed', '{}', '{}', now(), now(), 0) ON CONFLICT(param_key) DO NOTHING;

INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('2ad95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT5200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('4ad95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT5300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('6ad95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT5500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('11d95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT6200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('13d95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT6300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('15d95795-27ff-4c5c-a259-b8b61cec3f98', 'RG-CT6500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;


update t_rco_terminal_work_mode_mapping set platform = 'IDV' where id in ('2ad95795-27ff-4c5c-a259-b8b61cec3f98','4ad95795-27ff-4c5c-a259-b8b61cec3f98','6ad95795-27ff-4c5c-a259-b8b61cec3f98',
'11d95795-27ff-4c5c-a259-b8b61cec3f98','13d95795-27ff-4c5c-a259-b8b61cec3f98','15d95795-27ff-4c5c-a259-b8b61cec3f98');