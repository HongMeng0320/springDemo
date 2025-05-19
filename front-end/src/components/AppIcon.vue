<template>
  <el-icon :size="size" :color="color" class="app-icon">
    <component :is="name" />
  </el-icon>
</template>

<script setup lang="ts">
import * as ElementPlusIcons from '@element-plus/icons-vue';

// 定义组件属性
const props = defineProps({
  name: {
    type: String,
    required: true
  },
  size: {
    type: [Number, String],
    default: ''
  },
  color: {
    type: String,
    default: ''
  }
});
</script>

<style scoped>
.app-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style> 