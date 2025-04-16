package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MemberInfoDTO {
    private String id;
    private String name;
    private int articleCount;
    private int commentCount;
    private Timestamp createdAt;
    private MemberDTO profiles;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getArticleCount() { return articleCount; }
    public void setArticleCount(int articleCount) { this.articleCount = articleCount; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public String getCreatedAt() {
        // SimpleDateFormat을 사용하여 Timestamp를 원하는 형식으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(createdAt);
    }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
	public MemberDTO getProfiles() {
		return profiles;
	}
	public void setProfiles(MemberDTO profiles) {
		this.profiles = profiles;
	}
    
}