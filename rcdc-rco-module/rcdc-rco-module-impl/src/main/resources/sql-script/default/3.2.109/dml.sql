
--TODO:转测后删除delete语句
delete from t_rco_terminal_work_mode_mapping where id in ('3bb86f86-a16d-4e4c-84c4-1f14d46f72e9','f3df5227-4d9f-4aa9-a7c8-c89bb9ca4c5b','a98ee913-69de-4e61-84dc-14508d9dc79d',
'897e52c2-2415-47a8-86b8-2cfe870935c9','0a065eca-6158-4347-9378-6c7e4d31fb57','3d35510b-229e-4e53-871e-fa33c914e1fc'
);

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


delete from public.t_rco_image_type_support_osversion where id = 'd4c2606b-6d5c-4324-8f68-23f2db93868f';
INSERT INTO public.t_rco_image_type_support_osversion (id,cbb_image_type,os_type,os_version,"version") VALUES
('47fe19e4-8384-4df9-a77e-03b8b8660319','VDI','KYLIN_64','kylin_2107',0)  ON CONFLICT(cbb_image_type,os_type,os_version) DO NOTHING;

update t_rco_terminal_work_mode_mapping set platform = 'IDV' where id in ('2ad95795-27ff-4c5c-a259-b8b61cec3f98','4ad95795-27ff-4c5c-a259-b8b61cec3f98','6ad95795-27ff-4c5c-a259-b8b61cec3f98',
'11d95795-27ff-4c5c-a259-b8b61cec3f98','13d95795-27ff-4c5c-a259-b8b61cec3f98','15d95795-27ff-4c5c-a259-b8b61cec3f98','8e219bb0-7555-40f8-a1fb-fa594aee25df',
'4f2ce305-d4ca-4272-b184-2ef38c27fae1','474a6767-54f5-4790-b388-cfc5474650b8');

-- 远程唤醒超时配置
INSERT INTO t_rco_global_parameter (id,param_key,param_value,default_value,create_time,update_time,"version")
VALUES ('2fc8aceb-fbdd-e482-0775-fabc336e1635'::uuid,'terminal_wake_up_config','{"wakeCheckInterval":5000,"wakeupCheckTimeout":240000,"wakeupTimeout":60000}','{"wakeCheckInterval":5000,"wakeupCheckTimeout":240000,"wakeupTimeout":60000}',now(),now(),0)
ON CONFLICT(param_key) DO NOTHING;