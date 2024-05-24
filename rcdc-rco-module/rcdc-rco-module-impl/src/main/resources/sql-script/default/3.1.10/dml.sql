 -- 动态口令修改默认时长
update
	t_rco_global_parameter
set
	param_value = '{"openOtp":false,"otpType":"totp","algorithm":"SHA1","period": 60,"digits":6,"systemName":"RCDC-V5.3","systemHost":"ruijie.com.cn","hasOtpCodeTab":false,"timeDifferent":""}',
    default_value = '{"openOtp":false,"otpType":"totp","algorithm":"SHA1","period": 60,"digits":6,"systemName":"RCDC-V5.3","systemHost":"ruijie.com.cn","hasOtpCodeTab":false,"timeDifferent":""}'
where
	param_key = 'certification_otp_summary';