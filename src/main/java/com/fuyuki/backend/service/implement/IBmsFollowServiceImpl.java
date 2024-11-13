package com.fuyuki.backend.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyuki.backend.mapper.BmsFollowMapper;
import com.fuyuki.backend.model.entity.BmsFollow;
import com.fuyuki.backend.service.IBmsFollowService;
import org.springframework.stereotype.Service;


@Service
public class IBmsFollowServiceImpl extends ServiceImpl<BmsFollowMapper, BmsFollow> implements IBmsFollowService {
}