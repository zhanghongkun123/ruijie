-- 增加开机模式
ALTER TABLE t_rco_user_terminal ADD COLUMN IF NOT EXISTS boot_type VARCHAR(32) DEFAULT 'uefi';
COMMENT ON COLUMN t_rco_user_terminal.boot_type IS 'TCI开机模式';