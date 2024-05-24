INSERT INTO t_rco_user_profile_path_group VALUES ('476e2c79-73fa-4107-8587-400121894234', '常用配置', null, now(), now());

/**windows自带文件夹配置*/
INSERT INTO t_rco_user_profile_path VALUES ('bcc50c59-3475-0bec-73ea-c967a43439a4', '476e2c79-73fa-4107-8587-400121894234', 'Windows文件夹',
 'windows系统自带的文件夹', 'SPECIAL', now(), '2022-05-31 09:56:11.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('034842b4-5927-c792-51ff-aa8b9a02f936',  'SYNCHRO', 'FOLDER', 'bcc50c59-3475-0bec-73ea-c967a43439a4',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-3e9e4c06a03d', '%USERPROFILE%\Contacts', '034842b4-5927-c792-51ff-aa8b9a02f936',
 'bcc50c59-3475-0bec-73ea-c967a43439a4', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('c061e88b-b41e-435b-ac5e-4359f26b9be7', '%USERPROFILE%\Links', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 1, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-5e9e4c06a03d', '%USERPROFILE%\Videos', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 2, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-4e9e4c06a03d', '%USERPROFILE%\Favorites', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 3, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('9d46e741-c631-4de3-954e-3115a0b1b660', '%USERPROFILE%\Searches', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 4, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('601ae358-79f8-48dd-ae49-36752d74364d', '%USERPROFILE%\Pictures', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 5, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('aa55b3d8-e284-4a5b-af1a-1328e2031f96', '%USERPROFILE%\Documents', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 6, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e4c06a03d', '%USERPROFILE%\Music', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 7, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('d4ef6cf8-23c7-484b-a666-9721e3aed118', '%USERPROFILE%\Desktop', '034842b4-5927-c792-51ff-aa8b9a02f936',
'bcc50c59-3475-0bec-73ea-c967a43439a4', 8, 0);


/**windows系统的便签*/
INSERT INTO t_rco_user_profile_path VALUES ('99b5a12d-4875-01e7-0f13-40c3b7b54ae1', '476e2c79-73fa-4107-8587-400121894234', 'Windows便笺',
 '包括Windows便笺数据备份(win10)、Windows7便笺', 'SPECIAL', now(), '2022-05-31 09:56:12.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('78a70909-cde4-309c-cb1f-fa3e329157f4',  'SYNCHRO', 'FOLDER', '99b5a12d-4875-01e7-0f13-40c3b7b54ae1',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('773aa509-f0fa-4237-b08b-5adc84543041','%localappdata%\Packages\Microsoft.MicrosoftStickyNotes_8wekyb3d8bbwe',
'78a70909-cde4-309c-cb1f-fa3e329157f4', '99b5a12d-4875-01e7-0f13-40c3b7b54ae1', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('48c420e6-0281-4299-b19f-7fbce4160538','%USERPROFILE%\AppData\Roaming\Microsoft\Sticky Notes',
'78a70909-cde4-309c-cb1f-fa3e329157f4', '99b5a12d-4875-01e7-0f13-40c3b7b54ae1', 1, 0);

/**Windows桌面背景*/
INSERT INTO t_rco_user_profile_path VALUES ('3f830bb2-7829-77ae-f991-6d09c7ee4b80', '476e2c79-73fa-4107-8587-400121894234', 'Windows桌面背景',
 'Windows桌面背景', 'SPECIAL', now(), '2022-05-31 09:56:14.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('9b2d3cf1-81f2-2da9-e018-a14f3f2400b8',  'SYNCHRO', 'FOLDER', '3f830bb2-7829-77ae-f991-6d09c7ee4b80',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-9e9e4c06a03d', '%APPDATA%\Microsoft\Windows\Themes',
'9b2d3cf1-81f2-2da9-e018-a14f3f2400b8', '3f830bb2-7829-77ae-f991-6d09c7ee4b80', 0, 0);

/**Windows桌面图标*/
INSERT INTO t_rco_user_profile_path VALUES ('296cb47e-82f6-b2b0-1d93-dda600a57648', '476e2c79-73fa-4107-8587-400121894234', 'Windows桌面图标',
 '个性化配置中选择桌面要显示哪些桌面图标（计算机、用户文档、控制面板等），不保存快捷方式图标信息', 'SPECIAL', now(), '2022-05-31 09:56:17.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('0d4df0d9-699b-41f2-7347-c364ba213ff0',  'SYNCHRO', 'REGISTRY_KEY', '296cb47e-82f6-b2b0-1d93-dda600a57648',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('fc6a3785-1608-47bb-a4a1-6a59b32bd202', 'HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\HideDesktopIcons\NewStartPanel',
'0d4df0d9-699b-41f2-7347-c364ba213ff0', '296cb47e-82f6-b2b0-1d93-dda600a57648', 0, 0);

/**Windows桌面图标排序*/
INSERT INTO t_rco_user_profile_path VALUES ('40d6ea91-3440-486f-93a6-cb0ca2811824', '476e2c79-73fa-4107-8587-400121894234', 'Windows桌面图标排序',
 '记录桌面图标排序', 'SPECIAL', now(), '2022-05-31 09:56:17.217', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('b8c2e685-1475-414b-b387-119330549d29',  'SYNCHRO', 'REGISTRY_KEY', '40d6ea91-3440-486f-93a6-cb0ca2811824',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('380b950c-b582-4e6a-bd3a-1da505663b7c', 'HKEY_CURRENT_USER\Software\Microsoft\Windows\Shell\Bags',
'b8c2e685-1475-414b-b387-119330549d29', '40d6ea91-3440-486f-93a6-cb0ca2811824', 0, 0);


/**Windows键盘与输入法*/
INSERT INTO t_rco_user_profile_path VALUES ('2ff65afd-e2ad-1365-1eae-49e6853b11d3', '476e2c79-73fa-4107-8587-400121894234', 'Windows键盘与输入法',
 'Windows键盘布局、默认输入法设置、输入法列表顺序、用户安装输入法热键设置、系统自带输入法热键设置', 'SPECIAL', now(), '2022-05-31 09:56:18.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('64d3081b-e685-dff1-11f2-9acda0dc0668',  'SYNCHRO', 'REGISTRY_KEY', '2ff65afd-e2ad-1365-1eae-49e6853b11d3',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('8b96da93-5b42-4554-8023-958b2c18aad0', 'HKEY_CURRENT_USER\Keyboard Layout\Preload',
'64d3081b-e685-dff1-11f2-9acda0dc0668', '2ff65afd-e2ad-1365-1eae-49e6853b11d3', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('d4cd536d-c1bd-4bd1-a481-d9b91f5dff8a', 'HKEY_CURRENT_USER\Software\Microsoft\CTF\Assemblies',
'64d3081b-e685-dff1-11f2-9acda0dc0668', '2ff65afd-e2ad-1365-1eae-49e6853b11d3', 1, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('d3bbc414-b287-4435-8eae-eb0a3f18b63f', 'HKEY_CURRENT_USER\Software\Microsoft\CTF\SortOrder\AssemblyItem',
'64d3081b-e685-dff1-11f2-9acda0dc0668', '2ff65afd-e2ad-1365-1eae-49e6853b11d3', 2, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e7c06a03d', 'HKEY_CURRENT_USER\Control Panel\Input Method\Hot Keys',
'64d3081b-e685-dff1-11f2-9acda0dc0668', '2ff65afd-e2ad-1365-1eae-49e6853b11d3', 3, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e8c06a03d', 'HKEY_CURRENT_USER\Software\Microsoft\CTF\DirectSwitchHotkeys',
'64d3081b-e685-dff1-11f2-9acda0dc0668', '2ff65afd-e2ad-1365-1eae-49e6853b11d3', 4, 0);



/**Win7网络驱动器*/
INSERT INTO t_rco_user_profile_path VALUES ('d259fd29-89e3-6312-ca22-7f40a427ecca', '476e2c79-73fa-4107-8587-400121894234', 'Win7网络驱动器信息',
  '包括Win7网络驱动器的网络位置信息和要连接的文件夹列表', 'SPECIAL', now(), '2022-05-31 09:56:19.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('27205d7c-e9d3-24fd-1991-7661304997a0',  'SYNCHRO', 'REGISTRY_KEY', 'd259fd29-89e3-6312-ca22-7f40a427ecca',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e6c06a03d', 'HKEY_CURRENT_USER\Network',
'27205d7c-e9d3-24fd-1991-7661304997a0', 'd259fd29-89e3-6312-ca22-7f40a427ecca', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-6e9e4c06a03d', 'HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Map Network Drive MRU',
'27205d7c-e9d3-24fd-1991-7661304997a0', 'd259fd29-89e3-6312-ca22-7f40a427ecca', 1, 0);


/**文件夹重定向配置*/
INSERT INTO t_rco_user_profile_path VALUES ('0680816e-3aa8-ca5c-c124-30727fe74a5c', '476e2c79-73fa-4107-8587-400121894234', '文件夹重定向配置',
  '记录文件夹的文件存储位置', 'SPECIAL', now(), '2022-05-31 09:56:20.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('4745b697-f8ee-6d85-7ebc-a02fea415b72',  'SYNCHRO', 'REGISTRY_KEY', '0680816e-3aa8-ca5c-c124-30727fe74a5c',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e5c06a03d','HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Folders',
 '4745b697-f8ee-6d85-7ebc-a02fea415b72', '0680816e-3aa8-ca5c-c124-30727fe74a5c', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('56c097a2-fdf6-4dc6-88bb-1e9e5c06a02d','HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\User Shell Folders',
 '4745b697-f8ee-6d85-7ebc-a02fea415b72', '0680816e-3aa8-ca5c-c124-30727fe74a5c', 1, 0);


/**IE浏览器Internet选项*/
INSERT INTO t_rco_user_profile_path VALUES ('8b782e0a-0f1c-4fc8-80d0-8d4bf1d4533c', '476e2c79-73fa-4107-8587-400121894234', 'IE浏览器Internet选项',
  'IE浏览器Internet选项', 'SPECIAL', now(), '2022-05-31 09:56:21.208', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('b7ee3b69-f314-4e7e-9d05-efd3666e641a',  'SYNCHRO', 'REGISTRY_KEY', '8b782e0a-0f1c-4fc8-80d0-8d4bf1d4533c',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('ac59dd07-688a-4b77-b390-4f82de74403c', 'HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer',
 'b7ee3b69-f314-4e7e-9d05-efd3666e641a','8b782e0a-0f1c-4fc8-80d0-8d4bf1d4533c', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('acedfe43-1dcd-4c78-a088-10edb1f0f8c3', 'HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings',
'b7ee3b69-f314-4e7e-9d05-efd3666e641a', '8b782e0a-0f1c-4fc8-80d0-8d4bf1d4533c', 1, 0);

/**搜狗五笔输入法*/
INSERT INTO t_rco_user_profile_path VALUES ('b0ad67fc-1a23-461f-a93b-8c941d0516ec', '476e2c79-73fa-4107-8587-400121894234', '搜狗五笔输入法',
  '支持五笔拼音的属性设置', 'SPECIAL', now(), '2022-05-31 09:56:21.209', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('f5a93253-da5f-4bd1-9ac6-be938f232562',  'SYNCHRO', 'FOLDER', 'b0ad67fc-1a23-461f-a93b-8c941d0516ec',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('99917447-23b2-43eb-916b-6315a6db2f89', '%USERPROFILE%\AppData\LocalLow\SogouWB',
 'f5a93253-da5f-4bd1-9ac6-be938f232562','b0ad67fc-1a23-461f-a93b-8c941d0516ec', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('ccf4b25f-4563-483f-a007-341eeb5fb503', '%USERPROFILE%\AppData\LocalLow\SogouWB.users',
'f5a93253-da5f-4bd1-9ac6-be938f232562', 'b0ad67fc-1a23-461f-a93b-8c941d0516ec', 1, 0);

/**搜狗拼音输入法*/
INSERT INTO t_rco_user_profile_path VALUES ('b8ac36c9-5885-4672-99c5-5bfed925cc49', '476e2c79-73fa-4107-8587-400121894234', '搜狗拼音输入法',
  '支持搜狗拼音的属性设置', 'SPECIAL', now(), '2022-05-31 09:56:21.211', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('f48ba90f-4f75-4465-afda-41e761d7b2dc',  'SYNCHRO', 'FOLDER', 'b8ac36c9-5885-4672-99c5-5bfed925cc49',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('36344ec2-58f7-4122-80f9-9ca35c855d99', '%USERPROFILE%\AppData\LocalLow\SogouPY.users',
 'f48ba90f-4f75-4465-afda-41e761d7b2dc','b8ac36c9-5885-4672-99c5-5bfed925cc49', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('3c5f04c7-ae0f-482e-ba8b-0baf87166c2c', '%USERPROFILE%\AppData\LocalLow\SogouPY',
'f48ba90f-4f75-4465-afda-41e761d7b2dc', 'b8ac36c9-5885-4672-99c5-5bfed925cc49', 1, 0);

/**QQ五笔输入法*/
INSERT INTO t_rco_user_profile_path VALUES ('a49e481c-aefa-4d80-bcd3-25d4d5bdf121', '476e2c79-73fa-4107-8587-400121894234', 'QQ五笔输入法',
 '支持QQ五笔的属性设置', 'SPECIAL', now(), '2022-05-31 09:56:16.214', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('3d571743-0d36-45e8-bc1b-db6e9686b731',  'SYNCHRO', 'FOLDER', 'a49e481c-aefa-4d80-bcd3-25d4d5bdf121',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('da379f28-9ac6-4868-abe0-a38edd4b882d', '%APPDATA%\Tencent\QQWubi',
'3d571743-0d36-45e8-bc1b-db6e9686b731', 'a49e481c-aefa-4d80-bcd3-25d4d5bdf121', 0, 0);

/**QQ拼音输入法*/
INSERT INTO t_rco_user_profile_path VALUES ('67a16917-9bc6-4a6e-90e1-b5ebbef1e9ba', '476e2c79-73fa-4107-8587-400121894234', 'QQ拼音输入法',
 '支持QQ五笔的属性设置', 'SPECIAL', now(), '2022-05-31 09:56:16.217', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('5a20176d-522d-4e07-bc8f-b2dadd8079ec',  'SYNCHRO', 'FOLDER', '67a16917-9bc6-4a6e-90e1-b5ebbef1e9ba',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('2b4d9111-e187-4e70-84f3-52f5f510fe25', '%ProgramData%\Tencent\QQPinyin\users',
'5a20176d-522d-4e07-bc8f-b2dadd8079ec', '67a16917-9bc6-4a6e-90e1-b5ebbef1e9ba', 0, 0);

/**360极速浏览器*/
INSERT INTO t_rco_user_profile_path VALUES ('a8dcab34-6e50-4891-a205-a2b7dfdd6e57', '476e2c79-73fa-4107-8587-400121894234', '360极速浏览器',
 '支持收藏夹和历史记录', 'SPECIAL', now(), '2022-05-31 09:56:16.227', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('534be09c-f210-4e23-83f3-957d86178e69',  'SYNCHRO', 'FOLDER', 'a8dcab34-6e50-4891-a205-a2b7dfdd6e57',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('16d4b6cb-d922-4f1b-821f-c09f0076a061', '%LOCALAPPDATA%\360Chrome\Chrome\User Data\Default',
'534be09c-f210-4e23-83f3-957d86178e69', 'a8dcab34-6e50-4891-a205-a2b7dfdd6e57', 0, 0);

/**360安全浏览器*/
INSERT INTO t_rco_user_profile_path VALUES ('35ee3899-e291-402e-b5fe-73351fce27fa', '476e2c79-73fa-4107-8587-400121894234', '360安全浏览器',
 '支持收藏夹和历史记录', 'SPECIAL', now(), '2022-05-31 09:56:16.229', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('3e9886bc-e5c0-426c-bb15-b13a582f64ed',  'SYNCHRO', 'FOLDER', '35ee3899-e291-402e-b5fe-73351fce27fa',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('309a88aa-f506-4b35-ba3a-72bd11267971', '%APPDATA%\360se6\User Data\Default',
'3e9886bc-e5c0-426c-bb15-b13a582f64ed', '35ee3899-e291-402e-b5fe-73351fce27fa', 0, 0);

/**谷歌浏览器*/
INSERT INTO t_rco_user_profile_path VALUES ('dca6c4c9-c5a3-4d37-83b8-bd9f790ee9f7', '476e2c79-73fa-4107-8587-400121894234', '谷歌浏览器',
 '支持收藏夹和历史记录', 'SPECIAL', now(), '2022-05-31 09:56:16.237', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('eff63d5d-eed2-4d12-a0b0-ff064beac6bd',  'SYNCHRO', 'FOLDER', 'dca6c4c9-c5a3-4d37-83b8-bd9f790ee9f7',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('c431c25b-66cc-4811-9c80-b7b4a0dc069f', '%localappdata%\Google\Chrome\User Data\Default',
'eff63d5d-eed2-4d12-a0b0-ff064beac6bd', 'dca6c4c9-c5a3-4d37-83b8-bd9f790ee9f7', 0, 0);

/**用户配置文件*/
INSERT INTO t_rco_user_profile_path VALUES ('d15e8ef8-2615-4aee-838a-998993b8db07', '476e2c79-73fa-4107-8587-400121894234', '用户配置文件',
  '与登录账户相关的桌面设置', 'SPECIAL', now(), '2022-05-31 09:56:21.241', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('9abc7c8b-e4d4-4c83-9647-7b66eced9116',  'SYNCHRO', 'FOLDER', 'd15e8ef8-2615-4aee-838a-998993b8db07',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('2092fdf2-ddb2-4f7e-8c3d-1b4c885cc06b', '%USERPROFILE%',
 '9abc7c8b-e4d4-4c83-9647-7b66eced9116','d15e8ef8-2615-4aee-838a-998993b8db07', 0, 0);

 /**AD域统一配置*/
INSERT INTO t_rco_user_profile_path VALUES ('5e711bc7-21e6-c6ca-126e-d2d770959c27', '476e2c79-73fa-4107-8587-400121894234', 'AD域统一配置(加域桌面必选)',
  'Windows桌面加域场景统一配置，可保存域用户信息，避免每次重新初始化，加快登录过程', 'SPECIAL', now(), '2022-05-31 09:56:21.251', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('0b8998a5-8b41-96fe-8f62-eab765b37fbf',  'SYNCHRO', 'FOLDER', '5e711bc7-21e6-c6ca-126e-d2d770959c27',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('5b80a4d9-6e40-1bac-d591-a52295d1120d', '%USERPROFILE%',
 '0b8998a5-8b41-96fe-8f62-eab765b37fbf','5e711bc7-21e6-c6ca-126e-d2d770959c27', 0, 0);
INSERT INTO t_rco_user_profile_child_path VALUES('e2a99832-a7a1-ae4c-0a8a-a87f8d73fadf',  'SYNCHRO', 'REGISTRY_KEY', '5e711bc7-21e6-c6ca-126e-d2d770959c27',
1, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('a21a0194-d6a1-1cdb-42a3-9a8bb15100c2', 'HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\ProfileList',
 'e2a99832-a7a1-ae4c-0a8a-a87f8d73fadf','5e711bc7-21e6-c6ca-126e-d2d770959c27', 0, 0);
INSERT INTO t_rco_user_profile_child_path VALUES('8f567761-0006-70ce-9c5c-3a88ce129c96',  'EXCLUDE', 'FOLDER', '5e711bc7-21e6-c6ca-126e-d2d770959c27',
2, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('37d8c817-0cca-b972-6f1e-14b05c7ebc18', '%TEMP%',
 '8f567761-0006-70ce-9c5c-3a88ce129c96','5e711bc7-21e6-c6ca-126e-d2d770959c27', 0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('709b9106-d5ad-4df2-98fe-c708d37d5dbe', '%LOCALAPPDATA%\Packages',
 '8f567761-0006-70ce-9c5c-3a88ce129c96','5e711bc7-21e6-c6ca-126e-d2d770959c27', 1, 0);

/**Windows凭据*/
INSERT INTO t_rco_user_profile_path VALUES ('1245de2f-9485-01e5-c411-2ca817eecb7a', '476e2c79-73fa-4107-8587-400121894234', 'Windows凭据',
 '用户为网站、已连接的应用程序和网络保存的登录信息', 'SPECIAL', now(), '2022-05-31 09:56:22.207', 0, 'admin', 'f');
INSERT INTO t_rco_user_profile_child_path VALUES('4b3db8f4-01c1-e270-66a0-7d4867a17cff',  'SYNCHRO', 'FOLDER', '1245de2f-9485-01e5-c411-2ca817eecb7a',
0, 0);
INSERT INTO t_rco_user_profile_path_detail VALUES ('6e02dd0d-6783-4c98-bda6-1e980d60793a','%USERPROFILE%\AppData\Roaming\Microsoft\Credentials', '4b3db8f4-01c1-e270-66a0-7d4867a17cff',
'1245de2f-9485-01e5-c411-2ca817eecb7a', 0, 0);