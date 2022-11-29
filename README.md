# 果蔬识别系统使用及介绍

##### 一.使用前置条件

- 默认电脑已配置JDK,建议为1.8,如果没有配置，可以参考https://blog.csdn.net/weixin_60274198/article/details/125301317 进行配置；

- 默认电脑已配置maven,如果没有配置，可以参考https://blog.csdn.net/weixin_42460596/article/details/109479342 进行配置；

- 默认电脑上已配置MySQL,可以在https://www.bilibili.com/video/BV1Vy4y1z7EX/?spm_id_from=333.999.0.0&vd_source=1b3368ab196237e3164fac4562d666e6 视频评论区找找；

- 默认电脑已配置node,如果没有配置，可以参考https://www.cnblogs.com/lgx5/p/10732016.html 进行配置；

- 修改*fv-background/src/main/resources/application.properties*文件的*spring.datasource.url*（根据自己建的数据库名修改）， *spring.datasource.username*（修改成自己的数据库用户名），*spring.datasource.password*（修改成自己的数据库密码）；

- 修改*fv-background/src/main/java/com/fvbackground/controller/FileController.java，fv-background/src/main/java/com/fvbackground/common/Ingredient.java，fv-background\src\main\java\com\fvbackground\common\Retrieve.java*里的*private String fileUploadPath = "C:/Users/cxy/Desktop/创新综合实践/FV/files/";*（这里用来存放上传的图片，尽量把地址改成自己拉下来新建的目录中，最好别加中文）；

- 修改*fv-front\vue.config.js* 里的target(改成自己当前正在使用的网络，win+R键->cmd->ipconfig,就能看到ipv4,把这个ip复制过来替换192.168.1.6就可以了，9000就不用动了)；

- 安装后台依赖：把项目拉进IDEA后，一般会自动安装后台的一些依赖，等pom.xml不报红了，就说明依赖安装完毕；

- 安装前端依赖：win+R键进去，cd到fv-front所在的地方，npm i 安装依赖，会比较慢；

- 启动后台：IDEA进入*fv-background\src\main\java\com\fvbackground\FvBackgroundApplication.java*  ，代码的左侧会出现绿色的三角形，点击中间的三角，选择Run 'FvBackgroundApplication'也就是第一个选项，然后控制台没报错，就算启动成功了；

- 启动前台：参考https://blog.csdn.net/xiayngbaidu12345/article/details/128107163  进行配置，之后选择设置的serve,点击右边的绿色三角形启动就可以了，至此，项目启动完毕。

  

  

  

  

  

  

