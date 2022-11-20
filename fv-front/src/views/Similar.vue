<template>
    <div>
        <el-container style="height: 100%;position: relative">
            <Aside></Aside>
            <div>
                <el-container>
                    <Header></Header>
                    <el-main class="main-content">
                        <div class="step">
                            <el-steps :active="active" finish-status="success" space="400px">
                                <el-step title="上传图片"></el-step>
                                <el-step title="检索图片"></el-step>
                            </el-steps>
                        </div>
                        <el-divider></el-divider>
                        <!--                        上传图片-->
                        <div class="upload-img" v-if="this.active < 2">
                            <div class="step1">第一步：上传图片</div>
                            <el-upload
                                    class="avatar-uploader"
                                    action="/api/file/upload"
                                    :show-file-list="false"
                                    :limit="1"
                                    :data="user"
                                    ref="upload"
                                    :on-exceed="handleExceed"
                                    :on-success="handleSuccess"
                                    :before-upload="beforeUpload"
                            >
                                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                            <div class="tip">点击+号上传要识别的图片，图片支持jpg/png格式，大小不超过2M,图片内容为单个果蔬，上传成功后点击入库。</div>
                            <div v-if="imageUrl" class="delete" >
                                <el-popconfirm title="确定移除要上传到的图片吗？" @confirm="deleteUploadImg()">
                                    <el-button type="danger"  icon="el-icon-delete" slot="reference">移除</el-button>
                                </el-popconfirm>
                            </div>
                        </div>
                        <!--                        查看识别结果-->
                        <div class="show-result" v-if="this.active == 2">
                            <el-table
                                    ref="multipleTable"
                                    :data="retrieveResult"
                                    border
                                    stripe
                                    tooltip-effect="light"
                                    :header-cell-style="{backgroundColor:'#f1f3f4'}"
                                    style="width: 1500px;margin: 0 auto;">
                                <el-table-column
                                        type="index"
                                        label="ID"
                                        :index="1">
                                </el-table-column>
                                <el-table-column prop="brief" label="图片">
                                    <template slot-scope="scope">
                                        <img :src="scope.row.brief" style="width: 50px;height: 50px;">
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="score"
                                        label="分数">
                                </el-table-column>
                            </el-table>
                            <div class="pagination">
                                <el-pagination
                                        @size-change="handleSizeChange"
                                        @current-change="handleCurrentChange"
                                        :current-page="pageNum"
                                        :page-sizes="[5]"
                                        :page-size="pageSize"
                                        layout="total, sizes, prev, pager, next, jumper"
                                        :total="total"
                                        style="font-weight: normal">
                                </el-pagination>
                            </div>
                        </div>

                        <el-button type="primary" :disabled="this.active < 2" size="medium" class="previous-step" @click="previous()">上一步</el-button>
                        <el-button type="primary" :disabled="this.active == 2 " size="medium" class="next-step" @click="next()">下一步</el-button>
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
        name: "Similar",
        components:{
            Aside,
            Header,
        },
        data() {
            return{
                active:0,//步骤条激活
                imageUrl: '',//上传后要下载的图片地址
                user:'',//用户信息
                picture:'',//要识别的图片
                retrieveResult:[],//检索相似果蔬结果
                pageNum:1,//当前页
                total:10,//数据总数
                pageSize:5,//1页展示多少条数据
                logId:'',//检索结果关键id,用于表格数据获取

            }
        },
        created() {
            this.user = JSON.parse(localStorage.getItem('user'));

        },
        methods:{

            //图片上传成功，拿到返回的url
            handleSuccess(res) {
                this.imageUrl = '/api/file/'+res;
                this.picture = res;
                this.active = 1;
                this.$message({
                    showClose: true,
                    message: '图片上传成功！',
                    duration:1000,
                    type: 'success'
                });
            },

            //只能上传一个文件，超过一个给出提示
            handleExceed() {
                this.$message({
                    showClose: true,
                    message: '系统只支持单文件上传！',
                    type: 'warning'
                });
            },

            //图片上传前检查图片类型和大小，只支持png/jpg/jpeg,不能超过2M的图片
            beforeUpload(file) {
                const type =['image/jpeg','image/png'];
                let CorrectType = type.indexOf(file.type);
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (CorrectType == -1) {
                    this.$message({
                        showClose: true,
                        message: '上传图片只能是 JPG或PNG 格式!',
                        type: 'error'
                    });
                }
                if (!isLt2M) {
                    this.$message({
                        showClose: true,
                        message: '上传图片大小不能超过 2MB!',
                        type: 'error'
                    });
                }
                return CorrectType && isLt2M;
            },

            //删除要上传的图片
            deleteUploadImg() {
                this.$refs.upload.clearFiles();
                this.imageUrl = '';
                this.active = 0;
            },

            //相似图片检索
            retrieve() {
               request.get('/similar/search',{
                   params:{
                       token:this.user.searchAccessToken,
                       file:this.picture,
                       username:this.user.username
                   }
               }).then( res => {
                   this.logId = res.log_id;
                   //分页获取表格数据
                   this.getSimilar();
                   this.$message({
                       showClose: true,
                       message: '相似图片检索成功！',
                       duration:1000,
                       type: 'success'
                   });
               })
            },

            //分页获取
            getSimilar() {
               request.get('/similar/page',{
                   params:{
                       pageNum:this.pageNum,
                       pageSize:this.pageSize,
                       logId:this.logId
                   }
               }).then(res => {
                   this.retrieveResult = res.records;
                   for (let item of this.retrieveResult) {
                       item.brief =  '/api/file/'+item.brief;
                   }
                   this.total = res.total;
               })
            },

            //上一步
            previous() {
                this.active--;
            },

            //下一步
            next() {
                if (!this.imageUrl) {
                    this.active = 0;
                } else {
                    this.active++;
                    if ( this.active == 2) {
                        this.retrieve();
                    }
                }
            },

            // 分页
            handleSizeChange (pageSize) {
                this.pageSize = pageSize;
                this.getSimilar();
            },
            handleCurrentChange (pageNum) {
                this.pageNum = pageNum;
                this.getSimilar();
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
    .step {
        margin: 0 auto;
        width: 1100px;
        padding-left: 300px;
    }
    .avatar-uploader{
        width: 178px;
        height: 178px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
    .tip {
        position: absolute;
        font-size: 13px;
        margin-top: 15px;
        color: #c1c5d0;
    }
    .delete {
        position: absolute;
        font-size: 20px;
        top: 302px;
        left: 210px;
    }
    .step1 {
        color: #3d9fff;
        width: 200px;
        height: 30px;
        line-height: 30px;
        font-weight: bold;
        margin-bottom: 4px;
    }
    .show-result {
        position: absolute;
        top: 180px;
        width: 1700px;
        height: 400px;
        /*background-color: #909399;*/
    }
    .previous-step {
        position: absolute;
        bottom: 50px;
        right: 200px;
    }
    .next-step {
        position: absolute;
        bottom: 50px;
        right: 100px;
    }
    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: center;
    }
</style>