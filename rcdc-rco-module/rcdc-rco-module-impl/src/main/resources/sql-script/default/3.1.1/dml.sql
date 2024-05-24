-- 轻云终端增加VOI工作模式
UPDATE t_rco_terminal_work_mode_mapping
SET working_mode = 'IDV,VOI'
WHERE
	ID IN ( '74bedf46-6c87-4a16-8ac3-71319ea8ab40', '041d6c00-88e2-457b-ac19-5c3b5cca891e', '9b5f09f6-b675-4f81-8785-74fe25817307', '1c91ff78-7837-4ba0-aa53-d1a55957ff22' );

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('9123c51b-e925-4a0a-a4a6-6a4fe762436e', 'RG-CT5300L-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('290d07d6-59a8-4dc3-894a-44627ef2d4ce', 'RG-CT5500L-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('904666a1-b577-4e80-926c-6faef88a080a', 'RG-CT1000-G2', 'VDI', 'product', true, now(), now(),0, 'RG-CT1',null);


-- 新增分销IDV终端工作模式支持
-- 先删除再插入，防止从P3版本升级上来，重复插入数据
delete from t_rco_terminal_work_mode_mapping where support_mode = 'RG-CT5200C-G3';
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('3033bf8f-856f-45e3-9c1c-059f89d2232a', 'RG-CT5200C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

delete from t_rco_terminal_work_mode_mapping where support_mode = 'RG-CT5300C-G3';
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('ac110556-e0b3-4ef1-aa38-7507efa568f3', 'RG-CT5300C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

delete from t_rco_terminal_work_mode_mapping where support_mode = 'RG-CT5500C-G3';
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('77049160-fd42-4926-aa61-5f1d67ef1e99', 'RG-CT5500C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('d4af9aba-a02d-4945-9e95-56fda0480871', 'RG-CT6200C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT6',null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('94e0dbc3-6d6c-4b9e-b83d-b5b54366816a', 'RG-CT6300C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT6',null);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('d4aa39d2-2b4f-4019-af9e-46019218c652', 'RG-CT6500C-G3', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT6',null);

--配置辅助认证（硬件特征码）
INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('ed85433a-9341-4194-829f-1fd337999997', 'certification_hardware_summary',
 '{"openHardware":false,"maxHardwareNum":60,"autoApprove":false,"enableTerminalApproved":false}',
 '{"openHardware":false,"maxHardwareNum":60,"autoApprove":false,"enableTerminalApproved":false}', now(), now(), 0);

 -- 添加终端特征码序列号全局管理
INSERT INTO "t_sk_global_parameter"("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('205cfdaf-72cf-4e4e-8770-81c2c7456111', 'feature_code_sequence', '2396', '2396', now(), now(), 0);

--配置辅助认证（动态口令）
INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('38155a06-91a0-4f5a-a9da-8c9e23373387', 'certification_otp_summary',
 '{"openOtp":false,"otpType":"totp","algorithm":"SHA1","period": 30,"digits":6,"systemName":"RCDC-V5.3","systemHost":"ruijie.com.cn","hasOtpCodeTab":false}',
 '{"openOtp":false,"otpType":"totp","algorithm":"SHA1","period": 30,"digits":6,"systemName":"RCDC-V5.3","systemHost":"ruijie.com.cn","hasOtpCodeTab":false}', now(), now(), 0);



INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('860902cf-4f22-442e-9eef-3638662e9104', 'sms_and_scancode_switch',
 '{"enableCheck":false}',
 '{"enableCheck":false', now(), now(), 0);

INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('a563ab0c-8b38-4e22-9be4-fa86c37140d1', 'has_upload_customer_info', 'false', 'false', now(), now(), 0);
INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('a563ab0c-8b38-4e22-9be4-fa86c37140d2', 'collect_rule', '', '', now(), now(), 0);

-- 插入评测功能策略
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
	( '726361b1-7bc2-4206-9c8e-f4e0d63a75df', 'enable_evaluation_strategy', 'false', 'false', now(), now(), 0 );

-- 是否需要进行MINI服务器证书授权
INSERT INTO
    t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
    ('d16d159e-774d-4e95-9ad0-ff5c8b457f60', 'need_auth_mini_server', 'true', 'true', now(), now(), 0);



-- 全局绑定用户 --
INSERT INTO t_rco_global_parameter (id,param_key, param_value, default_value, create_time, update_time,version)
VALUES ('58e3f319-6c01-4f58-b435-4714d2581765','system_up_auto_bind_image','true', 'true',  now(), now(), 0);


--VDI IP网段限制
INSERT INTO t_rco_global_parameter ( "id", "param_key", "param_value", "default_value", "create_time", "update_time", "version" )
VALUES
	( '16cdc486-a7e9-4452-a33d-fffd79002c31', 'enable_vdi_ip_limit', 'false', 'false', now(), now(), 0 );

---IDV/VOI离线登录设置
INSERT INTO t_rco_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('f469d511-464f-4ae1-8e1d-23586edb6dee', 'enable_offline_login', 'true', 'true', now(), now(), 0);