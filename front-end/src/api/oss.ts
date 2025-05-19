import { get } from '@/utils/request';
import { OssPolicyResult } from '@/utils/oss';

/**
 * 获取OSS上传策略
 * @returns OSS上传策略
 */
export function getOssPolicy(): Promise<OssPolicyResult> {
  return get<OssPolicyResult>('/api/oss/policy');
} 