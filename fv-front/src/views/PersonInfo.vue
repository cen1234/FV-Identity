<template>
  <div class="main-content">
      <div class="header">
          <el-carousel :interval="4000" type="card" height="200px">
              <el-carousel-item class="card-image image1"></el-carousel-item>
              <el-carousel-item class="card-image image2"></el-carousel-item>
              <el-carousel-item class="card-image image3"></el-carousel-item >
              <el-carousel-item class="card-image image4"></el-carousel-item>
              <el-carousel-item class="card-image image5"></el-carousel-item>
          </el-carousel>
          <div class="goHome">
              <a href="/">Go Home</a>
          </div>
          <div class="user-content">
              <div class="user-image"></div>
              <div class="user-name">{{user.username}}</div>
              <div class="user-info">
                  <el-descriptions class="margin-top" title="个人信息" :column="4" direction="vertical" size="medium" border>
                      <el-descriptions-item label="ID">{{user.id}}</el-descriptions-item>
                      <el-descriptions-item label="用户名">{{user.username}}</el-descriptions-item>
                      <el-descriptions-item label="密码" v-if="false">{{user.password}}</el-descriptions-item>
                      <el-descriptions-item label="真实姓名">{{user.realname}}</el-descriptions-item>
                      <el-descriptions-item label="性别">{{user.sex}}</el-descriptions-item>
                      <el-descriptions-item label="年龄">{{user.age}}</el-descriptions-item>
                      <el-descriptions-item label="邮箱">{{user.email}}</el-descriptions-item>
                      <el-descriptions-item label="电话">{{user.phone}}</el-descriptions-item>
                      <el-descriptions-item label="地址">{{user.address}}</el-descriptions-item>
                  </el-descriptions>
<!--                  修改个人信息-->
                  <el-button type="text" @click="update()" class="updateInfo">修改个人信息</el-button>
                  <el-form :model="UserForm" :rules="rules" ref="UserForm">
                      <el-descriptions v-if="IsUpdate" :column="2" border size="medium">
                          <el-descriptions-item label="ID">
                              <el-form-item style="margin-bottom: 6px;">
                                  <el-input  v-model="UserForm.id" autocomplete="off" size="small"  style="width: 250px;" disabled></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="用户名">
                              <el-form-item  prop="username" style="margin-bottom: 6px;">
                                  <el-input  v-model="UserForm.username" autocomplete="off" size="small"  style="width: 250px;" clearable ></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="密码">
                              <el-form-item  prop="password" style="margin-bottom: 6px;">
                                  <el-input  v-model="UserForm.password" autocomplete="off" size="small"  style="width: 250px;" show-password></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="真实姓名">
                              <el-form-item  prop="realname" style="margin-bottom: 6px;">
                                  <el-input v-model="UserForm.realname" autocomplete="off" size="small"  style="width: 250px;" clearable ></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="性别">
                              <el-form-item  prop="sex" style="margin-bottom: 0px;">
                                  <el-radio-group v-model="UserForm.sex">
                                      <el-radio label="男"></el-radio>
                                      <el-radio label="女"></el-radio>
                                  </el-radio-group>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="年龄">
                              <el-form-item  prop="age" style="margin-bottom: 0px;">
                                  <el-input-number v-model="UserForm.age" size="small" :min="1" :max="130" label="年龄"></el-input-number>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="邮箱">
                              <el-form-item prop="email" style="margin-bottom: 6px;">
                                  <el-input  v-model="UserForm.email" autocomplete="off" size="small"  style="width: 250px;" clearable ></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="电话">
                              <el-form-item prop="phone" style="margin-bottom: 6px;">
                                  <el-input  v-model="UserForm.phone" autocomplete="off" size="small" maxlength="11" style="width: 250px;" clearable ></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                          <el-descriptions-item label="地址">
                              <el-form-item style="margin-bottom: 6px;">
                                  <el-input placeholder="请输入地址" type="textarea"  v-model="UserForm.address" autosize maxlength="255" show-word-limit style="width: 670px;"></el-input>
                              </el-form-item>
                          </el-descriptions-item>
                      </el-descriptions>
                      <div class="btn" v-if="IsUpdate">
                          <el-button @click="IsUpdate = false">取 消</el-button>
                          <el-button type="primary"  @click="save('UserForm')">确 定</el-button>
                      </div>
                  </el-form>
              </div>
          </div>
      </div>
  </div>
</template>

