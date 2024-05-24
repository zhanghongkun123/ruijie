CREATE TABLE "t_rco_image_template_terminal" (
  "id" uuid NOT NULL,
  "version" int4 NOT NULL,
  "image_id" uuid NOT NULL,
  "terminal_id" varchar(32) NOT NULL,
  CONSTRAINT "t_rco_image_template_terminal_pkey" PRIMARY KEY ("id")
);

/* 管理员自定义功能列表列数据记录表 */
DROP TABLE IF EXISTS t_rco_admin_function_list_custom;
CREATE TABLE t_rco_admin_function_list_custom (
id uuid NOT NULL,
admin_id uuid NOT NULL,
function_type varchar(32) NOT NULL,
column_text text NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
CONSTRAINT t_rco_admin_function_list_custom_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_admin_function_list_custom.id IS '主键id';
COMMENT ON COLUMN t_rco_admin_function_list_custom.admin_id IS '管理员id';
COMMENT ON COLUMN t_rco_admin_function_list_custom.function_type IS '功能模块类型';
COMMENT ON COLUMN t_rco_admin_function_list_custom.column_text IS '列数据';
COMMENT ON COLUMN t_rco_admin_function_list_custom.version IS '版本号';
COMMENT ON COLUMN t_rco_admin_function_list_custom.create_time IS '创建时间';