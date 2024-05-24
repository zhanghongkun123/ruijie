INSERT INTO public.t_rco_image_type_support_osversion (id,cbb_image_type,os_type,os_version,"version") VALUES
	 ('359aa6b4-3d60-4adc-a639-c9f0da7e611f','VOI','UOS_64','uos_1060',0),
	 ('2621f2c4-7521-4076-be28-dde7a0e018e3','VDI','UOS_64','uos_1042',0),
	 ('14b0677c-a49b-494a-b5e1-25084cd7fad5','VDI','UOS_64','uos_1032',0),
	 ('ce518a97-67b4-4cb7-9a68-87b6cb0fc97f','VDI','UOS_64','uos_1022',0),
	 ('c0893cfd-aabc-406f-9347-fe56c69ac9da','VDI','KYLIN_64','kylin_2101',0),
	 ('981c47e8-675d-4212-b9fa-c8305adcaa8a','VOI','KYLIN_64','kylin_GFB',0),
	 ('6901fc8f-ded4-4e33-b232-f16a432e64f2','VOI','KYLIN_64','kylin_2303',0),
	 ('d4c2606b-6d5c-4324-8f68-23f2db93868f','IDV','UOS_64','uos_1042',0),
	 ('ab972d7d-0b5b-434d-bc26-f53796937904','IDV','UOS_64','uos_1032',0),
	 ('887edcda-b1a0-4098-9a62-3487e11f6e39','IDV','UOS_64','uos_1022',0) ON CONFLICT(cbb_image_type,os_type,os_version) DO NOTHING;
