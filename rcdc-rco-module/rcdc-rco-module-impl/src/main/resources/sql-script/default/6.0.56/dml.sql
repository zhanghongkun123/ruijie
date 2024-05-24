-- 本次版本去掉user组件依赖，原来在user组件的相关sql脚本需要迁移到rco
/* license初始化授权 */
 INSERT INTO t_sk_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('205cfdbf-72bf-4e4e-8770-81c2c7456afa', 'auto_trial_license', 'can_license','', now(), now(), 0) ON CONFLICT("id") DO NOTHING;
/* 全局表http端口值 */
INSERT INTO t_sk_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('16f6d947-ff20-4dd3-92b0-b9cc0d1c3b99', 'http_port', '8080', '8080', now(), now(), 0) ON CONFLICT("id") DO NOTHING;
/* 初始化配置向导 */
INSERT INTO t_sk_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES (
   '69e3acab-ef15-4d83-a58f-423dec989424', 'configuration_wizard', '{"isShow": true,"index":0,"isJoinUserExperiencePlan":false}', '{"isShow": true,"index":0,"isJoinUserExperiencePlan":false}', '2019-04-02', '2019-04-02', '0'
) ON CONFLICT("id") DO NOTHING;
/* 回收站定时任务 */
INSERT INTO t_sk_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('2bd19062-796c-4002-b33b-dc47b94414c4', 'recycleBinState', 'OPEN', 'OPEN', now(), now(), 0) ON CONFLICT("id") DO NOTHING;

INSERT INTO t_sk_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('273d3d07-85c6-43fb-b046-07809bf38255', 'recycleBinPeriod', '6', '6', now(), now(), 0) ON CONFLICT("id") DO NOTHING;

/* 初始化云桌面策略推荐表数据 */
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('76fa1d15-71f2-4915-98e3-5a68990087da', now(), 'IDV普通桌面', 'PERSONAL', NULL, NULL, 60, NULL, false, false, true, 0, NULL, NULL, 'IDV', true, true, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', NULL, NULL) ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('a499ef87-2fac-4bd4-bb2f-4804329fa7a8', now(), 'TCI普通桌面', 'PERSONAL', NULL, NULL, 60, NULL, false, false, true, 0, NULL, NULL, 'VOI', true, true, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', NULL, NULL) ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('46475c28-bbda-4ddf-bb2f-20766df57248', now(), 'UOS或Kylin的TCI桌面', 'PERSONAL', NULL, NULL, 40, NULL, false, false, true, 0, NULL, NULL, 'VOI', true, false, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', NULL, NULL) ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('bedb7ed7-7754-4ce4-8baf-585a23dcff79', now(), 'UOS的IDV桌面', 'PERSONAL', NULL, NULL, 40, NULL, false, false, true, 0, NULL, NULL, 'IDV', true, false, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', NULL, NULL) ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('964f119e-eee5-4490-a676-350d72585209'::uuid, now(), 'VDI普通桌面', 'PERSONAL', 2, 4, 60, 80, true, false, true, 8, 'NO_LIMIT', false, 'VDI', NULL, true, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', 'EST', '{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0}}
') ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('ff968ce1-3f2f-4fde-9461-13f4a330d2d6'::uuid, now(), 'VDI高性能桌面', 'PERSONAL', 4, 8, 60, 100, true, false, true, 2, 'NO_LIMIT', false, 'VDI', NULL, true, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', 'EST', '{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0}}
') ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('916a79ae-1936-40e5-9cc8-a1ad674c0368', now(), 'UOS或Kylin的VDI普通桌面', 'PERSONAL', 4, 4, 40, 80, true, false, true, 0, 'NO_LIMIT', false, 'VDI', false, false, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', 'EST', '{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0}}
') ON CONFLICT("id") DO NOTHING;
INSERT INTO t_rco_user_desk_strategy_recommend
(id, create_time, "name", pattern, cpu, memory, system_size, personal_size, is_allow_internet, is_open_usb_read_only, is_show, "version", clip_board_mode, is_open_double_screen, cloud_desk_type, is_allow_local_disk, is_open_desktop_redirect, enable_disk_mapping, enable_disk_mapping_writeable, enable_lan_auto_detection, clip_board_support_type, usb_storage_device_mapping_mode, est_protocol_type, agreement_info)
VALUES('1cef3c08-d4f6-4e61-94d7-4e5bc969b0c3', now(), 'UOS或Kylin的VDI高性能桌面', 'PERSONAL', 4, 8, 70, 100, true, false, true, 0, 'NO_LIMIT', false, 'VDI', false, false, false, false, false, '[{"type": "FILE","mode": "NO_LIMIT"},{"type": "TEXT","mode": "NO_LIMIT","hostToVmCharLimit": 0,"vmToHostCharLimit": 0}]', 'CLOSED', 'EST', '{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":1,"transport":1,"enableSsl":true,"sndQuality":1,"enableWebAdvanceSetting":0}}
') ON CONFLICT("id") DO NOTHING;

-- USB默认设备驱动配置策略（企金2.0开始仅支持自研模式RJUSBDK_USBPCLIENT）
update t_sk_global_parameter set param_key = 'usb_default_device_driver',param_value = 'RJUSBDK_USBPCLIENT',default_value='RJUSBDK_USBPCLIENT'
where id = '52d29697-7696-489a-ab9b-5a9e5b5ed52b';

INSERT INTO t_sk_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('273d3d07-85c6-43fb-b046-078096d31234', 'desk_op_log_retain_day', '180', '180', now(), now(), 0) ON CONFLICT("id") DO NOTHING;


