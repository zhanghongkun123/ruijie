-- 锐捷客户端扫码
ALTER FOREIGN TABLE t_base_iac_user_identity_config
ADD COLUMN IF NOT EXISTS open_rjclient_certification bool DEFAULT true;

