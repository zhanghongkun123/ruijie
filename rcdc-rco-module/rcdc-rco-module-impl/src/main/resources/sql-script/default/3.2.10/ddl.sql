/* 分发文件表 */
CREATE TABLE IF NOT EXISTS t_rco_distribute_file (
id uuid NOT NULL,
file_name varchar(256)  NOT NULL,
file_size int8,
description varchar(256),
version int4 DEFAULT 0 NOT NULL,
create_time TIMESTAMP,
CONSTRAINT t_rco_distribute_file_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_distribute_file.file_name IS '文件名称';
COMMENT ON COLUMN t_rco_distribute_file.file_size IS '文件大小';
COMMENT ON COLUMN t_rco_distribute_file.description IS '描述';
COMMENT ON COLUMN t_rco_distribute_file.create_time IS '上传时间';

/* 分发参数表 */
CREATE TABLE IF NOT EXISTS t_rco_distribute_parameter (
id uuid NOT NULL,
parameter varchar NOT NULL,
version int4 DEFAULT 0 NOT NULL,
CONSTRAINT t_rco_distribute_parameter_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_distribute_parameter.parameter IS '任务参数';

/* 分发任务表（父任务） */
CREATE TABLE IF NOT EXISTS t_rco_distribute_task (
id uuid NOT NULL,
parameter_id uuid NOT NULL,
task_name varchar(128) NOT NULL,
create_time TIMESTAMP NOT NULL,
version int4 DEFAULT 0 NOT NULL,
CONSTRAINT t_rco_distribute_task_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_distribute_task.parameter_id IS '参数ID';
COMMENT ON COLUMN t_rco_distribute_task.task_name IS '任务名称';
COMMENT ON COLUMN t_rco_distribute_task.create_time IS '创建时间';


/* 分发任务表（父任务） */
CREATE TABLE IF NOT EXISTS t_rco_distribute_sub_task (
id uuid NOT NULL,
target_id varchar NOT NULL,
target_type varchar(32) NOT NULL,
parent_task_id uuid NOT NULL,
status varchar(32) DEFAULT 'WAITING' NOT NULL,
message varchar(128),
create_time TIMESTAMP NOT NULL,
start_time TIMESTAMP,
stash_status varchar(32) DEFAULT 'NONE' NOT NULL,
update_time TIMESTAMP,
version int4 DEFAULT 0 NOT NULL,
CONSTRAINT t_rco_distribute_sub_task_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_distribute_sub_task.target_id IS '分发对象ID';
COMMENT ON COLUMN t_rco_distribute_sub_task.target_type IS '分发对象类型';
COMMENT ON COLUMN t_rco_distribute_sub_task.parent_task_id IS '父任务ID';
COMMENT ON COLUMN t_rco_distribute_sub_task.status IS '任务状态';
COMMENT ON COLUMN t_rco_distribute_sub_task.message IS '任务状态信息';
COMMENT ON COLUMN t_rco_distribute_sub_task.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_distribute_sub_task.start_time IS '开始时间';
COMMENT ON COLUMN t_rco_distribute_sub_task.stash_status IS '任务暂存状态';
COMMENT ON COLUMN t_rco_distribute_sub_task.update_time IS '更新时间';
