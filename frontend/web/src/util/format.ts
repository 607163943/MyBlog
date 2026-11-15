/**
 * 格式化日期字符串为 YYYY-MM-DD
 * 后端 LocalDateTime 序列化为 ISO 字符串，直接截取避免时区偏移
 */
export function formatDate(value?: string | null) {
  if (!value) return '--'
  return value.slice(0, 10)
}

/**
 * 格式化日期字符串为 YYYY-MM-DD HH:mm
 */
export function formatDateTime(value?: string | null) {
  if (!value) return '--'
  return value.replace('T', ' ').slice(0, 16)
}

/** 千分位格式化，空值显示 -- */
export function formatNumber(value?: number | null) {
  if (value === null || value === undefined || Number.isNaN(value)) {
    return '--'
  }
  return value.toLocaleString('zh-CN')
}
