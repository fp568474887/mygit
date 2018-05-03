package com.icss.mvp.entity;

import java.io.Serializable;

public class ProjectKeyroles implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3611021770114047777L;
	private String no;//项目编号
    private String name;//姓名
    private String zrAccount;//中软工号
    private String hwAccount;//华为工号
    private String position;//职位
    private String rdpmExam;//RDPM考试（通过/不通过）
    private String replyResults;//答辩结果（通过/不通过）
    private String proCompetence;//胜任度
    private String status;//状态（在职/储备）
    
    
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZrAccount() {
		return zrAccount;
	}
	public void setZrAccount(String zrAccount) {
		this.zrAccount = zrAccount;
	}
	public String getHwAccount() {
		return hwAccount;
	}
	public void setHwAccount(String hwAccount) {
		this.hwAccount = hwAccount;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRdpmExam() {
		return rdpmExam;
	}
	public void setRdpmExam(String rdpmExam) {
		this.rdpmExam = rdpmExam;
	}
	public String getReplyResults() {
		return replyResults;
	}
	public void setReplyResults(String replyResults) {
		this.replyResults = replyResults;
	}
	public String getProCompetence() {
		return proCompetence;
	}
	public void setProCompetence(String proCompetence) {
		this.proCompetence = proCompetence;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}
