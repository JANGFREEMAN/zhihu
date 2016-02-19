package com.zhihu.model;

import java.io.Serializable;
import java.util.List;

/**
 * 用户model
 * @author zhangyx
 */
public class User  implements Serializable{
	private static final long serialVersionUID = -5257620614071365321L;
	/**用户名*/
	private String username;
	/**个性签名*/
	private String signature;
	/**居住地*/
	private String location;
	/**行业*/
	private String industry;
	/**性别*/
	private String sex;
	/**公司*/
	private String company;
	/**职位*/
	private String job;
	/**大学*/
	private String university;
	/**专业*/
	private String major;
	/**个人简介*/
	private String PersionProfile;
	/**关注*/
	private List<String> follow;
	/**关注者*/
	private List<String> follower;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getUniversity() {
		return university;
	}
	public void setUniversity(String university) {
		this.university = university;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getPersionProfile() {
		return PersionProfile;
	}

	public void setPersionProfile(String persionProfile) {
		PersionProfile = persionProfile;
	}
	public List<String> getFollow() {
		return follow;
	}
	public void setFollow(List<String> follow) {
		this.follow = follow;
	}
	public List<String> getFollower() {
		return follower;
	}
	public void setFollower(List<String> follower) {
		this.follower = follower;
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", signature=" + signature + ", location=" + location + ", industry="
				+ industry + ", sex=" + sex + ", company=" + company + ", job=" + job + ", university=" + university
				+ ", major=" + major + ", PersionProfile=" + PersionProfile + ", follow=" + follow + ", follower="
				+ follower + "]";
	}
	
}
