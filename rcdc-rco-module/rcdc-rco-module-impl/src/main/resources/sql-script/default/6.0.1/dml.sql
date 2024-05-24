INSERT INTO public.t_rco_user_profile_path (id,group_id,"name",description,import_user_profile_path_type,update_time,create_time,"version",creator_user_name,is_default,extra_config_info)
VALUES ('9d3a8d54-8f3f-4e25-8edf-6f252f7805f4','476e2c79-73fa-4107-8587-400121894234','打印机配置','支持标准打印机设备和配置。如有特殊打印机，选择该项的同时，还需新增并选择特殊打印机相关配置路径','SPECIAL',now(),now(),0,'admin',true,'{"config_printers_to_save":"1"}') ON CONFLICT(id) DO NOTHING;

INSERT INTO public.t_rco_user_profile_child_path (id,"mode","type",user_profile_path_id,"index","version") VALUES
('51194b97-d1aa-4530-b962-c69f7294b943','SYNCHRO','FOLDER','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',0,0),
('08d15291-6070-40cc-b2cc-ba3658812820','SYNCHRO','REGISTRY_KEY','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',1,0),
('08d15291-6070-40cc-b2cc-ba3658812831','SYNCHRO','DOCUMENT','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',2,0) ON CONFLICT(id) DO NOTHING;

INSERT INTO public.t_rco_user_profile_path_detail (id,"path",user_profile_child_path_id,user_profile_path_id,"index","version") VALUES
('e047c17f-e193-42db-bf11-a2f473821bd9'::uuid,'%USERPROFILE%\AppData\Roaming\Microsoft\Credentials','51194b97-d1aa-4530-b962-c69f7294b943','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',0,0),
('f9af711a-3fcf-4503-8559-2ae347d65553'::uuid,'HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Print','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',0,0),
('6fed42c2-4dff-454e-a5a8-808154ccb3ae'::uuid,'HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Ports','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',1,0),
('12bc2458-88b5-4777-99ce-99d46c528c72'::uuid,'HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows NT\CurrentVersion\Print','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',2,0),
('37797a50-7d2e-40f8-aed1-debcc2046bf8'::uuid,'HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows NT\CurrentVersion\Ports','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',3,0),
('c9457706-54ac-4603-9557-3026fcc6beb4'::uuid,'HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Print','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',4,0),
('90069e72-0127-4884-b85f-2613f5ebe0ab'::uuid,'HKEY_CURRENT_USER\Printers','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',5,0),
('64b46f49-58a6-431d-baff-0b67451c289f'::uuid,'HKEY_CURRENT_USER\Software\Microsoft\Windows NT\CurrentVersion\Devices','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',6,0),
('804eb4b2-bca3-4461-b7ad-ee08ef8abd18'::uuid,'HKEY_CURRENT_USER\Software\Microsoft\Windows NT\CurrentVersion\PrinterPorts','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',7,0),
('63891cac-b6b0-4e13-95ac-b77dac434343'::uuid,'HKEY_CURRENT_USER\Software\Microsoft\Windows NT\CurrentVersion\Windows','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',8,0),
('63891cac-b6b0-4e13-95ac-b77dac434344'::uuid,'Windows\System32\drivers\WSDPrint.sys','08d15291-6070-40cc-b2cc-ba3658812831','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',9,0),
('63891cac-b6b0-4e13-95ac-b77dac434345'::uuid,'HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\DeviceAssociationService','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',10,0),
('63891cac-b6b0-4e13-95ac-b77dac434346'::uuid,'HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WSDPrintDevice','08d15291-6070-40cc-b2cc-ba3658812820','9d3a8d54-8f3f-4e25-8edf-6f252f7805f4',11,0) ON CONFLICT(id) DO NOTHING;



INSERT INTO "t_rco_global_parameter"("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
 VALUES ('58e7c03a-fb0c-4408-a34f-c0275506ad3d', 'wake_terminal_source_port', '32767', '32767', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

INSERT INTO "t_rco_global_parameter"("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
 VALUES ('58e7c03a-fb0c-4408-a34f-c0275506ad4d', 'wake_terminal_dest_port', '9', '9', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

UPDATE public.t_rco_user_profile_path
SET description = '包括Windows便笺数据备份(Windows7及以上操作系统)'
WHERE id = '99b5a12d-4875-01e7-0f13-40c3b7b54ae1';

DELETE FROM t_rco_user_profile_path_detail WHERE ID = 'e047c17f-e193-42db-bf11-a2f473821bd9';


-- 系统已经填充硬件版本数据
INSERT INTO t_sk_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('bb350734-c8e3-85f0-cd30-21ea45cb1ad6', 'has_fill_hardware_version', 'false', 'false', now(), now(), 0)
    ON CONFLICT(param_key) DO NOTHING;