/**

  Mysql table structure.

**/

-- ----------------------------
-- Table structure for `crawlerdata`
-- ----------------------------
CREATE TABLE `crawlerdata` (
  `seqeueID` int(10) NOT NULL auto_increment,
  `tid` varchar(32) collate utf8_unicode_ci NOT NULL,
  `url` varchar(255) collate utf8_unicode_ci default NULL,
  `statusCode` int(3) default NULL,
  `pass` int(10) default NULL,
  `type` varchar(255) collate utf8_unicode_ci default NULL,
  `rootUrl` varchar(255) collate utf8_unicode_ci default NULL,
  `fromUrl` varchar(255) collate utf8_unicode_ci default NULL,
  `text` longtext collate utf8_unicode_ci,
  `html` longtext collate utf8_unicode_ci,
  `title` varchar(255) collate utf8_unicode_ci default NULL,
  `startTime` varchar(30) collate utf8_unicode_ci default NULL,
  `crawlTime` datetime default NULL,
  `publishTime` datetime default NULL,
  `depthfromSeed` int(10) default NULL,
  `count` int(10) default NULL,
  `tag` tinyint(5) default NULL,
  `fetched` tinyint(5) default NULL,
  PRIMARY KEY  (`seqeueID`)
) ENGINE=InnoDB AUTO_INCREMENT=10680 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
