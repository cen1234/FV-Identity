<template>
    <div class="aside">
        <el-aside width="200px" style="height: 100%;">
            <el-menu  style="border-right: none;" router active-text-color="#ffd04b">
                <div v-for="item in menus" :key="item.id">
<!--                    渲染不包含二级菜单菜单-->
                    <div v-if="item.children.length === 0">
                        <a :href="item.path" class="aside-menu-index2">
                            <i :class="item.icon" style="font-size: 18px;color: #909399;"></i>
                            {{item.name}}
                        </a>
                    </div>
<!--                    渲染包含二级菜单的菜单-->
                    <div v-else>
                        <el-submenu :index="item.id" >
                            <template slot="title"><i :class="item.icon"></i>{{item.name}}</template>
                            <el-menu-item-group>
                                <div v-for="childItem in item.children">
                                    <el-menu-item :index="childItem.path">{{childItem.name}}</el-menu-item>
                                </div>
                            </el-menu-item-group>
                        </el-submenu>
                    </div>
                </div>
            </el-menu>
        </el-aside>
    </div>
</template>

<script>
    export default {
        name: "Aside",
        data() {
            return {
                menus:[],//当前登录用户可以使用的功能
            }
        },
        created() {
            this.menus = JSON.parse(localStorage.getItem('user')).menus;
        },
    }
</script>

<style scoped>
    .el-aside {
        color: #333;
    }
    .aside {
        width: 200px;
        height: 930px;
        position: absolute;
        top: 0px;
        left: 0px;
        background-color: #FFFFFF;
        border-right: 1px solid #e6e6e6;
    }
    .el-menu-item-group__title {
        padding: 0px;
    }
    .aside-menu-index2 {
        height: 56px;
        width: 200px;
        line-height: 56px;
        font-size: 14px;
        color: #303133;
        padding: 0 20px;
        list-style: none;
        cursor: pointer;
        position: relative;
        transition: border-color .3s,background-color .3s,color .3s;
        box-sizing: border-box;
        padding-left: 24px;
    }
    .aside-menu-index2:hover {
        background-color: #ecf5ff;
    }
    .icon-size {
        font-size: 18px;
        color: #909399;
    }
    .router-link-exact-active {
        color: gold;
    }
    .router-link-active {
        color: red;
    }
</style>