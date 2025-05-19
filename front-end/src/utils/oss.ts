import axios from 'axios';

/**
 * 阿里云OSS上传工具
 */
export interface OssPolicyResult {
  accessId: string;
  policy: string;
  signature: string;
  dir: string;
  host: string;
  expire: number;
}

/**
 * 生成随机文件名
 * @param fileName 原始文件名
 * @returns 新文件名
 */
export function generateRandomFileName(fileName: string): string {
  const suffix = fileName.substring(fileName.lastIndexOf('.'));
  const timestamp = new Date().getTime();
  const random = Math.floor(Math.random() * 1000);
  return `${timestamp}_${random}${suffix}`;
}

/**
 * 上传文件到OSS
 * @param file 文件对象
 * @param dir 目录
 * @returns 上传后的文件URL
 */
export async function uploadFileToOss(file: File, dir = 'avatar/'): Promise<string> {
  try {
    // 使用动态导入避免循环依赖
    const { getOssPolicy } = await import('@/api/oss');
    
    // 获取OSS上传策略
    const ossData = await getOssPolicy();
    
    // 生成随机文件名
    const fileName = generateRandomFileName(file.name);
    const key = dir + fileName;
    
    // 创建FormData
    const formData = new FormData();
    formData.append('key', key);
    formData.append('policy', ossData.policy);
    formData.append('OSSAccessKeyId', ossData.accessId);
    formData.append('signature', ossData.signature);
    formData.append('success_action_status', '200');
    formData.append('file', file);
    
    // 上传到OSS
    await axios.post(ossData.host, formData);
    
    // 返回文件访问URL
    return `${ossData.host}/${key}`;
  } catch (error) {
    console.error('上传文件到OSS失败:', error);
    throw new Error('上传文件失败');
  }
}

/**
 * 检查文件是否符合要求
 * @param file 文件对象
 * @param maxSize 最大大小(MB)
 * @returns 是否符合要求
 */
export function validateFile(file: File, maxSize = 5): { valid: boolean; message?: string } {
  // 检查文件类型
  const acceptTypes = ['image/jpeg', 'image/png', 'image/gif'];
  if (!acceptTypes.includes(file.type)) {
    return { valid: false, message: '只能上传JPG/PNG/GIF格式的图片!' };
  }
  
  // 检查文件大小
  const isLt5M = file.size / 1024 / 1024 < maxSize;
  if (!isLt5M) {
    return { valid: false, message: `图片大小不能超过${maxSize}MB!` };
  }
  
  return { valid: true };
} 