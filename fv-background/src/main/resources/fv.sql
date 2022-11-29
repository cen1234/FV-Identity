-----------------------------------------------------------------------------------
-- TABLE USER --
-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `role` VARCHAR(50) DEFAULT '普通用户' COMMENT '角色',
  `password` VARCHAR(50) NOT NULL COMMENT '密码',
  `realname` VARCHAR(50) NULL COMMENT '真实姓名',
  `sex` CHAR(2) NULL,
  `age` INT(3) NULL COMMENT '年龄',
  `email` VARCHAR(50)  NULL COMMENT '邮箱',
  `phone` VARCHAR(50)  NULL COMMENT '电话',
  `address` VARCHAR(255)  NULL COMMENT '地址',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  CONSTRAINT `sex` CHECK (`sex` IN('男','女'))
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'administrator','管理员','123456',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (2,'放假的小苗','普通用户','123456','小苗','女',10,'111@163.com','18709261622','西安市',NULL);
INSERT INTO `user` VALUES (3,'要放假的小俱','普通用户','123456','小俱','女',10,'111@163.com','18709261622','西安市',NULL);
INSERT INTO `user` VALUES (4,'小李加油','普通用户','123456','小李','女',10,'111@163.com','18709261622','西安市',NULL);
INSERT INTO `user` VALUES (5,'小王加油','普通用户','123456','小王','女',10,'111@163.com','18709261622','西安市',NULL);
UNLOCK TABLES;



----------------------------------------------------------------------------------------------------
-- TABLE ROLE ---
----------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) NULL COMMENT '名称',
  `description` VARCHAR(255) NULL COMMENT '描述',
  `role_key` VARCHAR(50)  NULL COMMENT '唯一标识',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES(1,'管理员','可以使用系统的所有功能','admin',NULL);
INSERT INTO `role` VALUES(2,'普通用户','只能使用单个|多个果蔬识别功能及其子功能','user',NULL);
UNLOCK TABLES;




