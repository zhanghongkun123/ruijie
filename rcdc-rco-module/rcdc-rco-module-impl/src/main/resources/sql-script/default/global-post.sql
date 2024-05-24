-- 用户视图
create view v_rco_user as
  select
    u.id,
    u.user_role,
    u.name as user_name,
    u.group_id,
    g.name as group_name,
    u.real_name,
    u.user_type,
    u.password,
    u.phone_num,
    u.email,
    u.state,
    dt.desktop_num,
    u.create_time,
    u.update_time,
    u.version,
    u.ad_user_authority,
    u.need_update_password,
    u.account_expire_date,
    u.enable_domain_sync,
    u.invalid_time,
    u.invalid_recover_time,
    u.login_out_time,
    u.invalid,
    u.last_login_terminal_ip,
    u.last_login_terminal_time,
    u.is_user_modify_password,
    u.reset_password_by_admin,
    u.has_bind_domain_password,
    au.is_lock as lock,
    au.lock_time,
    au.lock_until_time as unlock_time,
    COALESCE(au.fail_count, 0)  as pwd_error_times,
    u.last_login_time,
    u.update_password_time,
    ( CASE WHEN uic.open_otp_certification IS NULL THEN FALSE ELSE uic.open_otp_certification END ) AS open_otp_certification,
	( CASE WHEN uic.open_radius_certification IS NULL THEN FALSE ELSE uic.open_radius_certification END) AS open_radius_certification,
	( CASE WHEN uic.has_bind_otp IS NULL THEN FALSE ELSE uic.has_bind_otp END ) AS has_bind_otp,
	(case when uic.open_cas_certification is null then false else uic.open_cas_certification end) as open_cas_certification,
	(case when uic.open_account_password_certification is null then false else uic.open_account_password_certification end) as open_account_password_certification,
	(case when uic.open_third_party_certification is null then false else uic.open_third_party_certification end) as open_third_party_certification,
	(case when uic.open_hardware_certification is null then false else uic.open_hardware_certification end) as
    open_hardware_certification,
    uic.max_hardware_num,
    ( CASE WHEN uic.open_sms_certification IS NULL THEN FALSE ELSE uic.open_sms_certification END ) AS open_sms_certification,
    ( CASE WHEN uic.open_work_weixin_certification IS NULL THEN FALSE ELSE uic.open_work_weixin_certification END ) AS open_work_weixin_certification,
    ( CASE WHEN uic.open_feishu_certification IS NULL THEN FALSE ELSE uic.open_feishu_certification END ) AS open_feishu_certification,
    ( CASE WHEN uic.open_dingding_certification IS NULL THEN FALSE ELSE uic.open_dingding_certification END ) AS open_dingding_certification,
    ( CASE WHEN uic.open_oauth2_certification IS NULL THEN FALSE ELSE uic.open_oauth2_certification END ) AS open_oauth2_certification,
    ( CASE WHEN uic.open_rjclient_certification IS NULL THEN FALSE ELSE uic.open_rjclient_certification END ) AS open_rjclient_certification,
    u.description as user_description,
    ul.auth_mode as auth_mode,
    ul.license_type as license_type,
    ul.license_duration as license_duration
  FROM t_base_iac_user u

    LEFT JOIN (
            select *
             from t_base_iac_user_group
            ) g ON u.group_id = g.id
    LEFT JOIN (
                select
                  count(*) as desktop_num,
                  ud.user_id
                from t_rco_user_desktop ud, (select *
                from t_cbb_desk_info
                        ) cbb_desk
                where ((ud.cbb_desktop_id = cbb_desk.desk_id) and (cbb_desk.is_delete = FALSE))
                group by ud.user_id
              ) dt ON dt.user_id = u.id
    LEFT JOIN t_base_iac_user_identity_config uic ON u.id = uic.related_id
    LEFT JOIN (
                select is_lock,lock_time,lock_until_time,fail_count,user_name
                from t_base_iac_lock_info
                where user_type = 'USER'
            ) AS au ON u.name = au.user_name
    LEFT JOIN t_rco_user_license ul ON ul.user_id = u.id;

-- 用户终端视图
create view v_cbb_user_terminal
  as
     select cbb.terminal_id,
    cbb.terminal_name,
    cbb.state as terminal_state,
    cbb.rain_os_version,
    cbb.hardware_version,
    cbb.rain_upgrade_version,
    cbb.last_online_time,
    cbb.last_offline_time,
    cbb.mac_addr,
    cbb.disk_size,
    cbb.sys_disk_sn,
    cbb.cpu_type,
    cbb.ip,
    cbb.memory_size,
    cbb.product_type,
    cbb.product_id,
    cbb.platform,
    cbb.terminal_os_type,
    cbb.serial_number,
    cbb.network_access_mode,
    cbb.subnet_mask,
    cbb.gateway,
    cbb.main_dns,
    cbb.second_dns,
    cbb.get_ip_mode,
    cbb.get_dns_mode,
    cbb.ssid,
    cbb.wireless_auth_mode,
    cbb.network_infos,
    cbb.wireless_net_card_num,
    cbb.ethernet_net_card_num,
    cbb.all_disk_info,
    cbb.authed,
    cbb.ocs_sn,
    cbb.enable_proxy,
    cbb.real_terminal_id,
    ut.enable_visitor_login,
    ut.enable_auto_login,
    ut.version,
    ut.id,
    ut.create_time,
    ut.terminal_mode as idv_terminal_mode,
    ut.bind_user_id,
    ut.bind_user_name,
    ut.bind_desk_id,
    cbb.group_id as terminal_group_id,
    ut.user_id,
    t_group.name as terminal_group_name,
    ut.has_login,
    (cbb.last_online_time > coalesce(cbb.last_offline_time, '1970-01-01 00:00:00'::timestamp without time zone)) AS has_online,
       CASE
           WHEN ((cbb.platform)::text = 'IDV'::text OR (cbb.platform)::text = 'VOI'::text) THEN ut.bind_desk_id
           WHEN (((cbb.platform)::text = 'VDI'::text) OR ((cbb.platform)::text = 'APP'::text))
               THEN u_desk.cbb_desktop_id
           ELSE NULL::uuid
           END                                                                                                      AS desktop_id,
       CASE
           WHEN ((cbb.platform)::text = 'IDV'::text OR (cbb.platform)::text = 'VOI'::text) THEN di.name
           WHEN (((cbb.platform)::text = 'VDI'::text) OR ((cbb.platform)::text = 'APP'::text)) THEN u_desk.desktop_name
           ELSE NULL::character varying
           END  AS desktop_name,
    di.desk_ip,
    td.person_size,
    di.desk_mac,
    ds.is_allow_local_disk,
    ds.pattern,
    td.system_size,
    uic.open_otp_certification,
    uic.has_bind_otp,
    it.image_template_name,
    it.id as image_id,
    it.os_type AS image_template_os_type,
    au.lock,
    au.lock_time,
    au.unlock_time,
    au.pwd_error_times,
    CASE WHEN cbb.platform = 'VOI' THEN ut.boot_type ELSE '' END AS boot_type,
    cbb.support_tc_start,
    cbb.support_remote_wake,
    -- BIOS、系统设置中有一个配置为关闭，则是关闭状态；其他都展示开启
    CASE WHEN cbb.bios_wake_up_status = 'CLOSE' OR cbb.system_wake_up_status = 'CLOSE'  THEN 'CLOSE' ELSE 'OPEN' END AS wake_up_status,
    CASE WHEN (cbb.platform = 'IDV' OR cbb.platform = 'VOI') AND ids.download_state IS NULL THEN 'NONE' ELSE ids.download_state END,
    ids.download_finish_time,
    fail_code,
    cbb.nic_work_mode,
    auth.auth_mode as auth_mode,
    case
        when auth.license_duration = 'TEMPORARY' THEN  'TEMPORARY'
        when auth.license_type = 'VOI_2' or auth.license_type = 'VOI_1' then 'VOI'
        when auth.license_type = 'VDI_1' or auth.license_type = 'VDI_2' then 'VDI'
        when auth.license_type = 'VOI_2,VOI_PLUS_UPGRADED' or auth.license_type = 'VOI_1,VOI_PLUS_UPGRADED' then 'VOI,VOI_PLUS_UPGRADED'
        else auth.license_type
    end as auth_type,
    CASE WHEN u_desk.enable_full_system_disk IS NOT NULL THEN u_desk.enable_full_system_disk
        WHEN ds.enable_full_system_disk IS NOT NULL THEN ds.enable_full_system_disk
        ELSE FALSE END AS enable_full_system_disk
   FROM ((((((( select cbb_1.id as real_terminal_id,
            cbb_1.terminal_name,
            cbb_1.terminal_id,
            cbb_1.mac_addr,
            cbb_1.ip,
            cbb_1.subnet_mask,
            cbb_1.gateway,
            cbb_1.main_dns,
            cbb_1.second_dns,
            cbb_1.get_ip_mode,
            cbb_1.get_dns_mode,
            cbb_1.product_type,
            cbb_1.terminal_type,
            cbb_1.serial_number,
            cbb_1.cpu_type,
            cbb_1.memory_size,
            cbb_1.disk_size,
            cbb_1.sys_disk_sn,
            cbb_1.terminal_os_type,
            cbb_1.terminal_os_version,
            cbb_1.rain_os_version,
            cbb_1.rain_upgrade_version,
            cbb_1.hardware_version,
            cbb_1.network_access_mode,
            cbb_1.create_time,
            cbb_1.last_online_time,
            cbb_1.last_offline_time,
            cbb_1.version,
            cbb_1.state,
            cbb_1.platform,
            cbb_1.group_id,
            cbb_1.os_inner_version,
            cbb_1.product_id,
            cbb_1.ssid,
            cbb_1.wireless_auth_mode,
            cbb_1.network_infos,
            cbb_1.wireless_net_card_num,
            cbb_1.ethernet_net_card_num,
            cbb_1.all_disk_info,
            cbb_1.authed,
            cbb_1.ocs_sn,
            case
            when (cbb_1.enable_proxy is null) then false
            else cbb_1.enable_proxy
            end as enable_proxy,
            cbb_1.support_tc_start,
            cbb_1.support_remote_wake,
            cbb_1.system_wake_up_status,
            cbb_1.bios_wake_up_status,
            cbb_1.nic_work_mode
           from t_cbb_terminal cbb_1) cbb
     JOIN t_rco_user_terminal ut ON (((cbb.terminal_id)::text = (ut.terminal_id)::text)))
     JOIN ( select t_cbb_terminal_group.id,
            t_cbb_terminal_group.name,
            t_cbb_terminal_group.parent_id,
            t_cbb_terminal_group.create_time,
            t_cbb_terminal_group.version
           from t_cbb_terminal_group) t_group ON ((cbb.group_id = t_group.id)))
     LEFT JOIN t_rco_user_desktop u_desk ON ((((u_desk.terminal_id)::text = (cbb.terminal_id)::text)
        AND (

            (
                (cbb.platform)::text != 'IDV'::text AND u_desk.has_terminal_running = true
            AND (u_desk.desktop_type)::text != 'IDV'::text
                )
           OR (
               (cbb.platform)::text != 'VOI'::text AND u_desk.has_terminal_running = true
            AND (
                u_desk.desktop_type)::text != 'IDV'::text
               )

            OR (
                ((cbb.platform)::text = 'IDV'::text)
                AND ((u_desk.desktop_type)::text = 'IDV'::text)
                )
             OR (
                ((cbb.platform)::text = 'VOI'::text)
                AND ((u_desk.desktop_type)::text = 'IDV'::text)
                )

            )

          )))
     LEFT JOIN ( select t_cbb_desk_info.desk_id,
            t_cbb_desk_info.desk_mac,
            t_cbb_desk_info.desk_ip,
            t_cbb_desk_info.name,
            t_cbb_desk_info.image_template_id,
            t_cbb_desk_info.strategy_id
           from t_cbb_desk_info) di ON (((u_desk.cbb_desktop_id)::text = (di.desk_id)::text)))
			LEFT JOIN (
			    select tt.desk_id,
                    max (case tt.TYPE when 'SYSTEM' then tt.capacity else null end) as system_size,
                    max ( case tt.TYPE when 'PERSONAL' then tt.capacity else null end) as person_size
                    from t_cbb_disk tt
                    where tt.active_status = 'ACTIVE'
                group by tt.desk_id) td ON di.desk_id = td.desk_id
     LEFT JOIN ( select t_cbb_desk_strategy.id,
            t_cbb_desk_strategy.pattern,
            t_cbb_desk_strategy.is_allow_local_disk,
            t_cbb_desk_strategy.enable_full_system_disk
           from t_cbb_desk_strategy) ds ON ((di.strategy_id = ds.id)))
     LEFT JOIN ( select t_base_iac_user_identity_config.open_otp_certification,
            t_base_iac_user_identity_config.related_id,
            t_base_iac_user_identity_config.has_bind_otp
           from t_base_iac_user_identity_config) uic ON ((ut.bind_user_id = uic.related_id))
     LEFT JOIN ( select t_cbb_image_template.id,
            t_cbb_image_template.image_template_name,
            t_cbb_image_template.os_type
           from t_cbb_image_template) it ON ((di.image_template_id = it.id)))
     LEFT JOIN ( select resource_id,lock,lock_time,unlock_time,pwd_error_times
        from t_rco_authentication where TYPE = 'TERMINAL') au
        ON cbb.real_terminal_id = au.resource_id
     LEFT JOIN (
        select
            terminal_id,
            download_state,
            download_finish_time,
            fail_code
        from t_rco_image_download_state) ids
        ON cbb.terminal_id = ids.terminal_id
     LEFT JOIN (
        select
           t_cbb_terminal_authorize.license_type,
           t_cbb_terminal_authorize.terminal_id,
           t_cbb_terminal_authorize.license_duration,
           t_cbb_terminal_authorize.auth_mode
        from t_cbb_terminal_authorize) auth
        ON cbb.terminal_id = auth.terminal_id
  ORDER BY ut.create_time DESC;


