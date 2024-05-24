ALTER TABLE t_rco_admin_data_permission ALTER COLUMN permission_data_type TYPE TEXT;
ALTER TABLE t_rco_terminal_work_mode_mapping ADD COLUMN if not exists platform varchar default NULL ;
