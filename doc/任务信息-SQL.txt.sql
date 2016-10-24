/**

爬虫任务模型，两张表，
第一张为基本描述，
第二张为任务配置。

 */

CREATE TABLE tbCrawlerTask(

  `id` VARCHAR(64) CHARACTER SET utf8 COMMENT '爬虫任务的id，每个任务的id是唯一的，字符串，在数据库的长度为64个字符。',
  `idUser` BIGINT COMMENT   '用户id，表明此任务属于哪个用户的任务。默认值为NULL。',
  `name` VARCHAR(512) CHARACTER SET utf8 DEFAULT '' COMMENT '爬虫任务名称。默认值为空字符串。',
  `remark` VARCHAR(1024)  CHARACTER SET utf8 DEFAULT '' COMMENT '爬虫任务描述',
  `type` INT DEFAULT 0 COMMENT  '爬虫任务类型，如：全站任务或栏目任务',
  `depthCrawl` INT  DEFAULT 3 COMMENT '爬取深度',
  `depthDynamic` INT DEFAULT 3 COMMENT   '动态爬取深度',
  `pass` INT DEFAULT 0 COMMENT '遍数，默认为0',
  `weight` INT DEFAULT 0 COMMENT   '任务的权重，默认为0',
  `threads` INT  DEFAULT 1 COMMENT  '任务爬取时的线程数，要大于0，默认为1',
  `scheduleType` INT DEFAULT 0 COMMENT '任务的调度类型，默认为0',
  `workerNumber` INT DEFAULT 1 COMMENT '执行任务的节点数量，大于0，默认为1',
  `completeTimes` INT  DEFAULT 0 COMMENT '爬取任务完成的次数',
  `cycleRecrawl` INT DEFAULT 72 COMMENT '任务爬取周期，以小时为单位，默认72',
  `status` INT DEFAULT 0 COMMENT '任务状态，用数字标识，默认为0',
  `deleteFlag` BOOLEAN DEFAULT FALSE COMMENT '任务的软删除标识，为true时不再进行爬取。默认false。',
  `activeFlag` BOOLEAN DEFAULT TRUE COMMENT '任务的激活标识，为 false 时不进行爬取。默认 true。',
  `timeStart` DATETIME DEFAULT NULL COMMENT '爬取任务启动的时间',
  `timeStop` DATETIME DEFAULT NULL COMMENT '爬取任务停止的时间',
  `timeLastCrawl` DATETIME DEFAULT NULL COMMENT '任务最后一次爬取的时间',
  `costLastCrawl` INT DEFAULT 0 COMMENT '上次爬取消耗的时间，单位分钟',
  `createDate` DATETIME DEFAULT NULL  COMMENT '任务创建的时间',
  `downloader` VARCHAR(512) CHARACTER SET utf8  DEFAULT '' COMMENT '下载器类型',
  `parser` VARCHAR(512) CHARACTER SET utf8 DEFAULT '' COMMENT '解析器类型',
  `tags` LONGTEXT CHARACTER SET utf8  COMMENT '任务标签，字符串JSON数组，如 ["tag1","tag2","tag3"]',

  PRIMARY KEY (`id`)
);


CREATE TABLE tbCrawlerTaskConfig(

  `id` BIGINT  AUTO_INCREMENT COMMENT '配置项id',
  `idTask` VARCHAR(64) CHARACTER SET utf8 COMMENT '爬虫任务的id，外键，参考表 tbCrawlerTask',
  `confName` VARCHAR(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置项名称',
  `confType` ENUM('undefine','seedUrls','templates','configs','clickRegex','regexFilter',
                  'protocolFilter','suffixFilter','proxy') CHARACTER SET utf8 DEFAULT 'undefine' COMMENT '配置类型',
  `confValue` LONGTEXT CHARACTER SET utf8,
  `addDate` DATETIME DEFAULT NULL COMMENT '配置项添加或修改的时间',

  PRIMARY KEY (`id`),
  FOREIGN KEY (`idTask`) REFERENCES tbCrawlerTask(`id`)

);

