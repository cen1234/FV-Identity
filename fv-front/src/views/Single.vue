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
                                <el-step title="识别图片"></el-step>
                                <el-step title="查看结果"></el-step>
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
                            <div class="tip">点击+号上传要识别的图片，图片支持jpg/png格式，大小不超过2M,图片内容为单个果蔬，上传成功后点击下一步进入识别。</div>
                            <div v-if="imageUrl" class="delete" >
                                <el-popconfirm title="确定移除要上传到的图片吗？" @confirm="deleteUploadImg()">
                                    <el-button type="danger"  icon="el-icon-delete" slot="reference">移除</el-button>
                                </el-popconfirm>
                            </div>
                        </div>
<!--                        查看识别结果-->
                        <div class="show-result" v-if="this.active >= 2">
                            <el-menu
                                    :default-active="activeIndex"
                                    class="el-menu-demo"
                                    mode="horizontal"
                                    size="medium"
                                    active-text-color="#3d9fff"
                                    @select="handleSelect">
                                <el-menu-item index="1">识别果蔬</el-menu-item>
                                <el-menu-item index="2">查看介绍</el-menu-item>
                                <el-menu-item index="3">查看价格</el-menu-item>
                            </el-menu>

                            <el-descriptions v-if="this.activeIndex === '1'" title="识别果蔬" direction="vertical" :column="2" border size="medium" style="color: #3d9fff;margin-top: 30px;width: 1600px;">
                                <el-descriptions-item label="识别结果">{{identityResult.name}}</el-descriptions-item>
                                <el-descriptions-item label="识别分数">{{identityResult.score}}</el-descriptions-item>
                            </el-descriptions>
                            <el-descriptions v-if="this.activeIndex === '2'" title="查看介绍" direction="vertical" :column="1" border size="medium" style="color: #3d9fff;margin-top: 30px;width: 1600px;">
                                <el-descriptions-item label="名称">{{crawResult.title}}</el-descriptions-item>
                                <el-descriptions-item label="主要介绍">{{crawResult.description}}</el-descriptions-item>
                                <el-descriptions-item label="果蔬益处">{{crawResult.benefit}}</el-descriptions-item>
                            </el-descriptions>
                            <el-descriptions v-if="this.activeIndex === '3'" title="查看价格" direction="vertical" :column="2" border size="medium" style="color: #3d9fff;margin-top: 30px;width: 1600px;">

                            </el-descriptions>
                        </div>

                        <el-button type="primary" :disabled="this.active < 2" size="medium" class="previous-step" @click="previous()">上一步</el-button>
                        <el-button type="primary" :disabled="this.active >=2 " size="medium" class="next-step" @click="next()">下一步</el-button>
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
        name: "Single",
        components:{
            Aside,
            Header,
        },
        data() {
            return{
                active:0,//步骤条激活
                activeIndex:'1',//菜单栏激活
                imageUrl: '',//上传后要下载的图片地址
                user:'',//用户信息
                picture:'',//要识别的图片
                identityResult:{},//识别果蔬结果
                crawResult:{},//爬取到的介绍
                flagIntroduction:0,//如果为0，当激活‘查看介绍’菜单时，触发getIntroduction()方法，爬取数据；为1时，直接使用crawResult里的数据
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

           //菜单激活
            handleSelect(key) {
                this.activeIndex = key;
                this.active = 2;
                switch (this.activeIndex) {
                    case "1": this.active = 3;
                              break;
                    case "2":
                            if (this.flagIntroduction === 0) {
                                this.getIntroduction();
                                break;
                            }
                            this.active = 3;
                            break;
                    case "3": this.getPrice();
                              break;
                }
            },


           //单个果蔬识别
            identify() {
                this.identityResult = {};
                //识别图片内容
                request.get('/single/identity',{
                    params:{
                        accessToken:this.user.accessToken,
                        file:this.picture,
                    }
                }).then(res => {
                    this.identityResult.logId = res.log_id + '';
                    this.identityResult.username = this.user.username;
                    this.identityResult.name = res.result[0].name;
                    this.identityResult.score = res.result[0].score;
                    this.identityResult.resuleNum  = res.result_num;
                    this.identityResult.photo = this.picture;
                    //将识别结果存储到数据库
                    request.post('/single',this.identityResult).then(res => {
                        this.$message({
                            showClose: true,
                            message: '识别成功',
                            type: 'success'
                        });
                        this.active = 3;
                    })
                })
            },

            //获取介绍
            getIntroduction() {
                 request.get('/single/introduction',{
                     params:{
                         search:this.identityResult.name,
                         logId:this.identityResult.logId,
                         username:this.identityResult.username
                     }
                 }).then(res => {
                     this.crawResult.title =res.title.substring(0,res.title.indexOf('_'));
                     this.crawResult.description = res.description;
                     this.crawResult.benefit = res.benefit;
                     this.$message({
                         showClose: true,
                         message: '获取果蔬介绍成功！',
                         type: 'success'
                     });
                     this.flagIntroduction = 1;
                     this.active = 3;
                 })
            },

            //获取价格
            getPrice() {

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
                        this.identify();
                    }
                }
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
        top: 110px;
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

</style>