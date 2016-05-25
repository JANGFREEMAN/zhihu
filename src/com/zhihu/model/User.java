package com.zhihu.model;

import java.io.Serializable;
import java.util.List;

/**
 * 知乎用户模型
 * @author zhangyx
 */
public class User  implements Serializable{
	private static final long serialVersionUID = -5257620614071365321L;
	/**主页地址*/
	private String homeUrl;
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
	/**我关注了谁*/
	private List<String> followeesUserName;
	/**谁关注了我*/
	private List<String> followersUserName;


	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void setPersionProfile(String persionProfile) {
		PersionProfile = persionProfile;
	}



	public String getHomeUrl() {

		return homeUrl;
	}

	public String getUsername() {
		return username;
	}

	public String getSignature() {
		return signature;
	}

	public String getLocation() {
		return location;
	}

	public String getIndustry() {
		return industry;
	}

	public String getSex() {
		return sex;
	}

	public String getCompany() {
		return company;
	}

	public String getJob() {
		return job;
	}

	public String getUniversity() {
		return university;
	}

	public String getMajor() {
		return major;
	}

	public String getPersionProfile() {
		return PersionProfile;
	}

	public List<String> getFolloweesUserName() {
		return followeesUserName;
	}

	@Override
	public String toString() {
		return "User{" +
				"homeUrl='" + homeUrl + '\'' +
				", username='" + username + '\'' +
				", signature='" + signature + '\'' +
				", location='" + location + '\'' +
				", industry='" + industry + '\'' +
				", sex='" + sex + '\'' +
				", company='" + company + '\'' +
				", job='" + job + '\'' +
				", university='" + university + '\'' +
				", major='" + major + '\'' +
				", PersionProfile='" + PersionProfile + '\'' +
				", followeesUserName=" + followeesUserName +
				", followersUserName=" + followersUserName +
				'}';
	}

	public void setFolloweesUserName(List<String> followeesUserName) {
		this.followeesUserName = followeesUserName;
	}

	public List<String> getFollowersUserName() {
		return followersUserName;
	}

	public void setFollowersUserName(List<String> followersUserName) {
		this.followersUserName = followersUserName;
	}
}
