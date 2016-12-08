/**

爬虫任务模型，两张表，
第一张为基本描述，
第二张为任务配置。

 */
/* 第一张为基本描述 */
CREATE TABLE `task` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '爬虫任务的id，每个任务的id是唯一的，字符串，在数据库的长度为64个字符。',
  `idUser` bigint(20) DEFAULT NULL COMMENT '用户id，表明此任务属于哪个用户的任务。默认值为NULL。',
  `name` varchar(512) DEFAULT '' COMMENT '爬虫任务名称。默认值为空字符串。',
  `remark` varchar(1024) DEFAULT '' COMMENT '爬虫任务描述',
  `type` int(11) DEFAULT '0' COMMENT '爬虫任务类型，如：全站任务或栏目任务',
  `depthCrawl` int(11) DEFAULT '3' COMMENT '爬取深度',
  `depthDynamic` int(11) DEFAULT '3' COMMENT '动态爬取深度',
  `pass` int(11) DEFAULT '0' COMMENT '遍数，默认为0',
  `weight` int(11) DEFAULT '0' COMMENT '任务的权重，默认为0',
  `threads` int(11) DEFAULT '1' COMMENT '任务爬取时的线程数，要大于0，默认为1',
  `scheduleType` int(11) DEFAULT '0' COMMENT '任务的调度类型，默认为0',
  `workerNumber` int(11) DEFAULT '1' COMMENT '执行任务的节点数量，大于0，默认为1',
  `completeTimes` int(11) DEFAULT '0' COMMENT '爬取任务完成的次数',
  `cycleRecrawl` int(11) DEFAULT '72' COMMENT '任务爬取周期，以小时为单位，默认72',
  `status` int(11) DEFAULT '0' COMMENT '任务状态，用数字标识，默认为0',
  `deleteFlag` tinyint(1) DEFAULT '0' COMMENT '任务的软删除标识，为true时不再进行爬取。默认false。',
  `activeFlag` tinyint(1) DEFAULT '1' COMMENT '任务的激活标识，为 false 时不进行爬取。默认 true。',
  `timeStart` datetime DEFAULT NULL COMMENT '爬取任务启动的时间',
  `timeStop` datetime DEFAULT NULL COMMENT '爬取任务停止的时间',
  `timeLastCrawl` datetime DEFAULT NULL COMMENT '任务最后一次爬取的时间',
  `costLastCrawl` int(11) DEFAULT '0' COMMENT '上次爬取消耗的时间，单位分钟',
  `createDate` datetime DEFAULT NULL COMMENT '任务创建的时间',
  `downloader` varchar(512) DEFAULT '' COMMENT '下载器类型',
  `parser` varchar(512) DEFAULT '' COMMENT '解析器类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 第二张为任务配置 */
CREATE TABLE `taskconfig` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置项id',
  `idTask` varchar(64) DEFAULT NULL COMMENT '爬虫任务的id，外键，参考表 task',
  `confType` enum('undefine','seedsInfoList','templates','configsMap','clickRegex','regexFilter','protocolFilter','suffixFilter','proxy','tags','categories') DEFAULT 'undefine' COMMENT '配置类型',
  `confValue` longtext,
  `addDate` datetime DEFAULT NULL COMMENT '配置项添加或修改的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idTask_confType` (`idTask`,`confType`),
  KEY `idTask` (`idTask`),
  CONSTRAINT `taskconfig_ibfk_1` FOREIGN KEY (`idTask`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;


CREATE TABLE `hbase_ddp_batch_get_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,主键，自动增加',
  `idTask` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '爬虫任务的id，每个任务的id是唯一的，字符串，在数据库的长度为64个字符。',
  `nextSign` varchar(512) CHARACTER SET utf8 DEFAULT '' COMMENT '读取下一批的标识，一般是时间戳',
  `nextRow` varchar(512) CHARACTER SET utf8 DEFAULT '' COMMENT 'HBASE 的下一行标识',
  `lastSize` bigint(20) DEFAULT '0' COMMENT '上次取出数据的条数',
  `allSize` bigint(20) DEFAULT '0' COMMENT '总的取出数据条数',
  `updateTime` datetime DEFAULT NULL COMMENT '数据更新时间',
  `createTime` datetime DEFAULT NULL COMMENT '记录添加时间',
  UNIQUE KEY `idTask` (`idTask`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



CREATE TABLE `hbase_ddp_batch_get_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,主键，自动增加',
  `idTask` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '爬虫任务的id，每个任务的id是唯一的，字符串，在数据库的长度为64个字符。',
  `nextSign` varchar(512) CHARACTER SET utf8 DEFAULT '' COMMENT '读取下一批的标识，一般是时间戳',
  `logInfo` text CHARACTER SET utf8 COMMENT '  logInfo 为JSON字符串,是表 hbase_ddp_batch_get_info 中的记录JSON化后的内容。',
  `createTime` datetime DEFAULT NULL COMMENT '记录添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `pos_duocai_export` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskId` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



CREATE TABLE `article_parse_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，表ID',
  `tid` varchar(32) DEFAULT NULL COMMENT '对应的任务ID',
  `config` text COMMENT '对应的解析类对应的文章字段Xpath的json数据',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tid` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 COMMENT='此表添加文章解析各个字段的Xpath，通过Xpath获得比如标题、正文、来源等文章字段'
;

CREATE TABLE `crawlerdata` (
  `seqeueID` int(10) NOT NULL AUTO_INCREMENT COMMENT '表的ID，自动递增',
  `tid` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '任务ID',
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `statusCode` int(3) DEFAULT NULL COMMENT 'http访问状态码',
  `pass` int(10) DEFAULT NULL COMMENT '爬虫爬取遍数',
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '爬虫类型',
  `rootUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fromUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` longtext COLLATE utf8_unicode_ci,
  `html` longtext COLLATE utf8_unicode_ci,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `startTime` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `crawlTime` datetime DEFAULT NULL,
  `publishTime` datetime DEFAULT NULL,
  `depthfromSeed` int(10) DEFAULT NULL,
  `count` int(10) DEFAULT NULL,
  `tag` longtext COLLATE utf8_unicode_ci,
  `fetched` tinyint(5) DEFAULT NULL,
  `pasedData` text COLLATE utf8_unicode_ci COMMENT '通过GenericParse解析到的数据，字段可以灵活增加',
  PRIMARY KEY (`seqeueID`),
  UNIQUE KEY `url unique_index` (`url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='此表存储爬虫程序下载并解析后的数据'
;