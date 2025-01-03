package com.fuyuki.backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@TableName("ums_user")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UmsUser implements Serializable {

    private static final long serialVersionUID = -5051120337175047163L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("username")
    @NotEmpty
    @Size(min = 2, max = 15)
    private String username;

    @TableField("isAdmin")
    private Boolean isAdmin;

    @TableField("alias")
    @NotEmpty(message = "昵称不能为空")
    @Size(min = 2, max = 15, message = "昵称长度在2-15字")
    private String alias;

    @JsonIgnore()
    @TableField("password")
    private String password;

    @Builder.Default
    @TableField("avatar")
    private String avatar = "";

    @TableField("email")
    private String email;

    @TableField("mobile")
    private String mobile;

    @Builder.Default
    @TableField("bio")
    @Size(max = 100, message = "简介长度不超过100字")
    private String bio = "随便写点什么吧";

    @Builder.Default
    @TableField("score")
    private Integer score = 0;

    @JsonIgnore
    @TableField("token")
    private String token;

    @Builder.Default
    @TableField("active")
    private Boolean active = true;

    /**
     * 状态。1:使用，0:已停用
     */
    @Builder.Default
    @TableField("`status`")
    private Boolean status = true;

    /**
     * 用户角色
     */
    @TableField("role_id")
    private Integer roleId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}