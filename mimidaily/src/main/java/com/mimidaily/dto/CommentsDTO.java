package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentsDTO {
	private int idx;
	private String context;
	private Timestamp created_at;
	private Timestamp updated_at;
	private String members_id;
	private int articles_idx;
	private boolean is_sameday;
	private boolean is_updated;
	
	
	public boolean getIs_updated() {
		return is_updated;
	}
	public void setIs_updated(boolean is_updated) {
		this.is_updated = is_updated;
	}
	public boolean getIs_sameday() {
		return is_sameday;
	}
	public void setIs_sameday(boolean is_sameday) {
		this.is_sameday = is_sameday;
	}
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
    // 시간 차이를 계산하여 n시간 전, n분 전 형식으로 반환
    public String getTimeAgo() {
        if (created_at == null) {
            return null;
        }

        long diffInMillis = new Date().getTime() - created_at.getTime();
        long diffInSeconds = diffInMillis / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;

        if (diffInHours > 0) {
            return diffInHours + "시간 전";
        } else if (diffInMinutes > 0) {
            return diffInMinutes + "분 전";
        } else {
            return "방금 전";
        }
    }
    // 현재 날짜와 비교하여 같은 날인지 확인
    public boolean isSameDay() {
        if (created_at == null) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(created_at).equals(sdf.format(new Date()));
    }
    
	public Timestamp getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}
	// 수정 유무
	public boolean isUpdated() {
	    if (created_at == null || updated_at == null) return false;
	    return created_at.getTime() != updated_at.getTime(); // 객체끼리 비교하면 객체 주소를 가져오기 때문에 getTime() 또는 equals() 사
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
