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
                            <div class="tip">点击+号上传要识别的图片，图片支持jpg/png格式，大小不超过2M,图片内容为多个果蔬，上传成功后点击下一步进入识别。</div>
                            <div v-if="imageUrl" class="delete" >
                                <el-popconfirm title="确定移除要上传到的图片吗？" @confirm="deleteUploadImg()">
                                    <el-button type="danger"  icon="el-icon-delete" slot="reference">移除</el-button>
                                </el-popconfirm>
                            </div>
                        </div>
                        <!--                        查看识别结果-->
                        <div class="show-result" v-if="this.active >= 2">
                            <div class="statistical-result">
                                <div class="statistical-title">统计结果</div>
                                <div id="statistical-identity" style=""></div>
                            </div>
                            <div class="identity-result">
                                <div class="identity-title">识别结果</div>
                                <el-table
                                        ref="multipleTable"
                                        :data="tableData"
                                        border
                                        stripe
                                        :default-sort = "{prop: 'name', order: 'ascending'}"
                                        :header-cell-style="{backgroundColor:'#f1f3f4'}"
                                        style="width: 1500px;margin: 40px auto;">
                                    <el-table-column
                                            type="index"
                                            label="ID"
                                            :index="1">
                                    </el-table-column>
                                    <el-table-column
                                            prop="name"
                                            sortable
                                            label="名称"
                                            >
                                    </el-table-column>
                                    <el-table-column
                                            prop="score"
                                            label="分数"
                                            >
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
        name: "Multiple",
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
                identityResult:{},//识别果蔬结果
                tableData: [],//果蔬表格信息
                pageNum:1,//当前页
                total:10,//数据总数
                pageSize:5,//1页展示多少条数据
                pieData:[],//饼图数据
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
                    duration:500,
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
                this.pieData = [];
            },


            //多个果蔬识别
            identify() {
                this.identityResult = {};
                //识别图片内容
                request.get('/multiple/identity',{
                    params:{
                        accessToken:this.user.accessToken,
                        file:this.picture,
                        username:this.user.username
                    }
                }).then(res => {
                       //获取识别的所有数据，用于饼图展示
                        let temp = res.result;
                        for(let item of temp) {
                            let obj = {};
                            obj.name = item.name;
                            obj.value = item.score;
                            this.pieData.push(obj);
                        }
                        //可视化展示数据
                        this.showData();
                        this.identityResult.logId = res.log_id;
                        //分页获取数据
                        this.getMuliple();
                        this.active = 3;
                })
            },

            //分页获取数据
            getMuliple() {
                request.get('/multiple/page',{
                    params:{
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                        username:this.user.username,
                        logId:this.identityResult.logId
                    }
                }).then(res => {
                    this.tableData = res.records;
                    this.total = res.total;
                    this.$message({
                        showClose: true,
                        message: '识别成功',
                        duration:500,
                        type: 'success'
                    });
                })
            },

            //可视化展示数据
            showData() {
                var myChart = this.$echarts.init(document.getElementById('statistical-identity'));
                var option;
                option = {
                    color:[ '#96cdff',
                            '#ffe9d5',
                            '#fec001',
                            "#afdc02",
                            '#86a609',
                            '#a5d5ff',
                            "#d6ecff",
                            '#c6e687',
                            '#ffdfc3',
                            "#f8b074",
                            '#bce0ff',
                            '#a4d6fe',
                            "#9cc000",
                        ],
                    tooltip: {
                        trigger: 'item',
                        borderColor: "#e8e8e8",
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'horizontal',
                        left: 'center',
                        top: 'bottom',
                    },
                    series: [
                        {
                            name: '果蔬名称',
                            type: 'pie',
                            radius: '50%',
                            data: this.pieData,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
             myChart.setOption(option);
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
            },

            // 分页
            handleSizeChange (pageSize) {
                this.pageSize = pageSize;
                this.getMuliple();
            },
            handleCurrentChange (pageNum) {
                this.pageNum = pageNum;
                this.getMuliple();
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
        display: flex;
        top: 110px;
        width: 1700px;
        height: 400px;
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
    .statistical-result {
        margin-top: 30px;
        width: 750px;
        height: 300px;
    }
    .statistical-title {
        font-weight: bold;
        color: #3d9fff;
    }
    #statistical-identity{
        margin: 0 auto;
        width: 700px;
        height: 270px;
    }
    .identity-result {
        margin-top: 30px;
        margin-left: 30px;
        width: 750px;
        height: 280px;
    }
    .identity-title {
        font-weight: bold;
        margin-bottom: 5px;
        color: #3d9fff;
    }
    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: center;
    }
</style>