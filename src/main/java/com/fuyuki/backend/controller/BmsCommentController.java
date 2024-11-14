package com.fuyuki.backend.controller;

import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.dto.CommentDTO;
import com.fuyuki.backend.model.entity.BmsComment;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.CommentVO;
import com.fuyuki.backend.service.IBmsCommentService;
import com.fuyuki.backend.service.IBmsPostService;
import com.fuyuki.backend.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.fuyuki.backend.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/comment")
public class BmsCommentController extends BaseController {

    @Resource
    private IBmsCommentService bmsCommentService;
    @Resource
    private IUmsUserService umsUserService;
    @Resource
    private IBmsPostService bmsPostService;

    @GetMapping("/get_comments")
    public ApiResult<List<CommentVO>> getCommentsByTopicID(@RequestParam(value = "topicid", defaultValue = "1") String topicid) {
        List<CommentVO> lstBmsComment = bmsCommentService.getCommentsByTopicID(topicid);
        return ApiResult.success(lstBmsComment);
    }

    @PostMapping("/add_comment")
    public ApiResult<BmsComment> add_comment(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody CommentDTO dto) {
        Assert.notNull(bmsPostService.getById(dto.getTopic_id()), "帖子不存在");
        UmsUser user = umsUserService.getUserByUsername(userName);
        BmsComment comment = bmsCommentService.create(dto, user);
        return ApiResult.success(comment);
    }

    @DeleteMapping("/delete_comment/{id}")
    public ApiResult<String> delete_comment(@PathVariable(value = "id") String id) {
        // 如果不存在评论
        Assert.notNull(bmsCommentService.getById(id), "评论不存在");
        bmsCommentService.removeById(id);
        return ApiResult.success("删除成功");
    }
}