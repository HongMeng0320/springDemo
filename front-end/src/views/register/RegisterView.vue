<template>
  <div class="register-container">
    <div class="register-box">
      <div class="system-logo">
        <img src="@/assets/logo.svg" alt="系统Logo" class="logo-image" />
      </div>
      <div class="register-header">
        <h2>知识积分管理系统</h2>
        <div class="register-subtitle">用户注册</div>
      </div>
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading" class="register-button">注册</el-button>
          <el-button @click="resetForm" class="reset-button">重置</el-button>
        </el-form-item>
        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
      <div class="register-footer">
        © 2023 知识积分管理系统 - 版权所有
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus'
import { register } from '@/api/auth'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const loading = ref(false)
const registerFormRef = ref<FormInstance>()

// 表单验证规则
const validatePass = (_: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      if (registerFormRef.value) {
        registerFormRef.value.validateField('confirmPassword')
      }
    }
    callback()
  }
}

const validatePass2 = (_: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const registerRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur' },
    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/, message: '密码必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
})

const submitForm = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { username, password } = registerForm
        await register({
          username,
          password
        })
        
        // 注册成功
        ElMessage({
          message: '注册成功，请登录',
          type: 'success',
          duration: 2000
        })
        
        // 延迟跳转，让用户看到成功提示
        setTimeout(() => {
          router.push('/login')
        }, 1000)
      } catch (error: any) {
        console.error('注册失败:', error)
        
        // 处理不同类型的错误
        if (error.response && error.response.data) {
          // 后端返回的错误信息
          ElMessage.error(error.response.data.message || '注册失败，请重试')
        } else if (error.message) {
          // 一般错误信息
          ElMessage.error(error.message)
        } else {
          // 未知错误
          ElMessage.error('注册失败，请重试')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  if (registerFormRef.value) {
    registerFormRef.value.resetFields()
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  background-size: cover;
  position: relative;
}

.register-box {
  width: 550px;
  max-width: 100%;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  animation: fadeIn 0.8s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.system-logo {
  text-align: center;
  margin-bottom: 15px;
}

.logo-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.register-subtitle {
  font-size: 16px;
  color: #666;
}

.register-button {
  width: 48%;
  height: 45px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  background: linear-gradient(90deg, #2575fc 0%, #6a11cb 100%);
  border: none;
  transition: all 0.3s ease;
}

.reset-button {
  width: 48%;
  height: 45px;
  border-radius: 8px;
  font-size: 16px;
  margin-left: 4%;
}

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(37, 117, 252, 0.4);
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #2575fc;
  font-weight: bold;
  text-decoration: none;
  transition: all 0.3s ease;
}

.login-link a:hover {
  color: #6a11cb;
  text-decoration: underline;
}

.register-footer {
  text-align: center;
  margin-top: 30px;
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .register-box {
    width: 90%;
    padding: 30px;
  }
  
  .register-button, .reset-button {
    width: 100%;
    margin-left: 0;
    margin-bottom: 10px;
  }
}
</style> 