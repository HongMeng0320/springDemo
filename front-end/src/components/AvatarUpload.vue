<template>
  <div class="avatar-upload-container">
    <el-upload
      class="avatar-uploader"
      action="#"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :http-request="customUpload"
      :disabled="uploading"
    >
      <div class="avatar-wrapper">
        <img v-if="avatarUrl" :src="avatarUrl" class="avatar" @error="handleImageError" />
        <el-icon v-else class="avatar-icon"><User /></el-icon>
        <div class="hover-mask">
          <el-icon><Camera /></el-icon>
          <span>更换头像</span>
        </div>
        <div v-if="uploading" class="uploading-mask">
          <el-progress type="circle" :percentage="uploadProgress" :width="70" />
        </div>
      </div>
    </el-upload>
    <div class="upload-tip">
      <el-icon><Upload /></el-icon>
      <span>点击或拖拽上传头像</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { User, Upload, Camera } from '@element-plus/icons-vue';
import { uploadFileToOss, validateFile } from '@/utils/oss';
import { updateUserAvatar } from '@/api/user';

const props = defineProps({
  currentAvatar: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['update:avatar']);

const avatarUrl = ref(props.currentAvatar);
const uploading = ref(false);
const uploadProgress = ref(0);

// 监听props变化
watch(() => props.currentAvatar, (newVal) => {
  if (newVal) {
    avatarUrl.value = newVal;
  }
});

// 处理图片加载错误
const handleImageError = () => {
  avatarUrl.value = '';  // 设置为空，显示默认图标
};

// 上传前验证
const beforeUpload = (file: File) => {
  // 验证文件类型和大小
  const { valid, message } = validateFile(file);
  if (!valid && message) {
    ElMessage.error(message);
    return false;
  }
  
  // 开始上传
  uploading.value = true;
  uploadProgress.value = 0;
  
  // 模拟进度
  const interval = setInterval(() => {
    if (uploadProgress.value < 90) {
      uploadProgress.value += 10;
    } else {
      clearInterval(interval);
    }
  }, 200);
  
  return true;
};

// 自定义上传
const customUpload = async (options: any) => {
  try {
    // 获取文件
    const file = options.file;
    
    // 上传到OSS
    const url = await uploadFileToOss(file);
    
    // 更新头像URL到后端
    await updateUserAvatar(url);
    
    // 更新本地显示
    avatarUrl.value = url;
    uploadProgress.value = 100;
    
    // 通知父组件
    emit('update:avatar', url);
    
    ElMessage.success('头像上传成功');
  } catch (error) {
    ElMessage.error('头像上传失败，请检查网络连接或CORS配置');
    console.error('头像上传失败:', error);
    // 重置为原头像
    avatarUrl.value = props.currentAvatar;
  } finally {
    setTimeout(() => {
      uploading.value = false;
    }, 500);
  }
};
</script>

<style scoped>
.avatar-upload-container {
  text-align: center;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-uploader {
  width: 150px;
  margin: 0 auto;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto;
  border-radius: 50%;
  border: 2px solid #d9d9d9;
  background-color: #fafafa;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s;
}

.avatar-wrapper:hover {
  border-color: #409eff;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
}

.avatar-wrapper:hover .hover-mask {
  opacity: 1;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-icon {
  font-size: 48px;
  color: #8c939d;
}

.hover-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  color: white;
  border-radius: 50%;
}

.hover-mask .el-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.uploading-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-tip .el-icon {
  margin-right: 4px;
  color: #409eff;
}
</style> 