package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CommentsDTO {
	private int idx;
	private String context;
	private Timestamp created_at;
	private String members_id;
	private int articles_idx;
	
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
	// 포맷된 날짜 getter
    public String getFormattedDate() {
        if (created_at != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            return sdf.format(created_at);
        }
        return null;
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
