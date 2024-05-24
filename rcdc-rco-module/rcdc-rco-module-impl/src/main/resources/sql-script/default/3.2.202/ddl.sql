-- OPENAPI异步任务表 （t_rco_open_api_task_info）修改异常码长度
ALTER TABLE "public"."t_rco_open_api_task_info" ALTER COLUMN exception_key TYPE varchar(512) USING exception_key::varchar;
