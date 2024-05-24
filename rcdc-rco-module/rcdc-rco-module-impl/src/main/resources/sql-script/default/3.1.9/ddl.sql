DROP INDEX IF EXISTS rcdc_t_rco_user_terminal_index;
CREATE INDEX rcdc_t_rco_user_terminal_index ON t_rco_user_terminal (terminal_id);