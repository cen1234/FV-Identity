import Vue from 'vue'
import App from './App.vue'
import router from './router'
// 导入element-ui相关
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//引入全局css
import './assets/global.css'
//引入配置的request.js
import request from "../utils/request";


Vue.config.productionTip = false
Vue.use(ElementUI,{size:"mini"});
Vue.prototype.axios=request();

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
