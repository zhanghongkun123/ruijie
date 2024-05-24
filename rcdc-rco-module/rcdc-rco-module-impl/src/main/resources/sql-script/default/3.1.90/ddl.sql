alter table t_rco_open_api_task_info add COLUMN IF NOT EXISTS task_item_list varchar;
alter table t_rco_open_api_task_info add COLUMN IF NOT EXISTS  task_result varchar;
alter table t_rco_open_api_task_info alter column exception_message type varchar using exception_message::varchar;
comment on column t_rco_open_api_task_info.task_item_list is '子任务列表';
comment on column t_rco_open_api_task_info.task_result is '任务结果';


-- 终端镜像下载状态表
CREATE TABLE "t_rco_image_download_state" (
  "id" uuid NOT NULL,
  "terminal_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "image_id" uuid,
  "download_state" varchar(32) COLLATE "pg_catalog"."default",
  "fail_code" int4,
  "download_finish_time" timestamp(6),
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "version" int4,
  CONSTRAINT "t_rco_image_download_state_pkey" PRIMARY KEY ("id")
);

-- 创建终端镜像下载状态表索引
CREATE INDEX idx_image_download_state ON t_rco_image_download_state (terminal_id);

COMMENT ON COLUMN "t_rco_image_download_state"."id" IS 'id';
COMMENT ON COLUMN "t_rco_image_download_state"."terminal_id" IS '终端id';
COMMENT ON COLUMN "t_rco_image_download_state"."image_id" IS '镜像模板id';
COMMENT ON COLUMN "t_rco_image_download_state"."download_state" IS '下发状态';
COMMENT ON COLUMN "t_rco_image_download_state"."fail_code" IS '错误码';
COMMENT ON COLUMN "t_rco_image_download_state"."download_finish_time" IS '下发成功的时间';
COMMENT ON COLUMN "t_rco_image_download_state"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_rco_image_download_state"."update_time" IS '修改时间';
COMMENT ON COLUMN "t_rco_image_download_state"."version" IS '乐观锁字段';