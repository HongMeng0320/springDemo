<template>
  <el-avatar 
    :size="size" 
    :src="avatarUrl" 
    :icon="avatarUrl ? '' : User" 
    :class="{'clickable': clickable}"
    @click="handleClick"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { User } from '@element-plus/icons-vue';

const props = defineProps({
  userId: {
    type: Number,
    required: true
  },
  avatarUrl: {
    type: String,
    default: ''
  },
  size: {
    type: [Number, String],
    default: 'default'
  },
  clickable: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['click']);

// 处理点击事件
const handleClick = () => {
  if (props.clickable) {
    emit('click', props.userId);
  }
};
</script>

<style scoped>
.clickable {
  cursor: pointer;
}
.clickable:hover {
  opacity: 0.8;
}
</style> 