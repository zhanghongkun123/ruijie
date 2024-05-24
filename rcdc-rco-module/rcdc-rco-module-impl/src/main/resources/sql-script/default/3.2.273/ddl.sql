-- 删除旧的唯一索引
DO
$$BEGIN
ALTER TABLE t_rco_user_hardware_certification DROP CONSTRAINT t_rco_user_hardware_certification_user_id_terminal_id_unique;
EXCEPTION
   WHEN undefined_object
      THEN NULL;  -- 忽略不存在的异常，防止重复执行出错
END$$;

-- 删除重复数据，防止后面创建索引失败
DELETE FROM t_rco_user_hardware_certification WHERE id IN (
    SELECT id FROM (
        SELECT id, user_id, mac_addr, state, ROW_NUMBER(*) over (partition by user_id, mac_addr ORDER BY state) row_num
		FROM t_rco_user_hardware_certification) tmp
	WHERE row_num > 1
);

-- 新的版本里面，用户id+mac地址为唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS  t_rco_user_hardware_certification_user_id_mac_addr_unique
    ON t_rco_user_hardware_certification ("user_id", "mac_addr");