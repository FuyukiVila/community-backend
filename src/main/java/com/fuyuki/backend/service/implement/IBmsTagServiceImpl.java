package com.fuyuki.backend.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fuyuki.backend.mapper.BmsTagMapper;
import com.fuyuki.backend.model.entity.BmsTag;
import com.fuyuki.backend.service.IBmsTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag 实现类
 */
@Service
public class IBmsTagServiceImpl extends ServiceImpl<BmsTagMapper, BmsTag> implements IBmsTagService {

    @Autowired
    private com.fuyuki.backend.service.IBmsTopicTagService IBmsTopicTagService;

    @Autowired
    private com.fuyuki.backend.service.IBmsPostService IBmsPostService;


    @Override
    public List<BmsTag> insertTags(List<String> tagNames) {
        List<BmsTag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            BmsTag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getName, tagName));
            if (tag == null) {
                tag = BmsTag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

//    @Override
//    public Page<BmsPost> selectTopicsByTagId(Page<BmsPost> topicPage, String id) {
//
//        // 获取关联的话题ID
//        Set<String> ids = IBmsTopicTagService.selectTopicIdsByTagId(id);
//        LambdaQueryWrapper<BmsPost> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(BmsPost::getId, ids);
//
//        return IBmsPostService.page(topicPage, wrapper);
//    }

}