-- 创建搜索桌面视图，根据桌面名称或桌面ip或桌面绑定终端进行搜索
create view v_cbb_desktop_search
  as
    select
        rco_info.ID as desk_id,
        cbb_info.desk_id as cbb_desk_id,
        cbb_info.desk_ip,
        cbb_info.desk_ip as ip,
        cbb_info.name,
        cbb_info.TIMESTAMP,
        cbb_info.VERSION,
        cbb_info.is_delete,
        cbb_terminal_info.terminal_name
    from
        t_rco_user_desktop rco_info,
        ( select * from t_cbb_desk_info ) cbb_info,
        ( select * from t_cbb_terminal ) cbb_terminal_info
    where
        rco_info.cbb_desktop_id = cbb_info.desk_id and
        rco_info.terminal_id = cbb_terminal_info.terminal_id
    order by
        timestamp desc;

-- 桌面视图
create view v_cbb_user_desktop_detail as
select all_dt.id,
    all_dt.desktop_name,
    all_dt.has_login,
    all_dt.create_time,
    all_dt.desktop_type,
    all_dt.session_type,
    all_dt.user_id,
    all_dt.cbb_strategy_id,
    all_dt.cbb_network_id,
    all_dt.cbb_desktop_id,
    all_dt.terminal_id,
    all_dt.version,
    all_dt.is_delete,
    all_dt.delete_time,
    all_dt.desk_mac,
    all_dt.ip,
    all_dt.desk_state,
    all_dt.desk_type,
    all_dt.is_windows_os_active,
    all_dt.os_active_by_system,
    all_dt.image_template_id,
    all_dt.config_ip,
    all_dt.strategy_name,
    all_dt.pattern,
    case when all_dt.desktop_type = 'THIRD'  then all_dt.dt_system_size else all_dt.system_size end as system_size,
    all_dt.cpu,
    all_dt.memory,
    all_dt.person_size,
    all_dt.enable_hyper_visor_improve,
    all_dt.strategy_enable_watermark,
    all_dt.vgpu_type,
    all_dt.vgpu_extra_info,
    all_dt.latest_login_time,
    all_dt.latest_running_time,
    all_dt.image_template_name,
    all_dt.root_image_name,
    all_dt.root_image_id,
    all_dt.image_role_type,
    all_dt.before_edit_guest_tool_version,
    all_dt.will_apply_image_id,
    all_dt.os_type,
    all_dt.cbb_image_type,
    all_dt.physical_server_id,
    all_dt.has_auto_join_domain,
    physical_server.physical_server_ip,
    physical_server.host_name,
    all_dt.desktop_role,
    all_u.user_group_id,
    all_u.real_name,
    all_u.user_type,
    all_u.user_name,
    all_u.password,
    all_u.phone_num,
    all_u.email,
    all_u.user_group_name,
    all_u.user_group_description,
    all_u.user_state,
    all_u.user_account_expire_date,
    all_u.user_invalid_recover_time,
    all_u.user_invalid_time,
    all_u.user_invalid,
    all_u.user_create_time,
    all_t.terminal_group_id,
    all_t.terminal_name,
    all_t.product_type,
    all_t.terminal_platform,
    all_t.idv_terminal_model,
    all_t.terminal_ip,
    all_t.terminal_mask,
    all_t.terminal_gateway,
    all_t.terminal_group_name,
    all_t.last_online_time,
    all_t.terminal_mac,
    all_t.enable_proxy,
    all_dt.has_terminal_running,
    all_dt.computer_name,
    all_dt.desktop_pool_type,
    all_dt.desktop_pool_id,
    all_dt.remark,
    all_dt.enable_web_client,
    all_dt.enable_custom,
    all_dt.desk_create_mode,
    all_dt.enable_agreement_agency,
    all_dt.enable_force_use_agreement_agency,
    all_dt.connect_closed_time,
    all_dt.software_strategy_name,
	all_dt.software_strategy_id,
	all_dt.user_profile_strategy_id,
    all_dt.user_profile_strategy_name,
    case when dt_f_info.fault_state = true then true
    else false end as fault_state,
    dt_f_info.fault_description,
    dt_f_info.fault_time,
    case when all_dt.desktop_type != 'VDI' and ids.download_state is null then 'NONE' else ids.download_state end,
    ids.download_finish_time,
    fail_code,
    all_dt.enable_full_system_disk,
    d_network.desk_network_name network_name,
    all_dt.image_usage,
    all_u.user_description,
    all_dt.cluster_id,
    all_dt.is_open_desk_maintenance,
    all_dt.show_root_pwd,
    d_pool.name desktop_pool_name,
    all_dt.os_version,
    all_dt.guest_tool_version,
    all_dt.platform_id,
    platform.name as platform_name,
    platform.type as platform_type,
    platform.status as platform_status,
    platform.cloud_platform_id,
    all_dt.cpu_arch,
    all_dt.desk_spec_id,
    all_dt.strategy_type,
    all_dt.est_protocol_type,
    dt_gt.register_state,
    dt_gt.register_message,
    case when all_dt.pattern = 'PERSONAL' or all_dt.desktop_type = 'THIRD' then 1 else 0 end as can_upgrade_agent
   from ((((
					select
						dt_u.id,
						dt_u.has_login,
						dt_u.desktop_type,
						dt_u.user_id,
						dt_u.terminal_id,
						dt_u. VERSION,
                        dt_u.connect_closed_time,
						case
				      when (dt_info.desk_type) = 'IDV' then dt_info.latest_running_time
				      when (dt_info.desk_type) = 'VDI' then dt_u.latest_login_time
				    end as latest_login_time,
                        dt_info.latest_running_time,
						dt_u.has_terminal_running,
                        dt_u.has_auto_join_domain,
						dt_u.enable_full_system_disk,
						dt_info.name as desktop_name,
						dt_info.strategy_id as cbb_strategy_id,
						dt_info.network_id as cbb_network_id,
						dt_info.image_template_id,
						dt_info.ip as config_ip,
						dt_info.desktop_role,
						dt_info.desk_id as cbb_desktop_id,
						dt_info.is_delete,
						dt_info.delete_time,
						dt_info.desk_mac,
						dt_info.desk_ip as ip,
						dt_info.desk_state,
						dt_info.desk_type,
						dt_info.session_type,
						dt_info.is_windows_os_active,
						dt_info.os_active_by_system,
						dt_info.physical_server_id,
						dt_info.timestamp as create_time,
						dt_info.computer_name,
						dt_info.desktop_pool_type,
                        dt_info.desktop_pool_id,
						dt_stg_v.name as strategy_name,
						dt_stg_v.pattern,
						dt_stg_v.strategy_type,
						dt_stg_v.enable_web_client,
                        dt_stg_v.enable_watermark as strategy_enable_watermark,
                        dt_stg_v.desk_create_mode,
                        dt_stg_v.enable_agreement_agency,
                        dt_stg_v.enable_force_use_agreement_agency,
                        dt_d.system_size,
                        dt_info.cpu,
                        dt_info.system_size as dt_system_size,
                        dt_info.memory,
                        dt_info.enable_hyper_visor_improve,
                        v_software_strategy.software_strategy_name,
						v_software_strategy.software_strategy_id,
						v_user_profile_strategy.user_profile_strategy_name,
                        v_user_profile_strategy.user_profile_strategy_id,
                        dt_info.remark,
					    dt_info.cluster_id,
					    dt_info.is_open_desk_maintenance,
                            CASE
                                WHEN ((dt_info.desk_type)::text = 'IDV'::text) THEN (dt_info.person_size)::integer
                                WHEN ((dt_info.desk_type)::text = 'THIRD'::text) THEN (dt_info.person_size)::integer
                                WHEN ((dt_info.desk_type)::text = 'VDI'::text) THEN dt_d.person_size
                                ELSE (NULL::smallint)::integer
                            END AS person_size,
                        dt_info.vgpu_type,
                        dt_info.vgpu_extra_info,
                        dt_info.enable_custom,
                        dt_info.os_version,
                        dt_info.guest_tool_version,
                        CASE
                            WHEN ((dt_info.desk_create_mode)::text = 'FULL_CLONE'::text) THEN NULL
                            ELSE img_tpl.image_template_name END AS image_template_name,
                        CASE
                            WHEN ((dt_info.os_type)::text != ''::text) THEN dt_info.os_type
                            WHEN ((dt_info.desk_create_mode)::text = 'FULL_CLONE'::text) THEN dt_info.os_type
                            ELSE img_tpl.os_type END AS os_type,
                        CASE
                            WHEN ((dt_info.desk_create_mode)::text = 'FULL_CLONE'::text) THEN 'VDI'
                            ELSE img_tpl.cbb_image_type END AS cbb_image_type,
                        img_tpl.image_usage,
                        dt_info.show_root_pwd,
                        CASE
                            WHEN ((dt_info.desk_create_mode)::text = 'FULL_CLONE'::text) THEN NULL
                            ELSE img_tpl.root_image_name END AS root_image_name,
                        CASE
                            WHEN ((dt_info.desk_create_mode)::text = 'FULL_CLONE'::text) THEN NULL
                            ELSE img_tpl.root_image_id END AS root_image_id,
                        img_tpl.image_role_type,
                        img_tpl.before_edit_guest_tool_version,
                        img_tpl.cpu_arch,
                        dt_info.will_apply_image_id,
                        dt_info.platform_id,
                        dt_info.est_protocol_type,
                        dt_info.desk_spec_id
           FROM (((((((t_rco_user_desktop dt_u
             JOIN t_cbb_desk_info dt_info ON dt_u.cbb_desktop_id = dt_info.desk_id)
             JOIN t_cbb_desk_strategy dt_stg_v ON ((dt_info.strategy_id = dt_stg_v.id))))
             LEFT JOIN t_cbb_image_template img_tpl ON ((img_tpl.id = dt_info.image_template_id))))
             LEFT JOIN (
							select
								T1.NAME as software_strategy_name,
								T1.ID as software_strategy_id,
								T2.desk_id
							from
								t_rco_software_strategy T1
								 join t_rco_desk_info T2 on T1.id = T2.software_strategy_id
							) v_software_strategy ON ( v_software_strategy.desk_id = dt_u.cbb_desktop_id )
						)
			 LEFT JOIN ( select T1.NAME as user_profile_strategy_name, T1.ID as user_profile_strategy_id, T2.desk_id
                          			from t_rco_user_profile_strategy T1 join t_rco_desk_info T2 on T1.id = T2.user_profile_strategy_id ) v_user_profile_strategy
                          			ON ( v_user_profile_strategy.desk_id = dt_u.cbb_desktop_id ))
             LEFT JOIN ( select tt.desk_id,
                    max(
                        case tt.type
                            when 'SYSTEM'::text THEN tt.capacity
                            ELSE NULL::integer
                        END) AS system_size,
                    max(
                        CASE tt.type
                            WHEN 'PERSONAL'::text THEN tt.capacity
                            ELSE NULL::integer
                        END) AS person_size
                   FROM t_cbb_disk tt
                  WHERE ((tt.active_status)::text = 'ACTIVE'::text)
                  GROUP BY tt.desk_id) dt_d ON (dt_d.desk_id = dt_info.desk_id))) all_dt
     LEFT JOIN ( select
			u.id as u_id,
            u.group_id as user_group_id,
            u.real_name,
            u.user_type,
            u.name as user_name,
            u.password,
            u.phone_num,
            u.email,
            u.state as user_state,
            u.account_expire_date as user_account_expire_date,
            u.invalid_recover_time as user_invalid_recover_time,
            u.invalid_time as user_invalid_time,
            u.invalid as user_invalid,
            g.name as user_group_name,
            g.description as user_group_description,
            u.create_time as user_create_time,
            u.description as user_description
           from
            t_base_iac_user u,
            t_base_iac_user_group g
          where (u.group_id = g.id)) all_u ON ((all_dt.user_id = all_u.u_id)))
     LEFT JOIN ( select
			u_t.id as tm_id,
            u_t.terminal_id,
            u_t.terminal_mode as idv_terminal_model,
            c_t.group_id as terminal_group_id,
            c_t.terminal_name,
            c_t.product_type,
            c_t.platform as terminal_platform,
            c_t.ip as terminal_ip,
            c_t.subnet_mask as terminal_mask,
            c_t.gateway as terminal_gateway,
            u_t_g.name as terminal_group_name,
            c_t.last_online_time,
            c_t.mac_addr as terminal_mac,
            c_t.enable_proxy
           from
            t_rco_user_terminal u_t,
            t_cbb_terminal c_t,
            t_cbb_terminal_group u_t_g
          where (((u_t.terminal_id)::text = (c_t.terminal_id)::text) AND (c_t.group_id = u_t_g.id))) all_t ON (((all_dt.terminal_id)::text = (all_t.terminal_id)::text)))
     LEFT JOIN ( select
			t_cbb_physical_server.id,
            t_cbb_physical_server.host_name,
            t_cbb_physical_server.ip as physical_server_ip
           from t_cbb_physical_server
           ) physical_server ON ((all_dt.physical_server_id = physical_server.id))
     LEFT JOIN t_rco_desk_fault_info dt_f_info ON ((all_dt.cbb_desktop_id = dt_f_info.desk_id))
		 LEFT JOIN (
			select
			t_cbb_disk.desk_id,
			t_cbb_disk.capacity
		from
			 t_cbb_disk where type = 'PERSONAL' ) t_cbb_disk ON ((
				all_dt.cbb_desktop_id = t_cbb_disk.desk_id
			))
     LEFT JOIN (
        select
            terminal_id,
            download_state,
            download_finish_time,
            fail_code
        from
            t_rco_image_download_state) ids
        ON all_t.terminal_id = ids.terminal_id
     LEFT JOIN t_cbb_desk_network d_network ON ((d_network.id = all_dt.cbb_network_id
            ))
     LEFT JOIN t_cbb_desktop_pool_info d_pool ON d_pool.id = all_dt.desktop_pool_id
     LEFT JOIN t_cbb_hciadapter_platform platform ON platform.id = all_dt.platform_id
     LEFT JOIN t_cbb_desk_gt_info dt_gt ON dt_gt.id = all_dt.cbb_desktop_id;


