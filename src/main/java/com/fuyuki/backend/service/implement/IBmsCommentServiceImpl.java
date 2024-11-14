package com.fuyuki.backend.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyuki.backend.mapper.BmsCommentMapper;
import com.fuyuki.backend.model.dto.CommentDTO;
import com.fuyuki.backend.model.entity.BmsComment;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.CommentVO;
import com.fuyuki.backend.service.IBmsCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class IBmsCommentServiceImpl extends ServiceImpl<BmsCommentMapper, BmsComment> implements IBmsCommentService {
    @Override
    public List<CommentVO> getCommentsByTopicID(String topicid) {
        List<CommentVO> lstBmsComment = new ArrayList<>();
        try {
            lstBmsComment = this.baseMapper.getCommentsByTopicID(topicid);
        } catch (Exception e) {
            log.info("lstBmsComment失败");
        }
        return lstBmsComment;
    }

    @Override
    public BmsComment create(CommentDTO dto, UmsUser user) {
        BmsComment comment = BmsComment.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .topicId(dto.getTopic_id())
                .createTime(new Date())
                .build();
        this.baseMapper.insert(comment);
        return comment;
    }
}