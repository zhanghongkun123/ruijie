-- 推送安装包格式去除 rpm
UPDATE t_sk_global_parameter
SET param_value='{"windows":["msi","msu","exe","cab"],"linux":["deb","sh"],"linuxZip":["tar","tar.gz","tgz","tar.bz2","tar.bz","tar.Z","gz","bz2","tar.xz"],"common":["zip","rar"]}'
WHERE param_key='push_installpackage_support_extension';
-- 推荐策略表默认EST协议配置修订
UPDATE t_rco_user_desk_strategy_recommend
SET agreement_info='{"lanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":16000,"framerate":30,"reencode":1,"quality":0,"transport":1,"enableSsl":false,"sndQuality":1,"enableWebAdvanceSetting":0},"wanEstConfig":{"templateId":1,"enableCustomTemplate":false,"bitrate":10000,"framerate":30,"reencode":0,"quality":0,"transport":1,"enableSsl":false,"sndQuality":1,"enableWebAdvanceSetting":0}}'
WHERE id in ('916a79ae-1936-40e5-9cc8-a1ad674c0368','964f119e-eee5-4490-a676-350d72585209');