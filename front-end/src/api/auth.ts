import { post } from '@/utils/request'

// 用户注册接口
export interface RegisterParams {
  username: string
  password: string
  email?: string
  phone?: string
  realName?: string
}

export interface RegisterResult {
  userId: number
  username: string
}

/**
 * 用户注册
 * @param data 注册信息
 */
export function register(data: RegisterParams) {
  return post<RegisterResult>('/api/auth/register', data)
}

/**
 * 用户登录
 * @param username 用户名
 * @param password 密码
 */
export function login(username: string, password: string) {
  return post<{token: string, user: any}>('/api/auth/login', { username, password })
}

/**
 * 退出登录
 */
export function logout() {
  return post('/api/auth/logout')
} 