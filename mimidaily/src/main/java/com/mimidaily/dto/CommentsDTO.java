package com.mimidaily.dto;

import java.sql.Timestamp;

public class CommentsDTO {
	int idx;
	String context;
	Timestamp created_at;
	String members_id;
	int articles_idx;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public String getMembers_id() {
		return members_id;
	}
	public void setMembers_id(String members_id) {
		this.members_id = members_id;
	}
	public int getArticles_idx() {
		return articles_idx;
	}
	public void setArticles_idx(int articles_idx) {
		this.articles_idx = articles_idx;
	}
}
