import { DirectiveBinding } from 'vue';

interface FastClickOptions {
  threshold?: number; // 点击阈值，默认为 300ms
  preventDefault?: boolean; // 是否阻止默认事件
}

// 快速点击指令
export default {
  mounted(el: HTMLElement, binding: DirectiveBinding<FastClickOptions>) {
    const options = binding.value || {};
    const threshold = options.threshold || 300;
    const preventDefault = options.preventDefault !== false;
    
    let touchStartTime = 0;
    let touchStartX = 0;
    let touchStartY = 0;
    
    // 触摸开始
    el.addEventListener('touchstart', (e: TouchEvent) => {
      if (preventDefault) e.preventDefault();
      
      touchStartTime = Date.now();
      touchStartX = e.touches[0].clientX;
      touchStartY = e.touches[0].clientY;
    }, { passive: !preventDefault });
    
    // 触摸结束
    el.addEventListener('touchend', (e: TouchEvent) => {
      if (preventDefault) e.preventDefault();
      
      const touchEndTime = Date.now();
      const touchEndX = e.changedTouches[0].clientX;
      const touchEndY = e.changedTouches[0].clientY;
      
      // 计算时间差和位移
      const timeDiff = touchEndTime - touchStartTime;
      const distanceX = Math.abs(touchEndX - touchStartX);
      const distanceY = Math.abs(touchEndY - touchStartY);
      
      // 如果时间小于阈值且位移很小，认为是点击
      if (timeDiff < threshold && distanceX < 10 && distanceY < 10) {
        // 触发点击事件
        const clickEvent = new MouseEvent('click', {
          bubbles: true,
          cancelable: true,
          view: window
        });
        
        el.dispatchEvent(clickEvent);
      }
    }, { passive: !preventDefault });
    
    // 移动中取消点击
    el.addEventListener('touchmove', (e: TouchEvent) => {
      const touchMoveX = e.touches[0].clientX;
      const touchMoveY = e.touches[0].clientY;
      
      // 如果移动距离超过阈值，重置开始时间
      if (Math.abs(touchMoveX - touchStartX) > 10 || Math.abs(touchMoveY - touchStartY) > 10) {
        touchStartTime = 0;
      }
    }, { passive: true });
  }
}; 