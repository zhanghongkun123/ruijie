-- 文件流转审计文件列表名冗余用于查询，防止大数据量时查询卡顿
ALTER TABLE t_rco_audit_apply ADD COLUMN IF NOT EXISTS file_name text NULL;
COMMENT ON COLUMN t_rco_audit_apply.file_name IS '文件名';
-- 文件流转审计文件列表默认排序
CREATE INDEX IF NOT EXISTS idx_trafa_update_time ON t_rco_audit_apply USING btree (update_time);

-- 防止桌面池分配用户数据重复
CREATE UNIQUE INDEX IF NOT EXISTS idx_trdpu_id_related_id_type ON t_rco_desktop_pool_user (desktop_pool_id,related_id,related_type);
--删除用户使用信息无效索引
DROP index if EXISTS t_cbb_user_login_record_login_time_idx;
DROP index if EXISTS t_cbb_user_login_record_auth_duration_idx;
DROP index if EXISTS t_cbb_user_login_record_connect_time_idx;
DROP index if EXISTS t_cbb_user_login_record_connect_duration_idx;
DROP index if EXISTS t_cbb_user_login_record_logout_time_idx;
DROP index if EXISTS t_cbb_user_login_record_use_duration_idx;
DROP index if EXISTS user_name;
DROP index if EXISTS user_group_name;

--计算使用时长
create or replace function get_use_duration(session_state varchar, connect_time timestamp, use_duration int8) returns int8 immutable as $$
begin
  if session_state = 'CONNECTED' then
    return floor(EXTRACT(epoch FROM now()) * 1000) - floor(EXTRACT(epoch FROM connect_time) * 1000 - 28800 * 1000);
  else
    return use_duration;
   end if;
end; $$ language plpgsql;

--添加用户使用信息索引
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_login_time_connext_time_idx ON public.t_cbb_user_login_record (login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_connect_time_login_time_idx ON public.t_cbb_user_login_record (connect_time, login_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_user_name_login_time_connext_time_idx ON public.t_cbb_user_login_record (user_name, login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_user_group_name_login_time_connext_time_idx ON public.t_cbb_user_login_record (user_group_name, login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_auth_duration_login_time_connext_time_idx ON public.t_cbb_user_login_record (auth_duration, login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_connect_duration_login_time_connext_time_idx ON public.t_cbb_user_login_record (connect_duration, login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_logout_time_login_time_connext_time_idx ON public.t_cbb_user_login_record (logout_time, login_time, connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_connected_use_duration_login_time_connext_time_idx ON public.t_cbb_user_login_record (get_use_duration(session_state, connect_time, use_duration), login_time, connect_time);

--修订：exception_key超过64位保存失败
ALTER TABLE t_rco_open_api_task_info ALTER COLUMN exception_key TYPE text;

-- 文件流转审计增加桌面池ID、桌面类型
ALTER TABLE t_rco_audit_apply ADD COLUMN IF NOT EXISTS desktop_pool_type varchar(64);
COMMENT ON COLUMN t_rco_audit_apply.desktop_pool_type IS '桌面池类型';
ALTER TABLE t_rco_audit_apply ADD COLUMN IF NOT EXISTS desktop_pool_Id uuid;
COMMENT ON COLUMN t_rco_audit_apply.desktop_pool_Id IS '桌面池ID';