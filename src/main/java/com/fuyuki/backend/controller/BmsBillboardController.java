package com.fuyuki.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.fuyuki.backend.common.api.ApiResult;
import com.fuyuki.backend.model.entity.BmsBillboard;
import com.fuyuki.backend.service.IBmsBillboardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/billboard")
public class BmsBillboardController extends BaseController {

    @Resource
    private IBmsBillboardService bmsBillboardService;

    @GetMapping("/show")
    public ApiResult<BmsBillboard> getNotices(){
        List<BmsBillboard> list = bmsBillboardService.list(new
                LambdaQueryWrapper<BmsBillboard>().eq(BmsBillboard::isShow,true));
        return ApiResult.success(list.getLast());
    }
}