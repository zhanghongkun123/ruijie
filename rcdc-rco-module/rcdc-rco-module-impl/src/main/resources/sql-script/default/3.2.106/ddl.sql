CREATE TABLE IF NOT EXISTS public.t_rco_image_type_support_osversion (
id uuid not null,
cbb_image_type varchar(8) not null,
os_type varchar(32) not null,
os_version text not null,
"version" int4 null
);

alter table t_rco_image_type_support_osversion add constraint uk_t_rco_image_type_support_osversion unique(cbb_image_type,os_type,os_version);