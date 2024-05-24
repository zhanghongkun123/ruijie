-- 外部消息记录表
CREATE TABLE IF NOT EXISTS t_rco_external_message_log (
  id uuid NOT NULL,
  related_type varchar(64) NOT NULL,
  platform_type varchar(64)  NOT NULL,
  related_target varchar(256) NOT NULL,
  send_content varchar(1024) NOT NULL,
  status varchar(64) NOT NULL,
  fail_msg varchar(1024),
  send_time timestamp(6),
  version int4 NOT NULL DEFAULT 0,
  CONSTRAINT t_rco_external_message_log_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_external_message_log.id IS 'id';
COMMENT ON COLUMN t_rco_external_message_log.related_type IS '关联类型：SMS_AUTH、PWD_RECOVER';
COMMENT ON COLUMN t_rco_external_message_log.platform_type IS '消息平台类型：SMS、EMAIL';
COMMENT ON COLUMN t_rco_external_message_log.related_target IS '关联对象：手机号码，邮箱';
COMMENT ON COLUMN t_rco_external_message_log.send_content IS '发送内容';
COMMENT ON COLUMN t_rco_external_message_log.status IS '消息状态：SUCCESS、FAIL';
COMMENT ON COLUMN t_rco_external_message_log.fail_msg IS '失败描述';
COMMENT ON COLUMN t_rco_external_message_log.send_time IS '发送时间';
COMMENT ON COLUMN t_rco_external_message_log.version IS '版本号';

-- 身份验证配置表（t_rco_user_identity_config）新增短信认证开关
ALTER TABLE "public"."t_rco_user_identity_config" ADD COLUMN IF NOT EXISTS "open_sms_certification" bool default false;
comment ON COLUMN "public"."t_rco_user_identity_config"."open_sms_certification" IS '开启短信认证：默认关闭';