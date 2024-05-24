-- OPENAPI异步任务表 （t_rco_open_api_task_info）修改异常码长度
ALTER TABLE "public"."t_rco_open_api_task_info" ALTER COLUMN exception_key TYPE varchar(512) USING exception_key::varchar;

-- 用户登录数据报表在设置了排序字段后sql查询需要25s+，导致jpa查询超时（修订bug 668720）
-- 给排序字段建立索引
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_login_time_idx ON public.t_cbb_user_login_record (login_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_auth_duration_idx ON public.t_cbb_user_login_record (auth_duration);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_connect_time_idx ON public.t_cbb_user_login_record (connect_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_connect_duration_idx ON public.t_cbb_user_login_record (connect_duration);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_logout_time_idx ON public.t_cbb_user_login_record (logout_time);
CREATE INDEX  IF NOT EXISTS t_cbb_user_login_record_use_duration_idx ON public.t_cbb_user_login_record (use_duration);
