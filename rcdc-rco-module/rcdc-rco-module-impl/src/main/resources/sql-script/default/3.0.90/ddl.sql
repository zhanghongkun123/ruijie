CREATE TABLE "t_rco_user_authentication" (
  "id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "is_lock" bool NOT NULL,
  "lock_time" timestamp(6),
  "unlock_time" timestamp(6),
  "pwd_error_times" int4 DEFAULT 0,
  "last_login_time" timestamp(6),
  "update_password_time" timestamp(6),
  "version" int4,
  CONSTRAINT "t_rco_user_authentication_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "t_rco_user_authentication_user_id_key" UNIQUE ("user_id")
);

COMMENT ON COLUMN "t_rco_user_authentication"."is_lock" IS '是否锁定，默认false';

COMMENT ON COLUMN "t_rco_user_authentication"."lock_time" IS '锁定时间';

COMMENT ON COLUMN "t_rco_user_authentication"."unlock_time" IS '解锁时间';

COMMENT ON COLUMN "t_rco_user_authentication"."pwd_error_times" IS '密码输错次数';

COMMENT ON COLUMN "t_rco_user_authentication"."last_login_time" IS '上一次登录时间';

COMMENT ON COLUMN "t_rco_user_authentication"."update_password_time" IS '密码修改时间';