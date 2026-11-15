/** 归档月份信息 */
export interface ArchiveMonthVO {
  year: number
  month: number
  articleCount: number
}

/** 归档年份信息 */
export interface ArchiveYearVO {
  year: number
  months: ArchiveMonthVO[]
}
