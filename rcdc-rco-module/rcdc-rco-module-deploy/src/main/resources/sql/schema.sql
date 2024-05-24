
 CREATE TABLE if not exists t_base_audit_log_global_config (
	id uuid NOT NULL,
	interval_seconds int4 NOT NULL,
	base_log_type varchar(32) NOT NULL,
	version int8 NULL,
	create_time timestamp NOT NULL,
	update_time timestamp NULL,
	CONSTRAINT t_base_audit_log_global_config_pkey PRIMARY KEY (id)
);
INSERT INTO public.t_base_audit_log_global_config
(id, interval_seconds, base_log_type, version, create_time, update_time)
VALUES('383e1ed1-0717-42e4-9840-9d86b620b16b', 15552000, 'OPERATOR_LOG', 0, '2020-09-14 11:18:30.000', NULL);
INSERT INTO public.t_base_audit_log_global_config
(id, interval_seconds, base_log_type, version, create_time, update_time)
VALUES('1f11a232-66dc-4cb1-8c67-2ee81aba6340', 15552000, 'SYSTEM_LOG', 0, '2020-09-14 11:20:13.000', NULL);
INSERT INTO public.t_base_audit_log_global_config
(id, interval_seconds, base_log_type, version, create_time, update_time)
VALUES('f25ba695-f7e7-4e03-b7a0-7c57f97c27d4', 15552000, 'OPENAPI_LOG', 0, '2021-09-09 13:14:15.000', NULL);


 CREATE TABLE if not exists t_base_dbupgrade_system_version (
	id uuid NOT NULL,
	upgrade_status varchar(64) NOT NULL,
	system_version varchar(64) NOT NULL,
	update_time timestamp NULL,
	version int4 NULL,
	CONSTRAINT t_base_dbupgrade_system_version_pkey PRIMARY KEY (id)
);

-- t_base_openapi_log definition

-- Drop table

-- DROP TABLE t_base_openapi_log;

 CREATE TABLE if not exists t_base_openapi_log (
	id uuid NOT NULL,
	method varchar(16) NULL,
	uri varchar(255) NULL,
	client_ip varchar(32) NULL,
	request_time timestamp(6) NULL,
	response_status varchar(32) NULL,
	latency int8 NULL,
	version int4 NULL,
	CONSTRAINT t_base_openapi_log_pkey PRIMARY KEY (id)
);

-- t_base_operate_log definition

-- Drop table

-- DROP TABLE t_base_operate_log;

 CREATE TABLE if not exists t_base_operate_log (
	id uuid NOT NULL,
	operator varchar(64) NULL,
	operation_time timestamp(6) NULL,
	login_ip varchar(20) NULL,
	content text NULL,
	operator_id uuid NOT NULL,
	version int8 NULL,
	CONSTRAINT t_base_operate_log_pkey PRIMARY KEY (id)
);
-- t_base_system_log definition

-- Drop table

-- DROP TABLE t_base_system_log;

 CREATE TABLE if not exists t_base_system_log (
	id uuid NOT NULL,
	content text NULL,
	create_time timestamp(6) NULL,
	version int8 NULL,
	CONSTRAINT t_base_system_log_pkey PRIMARY KEY (id)
);

-- t_base_task_schedule_monitor definition

-- Drop table

-- DROP TABLE t_base_task_schedule_monitor;

 CREATE TABLE if not exists t_base_task_schedule_monitor (
	id uuid NOT NULL,
	schedule_task_id uuid NULL,
	task_name varchar(128) NULL,
	schedule_type_code varchar(128) NOT NULL,
	total_millis int8 NOT NULL,
	average_millis int8 NOT NULL,
	total_count int4 NOT NULL,
	error_count int4 NOT NULL,
	create_time timestamp(6) NOT NULL,
	update_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	CONSTRAINT t_base_task_schedule_monitor_pkey PRIMARY KEY (id)
);

-- t_base_task_schedule_monitor_detail definition

-- Drop table

-- DROP TABLE t_base_task_schedule_monitor_detail;

 CREATE TABLE if not exists t_base_task_schedule_monitor_detail (
	id uuid NOT NULL,
	schedule_task_id uuid NULL,
	task_name varchar(128) NULL,
	schedule_type_code varchar(128) NOT NULL,
	before_running_time timestamp(6) NOT NULL,
	after_running_time timestamp(6) NOT NULL,
	running_millis int8 NOT NULL,
	error_message varchar(1024) NULL,
	finish_type varchar(64) NOT NULL,
	create_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	CONSTRAINT t_base_task_schedule_monitor_detail_pkey PRIMARY KEY (id)
);

-- t_base_task_sm_detail definition

-- Drop table

-- DROP TABLE t_base_task_sm_detail;

 CREATE TABLE if not exists t_base_task_sm_detail (
	id uuid NOT NULL,
	create_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	update_time timestamp(6) NOT NULL,
	next_schedule_time timestamp(6) NOT NULL,
	current_method_name varchar(64) NULL,
	state_process_class varchar(256) NULL,
	custom_state_context text NOT NULL,
	pre_defined_state_context text NOT NULL,
	delay_mills int8 NOT NULL DEFAULT 0,
	sm_dtx_id uuid NOT NULL,
	current_processor_name varchar(128) NULL,
	CONSTRAINT t_base_task_sm_detail_pkey PRIMARY KEY (id)
);

-- t_base_task_sm_exception definition

-- Drop table

