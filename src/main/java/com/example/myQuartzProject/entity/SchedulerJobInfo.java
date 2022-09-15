package com.example.myQuartzProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
//@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {
//public class SchedulerJobInfo implements Serializable {

//	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy= "uuid2")
	private String jobId;

	@Column(name = "job_name")
	private String jobName;

	@Column(name = "job_group")
	private String jobGroup;

	@Column(name = "job_status")
	private String jobStatus;

	@Column(name = "job_class")
	private String jobClass;

	@Column(name = "cron_expression")
	private String cronExpression;

	@Column(name = "desc")
	private String desc;

	@Column(name = "interface_name")
	private String interfaceName;

	@Column(name = "repeat_time")
	private Long repeatTime;

	@Column(name = "cron_job")
	private Boolean cronJob;

//
//	@Override
//	public String toString() {
//		return "SchedulerJobInfo{" +
//				"jobId='" + jobId + '\'' +
//				", jobName='" + jobName + '\'' +
//				", jobGroup='" + jobGroup + '\'' +
//				", jobStatus='" + jobStatus + '\'' +
//				", jobClass='" + jobClass + '\'' +
//				", cronExpression='" + cronExpression + '\'' +
//				", desc='" + desc + '\'' +
//				", interfaceName='" + interfaceName + '\'' +
//				", repeatTime=" + repeatTime +
//				", cronJob=" + cronJob +
//				'}';
//	}
//
//	public String getJobId() {
//		return jobId;
//	}
//
//	public void setJobId(String jobId) {
//		this.jobId = jobId;
//	}
//
//	public String getJobName() {
//		return jobName;
//	}
//
//	public void setJobName(String jobName) {
//		this.jobName = jobName;
//	}
//
//	public String getJobGroup() {
//		return jobGroup;
//	}
//
//	public void setJobGroup(String jobGroup) {
//		this.jobGroup = jobGroup;
//	}
//
//	public String getJobStatus() {
//		return jobStatus;
//	}
//
//	public void setJobStatus(String jobStatus) {
//		this.jobStatus = jobStatus;
//	}
//
//	public String getJobClass() {
//		return jobClass;
//	}
//
//	public void setJobClass(String jobClass) {
//		this.jobClass = jobClass;
//	}
//
//	public String getCronExpression() {
//		return cronExpression;
//	}
//
//	public void setCronExpression(String cronExpression) {
//		this.cronExpression = cronExpression;
//	}
//
//	public String getDesc() {
//		return desc;
//	}
//
//	public void setDesc(String desc) {
//		this.desc = desc;
//	}
//
//	public String getInterfaceName() {
//		return interfaceName;
//	}
//
//	public void setInterfaceName(String interfaceName) {
//		this.interfaceName = interfaceName;
//	}
//
//	public Long getRepeatTime() {
//		return repeatTime;
//	}
//
//	public void setRepeatTime(Long repeatTime) {
//		this.repeatTime = repeatTime;
//	}
//
//	public Boolean getCronJob() {
//		return cronJob;
//	}
//
//	public void setCronJob(Boolean cronJob) {
//		this.cronJob = cronJob;
//	}
}
