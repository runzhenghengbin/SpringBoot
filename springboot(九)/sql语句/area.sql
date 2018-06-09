/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-06-09 23:16:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `parent_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of area
-- ----------------------------
INSERT INTO `area` VALUES ('33700101', '高新区', '37001');
INSERT INTO `area` VALUES ('370', '山东', '0');
INSERT INTO `area` VALUES ('37001', '济南', '370');
INSERT INTO `area` VALUES ('37002', '泰安', '370');