-- 桌面统计视图
create or replace view v_cbb_desktop_info_stat
as
  select
    v_cbb_user_desktop_detail.id as desk_id,
    v_cbb_user_desktop_detail.desk_state,
    v_cbb_user_desktop_detail.desk_type,
    v_cbb_user_desktop_detail.version,
    v_cbb_user_desktop_detail.terminal_group_id,
    v_cbb_user_desktop_detail.user_group_id
  from v_cbb_user_desktop_detail;

-- 终端搜索视图
create view v_cbb_terminal_search
	as
  		select
  			id,
  			terminal_id,
  			terminal_name,
  			ip,
  			version,
  			create_time,
  			platform,
            group_id,
            network_infos
  		from (select *
  			from t_cbb_terminal
                )cbb_info;

-- 镜像导出视图
create or replace view v_cbb_image_export as select
	export.id,
	export.image_disk_type,
	export.image_template_id,
	export.export_file_name,
	export.export_file_desc,
	export.export_file_path,
	export.export_file_capacity,
	export.export_state,
	export.version,
	export.export_time,
	export.image_disk_id,
	export.cpu_arch,
	im.image_template_name,
	im.image_template_state,
	pf.name as platform_name,
    pf.type as platform_type,
    pf.status as platform_status,
    pf.id as platform_id
from t_cbb_image_template_export export
left join t_cbb_image_template im
 on export.image_template_id = im.id
 left join t_cbb_hciadapter_platform pf
 on im.platform_id=pf.id;

 -- 硬件特征码审批视图
create view "v_rco_user_hardware_certification" as  select sc.id,
    sc.user_id,
    sc.terminal_id,
    sc.state,
    sc.mac_addr,
    tfc.feature_code,
    sc.create_time,
    sc.update_time,
    sc.version,
    tu.name as user_name,
    tu.group_id as user_group_id,
    tg.name as user_group_name,
    tt.terminal_name,
    tt.ip
   from (((t_base_iac_user_hardware_certification sc
     left join t_rco_terminal_feature_code tfc ON sc.terminal_id = tfc.terminal_id
     left join t_base_iac_user tu on ((sc.user_id = tu.id)))
     LEFT JOIN t_base_iac_user_group tg ON ((tu.group_id = tg.id)))
     LEFT JOIN t_cbb_terminal tt ON (((sc.terminal_id)::text = (tt.terminal_id)::text)));

-- 动态口令管理信息列表视图
create view v_rco_user_otp_certification
            (id, user_id, open_otp_certification, has_bind_otp, otp_secret_key, update_time, version, user_name,
             real_name, user_type, user_state, user_group_id, user_group_name)
as
select sc.id,
       sc.related_id as user_id,
       sc.open_otp_certification,
       sc.has_bind_otp,
       sc.otp_secret_key,
       sc.update_time,
       sc.version,
       tu.name       as user_name,
       tu.real_name  as real_name,
       tu.user_type  as user_type,
       tu.state  as user_state,
       tu.group_id   as user_group_id,
       tg.name       as user_group_name
from t_base_iac_user_identity_config sc
         left join t_base_iac_user tu on sc.related_id = tu.id
         left join t_base_iac_user_group tg on tu.group_id = tg.id
where sc.related_type = 'USER';

-- 池中云桌面简要信息试图
create or replace view v_rco_pool_desktop_info as select dt_info.desk_id,
    dt_info.name as desktop_name,
    dt_info.strategy_id,
    dt_info.network_id,
    dt_info.image_template_id,
    dt_info.ip as config_ip,
    dt_info.desktop_role,
    dt_info.is_delete,
    dt_info.delete_time,
    dt_info.desk_mac,
    dt_info.desk_ip,
    dt_info.desk_state,
    dt_info.desk_type,
    dt_info.is_windows_os_active,
    dt_info.os_active_by_system,
    dt_info.physical_server_id,
    dt_info."timestamp" as create_time,
    dt_info.computer_name,
    dt_info.desktop_pool_type,
    dt_info.desktop_pool_id,
    dt_info.cpu,
    dt_info.memory,
    dt_info.vgpu_type,
    dt_info.vgpu_extra_info,
    dt_info.enable_hyper_visor_improve,
    case when dt_info.desk_type = 'THIRD' then dt_info.system_size else dt_d.system_size end as system_size,
    dt_d.person_size,
    dt_info.remark,
    dt_info.is_open_desk_maintenance,
    dt_info.version,
    dt_info.est_protocol_type,
    dt_u.has_login,
    dt_u.desktop_type,
    dt_u.user_id,
    u1.name user_name,
    u_group.id user_group_id,
    u_group.name user_group_name,
    dt_u.terminal_id,
    dt_u.has_terminal_running,
    pool_info.name as pool_name,
    pool_info.pool_model,
    pool_info.pool_state,
    dt_f_info.fault_state,
    r_d_info.software_strategy_id,
    r_d_info.user_profile_strategy_id,
    dt_u.connect_closed_time,
    dt_u.assignment_time,
    dt_info.desk_spec_id,
    dt_info.session_type
from t_cbb_desk_info dt_info
        inner join t_cbb_desktop_pool_info pool_info on dt_info.desktop_pool_id = pool_info.id
        left join t_rco_user_desktop dt_u on dt_info.desk_id = dt_u.cbb_desktop_id
        left join t_base_iac_user u1 on u1.id = dt_u.user_id
        left join t_base_iac_user_group u_group on u1.group_id = u_group.id
        left join t_rco_desk_fault_info dt_f_info on dt_info.desk_id = dt_f_info.desk_id
        left join t_rco_desk_info r_d_info on dt_info.desk_id = r_d_info.desk_id
        left join (
        select
            tt.desk_id,
            max( case tt.type when 'SYSTEM' then tt.capacity else null end) as system_size,
            max( case tt.type when 'PERSONAL' then tt.capacity else null end) as person_size
        from t_cbb_disk tt
        where tt.active_status = 'ACTIVE' group by tt.desk_id) dt_d on (dt_d.desk_id = dt_info.desk_id);

 -- 统计所有桌面池中不可用、在线、总的桌面数量
create or replace view v_rco_statistic_desktop_count as
select
    tmp.desktop_pool_id,
    tmp.desktop_pool_type,
    tmp.desk_type as desktop_source_type,
    tmp.total_count,
    tmp.error_count,
    coalesce(dsl.used_count, 0) used_count,
    0 as version -- 规避写法，框架要求实体类必须有version字段
from
    (
        select sum (case when di.desk_state = 'ERROR' then 1 else 0 end) error_count,
            count (1) total_count,
            desktop_pool_id, desktop_pool_type, desk_type
        from
            t_cbb_desk_info di
        where
            di.desk_type in ('VDI', 'THIRD')
            and di.is_delete is FALSE
            and di.desktop_pool_id is not null
        group by
            desktop_pool_id, desktop_pool_type, desk_type
    ) tmp
    left join (
        select
            count(distinct desktop_id) used_count,
            related_id
        from
            t_rco_desktop_session_log
        where
            state = 'CONNECTING'
        group by
            related_id
    ) dsl on tmp.desktop_pool_id = dsl.related_id;

-- 桌面池信息视图
create or replace view v_rco_desktop_pool_detail as select pool.id,
       pool.name,
       pool.desktop_name_prefix,
       pool.pool_model,
       pool.session_type,
       pool.idle_desktop_recover,
       pool.description,
       pool.image_template_id,
       image.root_image_id,
       image.root_image_name,
       image.image_role_type,
       pool.strategy_id,
       pool.network_id,
       pool.cpu_usage,
       pool.memory_usage,
       pool.system_disk_usage,
       pool.max_session,
       pool.load_balance_strategy,
       pool.pool_state,
       coalesce(desk_num.desktop_num, 0) AS desktop_num,
       pool.pre_start_desktop_num,
       pool.is_open_maintenance,
       pool.create_time,
       pool.pool_type,
       pool.update_time,
       pool.version,
       pool.cluster_id,
       pool.desk_spec_id,
       strategy.name as strategy_name,
       strategy.pattern as desktop_type,
       dspec.cpu,
       dspec.memory,
       dspec.person_size,
       dspec.person_disk_storage_pool_id,
       dspec.system_size,
       dspec.system_disk_storage_pool_id,
       dspec.vgpu_type,
       dspec.vgpu_extra_info,
       dspec.extra_disk_info,
       dspec.enable_hyper_visor_improve,
       strategy.desk_create_mode,
       net.desk_network_name as network_name,
       image.image_template_name,
       image.os_type,
       pool_config.software_strategy_id,
       pool_config.user_profile_strategy_id,
       pool_config.software_strategy_name,
       pool_config.user_profile_strategy_name,
       vrsdc.used_count as connected_num,
       pool.platform_id,
       p.name as platform_name,
       p.type as platform_type,
       p.status as platform_status,
       p.cloud_platform_id,
       case when strategy.pattern = 'PERSONAL' or pool_type = 'THIRD' then 1 else 0 end as can_upgrade_agent
from t_cbb_desktop_pool_info pool
         left join t_cbb_desk_strategy strategy on pool.strategy_id = strategy.id
         left join t_cbb_desk_spec dspec on pool.desk_spec_id = dspec.id
         left join t_cbb_desk_network net on pool.network_id = net.id
         left join t_cbb_image_template image on pool.image_template_id = image.id
         left join (select d_info.desktop_pool_id, count(d_info.desk_id) desktop_num
                    from t_cbb_desk_info d_info
                    where d_info.desktop_pool_id is not null and d_info.is_delete = false
                    group by d_info.desktop_pool_id) desk_num on desk_num.desktop_pool_id = pool.id
         left join ( select pool_config1.desktop_pool_id,
                            pool_config1.software_strategy_id,
                            pool_config1.user_profile_strategy_id,
                            soft.name as software_strategy_name,
                            profile.name as user_profile_strategy_name
                     from t_rco_desktop_pool_config pool_config1
                              left join t_rco_software_strategy soft on pool_config1.software_strategy_id = soft.id
                              left join t_rco_user_profile_strategy profile on pool_config1.user_profile_strategy_id = profile.id) pool_config on pool_config.desktop_pool_id = pool.id
         left join v_rco_statistic_desktop_count vrsdc on pool.id = vrsdc.desktop_pool_id
         left join t_cbb_hciadapter_platform p on pool.platform_id = p.id;

-- 用户对应的桌面池列表视图
create or replace view v_rco_user_desktop_pool as select pool.id,
       pool.name,
       pool.desktop_name_prefix,
       pool.pool_model,
       pool.idle_desktop_recover,
       pool.description,
       pool.image_template_id,
       pool.root_image_id,
       pool.root_image_name,
       pool.image_role_type,
       pool.strategy_id,
       pool.network_id,
       pool.pool_state,
       pool.desktop_num,
       tcpi.pool_type,
       tcpi.session_type,
       pool.is_open_maintenance,
       pool.create_time,
       pool.update_time,
       pool.version,
       pool.strategy_name,
       pool.desktop_type,
       pool.cpu,
       pool.memory,
       pool.person_size,
       pool.system_size,
       pool.desk_create_mode,
       pool.network_name,
       pool.image_template_name,
       pool.os_type,
       pool.software_strategy_id,
       pool.user_profile_strategy_id,
       pool.software_strategy_name,
       pool.user_profile_strategy_name,
       pool.connected_num,
       puser.related_id,
       puser.related_type,
       pool.cluster_id,
       pool.system_disk_storage_pool_id,
       pool.person_disk_storage_pool_id,
       pool.platform_id,
       pool.platform_name,
       pool.platform_type,
       pool.platform_status,
       pool.cloud_platform_id
from t_rco_desktop_pool_user puser
         join v_rco_desktop_pool_detail pool on puser.desktop_pool_id = pool.id
         join t_cbb_desktop_pool_info tcpi on puser.desktop_pool_id = tcpi.id;

