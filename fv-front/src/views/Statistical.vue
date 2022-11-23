<template>
    <div>
        <el-container style="height: 100%;position: relative">
            <Aside></Aside>
            <div>
                <el-container>
                    <Header></Header>
                    <el-main class="main-content">
                        <div class="box1">
                            <el-link :underline="false" class="title-basic-information" href="/user">用户基本信息</el-link>
                            <div class="user-basic-information">
                                <div class="user-basic-box">
                                    <el-empty v-if="this.roleArray.length === 0" description="无数据" style="position: absolute;left: 35%;"></el-empty>
                                    <div id="user-role-information"></div>
                                </div>
                                <div class="user-basic-box">
                                    <el-empty v-if="this.sexArray.length === 0" description="无数据" style="position: absolute;left: 35%;"></el-empty>
                                    <div id="user-sex-information"></div>
                                </div>
                                <div class="user-basic-box">
                                    <el-empty v-if="this.ageArray.length === 0" description="无数据" style="position: absolute;left: 35%;"></el-empty>
                                    <div id="user-age-information"></div>
                                </div>
                            </div>
                        </div>
                        <div class="box2">
                            <el-link :underline="false" class="title-behavior-information" href="/log">用户行为信息</el-link>
                            <div class="user-behavior-information">
                                <div class="user-behavior-box">
                                    <el-empty v-if="this.logArrayX.length === 0" description="无数据" style="position: absolute;left: 40%;top: 10%;"></el-empty>
                                    <div id="user-behavior-bar"></div>
                                </div>
                                <div class="user-behavior-box">
                                    <el-empty v-if="this.logArray.length === 0" description="无数据" style="position: absolute;left: 40%;top: 10%;"></el-empty>
                                    <div id="user-behavior-word-cloud"></div>
                                </div>
                            </div>
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
        name: "Statistical",
        components:{
            Aside,
            Header,
        },
        data() {
            return{
                roleArray:[],//用户基本信息-角色
                sexArray:[],//用户基本信息-性别
                ageArray:[],//用户基本信息-年龄
                logArrayX:[],//用户行为信息-使用的功能-X轴
                logArrayY:[],//用户行为信息-使用的功能-Y轴
                logArray:[],//用户行为信息-使用的功能
            }
        },
        created() {
            this.user = JSON.parse(localStorage.getItem('user'));
            this.load();
        },
        methods:{
            load() {
                //获取所有用户信息
                request.get('/user/findAllUser').then(res => {
                    this.countRole(res);
                    this.countSex(res);
                    this.countAge(res);
                })
                //获取日志信息
                request.get('/log/findAllLog').then(res => {
                   this.countLog(res);
                })
            },

            //统计不同角色的数量
            countRole(arr) {
               let hash = {};
               arr.forEach(item => {
                   if (!hash[item.role]) {
                       hash[item.role] = 1;
                   }else {
                       hash[item.role]++;
                   }
               })
                for (let [key,value] of Object.entries(hash)) {
                    let obj = {};
                    obj.value = value;
                    obj.name = key;
                    this.roleArray.push(obj);
                }
                //饼图显示数据
                this.showRole();
            },

            //统计不同角色的性别
            countSex(arr) {
                let hashsex = {};
                arr.forEach(item => {
                    if (!hashsex[item.sex]) {
                        hashsex[item.sex] = 1;
                    }else {
                        hashsex[item.sex]++;
                    }
                })
                for (let [key,value] of Object.entries(hashsex)) {
                    let obj = {};
                    obj.value = value;
                    obj.name = key;
                    this.sexArray.push(obj);
                }

                //饼图显示数据
                this.showSex();
            },

            //统计不同角色的年龄
            countAge(arr) {
                let hash = {};
                arr.forEach(item => {
                    if (!hash[item.age]) {
                        hash[item.age] = 1;
                    }else {
                        hash[item.age]++;
                    }
                })
                for (let [key,value] of Object.entries(hash)) {
                    let obj = {};
                    obj.value = value;
                    obj.name = key;
                    this.ageArray.push(obj);
                }
                //饼图显示数据
                this.showAge();
            },

            //统计用户行为
            countLog(arr) {
                let hash = {};
                arr.forEach(item => {
                    if (!hash[item.name]) {
                        hash[item.name] = 1;
                    }else {
                        hash[item.name]++;
                    }
                })
                for (let [key,value] of Object.entries(hash)) {
                    this.logArrayX.push(key);
                    this.logArrayY.push(value);
                    let obj = {};
                    obj.value = value;
                    obj.name = key;
                    this.logArray.push(obj);
                }
                //饼图显示数据
                this.showLogBybar();
                this.showLogWordCloud();
            },

            //可视化用户基本信息-角色
            showRole() {
                var myChart = this.$echarts.init(document.getElementById('user-role-information'));
                var option;
                option = {
                    color:[ '#96cdff', "#afdc02"],
                    title: {
                        text: '用户角色统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        borderColor: "#e8e8e8",
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'right',
                        top:'center',
                    },
                    series: [
                        {
                            name: '用户角色信息',
                            type: 'pie',
                            radius: '50%',
                            data: this.roleArray,
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

            //可视化用户基本信息-性别
            showSex() {
                var myChart = this.$echarts.init(document.getElementById('user-sex-information'));
                var option;
                option = {
                    color:[ '#c6e687', '#ffdfc3'],
                    title: {
                        text: '用户性别统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        borderColor: "#e8e8e8",
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'right',
                        top:'center',
                    },
                    series: [
                        {
                            name: '用户性别信息',
                            type: 'pie',
                            radius: '50%',
                            data: this.sexArray,
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

            //可视化用户基本信息-年龄
            showAge() {
                var myChart = this.$echarts.init(document.getElementById('user-age-information'));
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
                    title: {
                        text: '用户年龄统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        borderColor: "#e8e8e8",
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'right',
                        top:'center'
                    },
                    series: [
                        {
                            name: '用户年龄信息',
                            type: 'pie',
                            radius: '60%',
                            roseType: 'area',//是否表现为南丁格尔图
                            data: this.ageArray,
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

            //可视化用户行为信息-柱状图
            showLogBybar() {
                var myChart = this.$echarts.init(document.getElementById('user-behavior-bar'));
                var option;
                option = {
                    color:'#96cdff',
                    title: {
                        text: '用户使用功能频率统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow',
                        }
                    },
                    grid: {
                        left: '6%',
                        right: '6%',
                        bottom: '10%',
                        top:'20%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.logArrayX,
                        axisTick:{
                            show:false
                        },
                        axisLine:{
                            show:true,
                            lineStyle:{
                                color:'#c1c5d0',
                                width:1,
                                type:'solid',
                            },
                        },
                        axisLabel:{
                            color:'#909399',
                        },
                    },
                    yAxis: {
                        type: 'value',
                        show:true,
                        name:'次数(次)',
                        nameTextStyle:{
                            color:'#909399',
                        },
                        axisLine:{
                            show:true,
                            lineStyle:{
                                color:'#c1c5d0',
                                width:1,
                                type:'solid',
                            },
                        },
                        splitLine:{
                            lineStyle:{
                                color:'#c1c5d0',//分隔线颜色
                            }
                        },
                        axisLabel:{
                            color:'#909399',
                        },
                    },
                    series: [
                        {   name: '次数(次)',
                            type: 'bar',
                            barWidth:'30%',
                            data: this.logArrayY,
                            itemStyle:{
                                barBorderRadius:[5, 5, 0, 0]
                            },
                        }
                    ]
                };
                myChart.setOption(option);
            },

            //可视化用户行为信息-词云
            showLogWordCloud() {
                var myChart = this.$echarts.init(document.getElementById('user-behavior-word-cloud'));
                var option;
                mycolor:[ '#96cdff',
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
                option = {
                    title:{
                        text: '用户使用功能词云',
                        left:'center',
                    },
                    tooltip:{
                        trigger: 'item',
                        borderColor: "#e8e8e8",
                        formatter: '{a} <br/>{b} : {c}'
                    },
                    series: [ {
                        name:'使用功能',
                        type: 'wordCloud',
                        gridSize: 2,
                        sizeRange: [12, 50],
                        rotationRange: [-90, 90],
                        shape: 'pentagon',
                        width: 600,
                        height: 400,
                        drawOutOfBound: true,
                        textStyle: {
                            fontFamily: '微软雅黑',
                            color: function () {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 250),
                                    Math.round(Math.random() * 250),
                                    Math.round(Math.random() * 250)
                                ].join(',') + ')';
                            },
                            emphasis: {
                                shadowBlur: 10,
                                shadowColor: '#333'
                            }
                        },
                        data: this.logArray
                    } ]
                };
                myChart.setOption(option);
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
        padding-top: 0px;
        overflow: visible;
    }
    .box1 {
        width: 100%;
        height: 330px;
        box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
    }
    .user-basic-information {
        display: flex;
        justify-content: space-between;
        margin: 0px auto;
        width: 100%;
        height: 330px;
    }
    .title-basic-information ,.title-behavior-information {
        color: #3d9fff!important;
        font-size: 16px;
        font-weight: bold;
    }
    .user-basic-box {
        position: relative;
        width: 550px;
        height: 100%;
    }
    #user-role-information ,#user-sex-information,#user-age-information {
        position: absolute;
        z-index: 1;
        width: 550px;
        height: 100%;
    }
    .box2 {
        margin-top: 20px;
        width: 100%;
        height: 470px;
        box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
    }
    .user-behavior-information {
        display: flex;
        justify-content: space-between;
        margin: 0 auto;
        width: 100%;
        height: 420px;
    }
    .user-behavior-box {
        position: relative;
        width: 47%;
        height: 410px;
    }
    #user-behavior-bar ,#user-behavior-word-cloud{
        position: absolute;
        z-index: 1;
        width: 100%;
        height: 100%;
    }
</style>