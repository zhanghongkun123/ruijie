ALTER TABLE t_rco_computer add column if not exists agent_version varchar(64);
COMMENT ON COLUMN t_rco_computer.agent_version IS 'agent版本';
ALTER TABLE t_rco_computer add column if not exists type varchar(64) DEFAULT 'PC';
COMMENT ON COLUMN t_rco_computer.type IS '类型';
ALTER TABLE t_rco_computer add column if not exists work_model varchar(64);
COMMENT ON COLUMN t_rco_computer.work_model IS '工作模式';
ALTER TABLE t_rco_computer add column if not exists system_disk int4;
COMMENT ON COLUMN t_rco_computer.system_disk IS '系统盘';
ALTER TABLE t_rco_computer add column if not exists person_disk int4;
COMMENT ON COLUMN t_rco_computer.person_disk IS '个人盘';
ALTER TABLE t_rco_computer add column if not exists subnet_mask varchar(64);
COMMENT ON COLUMN t_rco_computer.subnet_mask IS '子网掩码';
ALTER TABLE t_rco_computer add column if not exists network_number varchar(64);
COMMENT ON COLUMN t_rco_computer.network_number IS '网段';
ALTER TABLE t_rco_computer add column if not exists terminal_id UUID;
COMMENT ON COLUMN t_rco_computer.terminal_id IS '终端id';


ALTER TABLE t_cbb_desk_strategy add column if not exists enable_shared_printing bool;
COMMENT ON COLUMN t_cbb_desk_strategy.enable_shared_printing IS '开启共享打印';
ALTER TABLE t_cbb_desk_strategy add column if not exists session_type varchar(64);
COMMENT ON COLUMN t_cbb_desk_strategy.session_type IS '会话类型';

CREATE TABLE IF NOT EXISTS t_rco_desktop_pool_computer(
                                         id uuid NOT NULL,
                                         desktop_pool_id uuid NOT NULL,
                                         related_id uuid NOT NULL,
                                         related_type varchar(64) NOT NULL,
                                         create_time timestamp,
                                         version int4 DEFAULT 0 NOT NULL,
                                         CONSTRAINT t_rco_desktop_pool_computer_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE t_rco_desktop_pool_computer IS '桌面池与PC终端关系表';
COMMENT ON COLUMN t_rco_desktop_pool_computer.id IS '桌面池与PC终端关系表id，主键';
COMMENT ON COLUMN t_rco_desktop_pool_computer.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_desktop_pool_computer.related_id IS '关联对象id';
COMMENT ON COLUMN t_rco_desktop_pool_computer.related_type IS '关联对象类型：;COMPUTER：PC终端;COMPUTER_GROUP：PC终端组';
COMMENT ON COLUMN t_rco_desktop_pool_computer.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_pool_computer.version IS '版本号';


ALTER TABLE t_rco_user_profile_strategy
ADD COLUMN IF NOT EXISTS external_storage_id UUID DEFAULT null;
COMMENT ON COLUMN t_rco_user_profile_strategy.external_storage_id IS '文件服务器id';

-- 系统报表-会话连接记录：添加桌面池类型字段（VDI、第三方）
ALTER TABLE t_rco_desktop_session_log
ADD COLUMN IF NOT EXISTS cbb_desktop_pool_type varchar(64) DEFAULT 'VDI';
COMMENT ON COLUMN t_rco_desktop_session_log.cbb_desktop_pool_type IS '桌面池类型：VDI、THIRD';

-- 系统报表-连接失败记录表：添加桌面池类型字段（VDI、第三方）
ALTER TABLE t_rco_user_connect_desktop_fault_log
ADD COLUMN IF NOT EXISTS cbb_desktop_pool_type varchar(64) DEFAULT 'VDI';
COMMENT ON COLUMN t_rco_user_connect_desktop_fault_log.cbb_desktop_pool_type IS '桌面池类型：VDI、THIRD';

CREATE TABLE IF NOT EXISTS  t_rco_host_user(
        id uuid NOT NULL,
        desktop_id uuid NOT NULL,
        desktop_pool_id uuid NOT NULL,
        user_id uuid NOT NULL,
        "version" int4 NULL,
        create_time timestamp(6) NULL,
        update_time timestamp(6) NULL,
        CONSTRAINT t_rco_host_user_pkey PRIMARY KEY (id)
);
COMMENT ON COLUMN t_rco_host_user.id IS '记录唯一标识';
COMMENT ON COLUMN t_rco_host_user.desktop_id IS '桌面主机id';
COMMENT ON COLUMN t_rco_host_user.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_host_user.user_id IS '用户ID';
COMMENT ON COLUMN t_rco_host_user.create_time IS '记录创建时间';
COMMENT ON COLUMN t_rco_host_user.update_time IS '记录更新时间';

-- 云桌面会话信息表
CREATE TABLE IF NOT EXISTS  t_rco_desk_user_session (
        id uuid NOT NULL,
        desk_id uuid NOT NULL,
        terminal_id uuid DEFAULT NULL,
        user_id uuid NOT NULL,
        session_id int4 NULL,
        session_status varchar(32) DEFAULT 'ONLINE',
        "version" int4 NULL,
        last_create_time timestamp(6) NULL,
        last_idle_time timestamp(6) NULL,
        create_time timestamp(6) NULL,
        update_time timestamp(6) NULL,
        CONSTRAINT t_rco_desk_user_session_pkey PRIMARY KEY (id)
);
COMMENT ON COLUMN t_rco_desk_user_session.id IS '记录唯一标识';
COMMENT ON COLUMN t_rco_desk_user_session.desk_id IS '云桌面ID';
COMMENT ON COLUMN t_rco_desk_user_session.terminal_id IS '终端ID';
COMMENT ON COLUMN t_rco_desk_user_session.user_id IS '用户ID';
COMMENT ON COLUMN t_rco_desk_user_session.session_id IS '会话ID';
COMMENT ON COLUMN t_rco_desk_user_session.session_status IS '会话状态：ONLINE(在线)、DESTROYING(注销中)';
COMMENT ON COLUMN t_rco_desk_user_session.last_create_time IS '最近会话建立时间';
COMMENT ON COLUMN t_rco_desk_user_session.last_idle_time IS '最近会话空闲开始时间';
COMMENT ON COLUMN t_rco_desk_user_session.create_time IS '记录创建时间';
COMMENT ON COLUMN t_rco_desk_user_session.update_time IS '记录更新时间';
COMMENT ON COLUMN t_rco_desk_user_session.last_create_time IS '最近会话建立时间';