-- 软件分组列表视图
create view "v_rco_software_group_count" as  select trsg.id,
    trsg.name,
    trsg.group_type,
    coalesce(t.count, 0::bigint) AS count,
    trsg.description,
    trsg.version
   FROM t_rco_software_group trsg
     LEFT JOIN ( select t_1.group_id,
            count(*) as count
           from t_rco_software t_1
           where t_1.parent_id is null
          group by t_1.group_id) t ON trsg.id = t.group_id;

-- 软件策略关联软件列表视图
create view "v_rco_software_strategy_count" as  select trss.id,
    trss.name,
    trss.description,
    trss.is_whitelist_mode,
    trss.version,
    coalesce(t.count, 0::bigint) AS count
   FROM ( select tcount.strategy_id,
            count(*) as count
           from ( select t_ssd.strategy_id,
                    trs.id,
                    trs.group_id,
                    trs.name,
                    trs.description,
                    trs.digital_sign,
                    trs.digital_sign_flag,
                    trs.install_path,
                    trs.install_path_flag,
                    trs.product_name,
                    trs.product_name_flag,
                    trs.process_name,
                    trs.process_name_flag,
                    trs.original_file_name,
                    trs.original_file_name_flag,
                    trs.file_custom_md5,
                    trs.file_custom_md5_flag,
                    trs.version
                   from t_rco_software_strategy_detail t_ssd
                     left join t_rco_software trs on t_ssd.related_id = trs.id
                  where t_ssd.related_type::text = 'SOFTWARE'::text
                UNION ALL
                 select t_ssd.strategy_id,
                    trs.id,
                    trs.group_id,
                    trs.name,
                    trs.description,
                    trs.digital_sign,
                    trs.digital_sign_flag,
                    trs.install_path,
                    trs.install_path_flag,
                    trs.product_name,
                    trs.product_name_flag,
                    trs.process_name,
                    trs.process_name_flag,
                    trs.original_file_name,
                    trs.original_file_name_flag,
                    trs.file_custom_md5,
                    trs.file_custom_md5_flag,
                    trs.version
                   from t_rco_software_strategy_detail t_ssd
                     left join t_rco_software trs on t_ssd.related_id = trs.group_id
                  where t_ssd.related_type::text = 'GROUP'::text AND trs.id IS NOT NULL) tcount
          GROUP BY tcount.strategy_id) t
     RIGHT JOIN t_rco_software_strategy trss ON trss.id = t.strategy_id;


-- 软件策略关联视图
create view "v_rco_software_strategy_related_software" as  select t.strategy_id,
    t.id,
    t.group_id,
    t.name,
    t.name_search,
    t.description,
    t.digital_sign,
    t.digital_sign_flag,
    t.install_path,
    t.install_path_flag,
    t.product_name,
    t.product_name_flag,
    t.process_name,
    t.process_name_flag,
    t.original_file_name,
    t.original_file_name_flag,
    t.file_custom_md5,
    t.file_custom_md5_flag,
    t.directory_flag,
    t.version,
    t.strategy_name,
    t.strategy_description,
    t.is_whitelist_mode,
    t.digital_sign_black_flag,
    t.product_name_black_flag,
    t.process_name_black_flag,
    t.original_file_name_black_flag,
    t.file_custom_md5_black_flag,
    trsg.name as group_name
   from ( select t_ssd.strategy_id,
            trs.id,
            trs.group_id,
            trs.name,
            trs.name_search,
            trs.description,
            trs.digital_sign,
            trs.digital_sign_flag,
            trs.install_path,
            trs.install_path_flag,
            trs.product_name,
            trs.product_name_flag,
            trs.process_name,
            trs.process_name_flag,
            trs.original_file_name,
            trs.original_file_name_flag,
            trs.file_custom_md5,
            trs.file_custom_md5_flag,
            trs.directory_flag,
            t_strategy.name as strategy_name,
            t_strategy.description as strategy_description,
            t_strategy.is_whitelist_mode as is_whitelist_mode,
            trs.digital_sign_black_flag,
            trs.product_name_black_flag,
            trs.process_name_black_flag,
            trs.original_file_name_black_flag,
            trs.file_custom_md5_black_flag,
            trs.version
           from
            t_rco_software_strategy t_strategy
			inner join t_rco_software_strategy_detail t_ssd on t_strategy.id = t_ssd.strategy_id
            left join t_rco_software trs on t_ssd.related_id = trs.id
          where t_ssd.related_type::text = 'SOFTWARE'::text
        ) t
     LEFT JOIN t_rco_software_group trsg ON trsg.id = t.group_id;


create view "v_rco_soft_related_soft_strategy" as  select t.id,
    t.soft_id,
    t.version,
    t.name,
    t.description,
    t.is_whitelist_mode,
	T2.count
   from ( select t_strategy.id,
            trs.id as soft_id,
            t_strategy.name as name,
            t_strategy.description as description,
            t_strategy.is_whitelist_mode as is_whitelist_mode,
            trs.version
           from
            t_rco_software_strategy t_strategy
			inner join t_rco_software_strategy_detail t_ssd on t_strategy.id = t_ssd.strategy_id
            left join t_rco_software trs on t_ssd.related_id = trs.id
          where t_ssd.related_type::text = 'SOFTWARE'::text
        ) t
          JOIN  v_rco_software_strategy_count T2  ON t.id=T2.id;

-- 磁盘池中磁盘关联用户-用户组-桌面信息查询
create or replace view v_rco_user_disk as
select dk.id as disk_id,
		 dk.name as disk_name,
		 dk.capacity,
		 dk.type as disk_type,
		 dk.state as disk_state,
		 dk.disk_sn,
		 dk.create_time,
		 dk.update_time,
		 dk.disk_pool_id,
		 dk.desk_id,
		 dk.version,
         dk.image_id,
         dk.restore_point_id,
         dk.active_status,
         dk.is_delay_create,
         dk.assign_storage_pool_ids,
         dk.journal,
         dk.delay_capacity,
         dk.disk_pool_type,
		 ds.name as desk_name,
         ds.desk_state as desktop_state,
         ds.desk_type,
		 dk_u.user_id,
         dk_u.latest_use_time,
		 ur.name as user_name,
		 ur.user_type,
		 ur.real_name,
		 ur.create_time as user_create_time,
		 ur.state as user_state,
		 ur.account_expire_date,
		 ur.invalid_recover_time,
		 ur.invalid_time,
		 ur.description as user_description,
		 ur.invalid,
		 up.id as group_id,
		 up.name as group_name,
		 dk.platform_id,
         p.name as platform_name,
         p.type as platform_type,
         p.status as platform_status,
         p.cloud_platform_id
    from t_cbb_disk dk
    left join t_cbb_desk_info ds
        on dk.desk_id = ds.desk_id
    left join t_rco_user_disk dk_u
        on dk.id = dk_u.disk_id
    left join t_base_iac_user ur
        on dk_u.user_id = ur.id
    left join t_base_iac_user_group up
        on ur.group_id = up.id
    left join t_cbb_hciadapter_platform p
        on dk.platform_id = p.id;

-- 用户与磁盘池列表视图
create or replace view v_rco_user_disk_pool
as select pool.id,
       pool.name,
       pool.disk_name_prefix,
       pool.disk_num,
       pool.disk_size,
       pool.enable_create_disk,
       pool.disk_letter,
       pool.pool_state,
       pool.description,
       pool.create_time,
       pool.update_time,
       pool.cluster_id,
       pool.storage_pool_id,
       pool.version,
       pu.related_id,
       pu.related_type,
	   pool.platform_id,
       p.name as platform_name,
       p.type as platform_type,
       p.status as platform_status,
       p.cloud_platform_id
from t_rco_disk_pool_user pu
         left join t_cbb_disk_pool_info pool on pu.disk_pool_id = pool.id
         left join t_cbb_hciadapter_platform p on pool.platform_id = p.id;

-- 用户配置路径组列表视图
create or replace view "v_rco_user_profile_path_group_count" as  select truppg.id,
    truppg.name,
    coalesce(t.count, 0::bigint) AS count,
    truppg.description,
    truppg.create_time,
    truppg.version
   FROM t_rco_user_profile_path_group truppg
     LEFT JOIN ( select trupp.group_id,
            count(*) as count
           from t_rco_user_profile_path trupp
          group by trupp.group_id) t ON truppg.id = t.group_id;


-- 用户配置策略关联路径列表视图
create or replace view "v_rco_user_profile_strategy_count" as select
    trups.ID,
    trups.NAME,
    trups.storage_type,
    ext.id as external_storage_id,
    coalesce ( t1.COUNT, 0 :: BIGINT ) AS COUNT,
    COALESCE ( t2.desk_count, 0 :: BIGINT ) AS desk_count,
    trups.description,
    trups.creator_user_name,
    trups.create_time,
    trups.VERSION
    FROM
            t_rco_user_profile_strategy trups
            LEFT JOIN t_cbb_ext_storage_info ext ON trups.external_storage_id=ext.id
            LEFT JOIN (
    select
            tcount.strategy_id,
            count ( * ) as COUNT
    from
            (
    select
            t_upsr.strategy_id,
            trupp.ID,
            trupp.group_id,
            trupp.NAME,
            trupp.description,
            trupp.VERSION
    from
            t_rco_user_profile_strategy_related t_upsr
            left join t_rco_user_profile_path trupp on t_upsr.related_id = trupp.ID
    where
            t_upsr.related_type :: TEXT = 'PATH' :: TEXT UNION ALL
    select
            t_upsr.strategy_id,
            trupp.ID,
            trupp.group_id,
            trupp.NAME,
            trupp.description,
            trupp.VERSION
    from
            t_rco_user_profile_strategy_related t_upsr
            left join t_rco_user_profile_path trupp on t_upsr.related_id = trupp.group_id
    where
            t_upsr.related_type :: TEXT = 'GROUP' :: TEXT
            AND trupp.ID IS NOT NULL
            ) tcount
    GROUP BY
            tcount.strategy_id
            ) t1 ON trups.ID = t1.strategy_id
            LEFT JOIN ( select user_profile_strategy_id, count ( * ) as desk_count from t_rco_desk_info group by user_profile_strategy_id ) t2 ON trups.ID = t2.user_profile_strategy_id;


-- 用户配置策略关联详情视图
create or replace view "v_rco_user_profile_strategy_related" as  select t.strategy_id,
    t.id,
    t.group_id,
    t.name,
    t.create_time,
    t.description,
    t.creator_user_name,
    t.version,
    t.extra_config_info
   from ( select t_upsr.strategy_id,
            t_upp.id,
            t_upp.group_id,
            t_upp.name,
            t_upp.create_time,
            t_upp.description,
            t_upp.creator_user_name,
            t_upp.version,
            t_upp.extra_config_info
           from
            t_rco_user_profile_strategy t_strategy
			inner join t_rco_user_profile_strategy_related t_upsr on t_strategy.id = t_upsr.strategy_id
            left join t_rco_user_profile_path t_upp on t_upsr.related_id = t_upp.id
          where t_upsr.related_type::text = 'PATH'::text AND t_upp.id IS NOT NULL
        ) t
     LEFT JOIN t_rco_user_profile_path_group t_uppg ON t_uppg.id = t.group_id;


 create or replace view "v_rco_user_profile_child_path" as select
    truppd.id,
    trupcp.mode AS mode,
    trupcp.type AS type ,
    truppd.path,
    truppd.user_profile_path_id,
    truppd.version
    FROM t_rco_user_profile_path_detail truppd
    LEFT JOIN t_rco_user_profile_child_path trupcp
       ON truppd.user_profile_child_path_id = trupcp.id;

 create or replace view "v_rco_user_profile_path" as select
     trupp.id,
     trupp.group_id,
     trupp.name,
     trupp.description,
     trupp.creator_user_name,
     trupp.import_user_profile_path_type,
     truppg.name as group_name,
     trupp.create_time,
     trupp.update_time,
     trupp.version
     from t_rco_user_profile_path trupp
     left join t_rco_user_profile_path_group truppg
        on trupp.group_id = truppg.id;

-- ad域安全组视图
 create or replace view v_rco_ad_group as select
    ag.id,
    ag.name,
  	ag.email,
    ag.domain,
 	ag.remark,
 	ag.ou,
 	ag.object_guid,
 	ag.server_type,
	ag.create_time,
	ag.update_time,
    ag.version
 from t_base_iac_ad_group ag;

-- 用户云应用配置信息列表视图
create view v_rca_strategy_user as
select vru.id,
       truc.basic_sragety_id as rca_strategy_id,
       trdusm.app_strategy_id as dynamic_user_rca_strategy_id,
       vru.user_name,
       vru.user_role,
       vru.group_id,
       vru.group_name,
       vru.real_name,
       vru.user_type,
       vru.password,
       vru.phone_num,
       vru.email,
       vru.state,
       vru.desktop_num,
       vru.ad_user_authority,
       vru.need_update_password,
       vru.lock,
       vru.lock_time,
       vru.unlock_time,
       vru.pwd_error_times,
       vru.last_login_time,
       vru.update_password_time,
       vru.open_otp_certification,
       vru.has_bind_otp,
       vru.open_cas_certification,
       vru.open_account_password_certification,
       vru.open_hardware_certification,
       vru.create_time,
       vru.update_time,
       truc.version
