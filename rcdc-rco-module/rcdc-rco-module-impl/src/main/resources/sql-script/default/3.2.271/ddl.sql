ALTER TABLE t_rco_user_hardware_certification DROP COLUMN IF EXISTS feature_code;

ALTER TABLE t_rco_user_hardware_certification ALTER COLUMN terminal_id DROP NOT NULL;