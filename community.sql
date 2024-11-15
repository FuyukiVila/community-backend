/*
 Navicat Premium Dump SQL

 Source Server         : MariaDB
 Source Server Type    : MariaDB
 Source Server Version : 110502 (11.5.2-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : community

 Target Server Type    : MariaDB
 Target Server Version : 110502 (11.5.2-MariaDB)
 File Encoding         : 65001

 Date: 15/11/2024 14:09:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bms_billboard
-- ----------------------------
DROP TABLE IF EXISTS `bms_billboard`;
CREATE TABLE `bms_billboard`
(
    `id`          int(11)                                                          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `content`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL COMMENT '公告',
    `create_time` datetime                                                         NULL DEFAULT NULL COMMENT '公告时间',
    `show`        tinyint(1)                                                       NULL DEFAULT NULL COMMENT '1：展示中，0：过期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '全站公告'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_billboard
-- ----------------------------
INSERT INTO `bms_billboard`
VALUES (2, 'R1.0 开始已实现护眼模式 ,妈妈再也不用担心我的眼睛了。', '2020-11-19 17:16:19', 0);
INSERT INTO `bms_billboard`
VALUES (4, '系统已更新至最新版1.0.1', NULL, 1);

-- ----------------------------
-- Table structure for bms_comment
-- ----------------------------
DROP TABLE IF EXISTS `bms_comment`;
CREATE TABLE `bms_comment`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci   NOT NULL COMMENT '主键',
    `content`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL DEFAULT '' COMMENT '内容',
    `user_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci   NOT NULL COMMENT '作者ID',
    `topic_id`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci   NOT NULL COMMENT 'topic_id',
    `create_time` datetime                                                          NOT NULL COMMENT '发布时间',
    `modify_time` datetime                                                          NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_comment_user` (`user_id` ASC) USING BTREE,
    INDEX `fk_comment_post` (`topic_id` ASC) USING BTREE,
    CONSTRAINT `fk_comment_post` FOREIGN KEY (`topic_id`) REFERENCES `bms_post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `ums_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '评论表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_comment
-- ----------------------------
INSERT INTO `bms_comment`
VALUES ('1857257661176311809', 'test', '1857257538169958401', '1857257590951079937', '2024-11-15 11:01:47', NULL);
INSERT INTO `bms_comment`
VALUES ('1857257726997524481', 'test', '1349290158897311745', '1857257590951079937', '2024-11-15 11:02:02', NULL);
INSERT INTO `bms_comment`
VALUES ('1857304580388900865', '评论', '1349290158897311745', '1857304476051394562', '2024-11-15 14:08:13', NULL);
INSERT INTO `bms_comment`
VALUES ('1857304605667971073', '测试', '1349290158897311745', '1857298410487943170', '2024-11-15 14:08:19', NULL);

-- ----------------------------
-- Table structure for bms_follow
-- ----------------------------
DROP TABLE IF EXISTS `bms_follow`;
CREATE TABLE `bms_follow`
(
    `id`          int(11)                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL DEFAULT NULL COMMENT '被关注人ID',
    `follower_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL DEFAULT NULL COMMENT '关注人ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_parent_user` (`parent_id` ASC) USING BTREE,
    INDEX `fk_follower_user` (`follower_id` ASC) USING BTREE,
    CONSTRAINT `fk_follower_user` FOREIGN KEY (`follower_id`) REFERENCES `ums_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_parent_user` FOREIGN KEY (`parent_id`) REFERENCES `ums_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 131
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '用户关注'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_follow
-- ----------------------------
INSERT INTO `bms_follow`
VALUES (130, '1857257538169958401', '1349290158897311745');

-- ----------------------------
-- Table structure for bms_post
-- ----------------------------
DROP TABLE IF EXISTS `bms_post`;
CREATE TABLE `bms_post`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NOT NULL COMMENT '主键',
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL DEFAULT '' COMMENT '标题',
    `content`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci     NULL     DEFAULT NULL COMMENT 'markdown内容',
    `user_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NOT NULL COMMENT '作者ID',
    `comments`    int(11)                                                          NOT NULL DEFAULT 0 COMMENT '评论统计',
    `collects`    int(11)                                                          NOT NULL DEFAULT 0 COMMENT '收藏统计',
    `view`        int(11)                                                          NOT NULL DEFAULT 0 COMMENT '浏览统计',
    `top`         bit(1)                                                           NOT NULL DEFAULT b'0' COMMENT '是否置顶，1-是，0-否',
    `essence`     bit(1)                                                           NOT NULL DEFAULT b'0' COMMENT '是否加精，1-是，0-否',
    `section_id`  int(11)                                                          NULL     DEFAULT 0 COMMENT '专栏ID',
    `create_time` datetime                                                         NOT NULL COMMENT '发布时间',
    `modify_time` datetime                                                         NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id` (`user_id` ASC) USING BTREE,
    INDEX `create_time` (`create_time` ASC) USING BTREE,
    CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `ums_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '话题表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_post
-- ----------------------------
INSERT INTO `bms_post`
VALUES ('1857257590951079937', 'test', '测试\n更新\n\n', '1857257538169958401', 0, 0, 13, b'0', b'0', 0, '2024-11-15 11:01:30', NULL);
INSERT INTO `bms_post`
VALUES ('1857264311647879169', '测试', '测试\n更新\n\n', '1349290158897311745', 0, 0, 6, b'0', b'0', 0, '2024-11-15 11:28:12', NULL);
INSERT INTO `bms_post`
VALUES ('1857265492902297602', '哈哈', ':confused: \n', '1349290158897311745', 0, 0, 7, b'0', b'0', 0, '2024-11-15 11:32:54', NULL);
INSERT INTO `bms_post`
VALUES ('1857283328689315842', 'test',
        '# 测试图片\n\n![](http://127.0.0.1:8081/uploads/72a937e5cfe3ebc3c66c1ba10a27d998.jpg)![](http://127.0.0.1:8081/uploads/75295bd2757ff736fabd5174113d12a6.jpg)\n![](http://127.0.0.1:8081/uploads/c28465650eb5c4efe3ef494b9d7d07d3.png)\n\n',
        '1349290158897311745', 0, 0, 22, b'0', b'0', 0, '2024-11-15 12:43:46', '2024-11-15 12:52:59');
INSERT INTO `bms_post`
VALUES ('1857290486399111170', '音频测试',
        '# flac\n\n<audio src=\"http://127.0.0.1:8081/uploads/3618f9da5e66c07512495b589bd6faf9.flac\" controls>01. a fairy with you.flac</audio>\n\n# wav\n\n<audio src=\"http://127.0.0.1:8081/uploads/42aca6a51907f1b3a2b573f30d1b6ae3.wav\" controls></audio>\n\n# mp3\n\n<audio src=\"http://127.0.0.1:8081/uploads/153d30220d48a4d12470f4c69af4090d.mp3\" controls></audio>\n\n\n',
        '1349290158897311745', 0, 0, 20, b'0', b'0', 0, '2024-11-15 13:12:13', '2024-11-15 13:51:38');
INSERT INTO `bms_post`
VALUES ('1857296731906510849', '视频测试',
        '<video src=\"http://127.0.0.1:8081/uploads/463ee9a5416970c4f23a12bdc35ab674.mp4\" controls>trailer.mp4</video>\n<video src=\"http://127.0.0.1:8081/uploads/46ee7104dc096d4e5273845da3244568.mp4\" controls></video>\n\n',
        '1349290158897311745', 0, 0, 10, b'0', b'0', 0, '2024-11-15 13:37:02', '2024-11-15 13:53:00');
INSERT INTO `bms_post`
VALUES ('1857298410487943170', '文件测试',
        '[下载文件.7z](http://127.0.0.1:8081/uploads/d27bcf52646e6bbba6946ba8eb11cf6e.7z)\n[下载文件.zip](http://127.0.0.1:8081/uploads/799c431dd022104adf76835188991c41.zip)\n[下载文件.rar](http://127.0.0.1:8081/uploads/d6181db3056a397182eaa6b3504377d8.rar)\n\n',
        '1349290158897311745', 0, 0, 5, b'0', b'0', 0, '2024-11-15 13:43:42', NULL);
INSERT INTO `bms_post`
VALUES ('1857304476051394562', '同名文件不同md5测试',
        '[下载文件](http://127.0.0.1:8081/uploads/4f7b6a6fc03df64a82c7b3d026b61fbd.zip)\n[下载文件](http://127.0.0.1:8081/uploads/5b1bccf65c2ae3d0e35d6b6199044948.zip)\n\n',
        '1349290158897311745', 0, 0, 6, b'0', b'0', 0, '2024-11-15 14:07:48', '2024-11-15 14:07:58');

-- ----------------------------
-- Table structure for bms_post_tag
-- ----------------------------
DROP TABLE IF EXISTS `bms_post_tag`;
CREATE TABLE `bms_post_tag`
(
    `id`       int(11)                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tag_id`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL COMMENT '标签ID',
    `topic_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL COMMENT '话题ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `tag_id` (`tag_id` ASC) USING BTREE,
    INDEX `topic_id` (`topic_id` ASC) USING BTREE,
    CONSTRAINT `fk_post_tag_post` FOREIGN KEY (`topic_id`) REFERENCES `bms_post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_post_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `bms_tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 61
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '话题-标签 中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_post_tag
-- ----------------------------
INSERT INTO `bms_post_tag`
VALUES (53, '1857257591018188802', '1857257590951079937');
INSERT INTO `bms_post_tag`
VALUES (54, '1857257591018188802', '1857264311647879169');
INSERT INTO `bms_post_tag`
VALUES (55, '1857257591018188802', '1857265492902297602');
INSERT INTO `bms_post_tag`
VALUES (56, '1857283328974528513', '1857283328689315842');
INSERT INTO `bms_post_tag`
VALUES (57, '1857257591018188802', '1857290486399111170');
INSERT INTO `bms_post_tag`
VALUES (58, '1857296731906510850', '1857296731906510849');
INSERT INTO `bms_post_tag`
VALUES (59, '1857298410550857730', '1857298410487943170');
INSERT INTO `bms_post_tag`
VALUES (60, '1857304476135280641', '1857304476051394562');

-- ----------------------------
-- Table structure for bms_promotion
-- ----------------------------
DROP TABLE IF EXISTS `bms_promotion`;
CREATE TABLE `bms_promotion`
(
    `id`          int(11)                                                          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL DEFAULT NULL COMMENT '广告标题',
    `link`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL DEFAULT NULL COMMENT '广告链接',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL DEFAULT NULL COMMENT '说明',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '广告推广表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_promotion
-- ----------------------------
INSERT INTO `bms_promotion`
VALUES (1, ' Github', 'https://github.com/fuyukivila', 'github');

-- ----------------------------
-- Table structure for bms_tag
-- ----------------------------
DROP TABLE IF EXISTS `bms_tag`;
CREATE TABLE `bms_tag`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NOT NULL COMMENT '标签ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL DEFAULT '' COMMENT '标签',
    `topic_count` int(11)                                                          NOT NULL DEFAULT 0 COMMENT '关联话题',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `name` (`name` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '标签表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bms_tag
-- ----------------------------
INSERT INTO `bms_tag`
VALUES ('1857257591018188802', 'test', 4);
INSERT INTO `bms_tag`
VALUES ('1857283328974528513', 'image', 1);
INSERT INTO `bms_tag`
VALUES ('1857296731906510850', 'video', 1);
INSERT INTO `bms_tag`
VALUES ('1857298410550857730', 'file', 1);
INSERT INTO `bms_tag`
VALUES ('1857304476135280641', 'MD5', 1);

-- ----------------------------
-- Table structure for ums_user
-- ----------------------------
DROP TABLE IF EXISTS `ums_user`;
CREATE TABLE `ums_user`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci   NOT NULL COMMENT '用户ID',
    `username`    varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci   NOT NULL DEFAULT '' COMMENT '用户名',
    `isAdmin`     bit(1)                                                            NOT NULL DEFAULT b'0' COMMENT '是否是管理员',
    `alias`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT NULL COMMENT '用户昵称',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT '' COMMENT '密码',
    `avatar`      varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NULL     DEFAULT NULL COMMENT '头像',
    `email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT NULL COMMENT '邮箱',
    `mobile`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT NULL COMMENT '手机',
    `score`       int(11)                                                           NOT NULL DEFAULT 0 COMMENT '积分',
    `token`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT '' COMMENT 'token',
    `bio`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci  NULL     DEFAULT NULL COMMENT '个人简介',
    `active`      bit(1)                                                            NOT NULL DEFAULT b'0' COMMENT '是否激活，1：是，0：否',
    `status`      bit(1)                                                            NULL     DEFAULT b'1' COMMENT '状态，1：使用，0：停用',
    `role_id`     int(11)                                                           NULL     DEFAULT NULL COMMENT '用户角色',
    `create_time` datetime                                                          NOT NULL COMMENT '加入时间',
    `modify_time` datetime                                                          NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_name` (`username` ASC) USING BTREE,
    INDEX `user_email` (`email` ASC) USING BTREE,
    INDEX `user_create_time` (`create_time` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user
-- ----------------------------
INSERT INTO `ums_user`
VALUES ('1349290158897311745', 'admin', b'1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'https://s3.ax1x.com/2020/12/01/DfHNo4.jpg', '23456@qq.com',
        '123456', 11, '', '自由职业者', b'1', b'1', NULL, '2021-01-13 17:40:17', NULL);
INSERT INTO `ums_user`
VALUES ('1857257538169958401', 'fuyuki', b'0', 'fuyuki', 'e10adc3949ba59abbe56e057f20f883e', '', '1234@qq.com', NULL, 1, '', '随便写点什么吧', b'1', b'1', NULL,
        '2024-11-15 11:01:17', NULL);

SET FOREIGN_KEY_CHECKS = 1;