<script>

    import request from "../../utils/request";

    export default {
        name: "PersonInfo",
        data() {
            return {
                user:{},//用户信息
                IsUpdate:false,//是否显示修改表格
                UserForm:{},//编辑信息表单
                username:'',
                rules:{
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'},
                        {pattern: /^([a-zA-Z0-9_-]|[\u4E00-\uFA29]|[\uE7C7-\uE7F3]){1,30}$/, message: `用户名支持大小写英文，数字，-,_,中文`, trigger: 'blur'}
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {pattern: /^[a-zA-Z0-9_-]{1,15}$/, message: `密码支持大小写英文，数字，-,_`, trigger: 'blur'}
                    ],
                    realname: [
                        { required: true, message: '请输入真实姓名', trigger: 'blur' },
                        { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'},
                        {pattern: /^([a-zA-Z]|[\u4E00-\uFA29]|[\uE7C7-\uE7F3]){1,30}$/, message: `用户名支持大小写英文，中文`, trigger: 'blur'}
                    ],
                    sex: [
                        { required: true, message: '请选则您的性别', trigger: 'change' }
                    ],
                    age: [
                        { required: true, message: '请填写你的年龄', trigger: 'blur' }
                    ],
                    email: [
                        { required: true, message: '请输入邮箱', trigger: 'blur' },
                        {pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/, message: `请输入正确的邮箱格式`, trigger: 'blur'}
                    ],
                    phone: [
                        { required: true, message: '请输入电话', trigger: 'blur' },
                        {pattern: /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/, message: `请输入正确的电话格式`, trigger: 'blur'}
                    ],
                },//表单校验规则
            }
        },
        created() {

            this.load();
        },
        methods:{
            //加载信息
            load: function () {
                this.username = JSON.parse(localStorage.getItem("user")).username;
                request.get('/personInfo?username='+this.username).then(res => {
                    if (res == null) {
                        this.$message({
                            showClose: true,
                            message: '用户信息获取失败！',
                            type: 'error'
                        });
                    } else {
                        this.user = res;
                        this.UserForm =JSON.parse(JSON.stringify(res));
                    }
                })
            },
            //修改个人信息
            update() {
                 this.IsUpdate = true;
            },
            //提交修改信息
            save(UserForm) {
                this.$refs[UserForm].validate((valid) => {
                    if (valid) {
                         request.post('/personInfo',this.UserForm).then(res => {
                             if (res) {
                                 this.$message({
                                     showClose: true,
                                     message: '用户信息编辑成功！',
                                     type: 'success'
                                 });
                                 let userMassage = JSON.parse(localStorage.getItem('user'));
                                 userMassage.username = this.UserForm.username;
                                 let user = JSON.stringify(userMassage);
                                 localStorage.setItem("user" , user);
                                 //把load放在这里,是因为修改本地缓存内容需要时间，防止在修改本地缓存还未完成时，就进入load，此时缓存中的用户名是之前的，
                                 // 但是数据库里的用户名已经被修改了，load拿着修改前的用户名去数据库里找对应数据，是找不到的。简单来说，这是个异步问题。
                                 this.load();
                             }
                         }).catch(e => {
                             this.$message({
                                 showClose: true,
                                 message: '用户信息编辑失败！',
                                 type: 'error'
                             });
                         })
                    } else {
                        return false;
                    }
                    this.IsUpdate = false;
                });
            }
        }
    }
</script>

<style scoped>
 .main-content {
     position: relative;
     width: 1350px;
     height: 1200px;
     margin: 0 auto;
     background-color: #f8f9fa;
 }
 .el-carousel__item h3 {
     color: #475669;
     font-size: 14px;
     opacity: 0.75;
     line-height: 200px;
     margin: 0;
 }
 .header {
     height: 225px;
     background-color: #ffffff;
     box-shadow:1px 1px 15px 5px #e5e5e5;
 }
 .card-image {
     background-size: contain;
 }
 .image1 {
     background: url("../assets/image/fv1.png") no-repeat;
 }
 .image2 {
     background: url("../assets/image/fv2.png") no-repeat;
 }
 .image3 {
     background: url("../assets/image/fv3.png") no-repeat;
 }
 .image4 {
     background: url("../assets/image/fv4.png") no-repeat;
 }
 .image5 {
     background: url("../assets/image/fv5.png") no-repeat;
 }
    .goHome {
        position: absolute;
        top: 250px;
        height: 45px;
        line-height: 45px;
        text-align: center;
        width: 100px;
        background-color: #84b813;
        cursor: pointer;
        border-radius: 0px 23px 22px 0px;
    }
    .goHome:hover {
        border: 1px solid #84b813;
        background-color: #ffffff;
        border-left: none;
    }
    .goHome a {
        color: #ffffff;
        font-family: "Bookman Old Style";
    }
    .goHome a:hover {
        color: #84b813;
    }
    .user-content {
        margin: 0px auto;
        display: flex;
        justify-content: center;
        /*background: white;*/
        width: 900px;
        height: 500px;
    }
    .user-image {
        width: 60px;
        height: 60px;
        margin-top: 80px;
        background: url("../assets/image/user.png") no-repeat;
        background-size: cover;
        border-radius: 30px;
    }
    .user-name {
        position: absolute;
        top: 375px;
    }
    .user-info {
        position: absolute;
        top: 410px;
        width: 800px;
        height: 200px;
    }
    .updateInfo {
        font-size: 15px;
        margin-top: 15px;
        font-weight: bold;
    }
    .btn {
        width: 800px;
        display: flex;
        justify-content: center;
        margin-top: 15px;
    }
</style>