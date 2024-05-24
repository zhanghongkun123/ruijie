CREATE TABLE IF NOT EXISTS public.t_rco_image_type_support_terminal (
id uuid not null,
cbb_image_type varchar(8) not null,
os_type varchar(32) not null,
product_type text not null,
"version" int4 null
);

alter table t_rco_image_type_support_terminal add constraint uk_t_rco_image_type_support_terminal unique(cbb_image_type,os_type,product_type);