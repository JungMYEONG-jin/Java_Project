const bootstrapSassAbstractsImports = require('vue-cli-plugin-bootstrap-vue/sassAbstractsImports.js')
const { defineConfig } = require('@vue/cli-service')
module.exports = {
  outputDir: "../src/main/resources/static",
  devServer: {
    proxy: {
      '/api': {
        // api uri 접근시 서버 포트로 변경
        target: 'http://localhost:8088',
        changeOrigin: true
      }
    }
  }
};




//defineConfig({
//   transpileDependencies: true
	css: {
		loaderOptions: {
			sass: {
				additionalData: bootstrapSassAbstractsImports.join('\n')
			}
			scss: {
				additionalData: [...bootstrapSassAbstractsImports, ''].join(';\n')
			}
		}
	}
// }