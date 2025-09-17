package com.mimidaily.dto;

import java.sql.Timestamp;

public class MemberDTO {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String tel;
    private String birth;
    private String gender;
    private Boolean marketing;
    private int role;
    private Integer profile_idx;
    private int visitcnt;
    
    private String ofile;
	private String sfile;
	private String file_path;
	private long file_size;
	private String file_type;
	private Timestamp created_at;
    
	
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
	}
	public Integer getProfile_idx() {
		return profile_idx;
	}
	public void setProfile_idx(Integer profile_idx) {
		this.profile_idx = profile_idx;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
	// 프로필 getter setter 추가
    public String getOfile() {
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfile() {
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long fileSize) {
		this.file_size = fileSize;
	}
        public String getFile_type() {
                return file_type;
        }
        public void setFile_type(String file_type) {
                this.file_type = file_type;
        }

        public String getImageUrl() {
                if (sfile == null || sfile.trim().isEmpty()) {
                        return null;
                }
                String key = sfile.trim();
                if (key.startsWith("http://") || key.startsWith("https://")) {
                        return key;
                }
                if (file_path == null || file_path.trim().isEmpty()) {
                        return sfile;
                }
                String base = file_path.trim();
                
                if (base.startsWith("http://") || base.startsWith("https://")) {
                        if (base.endsWith("/")) {
                                return base + key;
                        }
                        return base + "/" + key;
                }
                if (base.endsWith("/")) {
                        return base + key;
                }
                return base + "/" + key;
        }
}