<template>
    <div class="main-content">
        <div class="theme">

             <div class="themeWord">Welcome to the fruit and Vegetable Identification System.</div>
        </div>
        <div class="login">
            <el-form :model="user" :rules="rules" ref="user">
                <el-form-item  prop="username">
                    <el-input prefix-icon="el-icon-user" v-model="user.username" placeholder="请输入用户名" size="medium" clearable class="inputInfo"></el-input>
                 </el-form-item>
                <el-form-item prop="password">
                    <el-input prefix-icon="el-icon-unlock" placeholder="请输入密码" v-model="user.password" show-password size="medium" class="inputInfo"></el-input>
                </el-form-item>
                 <el-form-item>
                     <el-link type="primary" :underline="false" class="searchPwd">忘记密码？</el-link>
                 </el-form-item>
                <el-form-item>
                    <el-button v-if="this.loginBtn === true" type="primary" size="medium" class="loginBtn" @click="login('user')">登录</el-button>
                    <el-button v-if="this.loginBtn === false"  type="primary" size="medium" class="registerBtn" @click="regist('user')">注册</el-button>
                </el-form-item>
            </el-form>
            <div v-if="this.loginBtn === true">
                <el-divider><p style="color: #e8eaed">没有账号？</p></el-divider>
                <el-button type="primary" icon="el-icon-plus" circle class="registIcon"></el-button>
                <el-button type="text" class="registLink" size="medium" @click="goRegist()">点击注册账号</el-button>
            </div>
            <div v-if="this.loginBtn === false">
                <el-divider><p style="color: #e8eaed">已经有账号？</p></el-divider>
                <el-button type="primary" icon="el-icon-s-promotion" circle class="registIcon"></el-button>
                <el-button type="text" class="registLink" size="medium" @click="goLogin()">点击回到登录页</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import request from "../../utils/request";

    export default {
        name: "Login",
        data(){
            return{
                user:{},//用户信息
                loginBtn:true,//登录|注册按钮切换,
                rules: {
                    username: [
                        { required: true, message: '用户名不能为空', trigger: 'blur' },
                        { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'},
                        {pattern: /^([a-zA-Z0-9_-]|[\u4E00-\uFA29]|[\uE7C7-\uE7F3]){1,30}$/, message: `用户名支持大小写英文，数字，-,_,中文`, trigger: 'blur'}
                    ],
                    password: [
                        { required: true, message: '密码不能为空', trigger: 'blur' },
                        { min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'},
                        {pattern: /^[a-zA-Z0-9_-]{1,15}$/, message: `密码支持大小写英文，数字，-,_`, trigger: 'blur'}
                    ]
                },//校验规则
            }
        }
        ,
        methods:{
            login(user) {
                this.$refs[user].validate((valid) => {
                    if (valid) {
                        request.post('/user/login',this.user).then(res => {
                            if (res.code != '200') {
                                this.$message({
                                    showClose: true,
                                    message: res.meg,
                                    type: 'error'
                                });
                            } else {
                                this.$message({
                                    showClose: true,
                                    message: '登陆成功',
                                    type: 'success'
                                });
                                //登录成功跳转到首页，并将用户信息存储到浏览器本地
                                localStorage.setItem("user",JSON.stringify(res.data));
                                this.$router.push('/');
                            }
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            message: '用户名或密码不符合校验规则！',
                            type: 'error'
                        });
                        return false;
                    }
                });
            },

            //注册
            regist(user) {
                this.$refs[user].validate((valid) => {
                    if (valid) {
                        request.post('/user/register',this.user).then(res => {
                            if (res.code !== '200') {
                                this.$message({
                                    showClose: true,
                                    message: res.meg,
                                    type: 'error'
                                });
                            } else {
                                this.$message({
                                    showClose: true,
                                    message: '用户信息注册成功',
                                    duration:1000,
                                    type: 'success'
                                });
                                //注册成功后，用户接下来完成登录
                                this.loginBtn = true;
                            }
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            message: '用户名或密码不符合校验规则！',
                            type: 'error'
                        });
                        return false;
                    }
                });
            },

            //显示注册按钮,不显示登录相关部分
            goRegist() {
                this.loginBtn = false;
            },

            //显示登录按钮,不显示注册相关部分
            goLogin() {
                this.loginBtn = true;
            }



        }
    }
</script>

<style scoped>
    .main-content {
        position: relative;
    }
    .theme {
        width: 750px;
        height: 900px;
        position: absolute;
        /*top:160px;*/
        left: 250px;
        background: url("../assets/image/login2.png") no-repeat;
    }
    .themeWord {
        position: absolute;
        top: 450px;
        left: 150px;
        font-weight: bold;
        font-family: "Book Antiqua";
    }
    .login {
        position: absolute;
        top: 190px;
        right: 200px;
        width: 300px;
        height: 350px;
        text-align: center;
   }
    .inputInfo {
        display: block;
        margin: 10px auto;
        width: 300px;
    }
    .searchPwd {
        display: block;
        margin: 0px 0px 10px 230px
    }
    .loginBtn {
        display: block;
        margin: 4px auto;
        width: 300px;
        box-shadow:1px 5px 15px 3px #a4b5f8;
    }
    .registerBtn {
        display: block;
        margin: 4px auto;
        width: 300px;
        box-shadow:1px 5px 15px 3px #a4b5f8;
    }
    .registLink {
        margin: 0px 20px 0px 100px;
    }
    .registIcon {
        margin-left: 0px;
    }
</style>