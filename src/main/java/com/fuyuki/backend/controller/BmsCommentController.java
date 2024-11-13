package com.fuyuki.backend.controller;

import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.vo.CommentVO;
import com.fuyuki.backend.service.IBmsCommentService;
import com.fuyuki.backend.service.IUmsUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/comment")
public class BmsCommentController extends BaseController {

    @Resource
    private IBmsCommentService bmsCommentService;
    @Resource
    private IUmsUserService umsUserService;

    @GetMapping("/get_comments")
    public ApiResult<List<CommentVO>> getCommentsByTopicID(@RequestParam(value = "topicid", defaultValue = "1") String topicid) {
        List<CommentVO> lstBmsComment = bmsCommentService.getCommentsByTopicID(topicid);
        return ApiResult.success(lstBmsComment);
    }
//    @PostMapping("/add_comment")
//    public ApiResult<BmsComment> add_comment(@RequestHeader(value = USER_NAME) String userName,
//                                             @RequestBody CommentDTO dto) {
//        UmsUser user = umsUserService.getUserByUsername(userName);
//        BmsComment comment = bmsCommentService.create(dto, user);
//        return ApiResult.success(comment);
//    }
}