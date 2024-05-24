/* 用户身份验证配置表 */
CREATE TABLE t_rco_user_identity_config (
id uuid NOT NULL,
related_type varchar(32) NOT NULL,
related_id uuid NOT NULL,
login_identity_level varchar(32) DEFAULT 'AUTO' NOT NULL,
version int4,
update_time timestamp,
CONSTRAINT t_rco_user_identity_config_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE t_rco_user_identity_config IS '身份验证配置表';
COMMENT ON COLUMN t_rco_user_identity_config.id IS '身份验证配置表id，主键';
COMMENT ON COLUMN t_rco_user_identity_config.related_type IS '关联对象类型';
COMMENT ON COLUMN t_rco_user_identity_config.related_id IS '关联对象id';
COMMENT ON COLUMN t_rco_user_identity_config.login_identity_level IS '登录权限等级';
COMMENT ON COLUMN t_rco_user_identity_config.version IS '版本号';
COMMENT ON COLUMN t_rco_user_identity_config.update_time IS '更新时间';


CREATE TABLE t_rco_role_group_permission (
id uuid NOT NULL,
role_id uuid NOT NULL,
group_id varchar(64) NOT NULL,
group_type varchar(16) NOT NULL,
version int4 DEFAULT 0 NOT NULL,
CONSTRAINT t_rco_role_group_permission_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_role_group_permission.id IS '主键ID';
COMMENT ON COLUMN t_rco_role_group_permission.role_id IS '角色ID';
COMMENT ON COLUMN t_rco_role_group_permission.group_id IS '组ID';
COMMENT ON COLUMN t_rco_role_group_permission.group_type IS '组类型';

CREATE TABLE t_rco_computer (
id uuid NOT NULL,
group_id uuid NOT NULL,
ip varchar(64),
mac varchar(64),
name varchar(32),
alias varchar(32),
os varchar(32),
disk varchar(16),
cpu varchar(128),
memory varchar(16),
assist_pwd varchar(16),
state varchar(16),
fault_state bool DEFAULT false NOT NULL,
fault_description varchar(128),
create_time timestamp(6),
fault_time timestamp(6),
version int4,
CONSTRAINT t_rco_computer_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_computer.id IS '主键id';
COMMENT ON COLUMN t_rco_computer.group_id IS '终端组id';
COMMENT ON COLUMN t_rco_computer.ip IS 'pc-ip';
COMMENT ON COLUMN t_rco_computer.mac IS 'mac';
COMMENT ON COLUMN t_rco_computer.name IS '计算机名';
COMMENT ON COLUMN t_rco_computer.alias IS '备注名';
COMMENT ON COLUMN t_rco_computer.os IS '计算机操作系统';
COMMENT ON COLUMN t_rco_computer.disk IS '硬盘大小';
COMMENT ON COLUMN t_rco_computer.cpu IS 'CPU';
COMMENT ON COLUMN t_rco_computer.memory IS '内存';
COMMENT ON COLUMN t_rco_computer.state IS '状态';
COMMENT ON COLUMN t_rco_computer.fault_state IS '故障状态';
COMMENT ON COLUMN t_rco_computer.fault_description IS '故障描述';
COMMENT ON COLUMN t_rco_computer.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_computer.fault_time IS '创建时间';
COMMENT ON COLUMN t_rco_computer.version IS '版本号';

