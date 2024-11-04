package com.fuyuki.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fuyuki.backend.mapper.BmsBillboardMapper;
import com.fuyuki.backend.model.entity.BmsBillboard;
import com.fuyuki.backend.service.IBmsBillboardService;
import org.springframework.stereotype.Service;

@Service
public class IBmsBillboardServiceImpl extends ServiceImpl<BmsBillboardMapper
        , BmsBillboard> implements IBmsBillboardService {

}