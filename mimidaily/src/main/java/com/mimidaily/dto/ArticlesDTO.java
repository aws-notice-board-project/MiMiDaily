package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArticlesDTO {
	private int idx;
	private String title;
	private String content;
	private int category;
	private Timestamp created_at;
	private int visitcnt;
	private String members_id;
	private Integer thumbnails_idx;
	private int likes;
    
	private String ofile;
	private String sfile;
	private String file_path;
	private long file_size;
	private String file_type;

	private boolean is_liked; // 해당 게시글에 현재 사용자가 좋아요 눌렀는지의 여부

	private List<String> hashtags=new ArrayList<String>();

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
	public Integer getThumbnails_idx() {
		return thumbnails_idx;
	}
	public void setThumbnails_idx(Integer thumnails_idx) {
		this.thumbnails_idx = thumnails_idx;
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

	// 해시태그
	public ArrayList<String> getHashtags() {
		return (ArrayList<String>) hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	public String getHashtagString() {
	    StringBuilder sb = new StringBuilder();
	    for (String tag : hashtags) {
	        // 이미 '#'가 붙어있지 않으면 추가.
	        if (!tag.startsWith("#")) {
	            sb.append("#");
	        }
	        sb.append(tag).append(" ");
	    }
	    return sb.toString().trim();
	}

	// 현재 사용자의 해당 글의 좋아요 여부
	public boolean getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	
}