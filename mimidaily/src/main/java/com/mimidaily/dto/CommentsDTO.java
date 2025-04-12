package com.mimidaily.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CommentsDTO {
	private int idx;
	private String context;
	private Timestamp created_at;
	private String members_id;
	private int articles_idx;
	private boolean is_sameday;
	
	public boolean isIs_sameday() {
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

        // UTC → Asia/Seoul 변환
        ZonedDateTime createdAtKST = created_at.toInstant().atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        Duration duration = Duration.between(createdAtKST, nowKST);
        long diffInMinutes = duration.toMinutes();
        long diffInHours = duration.toHours();

        System.out.println("createdAtKST: " + createdAtKST);
        System.out.println("nowKST: " + nowKST);
        System.out.println("차이 (시간): " + diffInHours + ", (분): " + diffInMinutes);

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
