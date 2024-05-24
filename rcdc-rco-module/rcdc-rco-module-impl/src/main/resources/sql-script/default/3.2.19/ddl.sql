CREATE TABLE IF NOT EXISTS t_rco_terminal_online_time_record (
  id uuid NOT NULL,
  terminal_id varchar(64) NOT NULL,
  mac_addr varchar(64)  NOT NULL,
  platform varchar(64),
  last_online_time TIMESTAMP(6),
  online_total_time BIGINT,
  has_online bool,
  create_time timestamp(6),
  update_time timestamp(6),
  version int4 NOT NULL DEFAULT 0,
  CONSTRAINT t_rco_terminal_online_time_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_terminal_online_time_record.id IS 'id';

COMMENT ON COLUMN t_rco_terminal_online_time_record.terminal_id IS '终端id';

COMMENT ON COLUMN t_rco_terminal_online_time_record.mac_addr IS '终端mac';

COMMENT ON COLUMN t_rco_terminal_online_time_record.platform IS '终端类型';

COMMENT ON COLUMN t_rco_terminal_online_time_record.last_online_time IS '终端上线时间';

COMMENT ON COLUMN t_rco_terminal_online_time_record.online_total_time IS '在线总时长';

COMMENT ON COLUMN t_rco_terminal_online_time_record.has_online IS '终端是否在线';

COMMENT ON COLUMN t_rco_terminal_online_time_record.create_time IS '创建时间';

COMMENT ON COLUMN t_rco_terminal_online_time_record.update_time IS '更新时间';

COMMENT ON COLUMN t_rco_terminal_online_time_record.version IS '版本号';