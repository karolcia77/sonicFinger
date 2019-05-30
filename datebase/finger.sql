CREATE DATABASE `sonicFinger`;
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
VALUES ('OFF',NOW(),NOW()),('等待',NOW(),NOW()),('派单',NOW(),NOW()),('跟单',NOW(),NOW());


-- 用户表
DROP TABLE IF EXISTS `finger_user`;
CREATE TABLE `finger_user` (
  `f_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `f_name` VARCHAR(100) NULL COMMENT '名称',
  `f_finger` VARCHAR(100) NULL COMMENT '指纹号',
  `f_finger_txt` TEXT NULL COMMENT '指纹详细',
  `f_status` INT(11) DEFAULT '0' COMMENT '状态（1发布，0未发布）',
  `f_fs_id` INT(11) DEFAULT '1' COMMENT '派单状态',
  `f_createdate` DATETIME COMMENT '创建时间',
  `f_updatedate` DATETIME COMMENT '更新时间',
  `f_jobs_updatedate` DATETIME COMMENT '操作更新时间',
  PRIMARY KEY (`f_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 派单case
DROP TABLE IF EXISTS `finger_case`;
CREATE TABLE `finger_case` (
  `fc_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fc_no` VARCHAR(200) NULL COMMENT '单号',
  `fc_description` VARCHAR(200) NULL COMMENT '描述',
  `fc_startdate` DATETIME COMMENT '预计开始时间',
  `fc_enddate` DATETIME COMMENT '预计结束时间',
  `fc_createdate` DATETIME COMMENT '创建时间',
  `fc_status` INT(11) NULL DEFAULT 0 COMMENT '状态（2待做,1完成，0未完成,-1无）',
  PRIMARY KEY (`fc_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 派单关系表
DROP TABLE IF EXISTS `finger_case_relation`;
CREATE TABLE `finger_case_relation` (
  `fcr_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fcr_fc_id` INT(11) NULL COMMENT '单ID',
  `fcr_f_id` INT(11) NOT NULL COMMENT '人ID',
  `fcr_ind` INT(11) DEFAULT 0 COMMENT '单状态:0不做,1正在做',
  `fcr_finger_startdate` DATETIME COMMENT '开始签到指纹时间',
  `fcr_finger_enddate` DATETIME COMMENT '结束签到指纹时间',
  `fcr_createdate` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`fcr_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 派单日志记录
DROP TABLE IF EXISTS `finger_log`;
CREATE TABLE `finger_log` (
  `fl_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fl_fc_id` INT(11) NULL COMMENT '单ID',
  `fl_f_id` INT(11) NOT NULL COMMENT '人ID',
  `fl_fs_id` INT(11) DEFAULT 0 COMMENT '派单状态',
  `fl_createdate` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`fl_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


-- 打卡
DROP TABLE IF EXISTS `finger_recording`;
CREATE TABLE `finger_recording` (
  `fr_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fr_f_id` INT(11) NULL COMMENT '人ID',
  `fr_createdate` DATETIME COMMENT '创建时间-指纹打卡',
  `fr_updatedate` DATETIME COMMENT '结束时间-指纹打卡',
  `fr_seconds` VARCHAR(200) NULL COMMENT '打卡秒数',
  PRIMARY KEY (`fr_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;