-- DROP TABLE t_base_task_sm_exception;

 CREATE TABLE if not exists t_base_task_sm_exception (
	id uuid NOT NULL,
	ex_msg_key text NULL,
	ex_msg_args text NULL,
	ex_type_name varchar(256) NOT NULL,
	ex_message varchar(1024) NULL,
	create_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	processor_name varchar(128) NULL,
	ex_error_code varchar(16) NULL,
	CONSTRAINT t_base_task_sm_exception_pkey PRIMARY KEY (id)
);
-- t_base_task_sm_main definition

-- Drop table

-- DROP TABLE t_base_task_sm_main;

 CREATE TABLE if not exists t_base_task_sm_main (
	id uuid NOT NULL,
	create_time timestamp(6) NOT NULL,
	execution_stage varchar(16) NOT NULL,
	version int4 NOT NULL,
	state_machine_name varchar(64) NOT NULL,
	running_state varchar(16) NOT NULL,
	undo_reason varchar(64) NULL,
	undo_start_time timestamp(6) NULL,
	sm_dtx_id uuid NOT NULL,
	parent_sm_id uuid NULL,
	undo_processor_name varchar(128) NULL,
	enable_block_in_maintenance_mode bool NULL DEFAULT false,
	resource_id varchar(256) NULL,
	business_operate_type varchar(64) NULL,
	task_result varchar(16) NULL,
	pre_defined_state_context text NULL,
	custom_state_context text NULL,
	CONSTRAINT t_base_task_sm_main_pkey PRIMARY KEY (id)
);
--CREATE INDEX t_base_task_sm_main_resource_id ON t_base_task_sm_main USING btree (resource_id, business_operate_type);

-- t_base_task_sm_resource definition

-- Drop table

-- DROP TABLE t_base_task_sm_resource;

 CREATE TABLE if not exists t_base_task_sm_resource (
	id uuid NOT NULL,
	create_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	sm_id uuid NOT NULL,
	sm_dtx_id uuid NOT NULL,
	resource_id varchar(256) NOT NULL,
	business_operate_type varchar(64) NULL,
	running_state varchar(16) NOT NULL,
	CONSTRAINT t_base_task_sm_resource_pkey PRIMARY KEY (id)
);
--CREATE INDEX t_base_task_sm_resource_create_time ON public.t_base_task_sm_resource USING btree (create_time);
--CREATE INDEX t_base_task_sm_resource_resource_id ON public.t_base_task_sm_resource USING btree (resource_id, business_operate_type);
--CREATE INDEX t_base_task_sm_resource_sm_id ON public.t_base_task_sm_resource USING btree (sm_id);

-- t_base_task_sm_stat_detail definition

-- Drop table

-- DROP TABLE t_base_task_sm_stat_detail;

 CREATE TABLE if not exists t_base_task_sm_stat_detail (
	id uuid NOT NULL,
	create_time timestamp(6) NOT NULL,
	version int4 NOT NULL,
	main_id uuid NOT NULL,
	execution_stage varchar(32) NOT NULL,
	method_name varchar(64) NOT NULL,
	state_process_class varchar(256) NOT NULL,
	running_millis int8 NOT NULL,
	waiting_millis int8 NOT NULL,
	before_running_time timestamp(6) NOT NULL,
	after_running_time timestamp(6) NOT NULL,
	state_processor_name varchar(128) NULL,
	next_schedule_time timestamp(6) NULL,
	process_result_type varchar(8) NULL,
	CONSTRAINT t_base_task_sm_stat_detail_pkey PRIMARY KEY (id)
);

-- t_sk_dtx_main_active definition

-- Drop table

-- DROP TABLE t_sk_dtx_main_active;

 CREATE TABLE if not exists t_sk_dtx_main_active (
	id uuid NOT NULL,
	dtx_sponsor varchar(64) NULL,
	dtx_state varchar(32) NULL,
	create_time timestamp(6) NULL,
	version int4 NULL DEFAULT 1,
	update_time timestamp(6) NULL,
	finish_time timestamp(6) NULL,
	expire_time timestamp(6) NULL,
	finish_type varchar(32) NULL,
	undo_reason varchar(32) NULL,
	suspend_type varchar(32) NULL,
	next_detail_seq int4 NULL,
	call_stack_depth int4 NULL,
	undo_sponsor varchar(64) NULL,
	CONSTRAINT t_sk_dtx_main_active_pkey PRIMARY KEY (id)
);


-- t_sk_dtx_main_finish definition

-- Drop table

-- DROP TABLE t_sk_dtx_main_finish;

 CREATE TABLE if not exists t_sk_dtx_main_finish (
	id uuid NULL,
	dtx_sponsor varchar(64) NULL,
	create_time timestamp(6) NULL,
	finish_time timestamp(6) NULL,
	undo_reason varchar(32) NULL,
	finish_type varchar(32) NULL,
	record_details text NULL,
	version int4 NULL
);

-- t_sk_dtx_record_detail definition

-- Drop table

-- DROP TABLE t_sk_dtx_record_detail;

 CREATE TABLE if not exists t_sk_dtx_record_detail (
	id uuid NOT NULL,
	dtx_id uuid NULL,
	interface_class_name varchar(128) NULL,
	method_name varchar(128) NULL,
	create_time timestamp(6) NULL,
	update_time timestamp(6) NULL,
	version int4 NULL,
	request text NULL,
	response text NULL,
	ex text NULL,
	custom_data text NULL,
	seq_number int4 NULL,
	detail_state varchar(128) NULL,
	detail_sponsor varchar(64) NULL,
	CONSTRAINT t_sk_dtx_record_detail_pkey PRIMARY KEY (id)
);
