ALTER TABLE public.t_rco_image_download_state ADD COLUMN IF NOT EXISTS  terminal_download_finish_time timestamp(6) NULL ;
COMMENT ON COLUMN public.t_rco_image_download_state.terminal_download_finish_time IS '终端下载完成时间';
