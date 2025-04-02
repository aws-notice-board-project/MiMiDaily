package com.mimidaily.dto;

public class MemberDTO {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String tel;
    private String birth;
    private String gender;
    private Boolean marketing;
    private String role;
    private Integer profile_idx;
    private int visitcnt;
    private String created_at;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Boolean isMarketing() {
		return marketing;
	}
	public void setMarketing(Boolean marketing) {
		this.marketing = marketing;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getVisitcnt() {
		return visitcnt;
	}
	public void setVisitcnt(int visitcnt) {
		this.visitcnt = visitcnt;

	public Integer getProfile_idx() {
		return profile_idx;
	}
	public void setProfile_idx(Integer profile_idx) {
		this.profile_idx = profile_idx;

	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
}