/* 外系统映射表 */
DROP TABLE IF EXISTS t_rco_system_business_mapping;
CREATE TABLE t_rco_system_business_mapping
(
    id            uuid           NOT NULL,
    system_type   text           NOT NULL,
    business_type text           NOT NULL,
    src_id        text           NOT NULL,
    dest_id       text           NULL,
    context       text           NULL,
    create_date   timestamp(6)   NOT NULL,
    update_date   timestamp(6)   NOT NULL,
    version       int4 DEFAULT 0 NOT NULL,
    CONSTRAINT "t_rco_system_business_mapping_pkey" PRIMARY KEY ("id")
);

create index if not exists index_t_rco_system_business_mapping_1 on t_rco_system_business_mapping (system_type, business_type, src_id);

COMMENT ON COLUMN t_rco_system_business_mapping.id IS '主键标识';
COMMENT ON COLUMN t_rco_system_business_mapping.system_type IS '系统类型';
COMMENT ON COLUMN t_rco_system_business_mapping.business_type IS '业务类型';
COMMENT ON COLUMN t_rco_system_business_mapping.src_id IS '源业务标识';
COMMENT ON COLUMN t_rco_system_business_mapping.dest_id IS '目标业务标识';
COMMENT ON COLUMN t_rco_system_business_mapping.create_date IS '创建时间';
COMMENT ON COLUMN t_rco_system_business_mapping.update_date IS '更新时间';
COMMENT ON COLUMN t_rco_system_business_mapping.version IS '版本号';


DROP INDEX IF EXISTS t_cbb_desk_strategy_query_page_idx;
CREATE INDEX "t_cbb_desk_strategy_query_page_idx" ON "public"."t_cbb_desk_strategy" USING btree (
  "pattern" ,
  "system_size" ,
  "is_allow_local_disk" ,
  "strategy_type",
  "name"
);
