-- 增加云桌面策略、网络策略的自增序列
create sequence if not exists sequence_global_business_increment increment 1 minvalue 1 maxvalue 99999999 start 1 cache 1 cycle;
CREATE INDEX IF NOT EXISTS "index_t_rco_system_business_mapping_create_date" ON "t_rco_system_business_mapping" ("create_date" ASC);