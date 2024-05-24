ALTER TABLE t_rco_host_user ADD column IF NOT EXISTS  connect_closed_time timestamp(6);
COMMENT ON COLUMN t_rco_host_user.connect_closed_time IS '连接关闭时间';

ALTER TABLE t_rco_user_desktop ADD IF NOT EXISTS assignment_time timestamp(6);
COMMENT ON COLUMN t_rco_user_desktop.assignment_time IS '分配时间';