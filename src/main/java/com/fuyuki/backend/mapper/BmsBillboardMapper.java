package com.fuyuki.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyuki.backend.model.entity.BmsBillboard;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BmsBillboardMapper extends BaseMapper<BmsBillboard> {

}
