package com.fuyuki.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.dto.CreateTopicDTO;
import com.fuyuki.backend.model.entity.BmsPost;
import com.fuyuki.backend.model.entity.BmsTopicTag;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.PostVO;
import com.fuyuki.backend.service.IBmsPostService;
import com.fuyuki.backend.service.IBmsTagService;
import com.fuyuki.backend.service.IBmsTopicTagService;
import com.fuyuki.backend.service.IUmsUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fuyuki.backend.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/post")
public class BmsPostController extends BaseController {

    @Resource
    private IBmsPostService bmsPostService;
    @Resource
    private IUmsUserService umsUserService;
    @Resource
    private IBmsTopicTagService bmsTopicTagService;
    @Resource
    private IBmsTagService bmsTagService;

    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = bmsPostService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<BmsPost> create(@RequestHeader(value = USER_NAME) String userName, @RequestBody CreateTopicDTO dto) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        if (user == null) {
            return ApiResult.unauthorized(null);
        }
        Assert.isTrue(user.getStatus(), "用户已被封禁，请联系管理员");
        BmsPost topic = bmsPostService.create(dto, user);
        return ApiResult.success(topic);
    }

    @GetMapping()
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = bmsPostService.viewTopic(id);
        return ApiResult.success(map);
    }

    @GetMapping("/recommend")
    public ApiResult<List<BmsPost>> getRecommend(@RequestParam("topicId") String id) {
        List<BmsPost> topics = bmsPostService.getRecommend(id);
        return ApiResult.success(topics);
    }

    @PostMapping("/update")
    public ApiResult<BmsPost> update(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody BmsPost post) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        if (umsUser == null) {
            return ApiResult.unauthorized(null);
        }
        if (!umsUser.getId().equals(post.getUserId())) {
            return ApiResult.forbidden(null);
        }
        Assert.isTrue(umsUser.getStatus(), "用户已被封禁，请联系管理员");
        post.setModifyTime(new Date());
        post.setContent(EmojiParser.parseToAliases(post.getContent()));
        bmsPostService.updateById(post);
        return ApiResult.success(post);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        BmsPost byId = bmsPostService.getById(id);
        Assert.notNull(byId, "来晚一步，话题已不存在");
        if (umsUser == null) {
            return ApiResult.unauthorized(null);
        }
        if (!(byId.getUserId().equals(umsUser.getId()) || umsUser.getIsAdmin())) {
            return ApiResult.forbidden(null);
        }
        Assert.isTrue(umsUser.getStatus(), "用户已被封禁，请联系管理员");
        List<String> tagIds = bmsTopicTagService.list(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, id)).stream().map(BmsTopicTag::getTagId).toList();
        bmsTagService.removeTags(tagIds);
        bmsTopicTagService.remove(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, id));
        bmsPostService.removeById(id);
        return ApiResult.success(null, "删除成功");
    }

}