/**

  Mysql table structure.

**/

-- ----------------------------
-- Table structure for `crawlerdata`
-- ----------------------------
CREATE TABLE `crawlerdata`(
    `seqeueID` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `tid` VARCHAR(64) CHARACTER SET utf8 NOT NULL,
    `url` VARCHAR(255) CHARACTER SET utf8,
    `statusCode` INT,
    `pass` INT,
    `type` VARCHAR(255) CHARACTER SET utf8,
    `rootUrl` VARCHAR(255) CHARACTER SET utf8,
    `fromUrl` VARCHAR(255) CHARACTER SET utf8,
    `text` LONGTEXT CHARACTER SET utf8,
    `html` LONGTEXT CHARACTER SET utf8,
    `title` VARCHAR(255) CHARACTER SET utf8,
    `startTime` VARCHAR(30) CHARACTER SET utf8,
    `crawlTime` DATETIME,
    `publishTime` DATETIME,
    `depthfromSeed` INT,
    `count` INT,
    `tag` VARCHAR(255) CHARACTER SET utf8,
    `fetched` TINYINT,
    `author` VARCHAR(32) CHARACTER SET utf8,
    `sourceName` VARCHAR(32) CHARACTER SET utf8,
    `parsedData` LONGTEXT CHARACTER SET utf8,

    PRIMARY KEY (`seqeueID`)
);

