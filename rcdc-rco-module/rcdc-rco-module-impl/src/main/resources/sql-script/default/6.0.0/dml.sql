--3.2.102
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

--3.2.106
INSERT INTO public.t_rco_image_type_support_osversion (id,cbb_image_type,os_type,os_version,"version") VALUES
	 ('359aa6b4-3d60-4adc-a639-c9f0da7e611f','VOI','UOS_64','uos_1060',0),
	 ('2621f2c4-7521-4076-be28-dde7a0e018e3','VDI','UOS_64','uos_1042',0),
	 ('14b0677c-a49b-494a-b5e1-25084cd7fad5','VDI','UOS_64','uos_1032',0),
	 ('ce518a97-67b4-4cb7-9a68-87b6cb0fc97f','VDI','UOS_64','uos_1022',0),
	 ('c0893cfd-aabc-406f-9347-fe56c69ac9da','VDI','KYLIN_64','kylin_2101',0),
	 ('981c47e8-675d-4212-b9fa-c8305adcaa8a','VOI','KYLIN_64','kylin_GFB',0),
	 ('6901fc8f-ded4-4e33-b232-f16a432e64f2','VOI','KYLIN_64','kylin_2303',0),
	 ('d4c2606b-6d5c-4324-8f68-23f2db93868f','IDV','UOS_64','uos_1042',0),
	 ('ab972d7d-0b5b-434d-bc26-f53796937904','IDV','UOS_64','uos_1032',0),
	 ('887edcda-b1a0-4098-9a62-3487e11f6e39','IDV','UOS_64','uos_1022',0) ON CONFLICT(cbb_image_type,os_type,os_version) DO NOTHING;

--3.2.107
delete from t_rco_terminal_work_mode_mapping where id in ('1ad95795-27ff-4c5c-a259-b8b61cec3f98','3ad95795-27ff-4c5c-a259-b8b61cec3f98',
'5ad95795-27ff-4c5c-a259-b8b61cec3f98','8ad95795-27ff-4c5c-a259-b8b61cec3f98','9ad95795-27ff-4c5c-a259-b8b61cec3f98',
'12d95795-27ff-4c5c-a259-b8b61cec3f98','14d95795-27ff-4c5c-a259-b8b61cec3f98');
INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('bf964032-4937-4a23-a853-76dfb7c706cf', 'config_one_click_after_vip_changed', '{}', '{}', now(), now(), 0) ON CONFLICT(param_key) DO NOTHING;

--3.2.108
INSERT INTO public.t_rco_image_type_support_terminal (id,cbb_image_type,os_type,product_type,"version") VALUES
('7948c8b4-f80a-4d59-976d-907318e3fcc3','VOI','UOS_64','RG-CT7800-1000',0),
('16e46a16-6e2a-4b1e-8438-948e055aa64c','VOI','UOS_64','RG-CT7800-2000',0),
('ded815d1-df36-475c-a671-fa5666a936ed','VOI','UOS_64','RG-CT7900',0),
('a28ed199-7274-41f1-8226-590a20400657','VOI','UOS_64','RG-CT7900 V3.00',0),
('14af42b5-5714-4b02-91cb-185b012d06af','VOI','KYLIN_64','RG-CT7800-1000',0),
('022ccbf3-ec7a-45aa-9339-b2c67d61dd52','VOI','KYLIN_64','RG-CT7800-2000',0),
('5c2fad95-805e-4265-abec-06fae62419ca','VOI','KYLIN_64','RG-CT7900',0),
('7f4e8796-9957-443d-8306-3011989796bb','VOI','KYLIN_64','RG-CT7900 V3.00',0)  ON CONFLICT(cbb_image_type,os_type,product_type) DO NOTHING;

