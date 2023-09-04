// 项目配置页面
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader';
import vueJsx from '@vitejs/plugin-vue-jsx';
import path from 'path';

const CWD = process.cwd();

//配置参考 https://vitejs.dev/config/
export default defineConfig((mode) => {
  // const { VITE_BASE_URL } = loadEnv(mode, CWD);
  return {
    base: './',
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    plugins: [
      vue(),
      vueJsx(),
      svgLoader()
    ],
    server: {
      port: 18082,
      host: '0.0.0.0',
      proxy: {
        '/img-tx': {
          target:  'https://tjxt-dev.itheima.net/', // 'http://172.17.2.134',
          changeOrigin: true,
          // rewrite: (path) => {
          //   return path.replace(/^\/img-tx/, '')
          // }
        },
        '/mock/3359':{
          target: 'http://172.17.0.137:8321/mock/3359',
          changeOrigin: true,
        }
      }
    },
  }
})
