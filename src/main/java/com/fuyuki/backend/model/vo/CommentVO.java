package com.fuyuki.backend.model.vo;

import lombok.Data;

import java.util.Date;


@Data
public class CommentVO {

    private String id;

    private String avatar;

    private String content;

    private String topicId;

    private String userId;

    private String username;

    private String alias;

    private Date createTime;

}