FROM v_rco_user vru
         LEFT JOIN t_rca_user_config truc ON truc.user_id = vru.id
         LEFT JOIN t_rca_dynamic_user_strategy_mappings trdusm ON trdusm.user_id = vru.id;

-- 应用组分配用户信息列表视图
create view v_rca_dynamic_user_info as
select vru.id,
       vru.user_name,
       vru.user_role,
       vru.group_id,
       vru.group_name,
       vru.real_name,
       vru.user_type,
       vru.password,
       vru.phone_num,
       vru.email,
       vru.state,
       vru.desktop_num,
       vru.ad_user_authority,
       vru.need_update_password,
       vru.lock,
       vru.lock_time,
       vru.unlock_time,
       vru.pwd_error_times,
       vru.last_login_time,
       vru.update_password_time,
       vru.open_otp_certification,
       vru.has_bind_otp,
       vru.open_cas_certification,
       vru.open_account_password_certification,
       vru.open_hardware_certification,
       vru.create_time,
       vru.update_time,
       vru.account_expire_date,
       vru.invalid_time,
       vru.user_description,
       rdugm.group_id AS app_group_id,
       rdugm.version
   FROM t_rca_dynamic_user_group_mappings rdugm
        LEFT JOIN v_rco_user vru ON rdugm.user_id = vru.id;



-- 交付组视图
create view v_rco_uam_delivery_group as
select
	g.id,
	g.delivery_group_name,
	g.image_template_id,
	t.image_template_name,
	t.root_image_id,
	t.root_image_name,
	g.os_type,
	g.os_version,
	g.cbb_image_type,
	g.desktop_type ,
	g.app_delivery_type,
	g.push_app_config,
	g.delivery_group_desc,
	g.os_platform,
	g.create_time,
	g.update_time,
	platform.id as platform_id,
    platform.name as platform_name,
    platform.type as platform_type,
    platform.status as platform_status,
	g.version
from
	t_cbb_uam_delivery_group g
left join t_cbb_image_template t on
	g.image_template_id = t.id
left join t_cbb_hciadapter_platform platform on
    platform.id=t.platform_id;

-- 交付应用视图
create view v_rco_uam_delivery_app as
select
	t.id,
	t.delivery_group_id,
	t.app_id,
	c.app_name,
	c.app_type,
	(case
		when exists(
		select
			1
		from
			t_cbb_uam_delivery_detail dd
		where
			t.delivery_group_id = dd.delivery_group_id
			and t.app_id = dd.app_id
			and dd.delivery_status = 'DELIVERING')
		or (
		select
			count(*)
		from
			t_cbb_uam_delivery_detail dd
		where
			t.delivery_group_id = dd.delivery_group_id
			and t.app_id = dd.app_id) = 0

    then 'DELIVERING'
		when exists(
		select
			1
		from
			t_cbb_uam_delivery_detail dd
		where
			t.delivery_group_id = dd.delivery_group_id
			and t.app_id = dd.app_id
			and dd.delivery_status = 'DELIVERY_FAIL' )
    then 'DELIVERY_FAIL'
		else 'DELIVERY_SUCCESS'
	end) delivery_status,
	t.create_time,
	t.version
from
	t_cbb_uam_delivery_app t,
	t_cbb_uam_app c
where
	t.app_id = c.id;

-- 交付对象视图
create view v_rco_uam_delivery_object as
select
	o.id,
	o.delivery_group_id,
	o.cloud_desktop_id,
	g.delivery_group_name,
	g.app_delivery_type,
	(case
		when exists(
		select
			1
		from
			t_cbb_uam_delivery_detail dd
		where
			o.delivery_group_id = dd.delivery_group_id
			and o.cloud_desktop_id = dd.cloud_desktop_id
			and dd.delivery_status = 'DELIVERING')
		or (
		select
			count(*)
		from
			t_cbb_uam_delivery_detail dd
		where
			o.delivery_group_id = dd.delivery_group_id
			and o.cloud_desktop_id = dd.cloud_desktop_id) = 0
    then 'DELIVERING'
		when exists(
		select
			1
		from
			t_cbb_uam_delivery_detail dd
		where
			o.delivery_group_id = dd.delivery_group_id
			and o.cloud_desktop_id = dd.cloud_desktop_id
			and dd.delivery_status = 'DELIVERY_FAIL' )
    then 'DELIVERY_FAIL'
		else 'DELIVERY_SUCCESS'
	end) delivery_status,
	d.name as cloud_desktop_name,
	d.desk_state,
	d.desk_ip,
	d.os_version,
	case when d.desk_type = 'THIRD' then d.os_type else it.os_type end as os_type,
	it.cbb_image_type,
	u.name as user_name,
	case
		when t.terminal_name <> null then
            t.terminal_name
		else t2.terminal_name
	end as terminal_name,
	o.create_time,
	platform.id as platform_id,
    platform.name as platform_name,
    platform.type as platform_type,
    platform.status as platform_status,
	o.version
from
	t_cbb_uam_delivery_object o
inner join t_cbb_uam_delivery_group g on
	o.delivery_group_id = g.id
inner join t_cbb_desk_info d on
	o.cloud_desktop_id = d.desk_id
left join t_cbb_image_template it on
	d.image_template_id = it.id
left join t_cbb_hciadapter_platform platform on
    platform.id = d.platform_id
left join t_rco_user_desktop ud on
	d.desk_id = ud.cbb_desktop_id
left join t_base_iac_user u on
	ud.user_id = u.id
left join t_rco_user_terminal ut on
	ut.bind_desk_id = d.desk_id
left join t_cbb_terminal t on
	t.terminal_id = ut.terminal_id
left join t_cbb_terminal t2 on
	t2.terminal_id = ud.terminal_id;


-- 交付对象详情
create view v_rco_uam_delivery_detail as
select
	d.id,
	d.delivery_group_id,
	d.cloud_desktop_id,
	d.app_id,
	a.app_name,
	a.app_type,
	i.name as cloud_desktop_name,
	i.desk_ip,
	i.desk_state,
	i.os_version,
	u.name as user_name,
	case when i.desk_type = 'THIRD' then i.os_type else it.os_type end as os_type,
	it.cbb_image_type,
	ts.pattern as desktop_type,
	d.delivery_status,
	d.progress_status,
	d.progress_desc,
	d.delivery_time,
	d.create_time,
	d.update_time,
	platform.id as platform_id,
    platform.name as platform_name,
    platform.type as platform_type,
    platform.status as platform_status,
	d.version
from t_cbb_uam_delivery_detail d
inner join t_cbb_uam_app a on a.id = d.app_id
inner join t_cbb_desk_info i on d.cloud_desktop_id = i.desk_id
inner join t_cbb_desk_strategy ts on ts.id = i.strategy_id
left join t_cbb_image_template it on i.image_template_id  = it.id
left join t_rco_user_desktop ud on i.desk_id = ud.cbb_desktop_id
left join t_base_iac_user u on ud.user_id = u.id
left join t_rco_user_terminal ut on ut.bind_desk_id = i.desk_id
left join t_cbb_hciadapter_platform platform on platform.id = it.platform_id
left join t_cbb_terminal t on t.terminal_id = ut.terminal_id;


-- 应用磁盘
create view v_rco_uam_app_disk as
select
	a.id,
	a.app_name,
	a.app_type,
	p.os_type,
	p.app_software_package_type cbb_image_type,
	p.os_version,
	p.control_state,
	p.is_global_image,
	template.id image_template_id,
	template.image_template_name,
	template.root_image_id,
	template.root_image_name,
	template.image_role_type,
	a.app_status,
	test.test_processing_count,
	test.test_end_count,
	test.testing_desktop_count,
	delivery_group.delivery_group_count,
	(case
		when t.total>1 then true
		else false
	end ) enable_rollback,
	a.app_desc,
	a.create_time,
	a.update_time,
	a.version,
	platform.id as platform_id,
	platform.name as platform_name,
	platform.type as platform_type,
	platform.status as platform_status
from
	t_cbb_uam_app a
left join t_cbb_uam_app_software_package p on
	a.id = p.id
left join t_cbb_image_template template on
	template.id = p.image_template_id
left join t_cbb_hciadapter_platform platform on
    platform.id=template.platform_id
left join (
	select
		app_software_package_id,
		count(*) total
	from
		t_cbb_uam_app_software_package_version
	where
		state in ('ENABLED', 'AVAILABLE')
	group by
		app_software_package_id) t on
	a.id = t.app_software_package_id
left join (
	select
		a.id,
		sum(case
                when t.state in ('PROCESSING', 'PAUSING')  then 1
                else 0
        end) test_processing_count,
		sum(case
                when t.state = 'FINISHED' then 1
                else 0
        end) test_end_count,
        sum(case
                when tg.state <> 'COMPLETED' then 1
                else 0
            end) testing_desktop_count
	from
		t_cbb_uam_app a
	left join t_cbb_uam_app_test_application ta on
		ta.app_id = a.id
	left join t_cbb_uam_app_test t on
		t.id = ta.test_id
	left join t_cbb_uam_app_test_target tg on
	    tg.test_id = t.id
	group by
		a.id) test on
	test.id = a.id
left join (
	select
		a.id,
		count(t.id) delivery_group_count
	from
		t_cbb_uam_app a
	left join t_cbb_uam_delivery_app da on
		da.app_id = a.id
	left join t_cbb_uam_delivery_group t on
		t.id = da.delivery_group_id
	group by
		a.id) delivery_group on
	delivery_group.id = a.id
where
	a.app_type = 'APP_SOFTWARE_PACKAGE';


-- 推送安装包
create view v_rco_uam_push_install_package as
select
	p.id,
	p.app_id,
	p.app_name,
	p.app_type,
	p.app_status,
	p.file_name,
	p.package_status,
	delivery_group.delivery_group_count,
	p.app_desc,
	p.os_platform,
	p.manual_quiet_install_param,
	p.execute_file_path,
	p.create_time,
	p.update_time,
	p.version
from
	(
	select
		row_number() over(partition by t.app_id
	order by
		t.create_time desc) as rn,
		a.id,
		t.app_id,
		a.app_name,
		a.app_status,
		a.app_type,
		t.file_name,
		t.package_status,
		a.app_desc,
		t.os_platform,
		t.manual_quiet_install_param,
		t.execute_file_path,
		a.create_time,
		a.update_time,
		a.version
	from
		t_cbb_uam_push_install_package t,
		t_cbb_uam_app a
	where
		t.app_id = a.id) p
left join (
	select
		a.id,
		count(t.id) delivery_group_count
	from
		t_cbb_uam_app a
	left join t_cbb_uam_delivery_app da on
		da.app_id = a.id
	left join t_cbb_uam_delivery_group t on
		t.id = da.delivery_group_id
	group by
		a.id) delivery_group on
	delivery_group.id = p.id
	where p.rn=1;



-- 创建云桌面测试详情
CREATE VIEW v_rco_uam_app_test_desktop_detail AS
  SELECT
         target.id,
         desk.name as desktop_name,
         desk.desk_state,
         target.state as test_state,
         target.test_id,
         target.fail_reason,
         target.log_file_name,
         tu.name as user_name,
         strategy.pattern,
         desk.desk_ip,
         desk.os_version,
         target.resource_id as desk_id,
         image.cbb_image_type ,
         image.os_type,
         t.terminal_name as terminal_name,
         t.state as terminal_state,
         te.state as test_task_state,
         te.name as test_name,
         td.state as delivery_state,
         td.update_time as delivery_time,
         target.version,
         platform.id as platform_id,
         platform.name as platform_name,
         platform.type as platform_type,
         platform.status as platform_status,
         target.create_time
 FROM
         t_cbb_uam_app_test_target target
 LEFT JOIN t_cbb_uam_app_test te on
         target.test_id = te.id
 LEFT JOIN t_cbb_uam_app_test_detail td ON
         target.resource_id = td.resource_id
         and td.test_id = te.id
 LEFT JOIN t_cbb_desk_info desk ON
         target.resource_id = desk.desk_id
 LEFT JOIN t_rco_user_desktop u_desk ON
         target.resource_id = u_desk.cbb_desktop_id
 LEFT JOIN t_base_iac_user tu ON
         u_desk.user_id = tu.id
 LEFT JOIN t_cbb_desk_strategy strategy ON
         strategy.id = desk.strategy_id
 LEFT JOIN t_cbb_image_template image ON
         image.id = desk.image_template_id
 LEFT JOIN t_cbb_hciadapter_platform platform ON
        image.platform_id=platform.id
 LEFT JOIN t_cbb_terminal t ON
         t.terminal_id = u_desk.terminal_id;

-- 测试桌面应用详情
CREATE VIEW v_rco_uam_app_test_desk_app_detail AS
SELECT
    test_detail.id ,
	test_detail.state as app_test_state,
	test_detail.update_time,
	test_detail.test_id,
	app.app_name,
 	test_detail.resource_id as desk_id,
 	app.app_type,
 	test_detail.app_id,
 	test_detail.fail_reason,
 	test_detail.version
FROM
	t_cbb_uam_app_test_detail test_detail
LEFT JOIN t_cbb_uam_app  app ON
	test_detail.app_id = app.id;