--TODO:转测后删除
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('3bb86f86-a16d-4e4c-84c4-1f14d46f72e9', 'RG-CT5200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('f3df5227-4d9f-4aa9-a7c8-c89bb9ca4c5b', 'RG-CT5300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('a98ee913-69de-4e61-84dc-14508d9dc79d', 'RG-CT5500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('897e52c2-2415-47a8-86b8-2cfe870935c9', 'RG-CT6200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('0a065eca-6158-4347-9378-6c7e4d31fb57', 'RG-CT6300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('3d35510b-229e-4e53-871e-fa33c914e1fc', 'RG-CT6500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;

--3.2.109
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

--3.2.111
update t_rco_image_download_state set terminal_download_finish_time = download_finish_time where download_state = 'SUCCESS';

--3.2.113
update t_rco_user_desk_strategy_recommend set name = 'UOS或Kylin的VDI普通桌面' where id = '916a79ae-1936-40e5-9cc8-a1ad674c0368';
update t_rco_user_desk_strategy_recommend set name = 'UOS或Kylin的VDI高性能桌面' where id = '1cef3c08-d4f6-4e61-94d7-4e5bc969b0c3';
update t_rco_user_desk_strategy_recommend set name = 'UOS的IDV桌面' where id = 'bedb7ed7-7754-4ce4-8baf-585a23dcff79';
INSERT INTO t_rco_user_desk_strategy_recommend ("id", "create_time", "name", "pattern", "cpu", "memory", "system_size", "personal_size", "is_allow_internet", "is_open_usb_read_only", "is_show", "version", "clip_board_mode", "is_open_double_screen", "cloud_desk_type", "is_allow_local_disk", "is_open_desktop_redirect","enable_disk_mapping","enable_disk_mapping_writeable","enable_lan_auto_detection")
VALUES ('46475c28-bbda-4ddf-bb2f-20766df57248', now(), 'UOS或Kylin的TCI桌面', 'PERSONAL', NULL, NULL, 40, NULL, 'f', 'f', 't', 0, NULL, NULL, 'VOI', 't', 'f', 'f', 'f', 'f') ON CONFLICT(id) DO NOTHING;

-- 桌面密码显示配置标识位
INSERT INTO t_rco_global_parameter (id, param_key, param_value, default_value, create_time, update_time,version)
VALUES ('f0dedf46-5a50-02cc-962f-7c91fe426594','has_init_desk_root_pwd_config', 'false', 'false', now(), now(), 0) ON CONFLICT (param_key) DO NOTHING;

--3.2.131
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
VALUES
    ('f52e0794-8f19-4e2d-b6fb-c92ed8ec63f8', 'RG-CT5302C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('d480a6c3-ef09-4704-884c-1240347eec53', 'RG-CT5502C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('a3cbb789-32aa-430c-b69a-11c4c6a9a938', 'RG-CT5702C-G4', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null),
    ('3619771c-e078-207c-fd5d-6310423f64de', 'RG-CT5500C-CS', 'VOI', 'product', true, now(), now(),0, 'RG-CT5',null)
    ON CONFLICT(id) DO NOTHING;

  --3.1.133
  INSERT INTO public.t_rco_image_type_support_terminal (id,cbb_image_type,os_type,product_type,"version") VALUES
  ('16dc9a26-4c20-1710-714e-8b98ae636729','VOI','UOS_64','RG-CT7900 V5.00',0),
  ('a6ba6ac4-ebde-1f45-142c-04b34fcaf475','VOI','KYLIN_64','RG-CT7900 V5.00',0)
  ON CONFLICT(cbb_image_type,os_type,product_type) DO NOTHING;
  insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
  values ('b035b3ea-fc06-50e6-aa8a-631ea3b12b1c', 'RG-CT7900 V5.00', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT7',null)
  ON CONFLICT(id) DO NOTHING;

--3.2.134
update t_rco_terminal_work_mode_mapping set platform = 'VOI' where support_mode in ('RG-CT5302C-G4','RG-CT5502C-G4','RG-CT5702C-G4','RG-CT5500C-CS');
update t_rco_terminal_work_mode_mapping set platform = 'IDV' where support_mode = 'RG-CT7900 V5.00';