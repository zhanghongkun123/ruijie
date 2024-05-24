/**用户配置特殊配置表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_special_config (
	id uuid NOT NULL,
	config_version int8 NULL,
	config_content text NOT NULL,
	config_md5 varchar(256) NOT NULL,
	file_name varchar(1024) NULL,
	create_time timestamp(6) NULL,
	version int4 DEFAULT 0 NOT NULL,
	CONSTRAINT t_rco_user_profile_special_config_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_special_config.id IS '用户特殊配置ID';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_version IS '特殊配置版本号)';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_content IS '特殊配置表内容';
COMMENT ON COLUMN t_rco_user_profile_special_config.config_md5 IS '针对content的md5值';
COMMENT ON COLUMN t_rco_user_profile_special_config.file_name IS '配置文件名';
COMMENT ON COLUMN t_rco_user_profile_special_config.create_time IS '创建时间';

CREATE INDEX if NOT EXISTS t_rco_user_profile_special_config_id_index ON t_rco_user_profile_special_config USING btree (id);

/**缓存下发失败的清理路径请求*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_fail_clean_request
(
    id          uuid           NOT NULL,
    desktop_id     uuid           NOT NULL,
    type        varchar(16)    NOT NULL,
    path        text           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_fail_clean_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.id IS '失败请求ID';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.desktop_id IS '云桌面ID';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.path IS '路径';
COMMENT ON COLUMN t_rco_user_profile_fail_clean_request.type IS '类型(文件夹/文件/注册表)';

CREATE INDEX if NOT EXISTS t_rco_user_profile_fail_clean_request_id_index ON t_rco_user_profile_fail_clean_request USING btree (id);