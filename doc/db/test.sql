select * from xxl_job_qrtz_trigger_info
where id in (
    SELECT
	DISTINCT(job_id)
    FROM XXL_JOB_QRTZ_TRIGGER_LOG
    WHERE
        job_group = 6
    AND TIMESTAMPDIFF(
        SECOND,
        trigger_time,
        handle_time
    ) > 60
)

alter table XXL_JOB_QRTZ_TRIGGER_LOG rename to XXL_JOB_QRTZ_TRIGGER_LOG_20200310001;
create table XXL_JOB_QRTZ_TRIGGER_LOG like  XXL_JOB_QRTZ_TRIGGER_LOG_20200310001;