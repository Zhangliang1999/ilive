/*
Navicat MySQL Data Transfer

Source Server         : 172.18.2.34
Source Server Version : 50639
Source Host           : 172.18.2.34:3306
Source Database       : db_ilive

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-10 17:21:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ilive_field_id_manager
-- ----------------------------
DROP TABLE IF EXISTS `ilive_field_id_manager`;
CREATE TABLE `ilive_field_id_manager` (
  `id` int(11) NOT NULL,
  `table_name` varchar(255) NOT NULL,
  `field_name` varchar(255) NOT NULL,
  `next_id` bigint(20) DEFAULT NULL,
  `max_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ilive_field_id_manager
-- ----------------------------
INSERT INTO `ilive_field_id_manager` VALUES ('100', 'ilive_event', 'live_event_id', '1110', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('101', 'ilive_live_room', 'room_id', '811', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('102', 'ilive_fcode', 'code_id', '121', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('103', 'ilive_fcode_detail', 'id', '175', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('104', 'ilive_manager', 'user_id', '356', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('105', 'ilive_enterprise', 'enterprise_id', '155', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('106', 'ilive_message', 'msg_id', '1802', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('107', 'bbs_diyform', 'diyform_id', '158', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('108', 'bbs_diyform_data', 'id', '418', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('109', 'bbs_diymodel', 'diymodel_id', '431', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('111', 'ilive_media_file', 'file_id', '717', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('112', 'ilive_page_decorate', 'id', '3522', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('113', 'ilive_guests', 'guests_id', '127', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('114', 'ilive_qa_message', 'msg_id', '100', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('115', 'ilive_room_authbill', 'auth_id', '200', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('116', 'ilive_thematic', 'thematic_id', '102', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('118', 'ilive_media_file_comments', 'comments_id', '207', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('119', 'ilive_live_report', 'id', '100', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('120', 'ilive_enterprise_whitebill', 'whitebill_id', '143', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('121', 'ilive_comments', 'comments_id', '319', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('122', 'content_policy', 'id', '317', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('123', 'content_select', 'id', '498', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('124', 'ilive_enterprise_fans', 'id', '852', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('125', 'ilive_view_whitebill', 'bill_id', '134', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('126', 'ilive_enterprise_fans', 'id', '110', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('127', 'iLive_sensitive_word', 'id', '102', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('128', 'ilive_view_record', 'record_id', '711', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('129', 'imanager_cert_topic', 'topic_id', '109', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('130', 'ilive_appointment', 'id', '183', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('131', 'ilive_enterprise_terminaluser', 'id', '264', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('132', 'ilive_viewset_record', 'id', '106', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('133', 'ilive_room_register', 'id', '114', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('134', 'ilive_history_video', 'history_id', '112', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('135', 'ilive_record_random_task', 'task_id', '114', '100');
INSERT INTO `ilive_field_id_manager` VALUES ('136', 'ilive_roomshare_config', 'share_id', '100', '100');
