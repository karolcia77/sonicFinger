CREATE DATABASE `sonicFinger`
USE `sonicFinger`;

-- 后台管理
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `usr_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `usr_login_id` VARCHAR(100) NOT NULL COMMENT '账号',
  `usr_password` VARCHAR(100) NOT NULL COMMENT '密码',
  `usr_role` VARCHAR(100) NOT NULL COMMENT '角色（admin管理员用户，ordinary普通用户）',
  PRIMARY KEY (`usr_id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
-- 默认
INSERT  INTO `user`(`usr_id`,`usr_login_id`,`usr_password`,`usr_role`) VALUES (1,'admin','admin','admin');

-- 用户表
DROP TABLE IF EXISTS `finger_user`;
CREATE TABLE `finger_user` (
  `f_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `f_name` VARCHAR(100) NULL COMMENT '名称',
  `f_finger` TEXT NULL COMMENT '指纹号',
  `f_status` INT(11) DEFAULT '0' COMMENT '状态（1发布，0未发布）',
  `f_fs_id` INT(11) DEFAULT '1' COMMENT '派单状态',
  `f_createdate` DATETIME COMMENT '创建时间',
  `f_updatedate` DATETIME COMMENT '更新时间',
  `f_jobs_updatedate` DATETIME COMMENT '操作更新时间',
  PRIMARY KEY (`f_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 派单状态
DROP TABLE IF EXISTS `finger_status`;
CREATE TABLE `finger_status` (
  `fs_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fs_title` VARCHAR(100) NULL COMMENT '名称',
  `fs_createdate` DATETIME COMMENT '创建时间',
  `fs_updatedate` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`fs_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO finger_status (fs_title,fs_createdate,fs_updatedate)
VALUES ('OFF',NOW(),NOW()),('等待',NOW(),NOW()),('派单',NOW(),NOW()),('跟单',NOW(),NOW())

-- 派单历史记录
DROP TABLE IF EXISTS `finger_log`;
CREATE TABLE `finger_log` (
  `fl_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fl_f_id` INT(11) DEFAULT '0' COMMENT '人ID',
  `fl_fs_id` INT(11) DEFAULT '0' COMMENT '派单状态ID',
  `fl_no` VARCHAR(200) NULL COMMENT '单号',
  `fl_description` VARCHAR(200) NULL COMMENT '描述',
  `fl_startdate` DATETIME COMMENT '预计开始时间',
  `fl_enddate` DATETIME COMMENT '预计结束时间',
  `fl_createdate` DATETIME COMMENT '创建时间',
  `fl_status` INT(11) NULL COMMENT '状态（2待做,1完成，0未完成）',
  PRIMARY KEY (`fl_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;