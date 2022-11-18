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
                            <el-button type="primary" size="small" class="add-btn" @click="add()">新增角色</el-button>
                            <el-button type="primary" size="small" class="export-btn" @click="expUser()" >导出角色</el-button>
                            <el-popconfirm title="确认删除选中角色吗？" @confirm="deleteSelect()">
                                <el-button type="danger" size="small" class="delete-btn" slot="reference">删除选中</el-button>
                            </el-popconfirm>
                        </div>
                        <el-table
                                ref="multipleTable"
                                :data="tableData"
                                border
                                stripe
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
                                    width="240">
                            </el-table-column>
                            <el-table-column
                                    prop="description"
                                    sortable
                                    label="描述"
                                    width="380">
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button
                                            size="mini"
                                            @click="handleEdit(scope.row)" style="margin-right: 20px;">编辑</el-button>
                                    <el-button
                                            size="mini"
                                            @click="handleDistribute(scope.row.id)" style="margin-right: 20px;">权限</el-button>
                                    <el-popconfirm title="确认删除此角色吗？" @confirm="handleDelete(scope.row.id)">
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
                            <el-form :model="RoleForm" :rules="rules" ref="RoleForm">
                                <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                                    <el-input  v-model="RoleForm.name" autocomplete="off" size="medium"  style="width: 400px;" clearable ></el-input>
                                </el-form-item>
                                <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
                                    <el-input placeholder="请输入角色描述" type="textarea"  v-model="RoleForm.description" autosize maxlength="255" show-word-limit style="width: 400px;"></el-input>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="save('RoleForm')">确 定</el-button>
                            </div>
                        </el-dialog>
<!--                        权限分配弹出框-->
                        <el-dialog
                                title="权限分配"
                                :visible.sync="dialogRoleFormVisible"
                                width="550px"
                        >
                            <el-tree
                                    :data="menuData"
                                    :props="props"
                                    show-checkbox
                                    :default-expanded-keys="[2]"
                                    :default-checked-keys="roleMenuArr"
                                    node-key="id"
                                    ref="tree"
                                    >
                                    <span class="custom-tree-node" slot-scope="{node,data}">
                                        <span><i :class="data.icon"></i>  {{data.name}}</span>
                                    </span>
                            </el-tree>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogRoleFormVisible = false">取 消</el-button>
                                <el-button type="primary" @click="Menusave()">确 定</el-button>
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
        name: "Role",
        components:{
            Aside,
            Header,
        },
        data() {
            return {
                tableData: [],//角色信息
                multipleSelection: [],//多选框
                idList:[],//选中的id
                pageNum:1,//当前页
                total:10,//角色总数
                pageSize:10,//1页展示多少条数据
                search: '',//搜索内容
                select: "1",//搜索类型
                dialogFormVisible: false,//添加|编辑信息弹出框是否弹出
                dialogRoleFormVisible:false,//权限分配弹出框是否弹出
                RoleForm:{},//添加|编辑信息表单
                MenuForm:{},//权限分配表单
                titleInfo:"添加角色信息",//添加|编辑信息弹出框标题
                formLabelWidth: '120px',//添加|编辑信息表单字段长度
                menuData:[],//权限树型结构数据
                roleId:0,//点击权限按钮的角色id
                roleMenuArr:[],//点击权限按钮获取的角色拥有的所有权限
                props:{label: 'name'},//权限树型结构名称
                rules:{
                    name: [
                        { required: true, message: '请输入名称', trigger: 'blur' },
                        { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'},
                        // {pattern: , message: ``, trigger: 'blur'}
                    ],
                    description: [
                        { required: true, message: '请输入描述', trigger: 'blur' },
                        { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'},
                    ]
                },//角色表单校验规则
            }
        },
        created() {
            this.load();
        },
        methods:{
            //渲染角色表格
            load: function () {
                request.get('/role/page?pageNum='+this.pageNum+'&pageSize='+this.pageSize+'&search='+this.search+'&type='+this.select).then(res => {
                    this.tableData = res.records;
                    this.total = res.total;
                })
            },

            //新增角色
            add() {
                this.dialogFormVisible = true;
                this.titleInfo="添加角色信息";
                this.RoleForm={};
            },

            //编辑角色信息
            handleEdit(row) {
                this.dialogFormVisible = true;
                this.titleInfo="编辑角色信息";
                this.RoleForm=JSON.parse(JSON.stringify(row));
            },

            //分配权限
            handleDistribute(row) {
                this.dialogRoleFormVisible = true;
                this.roleId = row;
                //请求权限数据
                request.get('/menu',{
                    params:{
                        name:"",
                    }
                }).then(res => {
                    this.menuData = res;
                })

                //获取修改权限当前角色所有的菜单
                request.get('/role/roleMenu?roleId='+this.roleId).then(res => {
                   this.roleMenuArr = res;
                })

            },

            //提交角色表单信息
            save(RoleForm) {
                this.$refs[RoleForm].validate((valid) => {
                    if (valid) {
                        if (this.RoleForm.id === null) {
                            request.post('/role', this.RoleForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '角色信息添加成功！',
                                        type: 'success'
                                    });
                                }
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '角色信息添加失败！',
                                    type: 'error'
                                });
                            })
                        } else {
                            request.post('/role', this.RoleForm).then(res => {
                                if(res) {
                                    this.$message({
                                        showClose: true,
                                        message: '角色信息编辑成功！',
                                        type: 'success'
                                    });
                                }
                            }).catch(err=>{
                                this.$message({
                                    showClose: true,
                                    message: '角色信息编辑失败！',
                                    type: 'error'
                                });
                            })
                        }
                        this.dialogFormVisible = false;
                        this.load();
                    }
                });
            },

            //提交角色选择的权限信息
            Menusave() {
               request.post('/role/roleMenu/'+this.roleId,this.$refs.tree.getCheckedKeys()).then(res => {
                  if(res.code === '200') {
                      this.$message({
                          showClose: true,
                          message: '角色和菜单关系绑定成功！',
                          type: 'success'
                      });
                      this.dialogRoleFormVisible = false;
                  } else {
                      this.$message({
                          showClose: true,
                          message: '角色和菜单关系绑定失败！',
                          type: 'error'
                      });
                  }
              })

            },

            //删除单个角色
            handleDelete(id) {
                request.delete('/role/'+id).then(res => {
                    if (res) {
                        this.$message({
                            showClose: true,
                            message: '角色信息删除成功！',
                            type: 'success'
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '角色信息删除失败！',
                            type: 'error'
                        });
                    }
                })
                this.load();
            },

            //删除选中角色
            deleteSelect() {
                request.delete('/role?idlist='+this.idList).then(res => {
                    if(res) {
                        this.$message({
                            showClose: true,
                            message: '角色信息批量删除成功！',
                            type: 'success'
                        });
                        this.idList=[];
                        this.load();
                    } else {
                        this.$message({
                            showClose: true,
                            message: '角色信息批量删除失败！',
                            type: 'error'
                        });
                        this.idList=[];
                    }
                })
            },

            //导出角色信息
            expUser() {
                window.open('http://localhost:9000/role/export');
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

            //树型
            handleCheckChange(data, checked, indeterminate) {
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