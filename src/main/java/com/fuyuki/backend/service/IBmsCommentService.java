package com.fuyuki.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyuki.backend.model.dto.CommentDTO;
import com.fuyuki.backend.model.entity.BmsComment;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.CommentVO;

import java.util.List;


public interface IBmsCommentService extends IService<BmsComment> {
    /**
     * @param topicid
     * @return {@link BmsComment}
     */
    List<CommentVO> getCommentsByTopicID(String topicid);

    BmsComment create(CommentDTO dto, UmsUser principal);
}