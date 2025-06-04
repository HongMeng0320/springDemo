<template>
  <div class="login-container">
    <div class="login-box">
      <div class="system-logo">
        <img src="@/assets/logo.svg" alt="系统Logo" class="logo-image" />
      </div>
      <div class="login-title">知识积分管理系统</div>
      <div class="login-subtitle">欢迎使用知识积分管理平台</div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="userStore.loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
      <div class="login-footer">
        © 知识积分管理系统 - rj22-3第三小组
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, onBeforeMount } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';

import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();
const loginFormRef = ref<FormInstance>();

// 如果已登录，直接跳转到首页 - 此检查在组件挂载前执行
onBeforeMount(() => {
  checkLoginStatus();
});

// 页面加载完成后再次检查登录状态，处理页面刷新的情况
onMounted(() => {
  // 清除可能存在的过期提示标记
  window.hasShownLoginExpired = false;
  checkLoginStatus();
});

// 检查登录状态
function checkLoginStatus() {
  const token = localStorage.getItem('token');
  if (token) {
    // 如果是从别的页面跳转过来的，不重复跳转
    if (router.currentRoute.value.query.redirect) {
      return;
    }
    // 避免重复导航
    if (router.currentRoute.value.path !== '/') {
      router.push('/');
    }
  }
}

// 登录表单
const loginForm = reactive({
  username: '',
  password: ''
});

// 表单验证规则
const loginRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
});

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  // 清除登录过期标记，确保能够重新登录
  window.hasShownLoginExpired = false;
  
  console.log('验证表单...');
  await loginFormRef.value.validate((valid) => {
    if (valid) {
      console.log('表单验证通过，开始登录流程...');
      userStore.login(loginForm);
    } else {
      console.log('表单验证失败');
    }
  });
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  background-size: cover;
  position: relative;
}

.login-box {
  width: 420px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  animation: fadeIn 0.8s ease-in-out;
  position: relative;
  z-index: 2;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.system-logo {
  text-align: center;
  margin-bottom: 20px;
}

.logo-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 10px;
}

.login-subtitle {
  font-size: 16px;
  color: #666;
  text-align: center;
  margin-bottom: 30px;
}

.login-form {
  margin-top: 20px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 0 15px;
}

.login-form :deep(.el-input__inner) {
  height: 45px;
}

.login-button {
  width: 100%;
  height: 45px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  margin-top: 10px;
  background: linear-gradient(90deg, #2575fc 0%, #6a11cb 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(37, 117, 252, 0.4);
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #2575fc;
  font-weight: bold;
  text-decoration: none;
  transition: all 0.3s ease;
}

.register-link a:hover {
  color: #6a11cb;
  text-decoration: underline;
}

.login-footer {
  text-align: center;
  margin-top: 30px;
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .login-box {
    width: 90%;
    padding: 30px;
  }
}
</style> 