-- 测试桌面应用详情
CREATE VIEW v_rco_uam_app_test_application_detail AS
SELECT
    application.id ,
    app.id as app_id,
	application.app_type,
	application.create_time,
	application.test_id,
	app.app_name,
 	application.version
FROM
	t_cbb_uam_app_test_application application
LEFT JOIN t_cbb_uam_app  app ON
	application.app_id = app.id;


-- 用户组与桌面关联视图
create view v_rco_user_group_desktop_related as
select
	g.id as user_group_id,
	g."name" user_group_name,
	u.id user_id,
	u."name" user_name,
	d.cbb_desktop_id as cloud_desktop_id,
	d.desktop_name,
	i.desk_state,
	s.pattern as desktop_type,
	it.id image_template_id,
	it.image_template_name,
	it.root_image_id,
	it.root_image_name,
	case when i.desk_type = 'THIRD' then i.os_type else it.os_type end as os_type,
	it.cbb_image_type,
    it.before_edit_guest_tool_version,
	i.os_version,
	i.timestamp create_time,
    i.latest_running_time,
	t.state,
	t.terminal_name,
	t.ip,
	t.group_id as terminal_group_id,
	platform.id as platform_id,
	platform.name as platform_name,
    platform.type as platform_type,
    platform.status as platform_status,
	g.version
from
    t_base_iac_user_group g
inner join t_base_iac_user u on
	g.id = u.group_id
inner join t_rco_user_desktop d on
	u.id = d.user_id
inner join t_cbb_desk_info i on
	d.cbb_desktop_id = i.desk_id
inner join t_cbb_desk_strategy s on
	i.strategy_id = s.id
left join t_cbb_image_template it on
	i.image_template_id = it.id
left join t_cbb_hciadapter_platform platform on
    platform.id=i.platform_id
left join t_rco_user_terminal ut on
	ut.bind_desk_id = i.desk_id
left join t_cbb_terminal t on
	t.terminal_id = ut.terminal_id;


-- 终端组与桌面关联视图 只展示所有TCI/IDV终端的云桌面
create view v_rco_terminal_group_desktop_related as
select
	tg.id terminal_group_id,
	tg."name" terminal_group_name,
	t.terminal_id,
	t.terminal_name,
	d.cbb_desktop_id as cloud_desktop_id,
	d.desktop_name,
	i.desk_state,
	i.os_version,
	s.pattern as desktop_type,
	it.id image_template_id,
	it.image_template_name,
	it.os_type,
	it.cbb_image_type,
	t.state,
	t.ip,
	u.group_id as user_group_id,
	u."name" as user_name,
	i.timestamp create_time,
	tg.version
from
	t_cbb_terminal_group tg
inner join t_cbb_terminal t on
	tg.id = t.group_id
	and t.platform in ('IDV', 'VOI')
inner join t_rco_user_terminal ut on
	t.terminal_id = ut.terminal_id
inner join t_rco_user_desktop d on
	ut.terminal_id = d.terminal_id
inner join t_cbb_desk_info i on
	d.cbb_desktop_id = i.desk_id
inner join t_cbb_desk_strategy s on
	i.strategy_id = s.id
inner join t_cbb_image_template it on
	i.image_template_id = it.id
left join t_base_iac_user u on
	u.id = d.user_id;



-- 用户桌面视图
create view v_rco_uam_user_desktop as
select
	i.desk_id cloud_desktop_id,
	i.name as cloud_desktop_name,
	i.desk_ip,
	t.image_template_name,
	t.cbb_image_type ,
	s.pattern as desktop_type,
	u.id as user_id,
	u."name" as user_name,
	i.version
from
	t_cbb_desk_info i
left join t_cbb_desk_strategy s on
	i.strategy_id = s.id
left join t_cbb_image_template t on
	i.image_template_id = t.id
left join t_rco_user_desktop d on
	d.cbb_desktop_id = i.desk_id
left join t_base_iac_user u on
	u.id = d.user_id ;



-- 交付组中所含的交付对象的规格集
create view v_rco_uam_delivery_group_object_spec as
select
	o.delivery_group_id,
	t.cbb_image_type,
	t.os_type,
	t.version
from
	t_cbb_uam_delivery_object o
inner join
	t_cbb_desk_info i on
	o.cloud_desktop_id = i.desk_id
left join t_cbb_desk_strategy s on
	i.strategy_id = s.id
left join t_cbb_image_template t on
	i.image_template_id = t.id
group by
	o.delivery_group_id,
	t.cbb_image_type,
	t.os_type,
	t.version;


-- 测试任务视图
create view v_rco_uam_app_test as
select
	u_t.id,
	u_t.name,
	u_t.state,
	u_t.image_template_id,
	t.image_template_name,
	t.root_image_id,
	t.root_image_name,
	u_t.app_software_package_type,
	u_t.os_type,
	u_t.os_version,
	u_t.update_time,
	u_t.create_time,
	u_t.has_app_update,
	u_t.version
from
	t_cbb_uam_app_test u_t left join t_cbb_image_template t on u_t.image_template_id = t.id;


-- 交付组中桌面跟镜像的关联视图
create view v_cbb_desktop_image_related as
SELECT
    i.desk_id AS desk_id,
    ts.pattern AS desktop_type,
    i."name" AS desktop_name,
    i.os_version,
    it.id as image_template_id,
    it.os_type AS desktop_image_type,
    it.cbb_image_type AS cbb_image_type,
    i."version" AS "version"
FROM
    t_cbb_desk_info i
        INNER JOIN t_cbb_desk_strategy ts ON ts.id = i.strategy_id
        INNER JOIN t_cbb_image_template it ON i.image_template_id = it.id;


CREATE VIEW v_cbb_uam_app_version_relative_desk AS
SELECT
    i.desk_id,
    i.name,
    i.desk_type,
    i.desk_ip,
    i.desk_mac,
    i.desk_state,
    v.app_software_package_id as app_id,
    v.id as app_version_id,
    v.state as app_version_state,
    u.name as user_name,
    p.app_software_package_type,
    i.version
FROM
t_cbb_uam_app_software_package_version v
JOIN t_cbb_uam_app_software_package p ON p.id=v.app_software_package_id
JOIN t_cbb_disk d ON v.disk_id = d.image_id
JOIN t_cbb_desk_info i ON i.desk_id =d.desk_id
JOIN t_rco_user_desktop ud ON ud.cbb_desktop_id=i.desk_id
JOIN t_base_iac_user u ON u.id=ud.user_id;


CREATE VIEW v_rco_distribute_task AS
select
	t.id,
	t.task_name,
	t.create_time,
	sum(case when st.status = 'WAITING' then 1 else 0 end) as waiting_num,
	sum(case when st.status = 'RUNNING' then 1 else 0 end) as running_num,
	sum(case when st.status = 'SUCCESS' then 1 else 0 end) as success_num,
	sum(case when st.status = 'FAIL' then 1 else 0 end) as fail_num,
	sum(case when st.status = 'CANCELED' then 1 else 0 end) as canceled_num,
	t.version
from
	t_rco_distribute_task t
left join t_rco_distribute_sub_task st on
	st.parent_task_id = t.id
group by
	t.id,
	t.task_name,
	t.create_time;



-- 安全打印审计统计视图
create view  v_rco_audit_print_log_static as(select
	trafpi.id id,
	traa.id apply_id,
	traa.apply_type apply_type,
	traa.apply_serial_number apply_serial_number,
	traa.apply_reason apply_reason,
	traa.user_id user_id,
	traa.user_name user_name,
	tcug.id group_id,
	tcug.name group_name,
	traa.desktop_id desktop_id,
	traa.desktop_name desktop_name,
	traa.desktop_mac desktop_mac,
	traa.desktop_ip desktop_ip,
	traa.terminal_id terminal_id,
	traa.terminal_name terminal_name,
	traa.terminal_ip terminal_ip,
	traa.terminal_type terminal_type,
	traf.id file_id,
	traf.file_name file_name,
	traf.file_state file_state,
	traf.file_server_storage_path file_server_storage_path,
	trafpi.printer_name printer_name,
	trafpi.print_process_name print_process_name,
	trafpi.print_page_count print_page_count,
	trafpi.print_time print_time,
	trafpi.print_paper_size print_paper_size,
	trafpi.print_state print_state,
	trafpi.version
from
	t_rco_audit_apply traa
left join t_rco_audit_file traf on
	traa.id = traf.apply_id
left join t_rco_audit_file_print_info trafpi on
	traf.id = trafpi.file_id
left join t_base_iac_user tcu on
    traa.user_id = tcu.id
left join t_base_iac_user_group tcug on
    tcu.group_id = tcug.id);





-- 桌面GT状态视图视图
CREATE VIEW v_cbb_user_desktop_guesttool AS SELECT
    dt_u.id,
    dt_u.desktop_name,
    dt_u.cbb_desktop_id,
    dt_stg_v.pattern,
    dt_info.latest_running_time,
    dt_info.guest_tool_version,
    dt_info.desk_type,
    dt_info.desk_state,
    img_tpl.before_edit_guest_tool_version,
    img_tpl.cbb_image_type,
    img_tpl.os_type,
    dt_info.desk_create_mode,
    dt_gt.gt_heart_beat_time,
    dt_gt.module_info,
    dt_u.version
FROM
    t_rco_user_desktop dt_u
        JOIN t_cbb_desk_info dt_info ON dt_u.cbb_desktop_id = dt_info.desk_id
        JOIN t_cbb_desk_strategy dt_stg_v ON dt_info.strategy_id = dt_stg_v.
        ID LEFT JOIN t_cbb_image_template img_tpl ON img_tpl.ID = dt_info.image_template_id
        LEFT JOIN t_cbb_desk_gt_info dt_gt ON dt_gt.ID = dt_info.desk_id;



CREATE VIEW v_rco_user_group_sync_data AS
select
	ug.id,
	ug.name,
	ug.full_group_name,
	ug.depth,
	ug.is_ad_group,
	ug.is_ldap_group,
	ug.is_third_party_group,
	ug.enable_random_password,
	ug.ad_user_authority,
	ug.account_expire_date,
	ug.invalid_time,
	c.id as user_identity_config_id,
	c.related_type,
	c.related_id,
	c.login_identity_level,
	c.open_hardware_certification,
	c.max_hardware_num,
	c.open_otp_certification,
	c.has_bind_otp,
	c.otp_secret_key,
	c.open_cas_certification,
	c.open_account_password_certification,
	c.open_sms_certification,
	c.open_third_party_certification,
	c.open_radius_certification,
	ug.update_time,
	ug."version"
from
	(with recursive cbb_user_group as (
	select
		g.*,
		g.name::text full_group_name,
		1 as depth
	from
        t_base_iac_user_group g
	where
		g.parent_id is null
union all
	select
		g1.*,
		CONCAT(g2.full_group_name,
		'>',
		g1.name) as full_group_name,
		g2.depth + 1
	from
        t_base_iac_user_group g1
	inner join cbb_user_group g2 on
		g2.id = g1.parent_id
)
	select
		*
	from
		cbb_user_group) ug
left join t_base_iac_user_identity_config c
	on
	ug.id = c.related_id;


--
CREATE VIEW v_rco_user_sync_data AS
select
	u.id,
	u.name,
	u.real_name,
	ug.full_group_name,
	ug.depth,
	u.password,
	u.phone_num,
	u.email,
	u.need_update_password,
	u.reset_password_by_admin,
	u.description,
	u.user_type,
	u.state,
	u.ad_user_authority,
	u.is_user_modify_password,
	u.update_time,
	u.account_expire_date,
	u.enable_domain_sync,
	u.ad_group_json,
	u.invalid_time,
	u.invalid_recover_time,
	u.login_out_time,
	u.invalid,
	c.id as user_identity_config_id,
	c.related_type,
	c.related_id,
	c.login_identity_level,
	c.open_hardware_certification,
	c.max_hardware_num,
	c.open_otp_certification,
	c.has_bind_otp,
	c.otp_secret_key,
	c.open_cas_certification,
	c.open_account_password_certification,
	c.open_sms_certification,
	c.open_third_party_certification,
	c.open_radius_certification,
	u."version"
from
    t_base_iac_user u
inner join
(with recursive cbb_user_group as (
	select
		g.*,
		g.name::text full_group_name,
		1 as depth
	from
        t_base_iac_user_group g
	where
		g.parent_id is null
union all
	select
		g1.*,
		CONCAT(g2.full_group_name,
		'>',
		g1.name) as full_group_name,
		g2.depth + 1
	from
        t_base_iac_user_group g1
	inner join cbb_user_group g2 on
		g2.id = g1.parent_id
)
	select
		*
	from
		cbb_user_group) ug on
	u.group_id = ug.id
left join t_base_iac_user_identity_config c
	on
	u.id = c.related_id;

