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
                                <el-option label="真实姓名" value="2"></el-option>
                                <el-option label="用户邮箱" value="3"></el-option>
                                <el-option label="用户电话" value="4"></el-option>
                            </el-select>
                            <el-input
                                    prefix-icon="el-icon-search"
                                    placeholder="请输入内容"
                                    v-model="search"
                                    clearable
                                    class="search-input"
                                    @change="load()">
                            </el-input>
                            <el-button type="primary" size="small" class="add-btn" @click="add()">新增用户</el-button>
                            <el-button type="primary" size="small" class="export-btn" @click="expUser()" >导出用户</el-button>
                            <el-popconfirm title="确认删除选中用户吗？" @confirm="deleteSelect()">
                                <el-button type="danger" size="small" class="delete-btn" slot="reference">删除选中</el-button>
                            </el-popconfirm>
                        </div>
                        <el-table
                                ref="multipleTable"
                                :data="tableData"
                                border
                                stripe
                                :default-sort = "{prop: 'username', order: 'ascending'}"
                                @selection-change="handleSelectionChange"
                                :header-cell-style="{backgroundColor:'#f1f3f4'}"
                                style="width: 1200px;margin: 0 auto;">
                            <el-table-column
                                    type="selection"
                                    width="55">
                            </el-table-column>
                            <el-table-column
                                    type="index"
                                    label="ID"
                                    :index="1">
                            </el-table-column>
                            <el-table-column
                                    prop="username"
                                    sortable
                                    label="用户名"
                                    width="130">
                            </el-table-column>
                            <el-table-column
                                    prop="realname"
                                    sortable
                                    label="真实姓名"
                                    width="100">
                            </el-table-column>
                            <el-table-column
                                    prop="role"
                                    label="角色"
                                    width="130">
                            </el-table-column>
                            <el-table-column
                                    prop="sex"
                                    width="50px"
                                    label="性别">
                            </el-table-column>
                            <el-table-column
                                    prop="age"
                                    width="50px"
                                    label="年龄">
                            </el-table-column>
                            <el-table-column
                                    prop="email"
                                    label="邮箱">
                            </el-table-column>
                            <el-table-column
                                    prop="phone"
                                    label="电话">
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="地址"
                                    show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button
                                            size="mini"
                                            @click="handleEdit(scope.row)" style="margin-right: 15px;">编辑</el-button>
                                    <el-popconfirm title="确认删除此用户吗？" @confirm="handleDelete(scope.row.id)">
                                        <el-button
                                                size="mini"
                                                type="danger "
                                                slot="reference"
                                        >删除</el-button>
                                    </el-popconfirm>
                                </template>
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
                        <el-dialog
                                :title="titleInfo"
                                :visible.sync="dialogFormVisible"
                                width="650px"
                        >
                            <el-form :model="UserForm" :rules="rules" ref="UserForm">
                                <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
                                    <el-input  v-model="UserForm.username" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
                                    <el-input  v-model="UserForm.password" autocomplete="off" size="medium"  style="width: 400px;" show-password></el-input>
                                </el-form-item>
                                <el-form-item label="真实姓名" :label-width="formLabelWidth" prop="realname">
                                    <el-input v-model="UserForm.realname" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="性别" :label-width="formLabelWidth" prop="sex">
                                    <el-radio-group v-model="UserForm.sex">
                                        <el-radio label="男"></el-radio>
                                        <el-radio label="女"></el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="年龄" :label-width="formLabelWidth" prop="age">
                                    <el-input-number v-model="UserForm.age" size="medium" :min="1" :max="130" label="年龄"></el-input-number>
                                </el-form-item>
                                <el-form-item label="角色" :label-width="formLabelWidth" prop="role">
                                    <el-select v-model="UserForm.role" size="medium" clearable placeholder="请选择角色" style="width: 200px;">
                                        <el-option
                                                v-for="item in roles"
                                                :key="item.name"
                                                :label="item.name"
                                                :value="item.name"
                                              >
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
                                    <el-input  v-model="UserForm.email" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="电话" :label-width="formLabelWidth" prop="phone">
                                    <el-input  v-model="UserForm.phone" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="地址" :label-width="formLabelWidth">
                                    <el-input placeholder="请输入地址" type="textarea"  v-model="UserForm.address" autosize maxlength="255" show-word-limit style="width: 400px;"></el-input>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="save('UserForm')">确 定</el-button>
                            </div>
                        </el-dialog>
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
        name: "Home",
        components:{
            Aside,
            Header,
        },
        data() {
            return {
                tableData: [],//用户信息
                multipleSelection: [],//多选框
                idList:[],//选中的id
                pageNum:1,//当前页
                total:10,//用户总数
                pageSize:10,//1页展示多少条数据
                search: '',//搜索内容
                select: "1",//搜索类型
                dialogFormVisible: false,//添加|编辑信息弹出框是否弹出
                UserForm:{},//添加|编辑信息表单
                titleInfo:"添加用户信息",//添加|编辑信息弹出框标题
                formLabelWidth: '120px',//添加|编辑信息表单字段长度
                roles:[],//添加|编辑信息表单中角色选择项
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
                        { required: true, message: '请填写您的年龄', trigger: 'blur' }
                    ],
                    role:[
                         { required: true, message: '请选择您的角色', trigger: 'blur' }
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
            //渲染用户表格
            load: function () {
                request.get('/user/page?pageNum='+this.pageNum+'&pageSize='+this.pageSize+'&search='+this.search+'&type='+this.select).then(res => {
                    this.tableData = res.records;
                    this.total = res.total;
                })
            },

            //新增用户
            add() {
                this.dialogFormVisible = true;
                this.titleInfo="添加用户信息";
                //获取表单中角色下拉框选项
                this.getRole();
                this.UserForm={};
            },

            //编辑用户信息
            handleEdit(row) {
                this.dialogFormVisible = true;
                this.titleInfo="编辑用户信息";
                //获取表单中角色下拉框选项
                this.getRole();
                this.UserForm=JSON.parse(JSON.stringify(row));
            },

            //提交表单信息
            save(UserForm) {
                this.$refs[UserForm].validate((valid) => {
                    if (valid) {
                        if (this.UserForm.id === null) {
                            request.post('/user', this.UserForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '用户信息添加成功！',
                                        type: 'success'
                                    });
                                }
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '用户信息添加失败！',
                                    type: 'error'
                                });
                            })
                        } else {
                            request.post('/user', this.UserForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '用户信息编辑成功！',
                                        type: 'success'
                                    });
                                }
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '用户信息编辑失败！',
                                    type: 'error'
                                });
                            })
                        }
                        this.dialogFormVisible = false;
                        this.load();
                    }
                });
            },

            //删除单个用户
            handleDelete(id) {
                request.delete('/user/'+id).then(res => {
                    if (res) {
                        this.$message({
                            showClose: true,
                            message: '用户信息删除成功！',
                            type: 'success'
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '用户信息删除失败！',
                            type: 'error'
                        });
                    }
                })
                this.load();
            },

            //删除选中用户
            deleteSelect() {
                request.delete('/user?idlist='+this.idList).then(res => {
                    if(res) {
                        this.$message({
                            showClose: true,
                            message: '用户信息批量删除成功！',
                            type: 'success'
                        });
                        this.idList=[];
                        this.load();
                    } else {
                        this.$message({
                            showClose: true,
                            message: '用户信息批量删除失败！',
                            type: 'error'
                        });
                        this.idList=[];
                    }
                })
            },

            //导出用户信息
            expUser() {
                window.open('http://localhost:9000/user/export');
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

            //多选
            handleSelectionChange(val) {
                //val这里为选中的行的数据
                this.multipleSelection = val;
                this.idList=this.multipleSelection.map(item => {
                    return item.id;
                })
            },

            //获取角色下拉框选项
            getRole() {
                request.get('/role').then(res => {
                    this.roles = res;
                })
            }

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
    .add-btn {
        position: absolute;
        right: 420px;
    }
    .export-btn {
        position: absolute;
        right: 330px;
    }
    .delete-btn {
        position: absolute;
        right: 240px;
    }
</style>