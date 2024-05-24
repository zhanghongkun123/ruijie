--配置终端模式映射关系
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('d85ee3d1-f65b-4f6d-8ad0-c2b16046c5f7', 'IDV,VDI,VOI', 'IDV', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('f0dd34ec-a1f0-4498-b9f4-43099ca0be6b', 'IDV,VDI', 'IDV', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('0b5cc851-2adb-4c2e-aab9-addbfbfa193d', 'IDV,VOI', 'IDV', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('4e11e654-803a-4b0f-a224-89972147d122', 'IDV', 'IDV', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('6fa987f5-c458-4030-8da7-ab40b161962b', 'VDI,VOI', 'VDI', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('b774e84b-855b-4da6-a403-e3075862d10d', 'VDI', 'VDI', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('4e0e3508-3514-46bc-b7ed-a3b1e900a051', 'PC', 'PC', 'mode', true, now(), now(), 0);

insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
values ('5386b5ea-72c4-42d2-bf04-4b602714ad2f', 'RG-CT3120', 'VDI', 'product', true, now(), now(), 0);

-- 打印机特殊配置插入
insert into t_rco_printer_special_config(id, config_version, config_content, config_md5, version, file_name, create_time)
values ('e2ebad29-e50c-44eb-bcfc-765dab3b1d81', 1604634860, 'eNqtkU1Lw0AQhs1nt6NgOiBeRHL0ItiiIIiHNrFUbWswrUEawdVsaqDNlt2NRf+p/8a0IvVuLgMzh2dmnhe+bHgkFo5BJ1twiReSp2pJBYvn2avgqy6OsjzhS+kOR7FXCMFy9cCEzHgeByLL1U8tJyXCBkDivdF8yq59xyzR2gYd3nVHUfv+Kh5UgabExKeqrt7GekAXTITZJwMLjbNmy9HgmRCMq1rRwF2fpbSYKddn7wOeMMeAiNgYVrWBoB0qqgq5tmNs7PxX/K+dIZ3/sTMhdYyqun0HwROMKl5GkJRiUlLDl6rg+7jny1v2MV4kVLEuF2wqeJEnpacJ0at7QifWKoS1K+lo2IMDaITDjud2RsFx/7TldoJ+G2tgWXaN1OEIDnuB26eSiRum3EDwlMkVnM7coNk8OUcbTE03zG/RehvY', '8225f2bc9dfcad6d07abbfa3eb451e70', 0, 'spec_printer.conf', now());
