-- 终端极简部署配置
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
( 'ae5f5ef7-0a68-4cc3-afc0-6d3beefd13f1', 'terminal_simplify_deployment_config', '{"enableTerminalSimplifyDeployment":false}', '{"enableTerminalSimplifyDeployment":false}', now(), now(), 0 );