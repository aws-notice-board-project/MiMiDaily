package com.mimidaily.dao;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class LikeDAO extends DBConnPool {
	// 좋아요 갯수
	public int getLikeCnt(String idx) {
		ArticlesDTO dto=new ArticlesDTO();
		int likeCnt=dto.getLikes();
		return likeCnt;
	}
	
	public boolean toggleLike(String idx,String memberId) {
		
		return true;
	}
}
