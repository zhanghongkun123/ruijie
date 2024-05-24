-- EST广域网配置
INSERT INTO
    t_rco_global_parameter ( "id", "param_key", "param_value", "default_value", "create_time", "update_time", "version" )
VALUES
	( '9d0ec766-c3b5-354c-b60b-c85388d60d60', 'est_wan_config',
	'{"adaptdisplay":1,"adaptsound":1,"bitrate":16000,"customize":0,"enable_web_advance_setting":0,"faststreammode":1,"framerate":30,"minframerate":20,"name":"CLEAR_FIRST","quality":1,"reencode":1,"snd_playback":1,"snd_quality":1,"snd_udp":1,"transport":1,"videobitrate":10000,"videocodec":0}',
	'{"adaptdisplay":1,"adaptsound":1,"bitrate":16000,"customize":0,"enable_web_advance_setting":0,"faststreammode":1,"framerate":30,"minframerate":20,"name":"CLEAR_FIRST","quality":1,"reencode":1,"snd_playback":1,"snd_quality":1,"snd_udp":1,"transport":1,"videobitrate":10000,"videocodec":0}',
	now(), now(), 0 );

-- EST局域网配置
INSERT INTO
    t_rco_global_parameter ( "id", "param_key", "param_value", "default_value", "create_time", "update_time", "version" )
VALUES
	('78948f68-acdb-f1b8-63d2-1b739d140bfb', 'est_lan_config',
	'{"adaptdisplay":1,"adaptsound":1,"bitrate":16000,"customize":0,"enable_web_advance_setting":0,"faststreammode":1,"framerate":30,"minframerate":20,"name":"CLEAR_FIRST","quality":1,"reencode":1,"snd_playback":1,"snd_quality":1,"snd_udp":1,"transport":1,"videobitrate":10000,"videocodec":0}',
	'{"adaptdisplay":1,"adaptsound":1,"bitrate":16000,"customize":0,"enable_web_advance_setting":0,"faststreammode":1,"framerate":30,"minframerate":20,"name":"CLEAR_FIRST","quality":1,"reencode":1,"snd_playback":1,"snd_quality":1,"snd_udp":1,"transport":1,"videobitrate":10000,"videocodec":0}',
	now(), now(), 0 );

---AD域用户全局开关
INSERT INTO t_rco_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('92816b9f-ef9d-43e2-bd34-30b8bae1a5f3', 'ad_auto_logon', 'true', 'true', now(), now(), 0);