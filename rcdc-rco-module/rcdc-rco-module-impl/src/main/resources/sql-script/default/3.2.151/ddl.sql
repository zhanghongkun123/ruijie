CREATE TABLE IF NOT EXISTS t_rco_unified_manage_data (
  id uuid NOT NULL,
  related_id uuid NOT NULL,
  related_type varchar(64)  NOT NULL,
  unified_manage_data_id uuid NOT NULL,
  create_time timestamp(6),
  version int4 NOT NULL DEFAULT 0,
  CONSTRAINT t_rco_unified_manage_data_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_unified_manage_data.id IS 'id';

COMMENT ON COLUMN t_rco_unified_manage_data.related_id IS '关联ID';

COMMENT ON COLUMN t_rco_unified_manage_data.related_type IS '关联对象';

COMMENT ON COLUMN t_rco_unified_manage_data.unified_manage_data_id IS '统一管理数据唯一ID';
 
COMMENT ON COLUMN t_rco_unified_manage_data.create_time IS '创建时间';

COMMENT ON COLUMN t_rco_unified_manage_data.version IS '版本号';


/**rco文件流转审计申请表*/
CREATE TABLE IF NOT EXISTS t_rco_audit_apply
(
    id                uuid           NOT NULL,
    apply_serial_number varchar(32)  NOT NULL,
    apply_reason      varchar(1024),
    total_file_size   bigint,
    total_file_count  int4,
    state             varchar(32),
    alarm_id          uuid,
    user_id           uuid,
    user_name         varchar(256),
    desktop_id        uuid,
    desktop_name      varchar(64),
    desktop_mac       varchar(64),
    desktop_ip        varchar(64),
    terminal_id       varchar(32),
    terminal_name     varchar(32),
    terminal_ip       varchar(64),
    terminal_type     varchar(32),
    apply_type        varchar(32)    DEFAULT 'EXPORT',
    total_file_page   int4,
    terminal_mac      varchar(64),
    fail_reason       varchar(1024),
    create_time       timestamp(6)   NOT NULL,
    update_time       timestamp(6),
    version           int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_audit_apply_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_audit_apply.id IS '文件流转审计申请单ID';
COMMENT ON COLUMN t_rco_audit_apply.apply_serial_number IS '文件流转审计申请单号';
COMMENT ON COLUMN t_rco_audit_apply.apply_reason IS '申请导出理由';
COMMENT ON COLUMN t_rco_audit_apply.total_file_size IS '导出文件总大小';
COMMENT ON COLUMN t_rco_audit_apply.total_file_count IS '导出文件总数量';
COMMENT ON COLUMN t_rco_audit_apply.state IS '申请单状态（UPLOADING 待文件上传、COMPUTING 校验文件中、PENDING_APPROVAL 待审批、APPROVED 已批准、REJECTED 已驳回、CANCELED 已撤回）';
COMMENT ON COLUMN t_rco_audit_apply.alarm_id IS '申请单关联的告警记录';
COMMENT ON COLUMN t_rco_audit_apply.user_id IS '申请单关联用户ID';
COMMENT ON COLUMN t_rco_audit_apply.user_name IS '申请单关联用户名';
COMMENT ON COLUMN t_rco_audit_apply.desktop_id IS '申请单关联云桌面ID';
COMMENT ON COLUMN t_rco_audit_apply.desktop_name IS '申请单关联云桌面名';
COMMENT ON COLUMN t_rco_audit_apply.desktop_mac IS '申请单关联云桌面MAC地址';
COMMENT ON COLUMN t_rco_audit_apply.desktop_ip IS '申请单关联云桌面IP';
COMMENT ON COLUMN t_rco_audit_apply.terminal_id IS '申请单关联终端ID';
COMMENT ON COLUMN t_rco_audit_apply.terminal_name IS '申请单关联终端名';
COMMENT ON COLUMN t_rco_audit_apply.terminal_ip IS '申请单关联终端IP';
COMMENT ON COLUMN t_rco_audit_apply.terminal_type IS '申请单关联终端类型';
comment ON COLUMN t_rco_audit_apply.apply_type IS '申请单类型（EXPORT导出、PRINT打印、PRINTER添加打印机）';
comment ON COLUMN t_rco_audit_apply.total_file_page IS '文件总页数';
comment ON COLUMN t_rco_audit_apply.terminal_mac IS '终端MAC地址';
comment ON COLUMN t_rco_audit_apply.fail_reason IS '失败原因';
COMMENT ON COLUMN t_rco_audit_apply.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_audit_apply.update_time IS '更新时间';

CREATE UNIQUE INDEX IF NOT EXISTS idx_trafa_id ON t_rco_audit_apply USING btree (id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_trafa_apply_serial_number ON t_rco_audit_apply USING btree (apply_serial_number);
CREATE INDEX IF NOT EXISTS idx_trafa_user_id ON t_rco_audit_apply USING btree (user_id, user_name);
CREATE INDEX IF NOT EXISTS idx_trafa_desktop_id ON t_rco_audit_apply USING btree (desktop_id, desktop_name);
CREATE INDEX IF NOT EXISTS idx_trafa_terminal_id ON t_rco_audit_apply USING btree (terminal_id, terminal_name);

/**rco文件流转审计文件表*/
CREATE TABLE IF NOT EXISTS t_rco_audit_file
(
    id                uuid           NOT NULL,
    apply_id          uuid           NOT NULL,
    file_name         varchar(256)        NOT NULL,
    file_suffix       varchar(256),
    file_md5          varchar(32)        NOT NULL,
    file_size         bigint           NOT NULL,
    file_state             varchar(32)NOT NULL,
    file_client_storage_path             text,
    file_server_temp_path             varchar(512),
    file_server_storage_path             varchar(512),
    file_page         int4,
    retry_count       int4 DEFAULT 0,
    create_time       timestamp(6)   NOT NULL,
    update_time       timestamp(6),
    version           int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_audit_file_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_audit_file.id IS '文件ID';
COMMENT ON COLUMN t_rco_audit_file.apply_id IS '文件流转审计申请单ID';
COMMENT ON COLUMN t_rco_audit_file.file_name IS '文件名';
COMMENT ON COLUMN t_rco_audit_file.file_suffix IS '文件格式';
COMMENT ON COLUMN t_rco_audit_file.file_md5 IS '文件md5';
COMMENT ON COLUMN t_rco_audit_file.file_size IS '文件大小';
COMMENT ON COLUMN t_rco_audit_file.file_state IS '文件状态（NOT_NEED 无需上传、UPLOADING 待文件上传、COMPUTING 校验文件中、UPLOADED 文件上传完成、APPROVED 已批准、REJECTED 已驳回）';
COMMENT ON COLUMN t_rco_audit_file.file_client_storage_path IS '文件在客户端存储位置';
COMMENT ON COLUMN t_rco_audit_file.file_server_temp_path IS '文件在服务端临时存储位置';
COMMENT ON COLUMN t_rco_audit_file.file_server_storage_path IS '文件在服务端持久存储位置';
comment ON COLUMN t_rco_audit_file.file_page IS '文件页数';
comment ON COLUMN t_rco_audit_file.retry_count IS '重试次数';
COMMENT ON COLUMN t_rco_audit_file.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_audit_file.update_time IS '更新时间';

CREATE UNIQUE INDEX IF NOT EXISTS idx_traf_id ON t_rco_audit_file USING btree (id);
CREATE INDEX IF NOT EXISTS idx_traf_file_name ON t_rco_audit_file USING btree (file_name);
CREATE INDEX IF NOT EXISTS idx_traf_file_md5 ON t_rco_audit_file USING btree (file_md5);


/**rco文件流转审计申请审批记录表*/
CREATE TABLE IF NOT EXISTS t_rco_audit_apply_audit_log
(
    id                 uuid           NOT NULL,
    apply_id           uuid           NOT NULL,
    auditor_id         uuid,
    auditor_name       varchar(256),
    role_id         uuid,
    role_name       varchar(64),
    auditor_level      int4,
    auditor_state      varchar(32),
    auditor_opinion    varchar(1024),
    is_last_auditor    bool,
    create_time       timestamp(6)   NOT NULL,
    update_time       timestamp(6),
    version            int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_audit_apply_audit_log_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_audit_apply_audit_log.id IS '文件ID';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.apply_id IS '文件流转审计申请单ID';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.auditor_id IS '审批人ID';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.auditor_name IS '审批人名';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.role_id IS '审批角色ID';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.role_name IS '审批角色名';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.auditor_level IS '审批层级';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.auditor_state IS '审批状态(PENDING_APPROVAL 待审批、 APPROVED 已批准、 REJECTED 已驳回)';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.auditor_opinion IS '审批意见';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.is_last_auditor IS '是否最后一个审批节点';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_audit_apply_audit_log.update_time IS '更新时间';

CREATE UNIQUE INDEX IF NOT EXISTS idx_traf_id ON t_rco_audit_apply_audit_log USING btree (id);
CREATE INDEX IF NOT EXISTS idx_traf_auditor_id ON t_rco_audit_apply_audit_log USING btree (auditor_id);


CREATE TABLE IF NOT EXISTS "t_rco_audit_file_print_info" (
  "id" uuid NOT NULL,
  "file_id" uuid NOT NULL,
  "printer_name" varchar(256) NOT NULL,
  "printer_brand" varchar(256),
  "printer_model" varchar(256),
  "printer_sn" varchar(256),
  "print_process_name" varchar(256),
  "print_page_count" int4,
  "print_paper_size" varchar(32),
  "print_state" varchar(32) NOT NULL,
  "print_result_msg" varchar(1024),
  "print_time" timestamp(6),
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "version" int4,
  CONSTRAINT "t_rco_audit_file_print_info_key" PRIMARY KEY (id)
);

COMMENT ON COLUMN "t_rco_audit_file_print_info"."file_id" IS '文件记录ID';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."printer_name" IS '打印机名称';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."printer_brand" IS '打印机品牌名';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."printer_model" IS '打印机型号';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."printer_sn" IS '打印机序列码';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_process_name" IS '打印进程名';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_page_count" IS '打印页数';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_paper_size" IS '打印纸张大小';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_state" IS '打印状态';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_result_msg" IS '打印结果信息';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."print_time" IS '打印时间';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "t_rco_audit_file_print_info"."update_time" IS '更新时间';

-- 临时权限
CREATE TABLE IF NOT EXISTS t_rco_desktop_temp_permission_relation (
        id uuid NOT NULL,
        desktop_temp_permission_id uuid NOT NULL,
        related_id uuid NOT NULL,
        related_type varchar(64) NOT NULL,
        has_send_expire_notice bool,
        create_time timestamp(6) NOT NULL,
        version int4 NULL,
        CONSTRAINT t_rco_desk_temp_permission_relation_pkey PRIMARY KEY (id)
);
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.id IS '记录ID';
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.desktop_temp_permission_id IS '临时权限ID';
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.related_id IS '关联对象ID';
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.related_type IS '关联对象类型';
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.has_send_expire_notice IS '是否发送过到期通知';
COMMENT ON COLUMN t_rco_desktop_temp_permission_relation.create_time IS '创建时间';

-- 云桌面策略推荐表 新增支持传输的类型字段
ALTER TABLE t_rco_user_desk_strategy_recommend
ADD COLUMN IF NOT EXISTS clip_board_support_type varchar(256) DEFAULT '[{"type":"FILE"},{"type":"TEXT"}]';
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.clip_board_support_type IS '数据传输:支持传输的类型及限制';

-- 云桌面授权使用情况统计表
CREATE TABLE IF NOT EXISTS t_rco_desktop_license_stat (
  "id" uuid not null,
  "license_type" varchar(32),
  "used_count" int4 not null default 0,
  "total_count" int4 not null default 0,
  "vdi_used_count" int4 not null default 0,
  "idv_used_count" int4 not null default 0,
  "voi_used_count" int4 not null default 0,
  "rca_used_count" int4 not null default 0,
  "edu_voi_used_count" int4 not null default 0,
  "create_time" timestamp(6) default current_timestamp,
  "version" int4 not null default 0,
  constraint pk_t_rco_desktop_License_stat primary key (id)
);
comment on column t_rco_desktop_License_stat.id is '主键id';
comment on column t_rco_desktop_License_stat.license_type is '授权类型';
comment on column t_rco_desktop_License_stat.used_count is '使用的授权总数';
comment on column t_rco_desktop_License_stat.total_count is '授权总数，-1表示临时授权';
comment on column t_rco_desktop_License_stat.vdi_used_count is 'vdi使用的授权数';
comment on column t_rco_desktop_License_stat.idv_used_count is 'idv使用的授权数';
comment on column t_rco_desktop_License_stat.voi_used_count is 'voi使用的授权数';
comment on column t_rco_desktop_License_stat.rca_used_count is 'rca使用的授权数';
comment on column t_rco_desktop_License_stat.edu_voi_used_count is 'eduVoi使用的授权数';
comment on column t_rco_desktop_License_stat.create_time is '创建时间';
comment on column t_rco_desktop_License_stat.version is '版本号';

CREATE INDEX IF NOT EXISTS idx_t_rco_desktop_License_stat_create_time_index ON t_rco_desktop_License_stat USING btree(create_time);