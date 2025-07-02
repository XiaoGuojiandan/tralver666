const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:1236',
        changeOrigin: true,
        // pathRewrite: {
        //   '^/api': ''
        // }
      },
      '/img': {
        target: 'http://localhost:1236',
        changeOrigin: true
      }
    },
    client: {
      overlay:false
    }
  }
})
