/* 添加fdw插件 */
create extension IF NOT EXISTS postgres_fdw;
/* 添加身份中心数据库服务 */
CREATE SERVER gss_default_server FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'rccpcangjie', port '5432', dbname 'gss_default');

/* 添加用于访问身份中心数据库的用户信息 TODO 待数据库升级组件支持密码密文后，密码需要替换成密文 */
CREATE USER MAPPING FOR postgres SERVER gss_default_server
    OPTIONS (user 'gssuser', password 'ENC(wP72txlb2Z79IJFTNQLxz7VEbYNRtKGvWOCjgYInKNg=)');
CREATE USER MAPPING FOR rcdcuser SERVER gss_default_server
    OPTIONS (user 'gssuser', password 'ENC(wP72txlb2Z79IJFTNQLxz7VEbYNRtKGvWOCjgYInKNg=)');
/* 将gss_default数据库的表映射到rcdc_defalut数据库中 */
IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_user)
    FROM SERVER gss_default_server INTO public;
IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_user_group)
    FROM SERVER gss_default_server INTO public;
IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_user_identity_config)
    FROM SERVER gss_default_server INTO public;
IMPORT FOREIGN SCHEMA public LIMIT TO (t_base_iac_ad_group)
        FROM SERVER gss_default_server INTO public;
/* 授予rcdcuser用户外部表查询权限 */
GRANT SELECT ON t_base_iac_user TO rcdcuser;
GRANT SELECT ON t_base_iac_user_group TO rcdcuser;
GRANT SELECT ON t_base_iac_user_identity_config TO rcdcuser;
GRANT SELECT ON t_base_iac_ad_group TO rcdcuser;