<template>
    <div>
        <el-container style="height: 100%;position: relative">
            <Aside></Aside>
            <div>
                <el-container>
                    <Header></Header>
                    <el-main class="main-content">
                        <div class="btn-header">
                            <el-select v-model="select" slot="prepend" placeholder="请选择" class="search-select">
                                <el-option label="用户名" value="1"></el-option>
                                <el-option label="功能路径" value="2"></el-option>
                            </el-select>
                            <el-input
                                    prefix-icon="el-icon-search"
                                    placeholder="请输入内容"
                                    v-model="search"
                                    clearable
                                    class="search-input"
                                    @change="load()">
                            </el-input>
                            <el-button type="primary" size="small" class="export-btn" @click="expUser()" >导出日志</el-button>
                        </div>
                        <el-table
                                ref="multipleTable"
                                :data="tableData"
                                border
                                stripe
                                tooltip-effect="light"
                                :default-sort = "{prop: 'username', order: 'ascending'}"
                                :header-cell-style="{backgroundColor:'#f1f3f4'}"
                                style="width: 1200px;margin: 0 auto;">
                            <el-table-column
                                    type="index"
                                    label="ID"
                                    :index="1">
                            </el-table-column>
                            <el-table-column
                                    prop="username"
                                    sortable
                                    label="用户名">
                            </el-table-column>
                            <el-table-column
                                    prop="path"
                                    sortable
                                    label="功能路径">
                            </el-table-column>
                            <el-table-column
                                    prop="recordId"
                                    label="log_id">
                            </el-table-column>
                            <el-table-column
                                    prop="resuleNum"
                                    label="识别数">
                            </el-table-column>
                            <el-table-column
                                    prop="photo"
                                    label="图片"
                                    :show-overflow-tooltip="true">
                            </el-table-column>
                        </el-table>
                        <div class="pagination">
                            <el-pagination
                                    @size-change="handleSizeChange"
                                    @current-change="handleCurrentChange"
                                    :current-page="pageNum"
                                    :page-sizes="[5, 10, 15]"
                                    :page-size="pageSize"
                                    layout="total, sizes, prev, pager, next, jumper"
                                    :total="total"
                                    style="font-weight: normal">
                            </el-pagination>
                        </div>
                    </el-main>
                </el-container>
            </div>
        </el-container>
    </div>
</template>

<script>
    import Aside from "@/components/Aside";
    import Header from "@/components/Header";
    import request from "../../utils/request";
    export default {
        name: "Log",
        components:{
            Aside,
            Header,
        },
        data() {
            return {
                tableData: [],//用户信息
                pageNum:1,//当前页
                total:10,//用户总数
                pageSize:10,//1页展示多少条数据
                search: '',//搜索内容
                select: "1",//搜索类型
            }
        },
        created() {
            this.load();
        },
        methods:{
            //渲染日志表格
            load: function () {
                request.get('/log/page',{
                    params:{
                        pageNum :this.pageNum,
                        pageSize:this.pageSize,
                        search:this.search,
                        type:this.select
                    }
                }).then(res => {
                    this.tableData = res.records;
                    this.total = res.total;
                })
            },

            //导出用户信息
            expUser() {
                window.open('http://localhost:9000/log/export');
            },

            // 分页
            handleSizeChange (pageSize) {
                this.pageSize = pageSize;
                this.load();
            },
            handleCurrentChange (pageNum) {
                this.pageNum = pageNum;
                this.load();
            },


        }
    }
</script>

<style scoped>
    .el-menu-item-group__title {
        padding: 0px;
    }
    .main-content {
        position: absolute;
        width: 1720px;
        top: 80px;
        left: 200px;
        height: 855px;
    }
    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: center;
    }
    .btn-header {
        position: relative;
        height: 40px;
        margin-bottom: 10px;
    }
    .search-select {
        position: absolute;
        width: 130px;
        right: 1310px;
        top: 5px;
    }
    .search-input {
        position: absolute;
        right: 708px;
        width: 600px;
        padding: 5px;
    }
    .export-btn {
        position: absolute;
        right: 240px;
    }
</style>