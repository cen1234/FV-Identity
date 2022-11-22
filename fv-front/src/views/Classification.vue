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
                                <el-step title="查看分类"></el-step>
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
                            <div class="tip">点击+号上传要识别的图片，图片支持jpg/png格式，大小不超过2M,图片内容为多个果蔬，上传成功后点击下一步进行分类。</div>
                            <div v-if="imageUrl" class="delete" >
                                <el-popconfirm title="确定移除要上传到的图片吗？" @confirm="deleteUploadImg()">
                                    <el-button type="danger"  icon="el-icon-delete" slot="reference">移除</el-button>
                                </el-popconfirm>
                            </div>
                        </div>
                        <!--                        查看识别结果-->
                        <div class="show-result" v-if="this.active == 2">
                            <div class="type">
                                <div class="image fruit-image"></div>
                                <div class="introduction-type">水果类</div>
                                <div class="container-type">
                                    <el-tag
                                            :key="tag"
                                            v-for="tag in this.fruitArray"
                                            size="medium"
                                            type="danger"
                                            style="margin: 10px 0px 0px 10px;">
                                        {{tag}}
                                    </el-tag>
                                </div>
                            </div>
                            <div class="type">
                                <div class="image vegetable-type"></div>
                                <div class="introduction-type">蔬菜类</div>
                                <div class="container-type">
                                    <el-tag
                                            :key="tag"
                                            v-for="tag in this.vegetableArray"
                                            size="medium"
                                            type="success"
                                            style="margin: 10px 0px 0px 10px;">
                                        {{tag}}
                                    </el-tag>
                                </div>
                            </div>
                            <div class="type">
                                <div class="image nut-type"></div>
                                <div class="introduction-type">坚果类</div>
                                <div class="container-type">
                                    <el-tag
                                            :key="tag"
                                            v-for="tag in this.nutArray"
                                            size="medium"
                                            style="margin: 10px 0px 0px 10px;">
                                        {{tag}}
                                    </el-tag>
                                </div>
                            </div>
                            <div class="type have-border-bottom">
                                <div class="image not-fv-type"></div>
                                <div class="introduction-type">非果蔬类</div>
                                <div class="container-type">
                                    <el-tag
                                            :key="tag"
                                            v-for="tag in this.nothingArray"
                                            size="medium"
                                            type="warning"
                                            style="margin: 10px 0px 0px 10px;">
                                        {{tag}}
                                    </el-tag>
                                </div>
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
        name: "classification",
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
                fruitArray:[],//水果
                vegetableArray:[],//蔬菜
                nutArray:[],//坚果
                nothingArray:[],//非果蔬
                flag:0,//判断标志，需不要重新进行分类
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
                this.flag = 0;
                this.fruitArray = [];
                this.vegetableArray = [];
                this.nutArray = [];
                this.nothingArray = [];
            },

            //果蔬分类
            FVclassification() {
                request.get('/multiple/classification',{
                    params:{
                        accessToken:this.user.accessToken,
                        file:this.picture,
                        username:this.user.username
                    }
                }).then(res => {
                     let temp = res.result;
                     for (let item of temp) {
                         switch (item.type) {
                             case "水果" :this.fruitArray.push(item.name);
                                          break;
                             case "蔬菜" :this.vegetableArray.push(item.name);
                                          break;
                             case "坚果" :this.nutArray.push(item.name);
                                          break;
                             case "非果蔬" :this.nothingArray.push(item.name);
                                          break;
                         }
                     }
                    this.$message({
                        showClose: true,
                        message: '果蔬分类成功',
                        duration:1000,
                        type: 'success'
                    });
                     this.flag = 1;
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
                    if ( this.active == 2 && this.flag === 0 ) {
                        this.FVclassification();
                    }
                }
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
        left: 200px;
        top: 180px;
        width: 1300px;
        height: 550px;
    }
    .type  {
        position: relative;
        height: 25%;
        width: 100%;
        border: 1px solid #909399;
        border-bottom: none;
    }
    .have-border-bottom {
        border: 1px solid #909399;
    }
    .image {
        position: absolute;
        height: 100%;
        width: 27%;
        border-right: 1px solid #909399;
    }
    .fruit-image {
        background: url("../assets/image/basket1.png") no-repeat;
        background-size: contain;
    }
    .vegetable-type {
        background: url("../assets/image/basket2.png") no-repeat;
        background-size: contain;
    }
    .nut-type {
        background: url("../assets/image/basket3.png") no-repeat;
        background-size: contain;
    }
    .not-fv-type {
        background: url("../assets/image/basket4.png") no-repeat;
        background-size: contain;
    }
    .introduction-type {
        z-index: 1;
        position: absolute;
        width: 80px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        bottom: 0px;
        left: 271px;
        color: white;
        background-color: #84b813;
    }
    .container-type {
        position: absolute;
        left: 352px;
        height: 100%;
        line-height: 100%;
        width: 73%;
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
</style>