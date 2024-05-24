package com.ruijie.rcos.rcdc.rco.module.impl.sql;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月26日
 *
 * @author 徐国祥
 */
public interface SqlConstants {
    String QUERY_USER_DESKTOP_SQL =
            "select " +
                "dt_info.desk_id as id, " +
                "imtpl.image_template_name as image_name, " +
                "CASE " +
                "        WHEN dt_info.os_type != '' THEN dt_info.os_type " +
                "        WHEN dt_info.desk_create_mode = 'FULL_CLONE' THEN dt_info.os_type " +
                "        ELSE imtpl.os_type " +
                "    END AS os_name, " +
                "dt_info.desk_state as desktop_state, " +
                "dt_info.name as desktop_name, " +
                "dt_info.remark, " +
                "cd_strategy.enable_agreement_agency, " +
                "cd_strategy.enable_web_client, " +
                "    dt_info.desktop_pool_id, " +
                "dt_info.desktop_pool_type, " +
                "case " +
                "when dt_info.desktop_pool_type in ('STATIC', 'DYNAMIC') then true " +
                "else false " +
                "end as is_open, " +
                "case " +
                "when ch_platform.status in ('ONLINE', 'EDITING') then false " +
                "else true " +
                "end as is_offline, " +
                "dt_info.session_type, " +
                "ru_desk.desktop_type as desktop_category, " +
                "dt_info.timestamp as create_time, " +
                "imtpl.image_usage " +
            "from t_rco_user_desktop ru_desk " +
            "left join t_cbb_desk_info dt_info on ru_desk.cbb_desktop_id = dt_info.desk_id  " +
            "left join t_cbb_image_template imtpl on dt_info.image_template_id = imtpl.id " +
            "left join t_cbb_desk_strategy cd_strategy on dt_info.strategy_id = cd_strategy.id " +
            "left join t_cbb_hciadapter_platform ch_platform on dt_info.platform_id = ch_platform.id " +
            "where ru_desk.user_id = ? and dt_info.is_delete = ?;";

    String QUERY_DESKTOP_OS_TYPE_SQL =
            "select " +
            "   CASE " +
            "       WHEN (dt_info.desk_create_mode = 'FULL_CLONE') THEN img_tpl.os_type " +
            "       WHEN dt_info.os_type is null THEN img_tpl.os_type " +
            "       ELSE dt_info.os_type " +
            "   END " +
            "   AS os_type " +
            "from t_cbb_desk_info dt_info " +
            "left join t_cbb_image_template img_tpl on dt_info.image_template_id = img_tpl.id " +
            "where dt_info.desk_id =?;";
}
