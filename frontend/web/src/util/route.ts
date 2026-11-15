/** 将路由参数解析为正整数，非法值返回 null */
export function parsePositiveInt(value: unknown): number | null {
  if (typeof value !== 'string' && typeof value !== 'number') {
    return null
  }
  const parsed = Number(value)
  if (!Number.isInteger(parsed) || parsed < 1) {
    return null
  }
  return parsed
}

/** 校验归档月份是否在 1-12 之间 */
export function parseMonth(value: unknown): number | null {
  const parsed = parsePositiveInt(value)
  return parsed !== null && parsed <= 12 ? parsed : null
}
