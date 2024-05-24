ALTER TABLE public.t_rco_user_profile_path ADD COLUMN IF NOT EXISTS extra_config_info text;
COMMENT ON COLUMN public.t_rco_user_profile_path.extra_config_info IS '额外参数配置';
ALTER TABLE public.t_rco_image_download_state ADD COLUMN IF NOT EXISTS image_recovery_point_id varchar(255) NULL;
COMMENT ON COLUMN t_rco_image_download_state.image_recovery_point_id IS '镜像还原点';