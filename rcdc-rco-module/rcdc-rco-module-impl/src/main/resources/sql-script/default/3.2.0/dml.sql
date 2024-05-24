
-- VDI授权兼容IDV/TCI终端
INSERT INTO t_sk_global_parameter (id,param_key, param_value, default_value, create_time, update_time,version)
VALUES ('4c1719bd-099f-47f7-8a46-d3f1c1fbd904','enable_auth_compatible', 'false', 'false', now(), now(),0);

INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619600', 'RG-CT5200-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619601', 'RG-CT5300-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619602', 'RG-CT5500-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619603', 'RG-CT5700-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619604', 'RG-CT6200-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619605', 'RG-CT6300-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('7768cb9f-c458-4030-8da7-ab40b1619606', 'RG-CT6500-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;

INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('edf7aefe-4e21-4591-a17e-14fd4b411e39', 'hasGenSnKey', 'false', 'false', now(), now(), 0);