-- 用户软终端视图
create view v_cbb_user_soft_terminal AS
SELECT
       cbb.terminal_id,
       cbb.terminal_name,
       cbb.state as terminal_state,
       cbb.rain_os_version,
       cbb.hardware_version,
       cbb.rain_upgrade_version,
       cbb.last_online_time,
       cbb.last_offline_time,
       cbb.mac_addr,
       cbb.disk_size,
       cbb.cpu_type,
       cbb.ip,
       cbb.memory_size,
       cbb.product_type,
       cbb.product_id,
       cbb.platform,
       cbb.terminal_os_type,
       cbb.serial_number,
       cbb.network_access_mode,
       cbb.subnet_mask,
       cbb.gateway,
       cbb.main_dns,
       cbb.second_dns,
       cbb.get_ip_mode,
       cbb.get_dns_mode,
       cbb.ssid,
       cbb.wireless_auth_mode,
       cbb.network_infos,
       cbb.wireless_net_card_num,
       cbb.ethernet_net_card_num,
       cbb.all_disk_info,
       cbb.authed,
       cbb.ocs_sn,
       cbb.enable_proxy,
       cbb.real_terminal_id,
       cbb.client_version,
       ut.enable_visitor_login,
       ut.enable_auto_login,
       ut.version,
       ut.id,
       ut.create_time,
       ut.terminal_mode as idv_terminal_mode,
       ut.bind_user_id,
       ut.bind_user_name,
       ut.bind_desk_id,
       cbb.group_id as terminal_group_id,
       ut.user_id,
       t_group.name as terminal_group_name,
       ut.has_login,
       (cbb.last_online_time > coalesce(cbb.last_offline_time, '1970-01-01 00:00:00'::timestamp without time zone)) AS has_online,
       uic.open_otp_certification,
       uic.has_bind_otp,
       au.lock,
       au.lock_time,
       au.unlock_time,
       au.pwd_error_times,
       ut.boot_type,
       cbb.support_tc_start,
       cbb.support_remote_wake,
       cbb.nic_work_mode,
       auth.auth_mode as auth_mode,
       un.name as user_name,
       case
           when auth.license_duration = 'TEMPORARY' THEN  'TEMPORARY'
           when auth.license_type = 'VOI_2' or auth.license_type = 'VOI_1' then 'VOI'
           when auth.license_type = 'VDI_1' or auth.license_type = 'VDI_2' then 'VDI'
           when auth.license_type = 'VOI_2,VOI_PLUS_UPGRADED' or auth.license_type = 'VOI_1,VOI_PLUS_UPGRADED' then 'VOI,VOI_PLUS_UPGRADED'
           else auth.license_type
           end as auth_type
FROM ((((((( select cbb_1.id as real_terminal_id,
                    cbb_1.terminal_name,
                    cbb_1.terminal_id,
                    cbb_1.mac_addr,
                    cbb_1.ip,
                    cbb_1.subnet_mask,
                    cbb_1.gateway,
                    cbb_1.main_dns,
                    cbb_1.second_dns,
                    cbb_1.get_ip_mode,
                    cbb_1.get_dns_mode,
                    cbb_1.product_type,
                    cbb_1.terminal_type,
                    cbb_1.serial_number,
                    cbb_1.cpu_type,
                    cbb_1.memory_size,
                    cbb_1.disk_size,
                    cbb_1.terminal_os_type,
                    cbb_1.terminal_os_version,
                    cbb_1.rain_os_version,
                    cbb_1.rain_upgrade_version,
                    cbb_1.hardware_version,
                    cbb_1.network_access_mode,
                    cbb_1.create_time,
                    cbb_1.last_online_time,
                    cbb_1.last_offline_time,
                    cbb_1.version,
                    cbb_1.state,
                    cbb_1.platform,
                    cbb_1.group_id,
                    cbb_1.os_inner_version,
                    cbb_1.product_id,
                    cbb_1.ssid,
                    cbb_1.wireless_auth_mode,
                    cbb_1.network_infos,
                    cbb_1.wireless_net_card_num,
                    cbb_1.ethernet_net_card_num,
                    cbb_1.all_disk_info,
                    cbb_1.authed,
                    cbb_1.ocs_sn,
                    case
                        when (cbb_1.enable_proxy is null) then false
                        else cbb_1.enable_proxy
                        end as enable_proxy,
                    cbb_1.support_tc_start,
                    cbb_1.support_remote_wake,
                    cbb_1.nic_work_mode,
                    cbb_1.client_version
             from t_cbb_terminal cbb_1) cbb
    JOIN t_rco_user_terminal ut ON (((cbb.terminal_id)::text = (ut . terminal_id)::text)))
    JOIN (select t_cbb_terminal_group.id,
                 t_cbb_terminal_group.name,
                 t_cbb_terminal_group.parent_id,
                 t_cbb_terminal_group.create_time,
                 t_cbb_terminal_group.version
          from t_cbb_terminal_group) t_group ON ((cbb.group_id = t_group.id)))
    )
         ) )
     LEFT JOIN ( select t_base_iac_user_identity_config.open_otp_certification,
            t_base_iac_user_identity_config.related_id,
            t_base_iac_user_identity_config.has_bind_otp
           from t_base_iac_user_identity_config) uic ON ((ut.bind_user_id = uic.related_id))
    )
    LEFT JOIN ( select name,id FROM t_base_iac_user) un
    ON ((un.id = ut.user_id))
    LEFT JOIN ( select resource_id,lock,lock_time,unlock_time,pwd_error_times
    from t_rco_authentication where TYPE = 'TERMINAL') au
    ON cbb.real_terminal_id = au.resource_id
        LEFT JOIN (
        select
        t_cbb_terminal_authorize.license_type,
        t_cbb_terminal_authorize.terminal_id,
        t_cbb_terminal_authorize.license_duration,
        t_cbb_terminal_authorize.auth_mode
        from t_cbb_terminal_authorize) auth
        ON cbb.terminal_id = auth.terminal_id
    ORDER BY ut.create_time DESC;


-- 用户终端桌面简单视图
create view v_cbb_user_terminal_desk AS
SELECT u_desk.id,
       u_desk.desktop_name,
       u_desk.cbb_desktop_id ,
       u_desk.terminal_id,
       u_desk.version,
       u_desk.has_terminal_running
FROM t_rco_user_desktop u_desk
         LEFT JOIN (SELECT t_cbb_desk_info.desk_id,
                           t_cbb_desk_info.desk_mac,
                           t_cbb_desk_info.desk_ip,
                           t_cbb_desk_info.NAME,
                           t_cbb_desk_info.image_template_id,
                           t_cbb_desk_info.strategy_id
                    FROM t_cbb_desk_info) di
                   ON (((u_desk.cbb_desktop_id)
        :: TEXT = ( di.desk_id ) :: TEXT ) );

-- 用户信息报表视图
CREATE VIEW v_cbb_user_login_record AS
SELECT
    tculr.id,
    tculr.user_id,
    tculr.version,
    tculr.user_name,
    tculr.user_group_id,
    tculr.user_group_name,
    tculr.terminal_id,
    tculr.terminal_ip,
    tculr.terminal_mac,
    tculr.terminal_name,
    tculr.desk_type,
    tculr.desk_strategy_pattern,
    tculr.desk_id,
    tculr.desk_name,
    tculr.computer_name,
    tculr.desk_ip,
    tculr.desk_mac,
    tculr.desk_system,
    tculr.desk_image,
    tculr.desk_strategy,
    tculr.session_state,
    tculr.create_time,
    tculr.login_time,
    tculr.auth_duration,
    tculr.user_type,
    tculr.connect_time,
    tculr.connect_duration,
    get_use_duration(tculr.session_state, tculr.connect_time, tculr.use_duration) as use_duration,
    tculr.logout_time,
    tculr.connection_id
FROM t_cbb_user_login_record tculr;

-- 用于同步用户到rccm
CREATE VIEW v_rco_rccm_sync_user AS
select
	u.id,
	u."name" as user_name,
	u."version"
from
    t_base_iac_user u
where
	exists (
	select
		1
	from
		t_rco_desktop_pool_user pu
	where
		(pu.related_type = 'USER'
			and u.id = pu.related_id)
		or (pu.related_type = 'USERGROUP'
			and u.group_id = pu.related_id))
	or exists (
	select
		1
	from
		(
		select
			count(*) as desktop_num,
			ud.user_id
		from
			t_rco_user_desktop ud,
			t_cbb_desk_info cbb_desk
		where
			ud.cbb_desktop_id = cbb_desk.desk_id
			and cbb_desk.is_delete = false
		group by
			ud.user_id
              ) dt
	where
		dt.user_id = u.id
		and desktop_num > 0
                );

--不到现场编辑镜像 终端选择列表视图
CREATE OR REPLACE VIEW v_rco_terminal_with_image_info as
select
	random() as id,
	a.ip,
	a.cpu_arch cpu_arch_type,
	upper(a.mac_addr::text) as mac_addr,
	a.product_type,
	a.state as terminal_state,
	a.terminal_id,
	upper(a.terminal_name::text) as terminal_name_upper,
	t_group.id as terminal_group_id,
	t_group.name as terminal_group_name,
	a.terminal_name,
	a.download_update_time,
	a.start_mode,
	(case when (a.product_id not like '800%' and a.product_type not like 'RG-%') then 'other' ::text  else a.product_id end) as product_id,
	a.bind_user_name,
	a.terminal_mode,
	a.bind_desk_id,
	a.desk_ip ,
	case
    	when a.idv_terminal_mode is null then 'UNKNOWN' :: varchar(32)
    	else a.idv_terminal_mode
    end as idv_terminal_mode ,
	a.enable_proxy ,
	a.network_access_mode,
	a.image_id::character varying as image_id,
	case
		when a.drive_count > 0 then 'INSTALL'::text
		else 'UNINSTALL'::text
	end as install_drive_state,
	case
		when a.download_state_suc > 0 then 'COMPLETED'::text
		else 'UNCOMPLETED'::text
	end as download_state,
	1 as version
from
	(
	select
		c.terminal_name,
		c.cpu_arch,
		c.cpu_type,
		c.product_type,
		c.state,
		c.ip,
		c.mac_addr,
		c.id,
		c.terminal_id,
		m.id as image_id,
		c.group_id,
		c.product_id,
		ut.bind_user_name,
		ut.terminal_mode,
		ut.bind_desk_id,
		ut.desk_ip,
		ut.terminal_mode AS idv_terminal_mode,
		c.network_access_mode,
		(
		select
			u.boot_type
		from
			t_rco_user_terminal u
		where
			u.terminal_id::text = c.terminal_id::text
		limit 1) as start_mode,

		 CASE
                    WHEN c.enable_proxy IS NULL THEN false
                    ELSE c.enable_proxy
                END AS enable_proxy,

		(
		select
			t.update_time
		from
			t_rco_image_download_state t
		where
			c.mac_addr::text = t.terminal_id::text
			and t.image_id = m.id
			and t.download_state::text = 'SUCCESS'::text) as download_update_time,
		(
		select
			count(1) as count
		from
			t_rco_image_download_state t
		where
			c.terminal_id::text = t.terminal_id::text
			and m.last_recovery_point_id::character varying::text = t.image_recovery_point_id::text
			and t.image_id = m.id) as download_state_suc,
		( select
		     count(*)
	from
			t_cbb_image_template_driver d
	where
			c.cpu_type = d.driver_type::text
		and d.image_id = m.id and d.hardware_version = c.hardware_version ) as drive_count
	from
		t_cbb_image_template m,
		t_cbb_terminal c
	left join (
		select
		user_terminal.terminal_id ,
			user_terminal.bind_user_name,
			user_terminal.terminal_mode,
			user_terminal.bind_desk_id,
			desk.desk_ip
		from
			t_rco_user_terminal user_terminal,
			t_cbb_desk_info desk
		where
			bind_desk_id = desk.desk_id
			) ut on
		c.terminal_id = ut.terminal_id
	where
		(c.platform = m.cbb_image_type or ut.terminal_mode is null) and (strpos(c.support_work_mode , 'IDV') >0 or strpos(c.support_work_mode , 'VOI') >0)
		) a,
	t_cbb_terminal_group t_group
where
	a.group_id = t_group.id
order by
	download_state,
	A.download_update_time desc nulls last,
	install_drive_state asc nulls last,
	A.state desc nulls last,
	A.terminal_name asc;

create view v_rco_audit_apply as
SELECT
     afa.id,
     afa.apply_serial_number,
     afa.apply_reason,
     afa.total_file_size,
     afa.total_file_count,
     afa.total_file_page,
     afa.apply_type,
     afa.state,
     afa.alarm_ids,
     afa.user_id,
     afa.user_name,
     afa.desktop_id,
     afa.desktop_name,
     afa.desktop_mac,
     afa.desktop_ip,
     afa.terminal_id,
     afa.terminal_name,
     afa.terminal_ip,
     afa.terminal_mac,
     afa.terminal_type,
     afa.create_time,
     afa.update_time,
     afa.version,
     afa.fail_reason,
     afa.file_name,
     afa.desktop_pool_id,
     afa.desktop_pool_type,
     tcug.id group_id,
     tcug.name group_name
FROM t_rco_audit_apply afa
left join t_base_iac_user tcu on afa.user_id = tcu.id
left join t_base_iac_user_group tcug on tcu.group_id = tcug.id;

