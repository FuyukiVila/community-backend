package com.fuyuki.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;


    private String topic_id;

    /**
     * 内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(min = 1, max = 100, message = "评论内容长度在1-100")
    private String content;


}