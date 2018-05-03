package com.icss.mvp.entity;

import java.util.Date;

public class GitLogs {
	
	private String id;
    
    private String author; //提交人
    
    private Date commitime; //提交日期
    
    private String message; //提交备注信息
    
    private String revision; //版本号
    
	private int modifyNum;// 修改行数
    
    private int delNum;// 删除行数
    
    private String no;// 项目编号
    
    private Date colDate;// 收集日期
    
    private int fileNum; //提交的文件数目

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCommitime() {
		return commitime;
	}

	public void setCommitime(Date commitime) {
		this.commitime = commitime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public int getModifyNum() {
		return modifyNum;
	}

	public void setModifyNum(int modifyNum) {
		this.modifyNum = modifyNum;
	}

	public int getDelNum() {
		return delNum;
	}

	public void setDelNum(int delNum) {
		this.delNum = delNum;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getColDate() {
		return colDate;
	}

	public void setColDate(Date colDate) {
		this.colDate = colDate;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
    
	public String getRevision() {
			return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

}
