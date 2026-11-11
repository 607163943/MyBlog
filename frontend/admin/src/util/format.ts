/** 千分位格式化，空值显示 -- */
export function formatNumber(value?: number | null) {
  if (value === null || value === undefined || Number.isNaN(value)) {
    return '--'
  }
  return value.toLocaleString('zh-CN')
}
