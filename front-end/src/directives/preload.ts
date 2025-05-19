import { DirectiveBinding } from 'vue';

// 预加载路由组件指令
export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    // 获取需要预加载的路由路径
    const routePath = binding.value;
    
    // 如果没有指定路径，则不执行
    if (!routePath) return;
    
    // 创建一个交叉观察器，当元素进入视口时预加载对应组件
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          // 元素进入视口，预加载组件
          setTimeout(() => {
            // 动态导入对应的组件
            import(`@/views/${routePath}.vue`).catch(err => {
              console.error(`预加载组件失败: ${routePath}`, err);
            });
          }, 100);
          
          // 已预加载，取消观察
          observer.unobserve(el);
        }
      });
    }, {
      rootMargin: '100px', // 提前100px开始加载
      threshold: 0.1 // 10%可见时触发
    });
    
    // 开始观察元素
    observer.observe(el);
  }
}; 