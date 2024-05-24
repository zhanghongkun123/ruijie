-- 云桌面策略推荐表 给VDI设置默认值
update t_rco_user_desk_strategy_recommend set est_protocol_type='EST' where cloud_desk_type ='VDI' and est_protocol_type is null;

update t_rco_user_desk_strategy_recommend set agreement_info='{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0}}
' where est_protocol_type ='EST' and agreement_info is null;
-- 桌面规格
INSERT INTO  "t_sk_global_parameter" ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('92fba57d-c764-4bd5-bd56-ed2222222222', 'need_upgrade_desk_spec_info', 'true', 'true', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

--支持操作系统表新增uos1060以及kos2303
INSERT INTO t_rco_image_type_support_osversion (id, cbb_image_type, os_type, os_version,version)
VALUES  ('de618a96-71a3-5cb8-9a68-77b6cb0fc97d','VDI','UOS_64','uos_1060',0), ('ad427b86-54a3-2bc6-8e68-71a5bb1fd87f','VDI','KYLIN_64','kylin_2303',0) ON CONFLICT(cbb_image_type, os_type, os_version) DO NOTHING;

-- 初始化的推荐策略剪切板字段默认值格式修订
UPDATE t_rco_user_desk_strategy_recommend SET clip_board_support_type='[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]';

-- 用户并发授权模式开关
INSERT INTO t_sk_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
 VALUES('20ef1217-f0b2-4e57-9509-5909b768d2e2', 'active_user_license_mode', true, true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
