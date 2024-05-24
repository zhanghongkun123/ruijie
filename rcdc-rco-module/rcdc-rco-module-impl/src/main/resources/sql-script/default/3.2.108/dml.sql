INSERT INTO public.t_rco_image_type_support_terminal (id,cbb_image_type,os_type,product_type,"version") VALUES
('7948c8b4-f80a-4d59-976d-907318e3fcc3','VOI','UOS_64','RG-CT7800-1000',0),
('16e46a16-6e2a-4b1e-8438-948e055aa64c','VOI','UOS_64','RG-CT7800-2000',0),
('ded815d1-df36-475c-a671-fa5666a936ed','VOI','UOS_64','RG-CT7900',0),
('a28ed199-7274-41f1-8226-590a20400657','VOI','UOS_64','RG-CT7900 V3.00',0),
('14af42b5-5714-4b02-91cb-185b012d06af','VOI','KYLIN_64','RG-CT7800-1000',0),
('022ccbf3-ec7a-45aa-9339-b2c67d61dd52','VOI','KYLIN_64','RG-CT7800-2000',0),
('5c2fad95-805e-4265-abec-06fae62419ca','VOI','KYLIN_64','RG-CT7900',0),
('7f4e8796-9957-443d-8306-3011989796bb','VOI','KYLIN_64','RG-CT7900 V3.00',0)  ON CONFLICT(cbb_image_type,os_type,product_type) DO NOTHING;

--TODO:转测后删除
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('3bb86f86-a16d-4e4c-84c4-1f14d46f72e9', 'RG-CT5200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('f3df5227-4d9f-4aa9-a7c8-c89bb9ca4c5b', 'RG-CT5300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('a98ee913-69de-4e61-84dc-14508d9dc79d', 'RG-CT5500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('897e52c2-2415-47a8-86b8-2cfe870935c9', 'RG-CT6200C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('0a065eca-6158-4347-9378-6c7e4d31fb57', 'RG-CT6300C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version)
VALUES ('3d35510b-229e-4e53-871e-fa33c914e1fc', 'RG-CT6500C-G4', 'IDV,VOI', 'product', true, now(), now(), 0) ON CONFLICT(id) DO NOTHING;


