// 项目配置页面
import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import svgLoader from "vite-svg-loader";
import vueJsx from "@vitejs/plugin-vue-jsx";
import path from "path";

const CWD = process.cwd();

//配置参考 https://vitejs.dev/config/
export default defineConfig((mode) => {
  const { VITE_BASE_URL } = loadEnv(mode, CWD);
  return {
    base: "./",
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "./src"),
      },
    },
    plugins: [vue(), vueJsx(), svgLoader()],
    // server:{
    //   port: 8081
    // }
    server: {
      port: 18081,
      host: "0.0.0.0",
      // proxy: {
      //     '/img-tx': {
      //     target: 'http://wisehub-1312394356.cos.ap-shanghai.myqcloud.com',
      //     // rewrite: (path) => {
      //     //   return path.replace(/^\/img-tx/, '')
      //     // }
      //   },
      // },
      proxy: {
        "/img-tx": {
          target: "https://tjxt-dev.itheima.net",
          changeOrigin: true,
          // rewrite: (path) => {
          //   return path.replace(/^\/img-tx/, '')
          // }
        },
      },
    },
  };
});
