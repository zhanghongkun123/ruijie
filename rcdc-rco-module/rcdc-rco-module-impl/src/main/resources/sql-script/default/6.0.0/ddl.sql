--3.2.106
CREATE TABLE IF NOT EXISTS public.t_rco_image_type_support_osversion (
id uuid not null,
cbb_image_type varchar(8) not null,
os_type varchar(32) not null,
os_version text not null,
"version" int4 null
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_constraint
        WHERE conname = 'uk_t_rco_image_type_support_osversion'
        AND connamespace = (SELECT oid FROM pg_namespace WHERE nspname = 'public')
    ) THEN
        ALTER TABLE t_rco_image_type_support_osversion
        ADD CONSTRAINT uk_t_rco_image_type_support_osversion UNIQUE(cbb_image_type,os_type,os_version);
    END IF;
END
$$;

--3.2.107
CREATE TABLE IF NOT EXISTS public.t_rco_image_type_support_terminal (
id uuid not null,
cbb_image_type varchar(8) not null,
os_type varchar(32) not null,
product_type text not null,
"version" int4 null
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_t_rco_image_type_support_terminal'
        AND connamespace = (SELECT oid FROM pg_namespace WHERE nspname = 'public')
    ) THEN
        ALTER TABLE t_rco_image_type_support_terminal
        ADD CONSTRAINT uk_t_rco_image_type_support_terminal UNIQUE(cbb_image_type,os_type,product_type);
    END IF;
END
$$;

--3.2.111
ALTER TABLE public.t_rco_image_download_state ADD COLUMN IF NOT EXISTS  terminal_download_finish_time timestamp(6) NULL ;
COMMENT ON COLUMN public.t_rco_image_download_state.terminal_download_finish_time IS '终端下载完成时间';

--3.2.112
-- 将文件分发结果中的message字段修改为text类型
ALTER TABLE t_rco_distribute_sub_task ALTER COLUMN message TYPE TEXT;
