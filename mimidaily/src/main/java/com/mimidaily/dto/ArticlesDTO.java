package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ArticlesDTO {
    private int idx;
    private String title;
    private String content;
    private int category;
    private Timestamp created_at;
    private int visitcnt;
    private String members_id;
    private Integer thumnails_idx;
    private int likes;
    
	private String ofile;
    private String sfile;
	private String file_path;
	private long file_size;
	private String file_type;

	// 추가된 멤버 정보
    private String member_name; // 멤버 이름
    private String member_email; // 멤버 이메일
    private boolean is_liked; // 현재 사용자가 좋아요 눌렀는지의 여부

	// 포맷된 날짜 getter
    public String getFormattedDate() {
        if (created_at != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            return sdf.format(created_at);
        }
        return null;
    }
    
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp timestamp) {
		this.created_at = timestamp;
	}
	public int getVisitcnt() {
		return visitcnt;
	}
	public void setVisitcnt(int visitcnt) {
		this.visitcnt = visitcnt;
	}
	public String getMembers_id() {
		return members_id;
	}
	public void setMembers_id(String members_id) {
		this.members_id = members_id;
	}
	public Integer getThumnails_idx() {
		return thumnails_idx;
	}
	public void setThumnails_idx(Integer thumnails_idx) {
		this.thumnails_idx = thumnails_idx;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}

	// 썸네일 관련 getter setter 추가
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

	// 글쓴이 정보
	public String getMemberName() {
		return member_name;
	}

	public void setMemberName(String member_name) {
		this.member_name = member_name;
	}

	public String getMember_email() {
		return member_email;
	}

	public void setMemberEmail(String member_email) {
		this.member_email = member_email;
	}


	// 현재 사용자의 해당 글의 좋아요 여부
	public void setIsLiked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public boolean getIsLiked() {
		return is_liked;
	}
	
}
