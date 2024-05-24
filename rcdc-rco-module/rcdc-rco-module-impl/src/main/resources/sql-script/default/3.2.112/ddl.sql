-- 将文件分发结果中的message字段修改为text类型
ALTER TABLE t_rco_distribute_sub_task ALTER COLUMN message TYPE TEXT;