create view v_rco_audit_apply_print_log as
select
	trafpi.id id,
	traa.id apply_id,
	traa.apply_serial_number apply_serial_number,
	traa.state apply_state,
	traa.total_file_count total_file_count,
	traa.total_file_size total_file_size,
	traa.apply_reason apply_reason,
	traa.user_id user_id,
	traa.user_name user_name,
	tcug.id group_id,
	tcug.name group_name,
	traa.desktop_id desktop_id,
	traa.desktop_name desktop_name,
	traa.desktop_mac desktop_mac,
	traa.desktop_ip desktop_ip,
	traa.terminal_id terminal_id,
	traa.terminal_name terminal_name,
	traa.terminal_ip terminal_ip,
	traa.terminal_mac terminal_mac,
	traa.terminal_type terminal_type,
	traa.fail_reason,
	traf.id file_id,
	traf.file_name file_name,
	traf.file_state file_state,
	traf.file_server_storage_path file_server_storage_path,
	trafpi.printer_name printer_name,
	trafpi.print_process_name print_process_name,
	trafpi.print_page_count print_page_count,
	trafpi.print_time print_time,
	trafpi.print_paper_size print_paper_size,
	trafpi.print_state print_state,
	trafpi.print_result_msg print_result_msg,
	trafpi.version
from
	t_rco_audit_apply traa
left join t_rco_audit_file traf on
	traa.id = traf.apply_id
left join t_rco_audit_file_print_info trafpi on
	traf.id = trafpi.file_id
left join t_base_iac_user tcu on
    traa.user_id = tcu.id
left join t_base_iac_user_group tcug on
    tcu.group_id = tcug.id
where
	apply_type = 'PRINT';

create view v_rco_disk_pool_user_assignment as
SELECT
    dpu.id,
    dpu.disk_pool_id,
    dpu.related_id,
    dpu.related_type,
    dpu.create_time,
    dpu.VERSION,
    cu.NAME user_name,
    cug.NAME user_group_name
FROM
    t_rco_disk_pool_user dpu
        LEFT JOIN t_base_iac_user cu ON dpu.related_id = cu.ID
        AND dpu.related_type = 'USER'
        LEFT JOIN t_base_iac_user_group cug ON dpu.related_id = cug.ID
        AND dpu.related_type = 'USERGROUP';

-- 派生云主机桌面的详情表
CREATE
OR REPLACE VIEW v_cbb_rca_host_desktop_detail AS SELECT
                                                     all_dt.id,
                                                     all_dt.cbb_desktop_id,
                                                     all_rh.rca_host_id,
                                                     all_rh.rca_pool_id,
                                                     all_rh.rca_host_session_type,
                                                     all_rh.rca_pool_type,
                                                     all_rh.rca_pool_name,
                                                     all_rh.rca_max_session_count,
                                                     all_rh.rca_pre_start_host_num,
                                                     all_rh.rca_session_hold_time,
                                                     all_rh.rca_load_balance_mode,
                                                     all_rh.rca_session_hold_config_mode,
                                                     all_rh.rca_days_left,
                                                     all_rh.is_expire,
                                                     all_rh.one_agent_version,
                                                     all_dt.cbb_strategy_id,
                                                     all_dt.cbb_network_id,
                                                     all_dt.terminal_id,
                                                     all_dt.VERSION,
                                                     all_dt.desktop_type,
                                                     all_dt.is_delete,
                                                     all_dt.delete_time,
                                                     all_dt.create_time,
                                                     all_dt.desktop_name,
                                                     all_dt.desk_mac,
                                                     all_dt.ip,
                                                     all_dt.desk_state,
                                                     all_dt.desk_type,
                                                     all_dt.is_windows_os_active,
                                                     all_dt.os_active_by_system,
                                                     all_dt.image_template_id,
                                                     all_dt.config_ip,
                                                     all_dt.strategy_name,
                                                     all_dt.pattern,
                                                     all_dt.system_size,
                                                     all_dt.cpu,
                                                     all_dt.memory,
                                                     all_dt.person_size,
                                                     all_dt.enable_hyper_visor_improve,
                                                     all_dt.vgpu_type,
                                                     all_dt.vgpu_extra_info,
                                                     all_dt.latest_login_time,
                                                     all_dt.latest_running_time,
                                                     all_dt.image_template_name,
                                                     all_dt.root_image_name,
                                                     all_dt.root_image_id,
                                                     all_dt.image_role_type,
                                                     all_dt.before_edit_guest_tool_version,
                                                     all_dt.will_apply_image_id,
                                                     all_dt.os_type,
                                                     all_dt.cbb_image_type,
                                                     all_dt.physical_server_id,
                                                     all_dt.physical_server_ip,
                                                     all_dt.has_auto_join_domain,
                                                     all_dt.host_name,
                                                     all_dt.desktop_role,
                                                     all_dt.product_type,
                                                     all_dt.terminal_platform,
                                                     all_dt.last_online_time,
                                                     all_dt.enable_proxy,
                                                     all_dt.computer_name,
                                                     all_dt.has_terminal_running,
                                                     all_dt.remark,
                                                     all_dt.enable_web_client,
                                                     all_dt.enable_custom,
                                                     all_dt.desk_create_mode,
                                                     all_dt.connect_closed_time,
                                                     all_dt.fault_state,
                                                     all_dt.fault_description,
                                                     all_dt.fault_time,
                                                     all_dt.network_name,
                                                     all_dt.image_usage,
                                                     all_dt.cluster_id,
                                                     all_dt.is_open_desk_maintenance,
                                                     all_dt.os_version,
                                                     all_dt.has_login,
                                                     all_dt.platform_id,
                                                     all_dt.platform_name,
                                                     all_dt.platform_type,
                                                     all_dt.platform_status,
                                                     all_dt.register_state,
                                                     all_dt.register_message
                                                 FROM
                                                     (
                                                         SELECT
                                                             rh.ID AS rca_host_id,
                                                             rh.pool_id AS rca_pool_id,
                                                             rh.max_session_count AS rca_max_session_count,
                                                             rh.host_session_type AS rca_host_session_type,
                                                             rh.days_left AS rca_days_left,
                                                             rh.is_expire AS is_expire,
                                                             rh.one_agent_version,
                                                             rp.pool_type AS rca_pool_type,
                                                             rp.NAME AS rca_pool_name,
                                                             rp.pre_start_host_num AS rca_pre_start_host_num,
                                                             rp.session_hold_time AS rca_session_hold_time,
                                                             rp.load_balance_mode AS rca_load_balance_mode,
                                                             rp.session_hold_config_mode AS rca_session_hold_config_mode
                                                         FROM
                                                             t_cbb_rca_host rh,
                                                             t_cbb_rca_pool rp
                                                         WHERE
                                                             ( ( rh.pool_id ) :: TEXT = ( rp.ID ) :: TEXT )
                                                     ) all_rh
	JOIN v_cbb_user_desktop_detail all_dt ON all_dt.cbb_desktop_id = all_rh.rca_host_id;


-- 用户对应的应用分组列表视图
create or replace view v_rco_user_rca_group as
select
	a_pool.name as pool_name,
    a_group_member.id,
    a_group_member.name,
    a_group_member.pool_id,
    a_group_member.description,
    a_group_member.default_group,
    a_group_member.create_time,
    a_group_member.update_time,
    a_group_member.member_id,
    a_group_member.member_type,
    a_group_member.version
    FROM
    (select
        a_group.id,
        a_group.name,
        a_group.pool_id,
        a_group.description,
        a_group.default_group,
        a_group.create_time,
        a_group.update_time,
        a_group.version,
        a_member.member_id,
        a_member.member_type
    FROM t_cbb_rca_group a_group left
        join t_cbb_rca_group_member a_member on a_group.id = a_member.group_id) a_group_member
    left join t_cbb_rca_pool a_pool on a_group_member.pool_id = a_pool.id;

-- 会话管理-会话信息视图
CREATE OR REPLACE VIEW v_rco_desktop_session as
select
     tdus.id,
     tdus.desk_id,
     tcdi.name as desktop_name,
     tct.ip as terminal_ip,
     tcdpi.id as desktop_pool_id,
     tcdpi.name as desktop_pool_name,
     tcdpi.pool_model as desktop_pool_model,
     tdus.session_status,
     tdus.user_id,
     tcdi.desk_state,
     u.name AS user_name,
     u.real_name,
     u.group_id AS user_group_id,
     g.name AS user_group_name,
     tdus.session_id,
     tdus.last_create_time,
     case
        when tdus.last_create_time is not null then
        cast(extract(EPOCH from (current_timestamp - tdus.last_create_time)) as BIGINT)
        else null
     end as last_create_time_second,
     tdus.last_idle_time,
     case
        when tdus.last_idle_time is not null then
        cast(extract(EPOCH from (current_timestamp - tdus.last_idle_time)) as BIGINT)
     else null
     end as last_idle_time_second,
     tdus.version,
     tdus.create_time,
     tdus.update_time
from t_rco_desk_user_session tdus
left join t_base_iac_user u on u.id::text = tdus.user_id::text
left join t_base_iac_user_group g on u.group_id::text = g.id::text
left join t_cbb_terminal tct on tct.id::text = tdus.terminal_id::text
left join t_cbb_desk_info tcdi on tcdi.desk_id::text = tdus.desk_id::text
left join t_cbb_desktop_pool_info tcdpi on tcdpi.id::text = tcdi.desktop_pool_id::text
where tdus.session_id is not null;

--桌面池与PC终端关系视图
create or replace view v_rco_desktop_pool_computer
as
  select
     pool_computer.desktop_pool_id,
     pool_computer.related_type,
     pool_computer.related_id,
     pool_computer.version,
     pool_computer.id,
     computer.group_id,
     computer.ip,
     computer.mac,
     computer.alias,
     computer.os,
     computer.agent_version,
     computer.type,
     computer.work_model,
     computer.disk,
     computer.cpu,
     computer.memory,
     computer.create_time,
     computer.state,
     computer.system_disk,
     computer.person_disk,
	 computer.name
  from t_rco_desktop_pool_computer pool_computer
	 JOIN t_rco_computer computer ON pool_computer.related_id=computer.id;

--桌面与用户关系视图
create  or replace view v_rco_host_user as
  select
      u.id,
      hu.id as host_id,
      u.user_role,
      hu.desktop_pool_id,
      hu.desktop_id,
      u.user_name,
      u.group_id,
      u.group_name,
      u.real_name,
      u.user_type,
      u.password,
      u.phone_num,
      u.email,
      u.state,
      (COALESCE(u.desktop_num::bigint,0)+COALESCE(dt.desktop_num::bigint,0)) as desktop_num,
      u.create_time,
      u.update_time,
      u.version,
      u.ad_user_authority,
      u.need_update_password,
      u.account_expire_date,
      u.enable_domain_sync,
      u.invalid_time,
      u.invalid_recover_time,
      u.login_out_time,
      u.invalid,
      u.last_login_terminal_ip,
      u.last_login_terminal_time,
      u.is_user_modify_password,
      u.reset_password_by_admin,
      u.lock,
      u.lock_time,
      u.unlock_time,
      u.pwd_error_times,
      u.last_login_time,
      u.update_password_time,
      ( CASE WHEN u.open_otp_certification IS NULL THEN FALSE ELSE u.open_otp_certification END ) AS open_otp_certification,
  	( CASE WHEN u.open_radius_certification IS NULL THEN FALSE ELSE u.open_radius_certification END) AS open_radius_certification,
  	( CASE WHEN u.has_bind_otp IS NULL THEN FALSE ELSE u.has_bind_otp END ) AS has_bind_otp,
  	(case when u.open_cas_certification is null then false else u.open_cas_certification end) as open_cas_certification,
  	(case when u.open_account_password_certification is null then false else u.open_account_password_certification end) as open_account_password_certification,
  	(case when u.open_third_party_certification is null then false else u.open_third_party_certification end) as open_third_party_certification,
  	(case when u.open_hardware_certification is null then false else u.open_hardware_certification end) as
      open_hardware_certification,
      u.max_hardware_num,
      ( CASE WHEN u.open_sms_certification IS NULL THEN FALSE ELSE u.open_sms_certification END ) AS open_sms_certification,
      u.user_description,
      u.auth_mode as auth_mode,
      u.license_type as license_type,
      u.license_duration as license_duration
    FROM t_rco_host_user hu
   LEFT JOIN v_rco_user u on hu.user_id=u.id
   LEFT JOIN (
           select
             count(*) as desktop_num,
             rhu.user_id
           from t_rco_host_user rhu, (select *
           from t_cbb_desk_info
                   ) cbb_desk
           where ((rhu.desktop_id = cbb_desk.desk_id) and (cbb_desk.is_delete = FALSE))
           group by rhu.user_id
         ) dt ON dt.user_id = u.id;

-- 云桌面快照视图
create  or replace view v_rco_desk_snapshot as
select
	sp.*,
	pm.id as platform_id,
	pm.status as platform_status,
	pm."name" as platform_name,
	pm."type" as platform_type
from t_cbb_desk_snapshot sp
left join t_cbb_desk_info i on sp.desk_id=i.desk_id
left join t_cbb_hciadapter_platform pm on pm.id=i.platform_id;

-- 桌面关联的所有用户信息
create or replace view v_rco_desk_user_relation as
select
	tcdi.desk_id,
	tcdi.desktop_pool_id,
	tcdi.desk_state,
	coalesce(trud.user_id, trhu.user_id) as user_id,
	tcdi.session_type,
	tcdi.desktop_pool_type,
	coalesce(trud."version", trhu."version", 0) as version
from t_cbb_desk_info tcdi
left join t_rco_user_desktop trud on tcdi.desk_id = trud.cbb_desktop_id and tcdi.session_type = 'SINGLE'
left join t_rco_host_user trhu on trhu.desktop_id = tcdi.desk_id and tcdi.session_type = 'MULTIPLE';