--------------------------------------------------------------
--- TABLE MENU ----
--------------------------------------------------------------
DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` INT(11) NULL COMMENT '父级id',
  `name` VARCHAR(255) NULL COMMENT '名称',
  `description` VARCHAR(255) NULL COMMENT '描述',
  `path` VARCHAR(255) NULL COMMENT '路径',
  `icon` VARCHAR(255) NULL COMMENT '图标',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';


--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES(1,NULL,'用户管理','管理注册的所有用户，具有新增，编辑，删除，导出，查询,修改角色身份功能','/user','el-icon-user',NULL);
INSERT INTO `menu` VALUES(2,NULL,'统计结果','统计用户的基本信息和行为信息','/statistical','el-icon-pie-chart',NULL);
INSERT INTO `menu` VALUES(3,NULL,'权限与菜单','负责系统权限分配与菜单管理功能',NULL,'el-icon-plus',NULL);
INSERT INTO `menu` VALUES(4,3,'权限管理','系统当前只有两种角色身份，一个是管理员，一个是普通用户，管理员给不同身份的用户分配可以使用的功能','/role',NULL,NULL);
INSERT INTO `menu` VALUES(5,3,'菜单管理','系统当前所有可以使用的功能，具备新增，删除，编辑子菜单，编辑，查询功能','/menu',NULL,NULL);
INSERT INTO `menu` VALUES(6,NULL,'单个果蔬识别','具备单个果蔬识别，图片入库，相似图片检索功能',NULL,'el-icon-apple',NULL);
INSERT INTO `menu` VALUES(7,6,'单个识别','根据单果蔬照片，识别出果蔬名称，并具备果蔬介绍，果蔬价格参考的功能','/single',NULL,NULL);
INSERT INTO `menu` VALUES(8,6,'图片入库','只能由管理员使用，负责将图片上传到ai库中','/imageStorage',NULL,NULL);
INSERT INTO `menu` VALUES(9,6,'相似图片','根据识别到的结果，在ai库中检索分数大于0.5的照片，用于用户查找素材或给予用户一些推荐','/similar',NULL,NULL);
INSERT INTO `menu` VALUES(10,NULL,'多个果蔬识别','具备多个果蔬识别，果蔬分类功能',NULL,'el-icon-grape',NULL);
INSERT INTO `menu` VALUES(11,10,'多个识别','根据提供的多果蔬图片，识别当中出现的果蔬','/multiple',NULL,NULL);
INSERT INTO `menu` VALUES(12,10,'果蔬分类','根据提供的多果蔬图片，将识别到的果蔬分到不同的大类中','/classification',NULL,NULL);
INSERT INTO `menu` VALUES(13,NULL,'日志管理','记录用户的行为信息，仅供管理员使用','/log','el-icon-edit-outline',NULL);
UNLOCK TABLES;



----------------------------------------------------------------------------------
---TABLE DIST ----
----------------------------------------------------------------------------------
DROP TABLE IF EXISTS `dist`;

CREATE TABLE `dist` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(255) NULL COMMENT '名称',
  `value` VARCHAR(255) NULL COMMENT '值',
  `type` VARCHAR(255) NULL COMMENT '类型',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图标表';


--
-- Dumping data for table `dist`
--

LOCK TABLES `dist` WRITE;
/*!40000 ALTER TABLE `dist` DISABLE KEYS */;
INSERT INTO `dist` VALUES(1,'role','el-icon-plus','icon',NULL);
INSERT INTO `dist` VALUES(2,'user','el-icon-user','icon',NULL);
INSERT INTO `dist` VALUES(3,'log','el-icon-edit-outline','icon',NULL);
INSERT INTO `dist` VALUES(4,'single','el-icon-apple','icon',NULL);
INSERT INTO `dist` VALUES(5,'similar','el-icon-apple','icon',NULL);
INSERT INTO `dist` VALUES(6,'multiple','el-icon-grape','icon',NULL);
INSERT INTO `dist` VALUES(7,'fv','el-icon-grape','icon',NULL);
INSERT INTO `dist` VALUES(8,'statistical','el-icon-pie-chart','icon',NULL);
UNLOCK TABLES;



-----------------------------------------------------------------------------
-----TABLE ROLE-MENU ------
-----------------------------------------------------------------------------
DROP TABLE IF EXISTS `role_menu`;

CREATE TABLE `role_menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` INT NOT NULL COMMENT '角色id',
  `menu_id` INT  NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单对应表';


--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES(1,1,1);
INSERT INTO `role_menu` VALUES(2,1,2);
INSERT INTO `role_menu` VALUES(3,1,3);
INSERT INTO `role_menu` VALUES(4,1,4);
INSERT INTO `role_menu` VALUES(5,1,5);
INSERT INTO `role_menu` VALUES(6,1,6);
INSERT INTO `role_menu` VALUES(7,1,7);
INSERT INTO `role_menu` VALUES(8,1,8);
INSERT INTO `role_menu` VALUES(9,1,9);
INSERT INTO `role_menu` VALUES(10,1,10);
INSERT INTO `role_menu` VALUES(11,1,11);
INSERT INTO `role_menu` VALUES(12,1,12);
INSERT INTO `role_menu` VALUES(13,1,13);
INSERT INTO `role_menu` VALUES(14,2,6);
INSERT INTO `role_menu` VALUES(15,2,7);
INSERT INTO `role_menu` VALUES(16,2,8);
INSERT INTO `role_menu` VALUES(17,2,9);
INSERT INTO `role_menu` VALUES(18,2,10);
INSERT INTO `role_menu` VALUES(19,2,11);
INSERT INTO `role_menu` VALUES(29,2,12);
UNLOCK TABLES;`menu``role_menu``menu`




-----------------------------------------------------------------
----TABLE PICTURE-----
-----------------------------------------------------------------
CREATE TABLE `picture` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `name` VARCHAR(255) NULL COMMENT '图片名称',
  `type` VARCHAR(255) NULL COMMENT '文件类型',
  `size` BIGINT(20) NULL COMMENT '文件大小',
  `url`  VARCHAR(255) NULL COMMENT '下载链接',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图片表';


--------------------------------------------------------------------
--TABLE SINGLE --
--------------------------------------------------------------------
DROP TABLE IF EXISTS `single`;

CREATE TABLE `single` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `name` VARCHAR(50)  NULL COMMENT '名字',
  `score` DOUBLE(10,10) NULL COMMENT '分数',
  `title` VARCHAR(50) NULL COMMENT '标题',
  `description` VARCHAR(1000) NULL COMMENT '描述',
  `benefit` VARCHAR(1000) NULL COMMENT '益处',
  `price` DOUBLE(10,2) NULL COMMENT '最低价格',
  `link` VARCHAR(255) NULL COMMENT '最低价格商品的链接',
  `shop` VARCHAR(50) NULL COMMENT '最低价格商品的店家',
  `introduction` VARCHAR(255) NULL COMMENT '最低价格商品的介绍',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='单个果蔬识别表';




--------------------------------------------------------------------------
-----TABLE LOG -----
--------------------------------------------------------------------------
DROP TABLE IF EXISTS `log`;

CREATE TABLE `log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `record_id` VARCHAR(30) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户名',
   `path` VARCHAR(255) NULL COMMENT '访问路径',
   `name` VARCHAR(255) NULL COMMENT '使用功能名',
  `photo` VARCHAR(255) NULL COMMENT '识别图片',
  `resule_num` INT(11) NULL COMMENT '识别结果数',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日志表';



----------------------------------------------------------------------------
----TABLE  MULTIPLE-----
----------------------------------------------------------------------------
DROP TABLE IF EXISTS `multiple`;

CREATE TABLE `multiple` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `name` VARCHAR(50)  NULL COMMENT '名字',
  `score` DOUBLE(10,10) NULL COMMENT '分数',
  `type` CHAR(10) NULL COMMENT '类型',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `type` CHECK (`type` IN('水果','蔬菜','坚果','非果蔬'))
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='多个果蔬识别表';



-------------------------------------------------------------------------------
-----TABLE PRODUCT_INFORMATION ---------
-------------------------------------------------------------------------------
DROP TABLE IF EXISTS `product_information`;

CREATE TABLE `product_information` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `name` VARCHAR(50)  NULL COMMENT '名字',
  `price` DOUBLE(10,2) NULL COMMENT '价格',
  `link` VARCHAR(255) NULL COMMENT '商品链接',
  `shop` VARCHAR(50) NULL COMMENT '店家',
  `introduction` VARCHAR(255) NULL COMMENT '商品的介绍',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='价格表';




--------------------------------------------------------------------------------
----TABLE SIMILAR ------
--------------------------------------------------------------------------------
DROP TABLE IF EXISTS `similar`;

CREATE TABLE `similar` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `score` DOUBLE(10,2) NULL COMMENT '分数',
  `brief` VARCHAR(255) NULL COMMENT '图片信息',
  `cont_sign` VARCHAR(255) NULL COMMENT '图片签名',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='相似图片表';