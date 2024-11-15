package com.fuyuki.backend.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyuki.backend.jwt.JwtUtil;
import com.fuyuki.backend.mapper.BmsFollowMapper;
import com.fuyuki.backend.mapper.BmsTopicMapper;
import com.fuyuki.backend.mapper.UmsUserMapper;
import com.fuyuki.backend.model.dto.LoginDTO;
import com.fuyuki.backend.model.dto.RegisterDTO;
import com.fuyuki.backend.model.entity.BmsFollow;
import com.fuyuki.backend.model.entity.BmsPost;
import com.fuyuki.backend.model.entity.UmsUser;
import com.fuyuki.backend.model.vo.ProfileVO;
import com.fuyuki.backend.service.IUmsUserService;
import com.fuyuki.backend.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;



@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IUmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements IUmsUserService {

    @Autowired
    private BmsTopicMapper bmsTopicMapper;
    @Autowired
    private BmsFollowMapper bmsFollowMapper;

    @Override
    public UmsUser executeRegister(RegisterDTO dto) {
        //查询是否有相同用户名的用户
        LambdaQueryWrapper<UmsUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUser::getUsername, dto.getName()).or().eq(UmsUser::getEmail, dto.getEmail());
        UmsUser umsUser = baseMapper.selectOne(wrapper);
        Assert.isNull(umsUser, "账号或邮箱已存在！");
        UmsUser addUser = UmsUser.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .password(MD5Utils.getMD5String(dto.getPass()))
                .email(dto.getEmail())
                .createTime(new Date())
                .status(Boolean.TRUE)
                .build();
        baseMapper.insert(addUser);

        return addUser;
    }
    @Override
    public UmsUser getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getUsername, username));
    }
    @Override
    public String executeLogin(LoginDTO dto) {
        String token = null;
        try {
            UmsUser user = getUserByUsername(dto.getUsername());
            if (!MD5Utils.checkPassword(dto.getPassword(), user.getPassword()))
            {
                throw new Exception("密码错误");
            }
            token = JwtUtil.generateToken(String.valueOf(user.getUsername()));
        } catch (Exception e) {
            log.warn("用户不存在or密码验证失败=======>{}", dto.getUsername());
        }
        return token;
    }

    @Override
    public ProfileVO getUserProfile(String id) {
        ProfileVO profile = new ProfileVO();
        UmsUser user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, profile);
        // 用户文章数
        int count = Math.toIntExact(bmsTopicMapper.selectCount(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, id)));
        profile.setTopicCount(count);

        // 粉丝数
        int followers = Math.toIntExact(bmsFollowMapper.selectCount((new LambdaQueryWrapper<BmsFollow>().eq(BmsFollow::getParentId, id))));
        profile.setFollowerCount(followers);

        return profile;
    }
}