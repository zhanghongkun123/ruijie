-- window 桌面背景添加新配置，bug: 670914
INSERT INTO t_rco_user_profile_child_path VALUES('c6eebe1f-14ae-491f-854a-46ca3ddf08e1',  'SYNCHRO', 'REGISTRY_KEY', '3f830bb2-7829-77ae-f991-6d09c7ee4b80',
0, 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_user_profile_path_detail VALUES ('f3796fd8-0fd5-46d5-987f-49566b6c46ae', 'HKEY_CURRENT_USER\Control Panel\Desktop',
'c6eebe1f-14ae-491f-854a-46ca3ddf08e1', '3f830bb2-7829-77ae-f991-6d09c7ee4b80', 0, 0) ON CONFLICT(id) DO NOTHING;