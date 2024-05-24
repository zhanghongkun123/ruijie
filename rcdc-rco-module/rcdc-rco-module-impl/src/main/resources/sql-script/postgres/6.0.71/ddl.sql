IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_user_hardware_certification)
        FROM SERVER gss_default_server INTO public;

IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_lock_info)
        FROM SERVER gss_default_server INTO public;

GRANT SELECT ON t_base_iac_user_hardware_certification TO rcdcuser;
GRANT SELECT ON t_base_iac_lock_info TO rcdcuser;

ALTER FOREIGN TABLE t_base_iac_user
ADD COLUMN IF NOT EXISTS last_login_time timestamp(6);

ALTER FOREIGN TABLE t_base_iac_user
 ADD COLUMN IF NOT EXISTS update_password_time timestamp(6);

ALTER FOREIGN TABLE t_base_iac_lock_info
ADD COLUMN IF NOT EXISTS lock_time timestamp(6);

ALTER FOREIGN TABLE t_base_iac_lock_info
ADD COLUMN IF NOT EXISTS is_lock bool;
