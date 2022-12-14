const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    open:true,
    host:'localhost',
    port:8080,
    proxy: {                 //设置代理，必须填
      '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
        target: 'http://192.168.1.6:9000',     //代理的目标地址
        changeOrigin: true,              //是否设置同源，输入是的
        ws:true,
        pathRewrite: {                   //192.168.1.6路径重写
          '^/api': ''                     //选择忽略拦截器里面的内容
        }
      }
    }
  }
})
