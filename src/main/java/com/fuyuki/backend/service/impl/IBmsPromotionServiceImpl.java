package com.fuyuki.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyuki.backend.mapper.BmsPromotionMapper;
import com.fuyuki.backend.model.entity.BmsPromotion;
import com.fuyuki.backend.service.IBmsPromotionService;
import org.springframework.stereotype.Service;


@Service
public class IBmsPromotionServiceImpl extends ServiceImpl<BmsPromotionMapper, BmsPromotion> implements IBmsPromotionService {

}