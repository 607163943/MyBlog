const SITE_TITLE = 'MyBlog | 个人博客'

/** 设置页面标题；不传标题时恢复站点默认标题 */
export function setPageTitle(title?: string | null) {
  document.title = title ? `${title} - MyBlog` : SITE_TITLE
}

export { SITE_TITLE }
