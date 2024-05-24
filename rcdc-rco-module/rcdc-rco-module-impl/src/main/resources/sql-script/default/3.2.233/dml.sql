INSERT INTO public.t_rco_terminal_work_mode_mapping (id,support_mode,working_mode,judge_basis,enable_state,create_time,update_time,"version",match_rule,sub_match_rule,platform) VALUES
	 ('f6b8f624-1620-4312-b13c-d0a2aca61441'::uuid,'RG-CT7800-1000','VDI,IDV','product',true,now(),now(),0,NULL,NULL,'IDV') ON CONFLICT(id) DO NOTHING;

---修改短信认证与密码找回模板
CREATE OR REPLACE FUNCTION update_sms_template(p_key varchar, template_key varchar, replace_template_key varchar)
  RETURNS VOID AS
$body$
DECLARE
    isShow boolean := FALSE;
BEGIN
    isShow = (param_value::json->>'isShow')::boolean OR (param_value::json->>'show')::boolean FROM t_sk_global_parameter WHERE param_key = 'configuration_wizard';
    IF isShow IS TRUE AND EXISTS (SELECT * FROM t_rco_global_parameter WHERE param_key = p_key) THEN
        UPDATE t_rco_global_parameter SET param_value=regexp_replace(param_value::text, param_value::json->>template_key, replace_template_key, 'g')::json WHERE param_key = p_key;
    END IF;
END;

$body$
LANGUAGE plpgsql;
-- 执行存储过程
SELECT update_sms_template('sms_auth_config', 'smsTemplate', '您正在使用短信验证码登录云桌面或绑定用户手机号，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。');
SELECT update_sms_template('sms_recover_pwd_config', 'smsTemplate', '您正在使用短信验证码找回密码，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。');
SELECT update_sms_template('sms_auth_config_backup', 'smsTemplate', '您正在使用短信验证码登录云桌面或绑定用户手机号，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。');
SELECT update_sms_template('sms_recover_pwd_config_backup', 'smsTemplate', '您正在使用短信验证码找回密码，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。');
-- 删除存储过程
DROP FUNCTION update_sms_template(p_key varchar, template_key varchar, replace_template_key varchar);

-- window 桌面背景添加新配置，bug: 670914
INSERT INTO t_rco_user_profile_child_path VALUES('1b608249-2070-4d17-81ee-9f402ab4252d',  'SYNCHRO', 'REGISTRY_KEY', '3f830bb2-7829-77ae-f991-6d09c7ee4b80',
0, 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_user_profile_path_detail VALUES ('66374509-8c23-4ca7-8d92-e76443caadce', 'HKEY_CURRENT_USER\Control Panel\Colors',
'1b608249-2070-4d17-81ee-9f402ab4252d', '3f830bb2-7829-77ae-f991-6d09c7ee4b80', 0, 0) ON CONFLICT(id) DO NOTHING;