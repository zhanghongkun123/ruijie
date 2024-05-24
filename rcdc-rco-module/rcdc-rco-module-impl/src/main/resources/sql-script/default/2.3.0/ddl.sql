CREATE TABLE IF not EXISTS t_rco_global_parameter (
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
-- 创建用户桌面配置表
CREATE TABLE t_rco_user_desktop_config (
id uuid NOT NULL,
user_id uuid NOT NULL,
image_template_id uuid,
strategy_id uuid,
network_id uuid,
desk_type varchar(32)  NOT NULL,
version int4,
create_time timestamp(6) NOT NULL,
CONSTRAINT t_rco_user_desktop_config_pkey PRIMARY KEY (id)
);

-- 创建用户组桌面配置表
CREATE TABLE t_rco_user_group_desktop_config (
id uuid NOT NULL,
group_id uuid NOT NULL,
image_template_id uuid NOT NULL,
strategy_id uuid NOT NULL,
network_id uuid,
desk_type varchar(32)  NOT NULL,
version int4,
create_time timestamp(6) NOT NULL,
CONSTRAINT t_rco_user_group_desktop_config_pkey PRIMARY KEY (id)
);

-- 创建云桌面策略推荐表
CREATE TABLE t_rco_user_desk_strategy_recommend (
"id" uuid NOT NULL,
"create_time" timestamp(6) NOT NULL,
"name" varchar(32) NOT NULL,
"pattern" varchar(32) NOT NULL,
"cpu" int4,
"memory" int4,
"system_size" int4 NOT NULL,
"personal_size" int4,
"is_allow_internet" bool DEFAULT true NOT NULL,
"is_open_usb_read_only" bool DEFAULT false NOT NULL,
"is_show" bool DEFAULT true NOT NULL,
"version" int4 DEFAULT 0 NOT NULL,
"clip_board_mode" varchar(64) DEFAULT 'NO_LIMIT'::character varying,
"is_open_double_screen" bool DEFAULT false,
"cloud_desk_type" varchar(8),
"is_allow_local_disk" bool,
"is_open_desktop_redirect" bool DEFAULT false,
CONSTRAINT "t_rco_user_desk_strategy_recommend_pkey" PRIMARY KEY ("id")
);

-- 创建访客用户云桌面配置表
CREATE TABLE t_rco_user_desktop_visitor_config (
"user_id" uuid NOT NULL,
"image_template_id" uuid NOT NULL,
"strategy_id" uuid NOT NULL,
"network_id" uuid NOT NULL,
"version" int4,
"user_name" varchar(64),
"create_time" timestamp(6),
CONSTRAINT "t_rco_user_desktop_visitor_config_pkey" PRIMARY KEY ("user_id")
);

-- 创建终端组配置表
CREATE TABLE t_rco_user_terminal_group_desk_config (
"cbb_terminal_group_id" uuid NOT NULL,
"cbb_idv_desktop_strategy_id" uuid,
"cbb_idv_desktop_image_id" uuid,
"version" int4 DEFAULT 0,
CONSTRAINT "t_rco_user_terminal_group_desk_config_pkey" PRIMARY KEY ("cbb_terminal_group_id")
);

-- 创建云桌面表
CREATE TABLE t_rco_user_desktop (
"id" uuid NOT NULL,
"cbb_desktop_id" uuid NOT NULL,
"has_login" bool DEFAULT false,
"version" int4 DEFAULT 0,
"create_time" timestamp(6),
"desktop_type" varchar(9),
"user_id" uuid,
"terminal_id" varchar(64),
"desktop_name" varchar(64),
"latest_login_time" timestamp(6),
"has_terminal_running" bool DEFAULT false,
CONSTRAINT "t_rco_user_desktop_pkey" PRIMARY KEY ("id")
);

-- 创建终端表
CREATE TABLE t_rco_user_terminal (
"id" uuid NOT NULL,
"terminal_id" varchar(64) NOT NULL,
"create_time" timestamp(6) NOT NULL,
"user_id" uuid,
"version" int4 DEFAULT 0 NOT NULL,
"has_login" bool,
"bind_user_id" uuid,
"bind_user_time" timestamp(6),
"enable_visitor_login" bool DEFAULT true,
"enable_auto_login" bool DEFAULT true,
"terminal_mode" varchar(32),
"bind_user_name" varchar(64),
"bind_desk_id" uuid,
CONSTRAINT "t_rco_user_terminal_pkey" PRIMARY KEY ("id")
);

-- 创建云桌面错误信息表
CREATE TABLE t_rco_desk_fault_info (
"id" uuid NOT NULL,
"desk_id" uuid NOT NULL,
"mac" varchar(64),
"fault_state" bool DEFAULT false NOT NULL,
"fault_description" varchar(128),
"create_time" timestamp(6),
"fault_time" timestamp(6),
"version" int4,
CONSTRAINT "t_rco_desk_fault_info_pkey" PRIMARY KEY ("id")
);

-- 创建用户消息表
CREATE TABLE t_rco_user_message (
"id" uuid NOT NULL,
"title" varchar(512),
"content" varchar(2000),
"version" int4,
"create_time" timestamp(6),
CONSTRAINT "t_rco_user_message_pkey" PRIMARY KEY ("id")
);

-- 创建消息发送用户表
CREATE TABLE t_rco_user_message_user (
"message_id" uuid NOT NULL,
"user_id" uuid NOT NULL,
"version" int4 DEFAULT 0,
"state" varchar(32) NOT NULL,
"id" uuid NOT NULL,
"user_type" varchar(32),
"desktop_id" uuid,
CONSTRAINT "t_rco_user_message_user_pkey" PRIMARY KEY ("id")
);
