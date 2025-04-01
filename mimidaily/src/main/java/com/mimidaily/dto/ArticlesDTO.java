package com.mimidaily.dto;

public class ArticlesDTO {
    private int idx;
    private String title;
    private String content;
    private int category;
    private String created_at;
    private int visitcnt;
    private String members_id;
    private Integer thumnails_idx;
    
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
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
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

  
}
