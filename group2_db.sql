-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: May 03, 2014, 01:50 PM
-- 伺服器版本: 5.0.51
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- 資料庫: `group2_db`
-- 

-- --------------------------------------------------------

-- 
-- 資料表格式： `account`
-- 

CREATE TABLE `account` (
  `acc` varchar(20) collate utf8_unicode_ci NOT NULL,
  `pwd` varchar(40) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`acc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- 列出以下資料庫的數據： `account`
-- 

INSERT INTO `account` VALUES ('group2', 'imbox');

-- --------------------------------------------------------

-- 
-- 資料表格式： `block_map`
-- 

CREATE TABLE `block_map` (
  `uid` int(20) NOT NULL auto_increment,
  `blockName` varchar(40) collate utf8_unicode_ci NOT NULL,
  `sequence` int(5) NOT NULL,
  `fid` varchar(40) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `fid` (`fid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=24 ;

-- 
-- 列出以下資料庫的數據： `block_map`
-- 

INSERT INTO `block_map` VALUES (1, '4e4b036c810b7d2a4d03eb1ab124d0e8 ', 0, 'a65c4ca9e0e95759e77e5ad19b31c960');
INSERT INTO `block_map` VALUES (2, '47cd0be18d3456e83cc1b7d7823c8def ', 1, 'a65c4ca9e0e95759e77e5ad19b31c960');
INSERT INTO `block_map` VALUES (3, '3c3ddf981871d1ad7b33630255115ce0', 2, 'a65c4ca9e0e95759e77e5ad19b31c960');

-- --------------------------------------------------------

-- 
-- 資料表格式： `device`
-- 

CREATE TABLE `device` (
  `userID` int(3) NOT NULL auto_increment,
  `account` varchar(20) collate utf8_unicode_ci NOT NULL,
  `device` varchar(20) collate utf8_unicode_ci NOT NULL,
  `last_IP` varchar(15) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`userID`),
  KEY `account` (`account`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

-- 
-- 列出以下資料庫的數據： `device`
-- 

INSERT INTO `device` VALUES (1, 'group2', 'C8:E0:EB:5A:11:81', '180.218.164.45');
INSERT INTO `device` VALUES (2, 'group2', 'C4:85:08:20:D3:76', '192.168.0.101');

-- --------------------------------------------------------

-- 
-- 資料表格式： `group2`
-- 

CREATE TABLE `group2` (
  `fileName` varchar(20) collate utf8_unicode_ci NOT NULL,
  `fid` varchar(40) collate utf8_unicode_ci NOT NULL,
  `f_MD5` varchar(40) collate utf8_unicode_ci NOT NULL,
  `antedent_f_MD5` varchar(40) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`fileName`),
  KEY `fid` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- 列出以下資料庫的數據： `group2`
-- 

INSERT INTO `group2` VALUES ('test.pdf', 'a65c4ca9e0e95759e77e5ad19b31c960', 'a65c4ca9e0e95759e77e5ad19b31c960', 'a65c4ca9e0e95759e77e5ad19b31c960');

-- --------------------------------------------------------

-- 
-- 資料表格式： `server_file`
-- 

CREATE TABLE `server_file` (
  `fid` varchar(40) collate utf8_unicode_ci NOT NULL,
  `counter` int(5) NOT NULL,
  `f_MD5` varchar(40) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- 列出以下資料庫的數據： `server_file`
-- 

INSERT INTO `server_file` VALUES ('a65c4ca9e0e95759e77e5ad19b31c960', 1, 'a65c4ca9e0e95759e77e5ad19b31c960');

-- 
-- 備份資料表限制
-- 

-- 
-- 資料表限制 `block_map`
-- 
ALTER TABLE `block_map`
  ADD CONSTRAINT `block_map_ibfk_1` FOREIGN KEY (`fid`) REFERENCES `server_file` (`fid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 
-- 資料表限制 `device`
-- 
ALTER TABLE `device`
  ADD CONSTRAINT `device_ibfk_1` FOREIGN KEY (`account`) REFERENCES `account` (`acc`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 
-- 資料表限制 `group2`
-- 
ALTER TABLE `group2`
  ADD CONSTRAINT `group2_ibfk_1` FOREIGN KEY (`fid`) REFERENCES `server_file` (`fid`) ON DELETE CASCADE ON UPDATE CASCADE;
