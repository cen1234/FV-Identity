<template>
    <div>
        <el-container style="height: 100%;position: relative">
            <Aside></Aside>
            <div>
                <el-container>
                    <Header></Header>
                    <el-main class="main-content">
                        <div class="btn-header">
                            <el-input
                                    prefix-icon="el-icon-search"
                                    placeholder="请输入内容"
                                    v-model="search"
                                    clearable
                                    class="search-input"
                                    @change="load()">
                            </el-input>
                            <el-button type="primary" size="small" class="add-btn" @click="add()">新增菜单</el-button>
                            <el-button type="primary" size="small" class="export-btn" @click="expUser()" >导出菜单</el-button>
                            <el-popconfirm title="确认删除选中菜单吗？" @confirm="deleteSelect()">
                                <el-button type="danger" size="small" class="delete-btn" slot="reference">删除选中</el-button>
                            </el-popconfirm>
                        </div>
                        <el-table
                                ref="multipleTable"
                                :data="tableData"
                                border
                                stripe
                                row-key="id"
                                default-expand-all
                                :default-sort = "{prop: 'name', order: 'ascending'}"
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
                                    prop="name"
                                    sortable
                                    label="名称"
                                    width="150">
                            </el-table-column>
                            <el-table-column
                                    prop="description"
                                    label="描述"
                                    width="400">
                            </el-table-column>
                            <el-table-column
                                    prop="path"
                                    sortable
                                    label="路径"
                                    width="160">
                            </el-table-column>
                            <el-table-column
                                    label="图标"
                                    width="100">
                                <template slot-scope="scope">
                                    <i :class="scope.row.icon" style="font-size: 15px;"></i>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button v-if="!scope.row.pid" size="mini" style="margin-right: 20px;" @click="addChildren(scope.row.id)">新增子菜单</el-button>
                                    <el-button size="mini" style="margin-right: 20px;" @click="handleEdit(scope.row)">编辑</el-button>
                                    <el-popconfirm title="确认删除此菜单吗？" @confirm="handleDelete(scope.row.id)">
                                        <el-button
                                                size="mini"
                                                type="danger "
                                                slot="reference"
                                        >删除</el-button>
                                    </el-popconfirm>
                                </template>
                            </el-table-column>
                        </el-table>
<!--                       菜单弹出框-->
                        <el-dialog
                                :title="titleInfo"
                                :visible.sync="dialogFormVisible"
                                width="650px"
                        >
                            <el-form :model="MenuForm" :rules="rules" ref="MenuForm">
                                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                                    <el-input  v-model="MenuForm.name" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
                                    <el-input placeholder="请输入菜单描述" type="textarea"  v-model="MenuForm.description" autosize maxlength="255" show-word-limit style="width: 400px;"></el-input>
                                </el-form-item>
                                <el-form-item label="路径" :label-width="formLabelWidth">
                                    <el-input  v-model="MenuForm.path" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="图标" :label-width="formLabelWidth" prop="icon">
                                        <el-select v-model="MenuForm.icon" size="medium" clearable placeholder="请选择图标">
                                            <el-option
                                                    v-for="item in options"
                                                    :key="item.name"
                                                    :label="item.name"
                                                    :value="item.value">
                                                <i :class="item.value">    {{item.name}}</i>
                                            </el-option>
                                        </el-select>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="save('MenuForm')">确 定</el-button>
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
        name: "menu",
        components:{
            Aside,
            Header,
        },
        data() {
            return {
                tableData: [],//菜单信息
                multipleSelection: [],//多选框
                idList:[],//选中的id
                search: '',//搜索内容
                dialogFormVisible: false,//添加|编辑信息弹出框是否弹出
                MenuForm:{},//添加|编辑信息表单
                titleInfo:"添加菜单信息",//添加|编辑信息弹出框标题
                formLabelWidth: '120px',//添加|编辑信息表单字段长度
                options: [],//菜单表单下拉框各选项
                rules:{
                    name: [
                        { required: true, message: '请输入名称', trigger: 'blur' },
                        { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'},
                        // {pattern: , message: ``, trigger: 'blur'}
                    ],
                    description: [
                        { required: true, message: '请输入描述', trigger: 'blur' },
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'},
                    ],
                },//表单校验规则
            }
        },
        created() {
            this.load();
        },
        methods:{
            //渲染菜单表格
            load: function () {
                request.get('/menu/?name='+this.search).then(res => {
                    this.tableData = res;
                })
            },

            //新增菜单
            add() {
                this.dialogFormVisible = true;
                this.titleInfo="添加菜单信息";
                //获取下拉框选项信息
                this.getSelectOption();
                this.MenuForm={};
            },

            //编辑菜单信息
            handleEdit(row) {
                this.dialogFormVisible = true;
                this.titleInfo="编辑菜单信息";
                this.MenuForm=JSON.parse(JSON.stringify(row));
                //获取下拉框选项信息
                this.getSelectOption();
            },

            //新增子菜单
            addChildren(pid) {
                this.dialogFormVisible = true;
                this.titleInfo="添加子菜单信息";
                this.MenuForm={};
                if (pid) {
                    this.MenuForm.pid = pid;
                }
            },

            //提交表单信息
            save(MenuForm) {
                this.$refs[MenuForm].validate((valid) => {
                    if (valid) {
                        if (this.MenuForm.id === null) {
                            request.post('/menu', this.MenuForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '菜单信息添加成功！',
                                        type: 'success'
                                    });
                                }
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '菜单信息添加失败！',
                                    type: 'error'
                                });
                            })
                        } else {
                            request.post('/menu', this.MenuForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '菜单信息编辑成功！',
                                        type: 'success'
                                    });
                                }
                                this.dialogFormVisible = false;
                                this.load();
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '菜单信息编辑失败！',
                                    type: 'error'
                                });
                                this.dialogFormVisible = false;
                            })
                        }
                    }
                });
            },

            //删除单个菜单
            handleDelete(id) {
                request.delete('/menu/'+id).then(res => {
                    if (res) {
                        this.$message({
                            showClose: true,
                            message: '菜单信息删除成功！',
                            type: 'success'
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '菜单信息删除失败！',
                            type: 'error'
                        });
                    }
                    this.load();
                })
            },

            //删除选中菜单
            deleteSelect() {
                request.delete('/menu?idlist='+this.idList).then(res => {
                    if(res) {
                        this.$message({
                            showClose: true,
                            message: '菜单信息批量删除成功！',
                            type: 'success'
                        });
                        this.idList=[];
                        this.load();
                    } else {
                        this.$message({
                            showClose: true,
                            message: '菜单信息批量删除失败！',
                            type: 'error'
                        });
                        this.idList=[];
                    }
                })
            },

            //导出菜单信息
            expUser() {
                window.open('http://localhost:9000/menu/export');
            },

            //多选
            handleSelectionChange(val) {
                //val这里为选中的行的数据
                this.multipleSelection = val;
                this.idList=this.multipleSelection.map(item => {
                    return item.id;
                })
            },

            //获取下拉框可选项信息
            getSelectOption() {
                request.get('/menu/icons').then(res => {
                    this.options = res;
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
    .search-input {
        position: absolute;
        right: 828px;
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