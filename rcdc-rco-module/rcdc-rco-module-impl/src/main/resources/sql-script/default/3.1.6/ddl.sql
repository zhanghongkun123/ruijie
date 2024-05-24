-- openAPI修改任务状态表
ALTER TABLE "public"."t_rco_open_api_task_info" ALTER COLUMN exception_name DROP NOT NULL;
ALTER TABLE "public"."t_rco_open_api_task_info" ALTER COLUMN exception_message DROP NOT NULL;
ALTER TABLE "public"."t_rco_open_api_task_info" ADD COLUMN IF NOT EXISTS "business_id" uuid;
COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."business_id" IS '业务id';
ALTER TABLE "public"."t_rco_open_api_task_info" ADD COLUMN IF NOT EXISTS "task_state" varchar(32);
COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."task_state" IS '任务状态';
ALTER TABLE "public"."t_rco_open_api_task_info" ADD COLUMN IF NOT EXISTS "resource_info" varchar;
COMMENT ON COLUMN "public"."t_rco_open_api_task_info"."resource_info" IS '资源信息';