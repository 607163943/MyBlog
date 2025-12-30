<script setup>
import { ref } from 'vue'
import { homeGetDataService } from '@/api/home'

const configurations = ref({
  websiteName: '我的博客',
  websiteDescription: '我的博客',
  websiteIcon: '/favicon.ico',
  websiteUrl: 'https://www.example.com',
  websiteEmail: 'example@example.com',
})

const blogPageResult = ref({})
const hotTags = ref({})
const newBlogs = ref({})
const hotBlogs = ref({})

const getBlogHomeData = async () => {
  const res = await homeGetDataService()
  configurations.value = res.data.data.configurations
  blogPageResult.value = res.data.data.blogPageResult
  hotTags.value = res.data.data.hotTags
  newBlogs.value = res.data.data.newBlogs
  hotBlogs.value = res.data.data.hotBlogs
}

getBlogHomeData()
</script>
<template>
  <div class="am-g am-g-fixed blog-fixed">
    <div class="am-u-lg-8 am-u-sm-12">
      <main class="articles-list">
        <div>
          <div v-if="null != blogPageResult">
            <div v-for="blog in blogPageResult.list" :key="blog.blogId">
              <article class="am-g blog-entry-article">
                <div class="am-u-lg-6 am-u-lg-12 am-u-sm-12 blog-entry-text post-preview">
                  <div class="topic-header">
                    <div class="pull-left">
                      <div class="blog-flex-center">
                        <div class="blog-flex0">
                          <img
                            :src="'../../../public/' + configurations.yourAvatar"
                            alt=""
                            class="link avatar avatar-image"
                          />
                        </div>
                        <div class="author-lockup blog-flex1">
                          <a class="link" href="javascript:;">{{ configurations.yourName }}</a>
                          <span class="in">in</span>
                          <span class="category-name">
                            <a
                              class="category-name"
                              rel="tag"
                              :href="'/category/' + blog.blogCategoryName"
                            >
                              {{ blog.blogCategoryName }}
                            </a>
                          </span>
                        </div>
                      </div>
                    </div>
                    <span class="pull-right time pc_time">{{ blog.createTime }}</span>
                  </div>
                  <h1 class="post-index-title">
                    <a itemtype="url" :href="'/article/' + blog.blogId">
                      {{ blog.blogTitle }}
                    </a>
                  </h1>
                  <div class="post-content-preview article-body">
                    <a :href="'/article/' + blog.blogId">
                      <img :src="'../../../public/' + blog.blogCoverImage" alt=""
                    /></a>
                  </div>
                  <div class="clearfix topic-footer">
                    <span class="pull-left time mobile_time">2018-01</span>
                  </div>
                </div>
              </article>
            </div>
          </div>
          <div v-if="blogPageResult">
            <ul class="am-pagination">
              <li
                :class="{
                  disabled: blogPageResult.currPage == 1,
                  'am-pagination-prev': blogPageResult.currPage != 1,
                }"
              >
                <a
                  :href="
                    blogPageResult.currPage == 1 ? '##' : '/page/' + (blogPageResult.currPage - 1)
                  "
                  >&laquo;</a
                >
              </li>
              <li :if="blogPageResult.currPage - 3 >= 1">
                <a :href="'/page/' + (blogPageResult.currPage - 3)">{{
                  blogPageResult.currPage - 3
                }}</a>
              </li>
              <li :if="blogPageResult.currPage - 2 >= 1">
                <a :href="'/page/' + (blogPageResult.currPage - 2)">{{
                  blogPageResult.currPage - 2
                }}</a>
              </li>
              <li :if="blogPageResult.currPage - 1 >= 1">
                <a :href="'/page/' + (blogPageResult.currPage - 1)">{{
                  blogPageResult.currPage - 1
                }}</a>
              </li>
              <li class="active"><a href="#" th:text="${blogPageResult.currPage}">1</a></li>
              <li :if="blogPageResult.currPage + 1 <= blogPageResult.totalPage">
                <a :href="'/page/' + (blogPageResult.currPage + 1)">{{
                  blogPageResult.currPage + 1
                }}</a>
                >
              </li>
              <li v-if="blogPageResult.currPage + 2 <= blogPageResult.totalPage">
                <a :href="'/page/' + (blogPageResult.currPage + 2)">{{
                  blogPageResult.currPage + 2
                }}</a>
                >
              </li>
              <li v-if="blogPageResult.currPage + 3 <= blogPageResult.totalPage">
                <a :href="'/page/' + (blogPageResult.currPage + 3)">{{
                  blogPageResult.currPage + 3
                }}</a>
                >
              </li>
              <li :class="{ disabled: blogPageResult.currPage == blogPageResult.totalPage }">
                <a
                  :href="
                    blogPageResult.currPage == blogPageResult.totalPage
                      ? '##'
                      : '/page/' + (blogPageResult.currPage + 1)
                  "
                  >&raquo;</a
                >
              </li>
            </ul>
          </div>
        </div>
      </main>
    </div>
    <div class="am-u-lg-4 am-u-sm-12 am-u-lg-offset-0 am-u-sm-offset-0 sidebar-container">
      <section>
        <section>
          <div class="sidebar-header">
            <span class="sidebar-header-title"> 搜索文章 </span>
          </div>
          <div class="sidebar-search">
            <form method="get" onsubmit="return false" accept-charset="utf-8">
              <div class="search-input">
                <input type="text" name="keyword" id="searchbox" placeholder="输入关键字搜索" />
                <button type="submit" class="search-submit" onclick="search()"></button>
              </div>
            </form>
          </div>
        </section>
        <section>
          <div class="sidebar-header">
            <span class="sidebar-header-title"> 热门标签 </span>
          </div>
          <div class="tags">
            <div v-if="hotTags">
              <div v-for="hotTag in hotTags" :key="hotTag.tagName">
                <a rel="tag" :href="'/tag/' + hotTag.tagName">
                  {{ hotTag.tagName }}({{ hotTag.tagCount }})
                </a>
              </div>
            </div>
          </div>
        </section>
        <section>
          <div class="sidebar-header">
            <span class="sidebar-header-title"> 最新发布 </span>
          </div>
          <div class="sidebar-list">
            <ul class="sidebar-list-body sidebar-list-items">
              <div v-if="newBlogs">
                <div v-for="newBlog in newBlogs" :key="newBlog.blogId">
                  <li class="sidebar-list-item">
                    <a :href="'/article/' + newBlog.blogId" class="tag tooltipped tooltipped-n">
                      {{ newBlog.blogTitle }}
                    </a>
                  </li>
                </div>
              </div>
            </ul>
          </div>
        </section>
        <section>
          <div class="sidebar-header">
            <span class="sidebar-header-title"> 点击最多 </span>
          </div>
          <main class="sidebar-lists">
            <ul>
              <div v-if="hotBlogs">
                <div v-for="hotBlog in hotBlogs" :key="hotBlog.blogId">
                  <li class="sidebar-list-item">
                    <a class="tag tooltipped tooltipped-n" :href="'/article/' + hotBlog.blogId">
                      <th:block th:text="${hotBlog.blogTitle}"></th:block>
                    </a>
                  </li>
                </div>
              </div>
            </ul>
          </main>
        </section>
      </section>
    </div>
  </div>
</template>

<style scoped lang="less"></style>
