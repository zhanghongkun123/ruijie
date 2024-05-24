
delete from t_rco_terminal_work_mode_mapping where id in ('1ad95795-27ff-4c5c-a259-b8b61cec3f98','3ad95795-27ff-4c5c-a259-b8b61cec3f98',
'5ad95795-27ff-4c5c-a259-b8b61cec3f98','8ad95795-27ff-4c5c-a259-b8b61cec3f98','9ad95795-27ff-4c5c-a259-b8b61cec3f98',
'12d95795-27ff-4c5c-a259-b8b61cec3f98','14d95795-27ff-4c5c-a259-b8b61cec3f98');
INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('bf964032-4937-4a23-a853-76dfb7c706cf', 'config_one_click_after_vip_changed', '{}', '{}', now(), now(), 0) ON CONFLICT(param_key) DO NOTHING;