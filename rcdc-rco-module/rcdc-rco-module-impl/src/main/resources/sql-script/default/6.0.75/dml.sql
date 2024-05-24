---迁移t_rco_global_parameter的字段app_one_click_install到t_sk_global_parameter，如果存在的话
do $$
begin
	if exists(select * from information_schema.tables where table_name = 't_rco_global_parameter')
	then
	    if exists(select * from t_rco_global_parameter where param_key = 'app_one_click_install')
	    then
		insert into t_sk_global_parameter (id,param_key,param_value,default_value,create_time,update_time,"version")
            select id,param_key ,param_value ,default_value ,create_time ,update_time ,"version" from t_rco_global_parameter
            where param_key = 'app_one_click_install' and not exists (select * from t_sk_global_parameter where param_key = 'app_one_click_install');
        end if;
    end if;
end $$
;