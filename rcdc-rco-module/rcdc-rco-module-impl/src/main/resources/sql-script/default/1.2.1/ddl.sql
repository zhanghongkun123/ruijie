/* license授权产品类型 */
CREATE TABLE t_rco_license_product_type (
id uuid NOT NULL,
feature_id varchar(64) COLLATE "default" NOT NULL,
feature_code varchar(255) COLLATE "default" NOT NULL,
license_num int4,
create_time timestamp(6) NOT NULL,
version int4 DEFAULT 0 NOT NULL,
CONSTRAINT t_rco_license_product_type_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_license_product_type.id IS 'ID';
COMMENT ON COLUMN t_rco_license_product_type.feature_id IS '产品类型编号';
COMMENT ON COLUMN t_rco_license_product_type.feature_code IS '产品的控制项信息';
COMMENT ON COLUMN t_rco_license_product_type.license_num IS '可授权数量';
COMMENT ON COLUMN t_rco_license_product_type.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_license_product_type.version IS '版本号';

DROP TABLE IF EXISTS t_rco_global_parameter;
CREATE TABLE t_rco_global_parameter (
  "id" uuid NOT NULL,
  "param_key" varchar(1024) COLLATE "default" NOT NULL,
  "param_value" varchar(4096) COLLATE "default",
  "default_value" varchar(4096) COLLATE "default",
  "create_time" date NOT NULL,
  "update_time" date NOT NULL,
  "version" int4 NOT NULL DEFAULT 0,
  CONSTRAINT "t_rco_global_parameter_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "rco_param_key_unique" UNIQUE ("param_key")
);
