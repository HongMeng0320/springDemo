import { App } from 'vue';
import preload from './preload';
import fastClick from './fastClick';

// 注册全局指令
export default {
  install(app: App) {
    // 注册预加载指令
    app.directive('preload', preload);
    
    // 注册快速点击指令
    app.directive('fast-click', fastClick);
  }
}; 