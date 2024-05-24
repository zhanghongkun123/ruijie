 -- 增加索引
CREATE INDEX IF NOT EXISTS "index_t_rco_authentication_resource_type" ON "t_rco_authentication" USING btree (
  "resource_id" ASC NULLS LAST,
  "type" ASC NULLS LAST
);