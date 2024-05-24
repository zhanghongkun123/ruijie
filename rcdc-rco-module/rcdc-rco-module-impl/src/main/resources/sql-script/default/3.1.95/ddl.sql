-- 增加目标业务标识索引
create index if not exists index_t_rco_system_business_mapping_2 on t_rco_system_business_mapping (system_type, business_type, dest_id);