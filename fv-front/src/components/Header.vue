<template>
        <div class="container">
            <el-header style="text-align: right; font-size: 12px;border-top: none;border-bottom: none;">
                <el-dropdown placement="bottom" size="medium">
                    <i class="el-icon-setting icon-size" style="margin-right: 5px;"></i>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item><a href="/personInfo">个人信息</a></el-dropdown-item>
                        <el-dropdown-item>
                            <el-button type="text" size="medium" style="color:#333333;" @click="exit()">退出登录</el-button>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <span class="header-dropdown">您好，{{user.username}}</span>
            </el-header>
        </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            return {
                user:{},//用户信息
            }
        },
        created() {
            // 避免用户未登录，直接进入系统
            if (localStorage.getItem("user") == null) {
                this.$router.push('/login');
            } else {
                //从浏览器本地存储取出用户信息
                this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{};

                //获取当前访问的文件地址
               let pathname = window.location.pathname;
               //获取当前登录用户可以使用的功能菜单
               let menus =  JSON.parse(localStorage.getItem('user')).menus;
               //如果功能菜单中没有当前访问的文件地址，就不路由到那个页面，避免因为从url直接访问到不该访问的页面
               let flag = true;
               for (let i = 0;i < menus.length;i++) {
                   if (menus[i].children.length > 0) {
                       for (let j = 0; j < menus[i].children.length;j++) {
                           if (menus[i].children[j].path == pathname) {
                               flag = false;
                               break;
                           }
                       }

                   } else {
                       if (menus[i].path == pathname) {
                           flag = false;
                           break;
                       }
                   }
               }
               if (flag) {
                   this.$router.push('/single');
               }
            }
        },
        methods:{
            //退出登录
            exit() {
                localStorage.clear();
                this.user = {};
                this.$router.push('/login');
                this.$message({
                    showClose: true,
                    message: '退出系统成功',
                    duration: 1000,
                    type: 'success'
                });
            }
        }
    }
</script>

<style scoped>
    .el-header {
        /*background-color: #B3C0D1;*/
        color: #333;
        line-height: 60px;
        border: 1px solid #dcdfe6;
    }
    .container {
        position: absolute;
        width: 1720px;
        top: 0px;
        left: 200px;
        border-top: none;
        border-bottom: 1px solid #e6e6e6;
    }
    .icon-size {
        font-size: 18px;
        color: #909399;
    }
    .header-dropdown {
        margin-right: 50px;
    }
</style>