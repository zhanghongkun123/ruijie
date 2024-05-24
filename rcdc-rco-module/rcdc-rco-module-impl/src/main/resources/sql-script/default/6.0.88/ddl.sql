--创建客户端操作日志表
create TABLE IF NOT EXISTS t_rco_client_op_log(
   id uuid NOT NULL,
   mac VARCHAR(40),
   ip VARCHAR(20),
   user_id uuid,
   user_name VARCHAR(255),
   oper_time timestamp(6),
   oper_msg VARCHAR(255),
   version int4,
   create_time timestamp(6),
   update_time timestamp(6),
   CONSTRAINT t_rco_client_op_log_pkey PRIMARY KEY (id)
);

comment on column t_rco_client_op_log.id is 'id，主键';
comment on column t_rco_client_op_log.mac is '宿主机mac';
comment on column t_rco_client_op_log.ip is '宿主机ip';
comment on column t_rco_client_op_log.user_id is '用户id';
comment on column t_rco_client_op_log.user_name is '用户名';
comment on column t_rco_client_op_log.oper_time is '创建时间';
comment on column t_rco_client_op_log.oper_msg is '操作内容';
comment on column t_rco_client_op_log.version is '版本号';
comment on column t_rco_client_op_log.create_time is '创建时间';
comment on column t_rco_client_op_log.update_time is '更新时间';
