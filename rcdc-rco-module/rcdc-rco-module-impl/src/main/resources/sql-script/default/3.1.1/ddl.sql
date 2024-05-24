-- 策略属性附加表
CREATE TABLE "public"."t_rco_strategy_additional_attribute" (
  "id" uuid NOT NULL,
  "strategy_id" uuid NOT NULL,
  "strategy_type" varchar(32) NOT NULL,
  "strategy_key" varchar(32) NOT NULL,
  "strategy_value" text,
  "create_date" timestamp(6) NOT NULL,
  "update_date" timestamp(6) NOT NULL,
  "version" int4 NOT NULL DEFAULT 0,
  CONSTRAINT t_rco_strategy_additional_attribute_pkey PRIMARY KEY (id)
)
;

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."id" IS '主键id';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."strategy_id" IS '关联标识';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."strategy_type" IS '策略类型';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."strategy_key" IS '策略键';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."strategy_value" IS '策略值';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."create_date" IS '创建时间';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."update_date" IS '更新时间';

COMMENT ON COLUMN "public"."t_rco_strategy_additional_attribute"."version" IS '版本号';

--终端特征码关联表
CREATE TABLE "t_rco_terminal_feature_code" (
  "id" uuid NOT NULL,
  "terminal_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "feature_code" varchar(32) NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "version" int4 NOT NULL DEFAULT 0,
  CONSTRAINT "t_rco_terminal_feature_code_pkey" PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "t_rco_terminal_feature_code_terminal_id_index" ON "t_rco_terminal_feature_code" USING btree (
  "terminal_id"
);
CREATE UNIQUE INDEX "t_rco_terminal_feature_code_feature_code_index" ON "t_rco_terminal_feature_code" USING btree (
  "feature_code"
);
COMMENT ON COLUMN "t_rco_terminal_feature_code"."terminal_id" IS '终端唯一标识,由终端主动上报，约定为第一张网卡的mac地址';
COMMENT ON COLUMN "t_rco_terminal_feature_code"."feature_code" IS '特征码';
COMMENT ON COLUMN "t_rco_terminal_feature_code"."create_time" IS '生成时间';
COMMENT ON COLUMN "t_rco_terminal_feature_code"."version" IS '版本号';
COMMENT ON TABLE "t_rco_terminal_feature_code" IS '终端特征码关联表';

--硬件认证-用户硬件特征码审批记录表
CREATE TABLE "t_rco_user_hardware_certification" (
  "id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "terminal_id" varchar(32) NOT NULL,
  "state" varchar(32) NOT NULL,
  "mac_addr" varchar(64) NOT NULL,
  "feature_code" varchar(32) NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "version" int4,
  PRIMARY KEY ("id"),
  CONSTRAINT "t_rco_user_hardware_certification_user_id_terminal_id_unique" UNIQUE ("user_id", "terminal_id")
);
CREATE INDEX "t_rco_user_hardware_certification_feature_code_index" ON "t_rco_user_hardware_certification" USING btree (
  "feature_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON COLUMN "t_rco_user_hardware_certification"."user_id" IS '用户ID';
COMMENT ON COLUMN "t_rco_user_hardware_certification"."terminal_id" IS '终端上报的终端ID,t_cbb_terminal的terminal_id';
COMMENT ON COLUMN "t_rco_user_hardware_certification"."state" IS '状态';
COMMENT ON COLUMN "t_rco_user_hardware_certification"."mac_addr" IS 'mac地址';
COMMENT ON COLUMN "t_rco_user_hardware_certification"."feature_code" IS '硬件特征码';

--身份验证配置表（t_rco_user_identity_config）修改
ALTER TABLE "public"."t_rco_user_identity_config" ADD COLUMN "open_hardware_certification" bool,
ADD COLUMN "max_hardware_num" int4;
COMMENT ON COLUMN "public"."t_rco_user_identity_config"."open_hardware_certification" IS '开启硬件特征码认证';
COMMENT ON COLUMN "public"."t_rco_user_identity_config"."max_hardware_num" IS '可绑定最大终端数';
ALTER TABLE "public"."t_rco_user_identity_config" ADD COLUMN "open_otp_certification" bool default false;
COMMENT ON COLUMN "public"."t_rco_user_identity_config"."open_otp_certification" IS '开启动态口令认证';
ALTER TABLE "public"."t_rco_user_identity_config" ADD COLUMN "has_bind_otp" bool default false;
comment ON COLUMN "public"."t_rco_user_identity_config"."has_bind_otp" IS 'OTP计算密钥是否进行绑定 默认false';
ALTER TABLE "public"."t_rco_user_identity_config" ADD COLUMN "otp_secret_key" varchar(64);
comment ON COLUMN "public"."t_rco_user_identity_config"."otp_secret_key" IS 'OTP计算密钥';


-- 管理员数据权限关联关系表
CREATE TABLE "public"."t_rco_admin_data_permission" (
  "id" uuid NOT NULL,
  "admin_id" uuid NOT NULL,
  "permission_data_id" varchar(64)  NOT NULL,
  "permission_data_type" varchar(16) NOT NULL,
  "create_date" timestamp(6) NOT NULL,
  "update_date" timestamp(6) NOT NULL,
  "version" int4 NOT NULL DEFAULT 0,
  CONSTRAINT t_rco_admin_data_permission_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."admin_id" IS '管理员标识';
COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."permission_data_id" IS '数据权限标识';
COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."permission_data_type" IS '数据权限类型';
COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."permission_data_type" IS '数据权限类型';
COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."create_date" IS '创建时间';
COMMENT ON COLUMN "public"."t_rco_admin_data_permission"."update_date" IS '更新时间';

DROP TABLE IF EXISTS t_rco_desksoft_use_record;
CREATE TABLE t_rco_desksoft_use_record (
                                           id uuid NOT NULL,
                                           soft_version text,
                                           name text,
                                           version int4 DEFAULT 0,
                                           company_name text,
                                           file_path text,
                                           operate_times int,
                                           update_time bigint,
                                           CONSTRAINT t_rco_desksoft_use_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desksoft_use_record.id IS '主键id';
COMMENT ON COLUMN t_rco_desksoft_use_record.soft_version IS '软件版本号';
COMMENT ON COLUMN t_rco_desksoft_use_record.name IS '软件名';
COMMENT ON COLUMN t_rco_desksoft_use_record.company_name IS '公司名';
COMMENT ON COLUMN t_rco_desksoft_use_record.file_path IS '软件文件路径';
COMMENT ON COLUMN t_rco_desksoft_use_record.operate_times IS '操作次数';
COMMENT ON COLUMN t_rco_desksoft_use_record.update_time IS '更新时间';

DROP TABLE IF EXISTS "public"."t_rcc_customer_info";
CREATE TABLE "public"."t_rcc_customer_info" (
                                                "id" uuid not null,
                                                "customer_name" varchar(32) COLLATE "pg_catalog"."default",
                                                "customer_type" varchar(16) COLLATE "pg_catalog"."default",
                                                "province" varchar(32) COLLATE "pg_catalog"."default",
                                                "city" varchar(32) COLLATE "pg_catalog"."default",
                                                "area" varchar(32) COLLATE "pg_catalog"."default",
                                                "edb" varchar(32) COLLATE "pg_catalog"."default",
                                                "channel_company" varchar(32) COLLATE "pg_catalog"."default",
                                                "channel_contact_details" varchar(32) COLLATE "pg_catalog"."default",
                                                "maintenance_personnel" varchar(32) COLLATE "pg_catalog"."default",
                                                "contact_details" varchar(32) COLLATE "pg_catalog"."default",
                                                "version" int4 NOT NULL DEFAULT 0,
                                                CONSTRAINT t_rcc_customer_info_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS  "public"."t_rcc_increment_mark";
CREATE TABLE "public"."t_rcc_increment_mark" (
                                                 "id" uuid not null,
                                                 "item_key" varchar(64) COLLATE "pg_catalog"."default",
                                                 "mark" varchar(64) COLLATE "pg_catalog"."default",
                                                 "version" int4 NOT NULL DEFAULT 0,
                                                 CONSTRAINT t_rcc_data_collect_mark_pKey PRIMARY KEY (id)
);

-- 新建权限表
CREATE TABLE "t_rco_authentication" (
                                        "id" uuid NOT NULL,
                                        "resource_id" uuid NOT NULL,
                                        "type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
                                        "lock" bool NOT NULL,
                                        "lock_time" timestamp(6),
                                        "unlock_time" timestamp(6),
                                        "pwd_error_times" int4 DEFAULT 0,
                                        "last_login_time" timestamp(6),
                                        "update_password_time" timestamp(6),
                                        "create_time" timestamp(6),
                                        "update_time" timestamp(6),
                                        "version" int4,
                                        CONSTRAINT "t_rco_authentication_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "t_rco_authentication"."id" IS 'id';
COMMENT ON COLUMN "t_rco_authentication"."resource_id" IS '用户、管理员或者终端id';
COMMENT ON COLUMN "t_rco_authentication"."type" IS '类型，ADMIN为管理员，USER为用户，TERMINAL为终端管理密码';
COMMENT ON COLUMN "t_rco_authentication"."lock" IS '是否锁定，默认false';
COMMENT ON COLUMN "t_rco_authentication"."lock_time" IS '锁定时间';
COMMENT ON COLUMN "t_rco_authentication"."unlock_time" IS '解锁时间';
COMMENT ON COLUMN "t_rco_authentication"."pwd_error_times" IS '密码输错次数';
COMMENT ON COLUMN "t_rco_authentication"."last_login_time" IS '上一次登录时间';
COMMENT ON COLUMN "t_rco_authentication"."update_password_time" IS '密码修改时间';
COMMENT ON COLUMN "t_rco_authentication"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_rco_authentication"."update_time" IS '修改时间';

-- VDI IP限制表
CREATE TABLE "t_rco_ip_limit" (
  "id" uuid NOT NULL,
  "ip_start" varchar(32) NOT NULL,
  "ip_end" varchar(32) NOT NULL,
  "create_time" date,
  "update_time" date,
  "version" int4 DEFAULT 0,
  CONSTRAINT "t_rco_ip_limit_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "t_rco_ip_limit"."id" IS 'id';

COMMENT ON COLUMN "t_rco_ip_limit"."ip_start" IS 'IP段起始位置';

COMMENT ON COLUMN "t_rco_ip_limit"."ip_end" IS 'IP段结束位置';

COMMENT ON COLUMN "t_rco_ip_limit"."create_time" IS '创建时间';

COMMENT ON COLUMN "t_rco_ip_limit"."update_time" IS '修改时间';

-- 修改产品id的字符长度
ALTER TABLE t_rco_terminal_driver_config ALTER COLUMN product_id TYPE text