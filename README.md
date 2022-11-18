**一.果蔬系统使用前置步骤：**

 1.修改fv-background/src/main/resources/application.properties文件的spring.datasource.url（根据自己建的数据库名修改），
 spring.datasource.username（修改成自己的数据库用户名），spring.datasource.password（修改成自己的数据库密码）
 
 2.修改fv-background/src/main/java/com/fvbackground/controller/FileController.java 里的private String fileUploadPath = 
 "C:/Users/cxy/Desktop/创新综合实践/FV/files/";（这里用来存放上传的图片，尽量把地址改成自己拉下来新建的目录中，最好别加中文）
 
 3.修改fv-background/src/main/java/com/fvbackground/common/Ingredient.java里的 private String fileUploadPath =
  "C:/Users/cxy/Desktop/创新综合实践/FV/files/";（修改地址，同2保持一致的地址）
  
 4.数据库里的插入数据语句后续我会补充上去，主要是user表和menu表影响 
 
 **二.果蔬系统使用**
 
 这里主要补充一些功能和api接口，补充ing