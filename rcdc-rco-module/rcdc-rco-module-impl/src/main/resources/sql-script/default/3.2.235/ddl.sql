--修订：一个申请单关联的告警ID可能有多个把关联告警ID字段改成text
ALTER TABLE t_rco_audit_apply RENAME alarm_id TO alarm_ids;
ALTER TABLE t_rco_audit_apply ALTER COLUMN alarm_ids TYPE text;