INSERT INTO public.t_rco_image_type_support_terminal (id,cbb_image_type,os_type,product_type,"version") VALUES
('16dc9a26-4c20-1710-714e-8b98ae636729','VOI','UOS_64','RG-CT7900 V5.00',0),
('a6ba6ac4-ebde-1f45-142c-04b34fcaf475','VOI','KYLIN_64','RG-CT7900 V5.00',0)
ON CONFLICT(cbb_image_type,os_type,product_type) DO NOTHING;
insert into t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
values ('b035b3ea-fc06-50e6-aa8a-631ea3b12b1c', 'RG-CT7900 V5.00', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT7',null)
ON CONFLICT(id) DO NOTHING;

