/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50521
Source Host           : 127.0.0.1:3306
Source Database       : ilive

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2018-03-26 09:18:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ilive_server
-- ----------------------------
DROP TABLE IF EXISTS `ilive_server`;
CREATE TABLE `ilive_server` (
  `server_id` int(11) NOT NULL,
  `server_name` varchar(255) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `mode` int(11) DEFAULT NULL,
  PRIMARY KEY (`server_id`),
  KEY `FKE94BADED6CE19DE7` (`group_id`),
  KEY `FKE94BADEDE969C0AB` (`group_id`),
  CONSTRAINT `FKE94BADED6CE19DE7` FOREIGN KEY (`group_id`) REFERENCES `ilive_server_group` (`server_group_id`),
  CONSTRAINT `FKE94BADEDE969C0AB` FOREIGN KEY (`group_id`) REFERENCES `ilive_server_group` (`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ilive_server
-- ----------------------------
INSERT INTO `ilive_server` VALUES ('100', '直播服务器1', '100', '1');
INSERT INTO `ilive_server` VALUES ('101', '点播服务器1', '102', '1');

-- ----------------------------
-- Table structure for ilive_server_access_method
-- ----------------------------
DROP TABLE IF EXISTS `ilive_server_access_method`;
CREATE TABLE `ilive_server_access_method` (
  `method_id` int(11) NOT NULL,
  `method_name` varchar(50) DEFAULT NULL,
  `mms_address` varchar(50) DEFAULT NULL,
  `ftp_address` varchar(50) DEFAULT NULL,
  `ftp_address_inner` varchar(50) DEFAULT NULL,
  `ftp_user` varchar(50) DEFAULT NULL,
  `ftp_pwd` varchar(50) DEFAULT NULL,
  `http_address` varchar(50) DEFAULT NULL,
  `default_method` int(11) DEFAULT NULL,
  `umsport` int(11) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `ftp_path` varchar(255) DEFAULT NULL,
  `ftp_encode` varchar(255) DEFAULT NULL,
  `ftp_port` int(11) DEFAULT NULL,
  `web_httpUrl` varchar(255) DEFAULT NULL,
  `record_server_addr` varchar(255) DEFAULT NULL,
  `h5_httpUrl` varchar(255) DEFAULT NULL,
  `livemsport` int(11) DEFAULT NULL,
  PRIMARY KEY (`method_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ilive_server_access_method
-- ----------------------------
INSERT INTO `ilive_server_access_method` VALUES ('100', 'live', 'rtmp://127.0.0.1', '127.0.0.1', '127.0.0.1', 'administrator', 'jwztadmin1992', '127.0.0.1', '1', '1935', '100', '/img', 'UTF-8', '21', null, 'http://127.0.0.1', 'http://127.0.0.1', '8098');
INSERT INTO `ilive_server_access_method` VALUES ('101', 'vod', null, '127.0.0.1', '127.0.0.1', 'administrator', 'jwztadmin1992', '127.0.0.1', '1', '1935', '101', null, 'UTF-8', '21', null, 'http://127.0.0.1', 'http://127.0.0.1', '8098');

-- ----------------------------
-- Table structure for ilive_server_group
-- ----------------------------
DROP TABLE IF EXISTS `ilive_server_group`;
CREATE TABLE `ilive_server_group` (
  `server_group_id` int(11) NOT NULL,
  `server_group_name` varchar(255) DEFAULT NULL,
  `distribute_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ilive_server_group
-- ----------------------------
INSERT INTO `ilive_server_group` VALUES ('100', '直播服务器1', '1');
INSERT INTO `ilive_server_group` VALUES ('101', '直播服务器2', '1');
INSERT INTO `ilive_server_group` VALUES ('102', '点播服务器1', '1');

-- ----------------------------
-- Table structure for ilive_server_mount
-- ----------------------------
DROP TABLE IF EXISTS `ilive_server_mount`;
CREATE TABLE `ilive_server_mount` (
  `server_mount_id` int(11) NOT NULL,
  `server_mount_name` varchar(100) DEFAULT NULL,
  `storage_space` int(11) DEFAULT NULL,
  `used_space` int(11) DEFAULT NULL,
  `base_path` varchar(255) DEFAULT NULL,
  `ftp_path` varchar(255) DEFAULT NULL,
  `mount_type` int(11) DEFAULT NULL,
  `mirror_mount_id` int(11) DEFAULT NULL,
  `mount_desc` varchar(1000) DEFAULT NULL,
  `if_use` int(11) DEFAULT NULL,
  `public_disk_space` int(11) DEFAULT NULL,
  `warning_space` int(11) DEFAULT NULL,
  `left_space` int(11) DEFAULT NULL,
  `iscut` int(11) DEFAULT NULL,
  `is_default` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `server_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`server_mount_id`),
  KEY `FKBC8B67476CE19DE7` (`group_id`),
  KEY `FKBC8B6747B20A3E27` (`server_group_id`),
  KEY `FKBC8B6747E969C0AB` (`group_id`),
  CONSTRAINT `FKBC8B67476CE19DE7` FOREIGN KEY (`group_id`) REFERENCES `ilive_server_group` (`server_group_id`),
  CONSTRAINT `FKBC8B6747B20A3E27` FOREIGN KEY (`server_group_id`) REFERENCES `ilive_server_group` (`server_group_id`),
  CONSTRAINT `FKBC8B6747E969C0AB` FOREIGN KEY (`group_id`) REFERENCES `ilive_server_group` (`server_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ilive_server_mount
-- ----------------------------
INSERT INTO `ilive_server_mount` VALUES ('100', '点播1', null, null, '/vod/', '/vod/', null, null, null, null, null, null, null, null, '1', '102', '102');
INSERT INTO `ilive_server_mount` VALUES ('101', '直播1', null, null, null, null, null, null, null, null, null, null, null, null, '1', '100', '100');
