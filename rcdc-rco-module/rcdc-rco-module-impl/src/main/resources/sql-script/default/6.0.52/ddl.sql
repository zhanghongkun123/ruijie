-- 云桌面策略推荐表 新增协议配置
ALTER TABLE t_rco_user_desk_strategy_recommend ADD COLUMN IF NOT EXISTS est_protocol_type varchar(16);
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.est_protocol_type IS '连接协议';

ALTER TABLE t_rco_user_desk_strategy_recommend ADD COLUMN IF NOT EXISTS agreement_info text;
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.agreement_info IS '连接协议配置内容';

ALTER TABLE t_rco_user_desk_strategy_recommend ADD COLUMN IF NOT EXISTS enable_transparent_encrypt bool DEFAULT false;
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.enable_transparent_encrypt IS '是否开启透明加解密';

-- 桌面规格
ALTER TABLE t_rco_user_group_desktop_config add COLUMN IF NOT EXISTS desk_spec_id uuid;
COMMENT ON COLUMN "t_rco_user_group_desktop_config"."desk_spec_id" IS '桌面规格';

-- 用户会话记录表
CREATE TABLE IF NOT EXISTS public.t_rco_user_session (
	id uuid NOT NULL,
	terminal_id varchar(64) NOT NULL,
	terminal_type varchar(32) NOT NULL,
	user_id uuid NOT NULL,
	resource_id uuid NOT NULL,
	resource_type varchar(32) NOT NULL,
	"version" int4 NOT NULL,
	create_time timestamp(6) NOT NULL,
	update_time timestamp(6) NULL,
	CONSTRAINT pk_t_rco_user_session PRIMARY KEY (id)
);

comment on column t_rco_user_session.id is '会话id';
comment on column t_rco_user_session.terminal_id is '终端ID（SHINE、OC终端表ID；WEB_CLIENT接入集群ID）';
comment on column t_rco_user_session.terminal_type is '终端类型（VDI、APP、WEB_CLIENT）';
comment on column t_rco_user_session.user_id is '用户ID';
comment on column t_rco_user_session.resource_id is '会话资源ID（云桌面会话为deskId、云应用会话为应用主机hostId）';
comment on column t_rco_user_session.resource_type is '会话资源类型（DESK 云桌面、APP 云应用）';
comment on column t_rco_user_session.create_time is '创建时间';
comment on column t_rco_user_session.update_time is '修改时间';
comment on column t_rco_user_session.version is '版本号';

CREATE INDEX IF NOT EXISTS t_rco_user_session_user_id_index ON t_rco_user_session (user_id);
CREATE INDEX IF NOT EXISTS t_rco_user_session_terminal_id_index ON t_rco_user_session (terminal_id);
CREATE INDEX IF NOT EXISTS t_rco_user_session_terminal_id_type_index ON t_rco_user_session (terminal_id, terminal_type);
CREATE INDEX IF NOT EXISTS t_rco_user_session_resource_id_index ON t_rco_user_session (resource_id);
CREATE INDEX IF NOT EXISTS t_rco_user_session_resource_id_type_index ON t_rco_user_session (resource_id, resource_type);

CREATE TABLE IF NOT EXISTS public.t_rco_user_license (
	id uuid NOT NULL,
	user_id uuid NOT NULL,
	auth_mode varchar(64) NOT NULL,
	license_type varchar(32) NOT NULL,
	license_duration varchar(32) NOT NULL,
	"version" int4 NOT NULL,
	create_time timestamp(6) NOT NULL,
	update_time timestamp(6) NULL,
	CONSTRAINT pk_t_rco_user_license PRIMARY KEY (id)
);

comment on column t_rco_user_license.id is '会话id';
comment on column t_rco_user_license.user_id is '用户ID';
comment on column t_rco_user_license.auth_mode is '授权模式';
comment on column t_rco_user_license.license_type is 'VDI证书类型，允许值: "VDI_1", "VDI_2"';
comment on column t_rco_user_license.license_duration is '证书持续类型：长期、临时';
comment on column t_rco_user_license.create_time is '创建时间';
comment on column t_rco_user_license.update_time is '修改时间';
comment on column t_rco_user_license.version is '版本号';

CREATE INDEX IF NOT EXISTS t_rco_user_license_user_id_index ON t_rco_user_license (user_id);
