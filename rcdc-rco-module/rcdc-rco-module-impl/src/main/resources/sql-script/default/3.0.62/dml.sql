
-- 新增云桌面策略推荐记录
INSERT INTO t_rco_user_desk_strategy_recommend ("id", "create_time", "name", "pattern", "cpu", "memory", "system_size", "personal_size", "is_allow_internet", "is_open_usb_read_only", "is_show", "version", "clip_board_mode", "is_open_double_screen", "cloud_desk_type", "is_allow_local_disk", "is_open_desktop_redirect")
VALUES ('a499ef87-2fac-4bd4-bb2f-4804329fa7a8', now(), 'TCI普通桌面', 'PERSONAL', NULL, NULL, 60, NULL, 'f', 'f', 't', 0, NULL, NULL, 'VOI', 't', 't');

INSERT INTO t_rco_user_desk_strategy_recommend ("id", "create_time", "name", "pattern", "cpu", "memory", "system_size", "personal_size", "is_allow_internet", "is_open_usb_read_only", "is_show", "version", "clip_board_mode", "is_open_double_screen", "cloud_desk_type", "is_allow_local_disk", "is_open_desktop_redirect")
VALUES ('46475c28-bbda-4ddf-bb2f-20766df57248', now(), '国产化TCI桌面', 'PERSONAL', NULL, NULL, 40, NULL, 'f', 'f', 't', 0, NULL, NULL, 'VOI', 't', 'f');