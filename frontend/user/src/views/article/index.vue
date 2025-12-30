<script setup>
import { ref } from 'vue'
import { articleGetDetailService } from '@/api/article'
import { useRoute } from 'vue-router'

const route = useRoute()
const blogDetailVO = ref({})
const commentPageResult = ref({})

const configurations = ref({
  websiteName: '我的博客',
  websiteDescription: '我的博客',
  websiteIcon: '/favicon.ico',
  websiteUrl: 'https://www.example.com',
  websiteEmail: 'example@example.com',
})

const getArticleDetail = async () => {
  const res = await articleGetDetailService(route.params.articleId)
  blogDetailVO.value = res.data.data.blogDetailVO
  commentPageResult.value = res.data.data.commentPageResult
  configurations.value = res.data.data.configurations
}

getArticleDetail()
</script>
<template>
  <div class="container">
    <div class="am-g am-g-fixed blog-fixed">
      <div class="am-u-lg-12 am-u-sm-12">
        <article class="am-article blog-article-p article-trigger">
          <div id="post-content" class="am-article-bd article-body">
            <div v-html="blogDetailVO.blogContent" />
            <aside class="blog-rights clearfix">
              <p>
                本站文章除注明转载/出处外，皆为作者原创，欢迎转载，但未经作者同意必须保留此段声明，且在文章页面明显位置给出原文链接，否则保留追究法律责任的权利。
              </p>
            </aside>
          </div>
        </article>
        <hr />
      </div>
    </div>
    <aside class="comments" id="comments">
      <div v-if="commentPageResult">
        <div v-for="comment in commentPageResult.list" :key="comment.commentId">
          <article class="comment">
            <header class="clearfix">
              <img src="../../../public/blog/default/img/avatar.png" class="avatar" />
              <div class="meta">
                <h3>{{ comment.commentator }}</h3>
                <span class="date"> 评论时间：{{ comment.commentCreateTime }} </span>
              </div>
            </header>
            <div class="body">
              {{ comment.commentBody }}
            </div>
          </article>
          <div v-if="!comment.replyBody">
            <article class="comment reply">
              <header class="clearfix">
                <img
                  :src="configurations.yourAvatar"
                  style="float: left; border-radius: 100px; width: 50px"
                />
                <div class="meta2">
                  <h3>{{ configurations.yourName }}</h3>
                  <span class="date">
                    回复时间：
                    {{ comment.replyCreateTime }}
                  </span>
                </div>
              </header>
              <div class="reply-body">
                {{ comment.replyBody }}
              </div>
            </article>
          </div>
        </div>
      </div>
      <div v-if="commentPageResult">
        <ul class="am-pagination">
          <li :class="commentPageResult.currPage == 1 ? 'disabled' : ''">
            <a
              :href="
                commentPageResult.currPage == 1
                  ? '##'
                  : '/blog/' +
                    blogDetailVO.blogId +
                    '?commentPage=' +
                    (commentPageResult.currPage - 1) +
                    '#comments'
              "
              >&laquo;</a
            >
          </li>
          <li v-if="commentPageResult.currPage - 3 >= 1">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage - 3) +
                '#comments'
              "
              :text="commentPageResult.currPage - 3"
              >1</a
            >
          </li>
          <li v-if="commentPageResult.currPage - 2 >= 1">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage - 2) +
                '#comments'
              "
              :text="commentPageResult.currPage - 2"
              >1</a
            >
          </li>
          <li v-if="commentPageResult.currPage - 1 >= 1">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage - 1) +
                '#comments'
              "
              :text="commentPageResult.currPage - 1"
              >1</a
            >
          </li>
          <li class="active"><a href="#" :text="commentPageResult.currPage">1</a></li>
          <li th:if="${commentPageResult.currPage+1 <=commentPageResult.totalPage}">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage + 1) +
                '#comments'
              "
              :text="commentPageResult.currPage + 1"
              >1</a
            >
          </li>
          <li v-if="commentPageResult.currPage + 2 <= commentPageResult.totalPage">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage + 2) +
                '#comments'
              "
              :text="commentPageResult.currPage + 2"
              >1</a
            >
          </li>
          <li v-if="commentPageResult.currPage + 3 <= commentPageResult.totalPage">
            <a
              :href="
                '/blog/' +
                blogDetailVO.blogId +
                '?commentPage=' +
                (commentPageResult.currPage + 3) +
                '#comments'
              "
              :text="commentPageResult.currPage + 3"
              >1</a
            >
          </li>
          <li :class="commentPageResult.currPage == commentPageResult.totalPage ? 'disabled' : ''">
            <a
              :href="
                commentPageResult.currPage == commentPageResult.totalPage
                  ? '##'
                  : '/blog/' +
                    blogDetailVO.blogId +
                    '?commentPage=' +
                    (commentPageResult.currPage + 1) +
                    '#comments'
              "
              >&raquo;</a
            >
          </li>
        </ul>
      </div>
    </aside>
    <div v-if="blogDetailVO.enableComment == 0">
      <aside class="create-comment" id="create-comment">
        <hr />
        <h2><i class="fa fa-pencil"></i> 添加评论</h2>
        <table id="commentForm" class="form">
          <tbody>
            <tr>
              <td>
                <input type="hidden" id="blogId" name="blogId" :value="blogDetailVO.blogId" />
                <input
                  type="text"
                  name="commentator"
                  id="commentator"
                  placeholder="(*必填)怎么称呼你?"
                />
              </td>
            </tr>
            <tr>
              <td>
                <input type="email" name="email" id="email" placeholder="(*必填)你的联系邮箱" />
              </td>
            </tr>
            <tr>
              <td>
                <input
                  type="text"
                  name="websiteUrl"
                  id="websiteUrl"
                  placeholder="你的网站地址(可不填)"
                />
              </td>
            </tr>
            <tr>
              <td>
                <input
                  style="width: 50%"
                  type="text"
                  name="verifyCode"
                  id="verifyCode"
                  placeholder="(*必填)请输入验证码"
                />&nbsp;&nbsp;
                <img
                  alt="单击图片刷新！"
                  class="pointer"
                  style="margin-top: 15px; border-radius: 25px"
                  :src="'/api/captcha'"
                  onclick="this.src = '/api/captcha?d=' + new Date() * 1"
                />
              </td>
            </tr>
            <tr>
              <td>
                <textarea
                  rows="5"
                  name="commentBody"
                  id="commentBody"
                  placeholder="(*必填)请输入你的评论"
                ></textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2" align="center">
                <button id="commentSubmit" type="submit" class="submit am-btn am-btn-primary">
                  提交
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </aside>
    </div>
    <p class="back-top" id="back-top" style="display: none">
      <a href="#top"><span></span></a>
    </p>
  </div>
</template>

<style lang="less" scoped></style>
