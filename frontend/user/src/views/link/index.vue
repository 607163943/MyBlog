<script setup>
import { ref } from 'vue'
import { linkFavoriteLinksService } from '@/api/link'
import { isEmpty } from 'radash'

const configurations = ref({
  websiteName: '我的博客',
  websiteDescription: '我的博客',
  websiteIcon: '/favicon.ico',
  websiteUrl: 'https://www.example.com',
  websiteEmail: 'example@example.com'
})
const favoriteLinks = ref([])
const recommendLinks = ref([])
const personalLinks = ref([])

const getLinkData = async () => {
  const res = await linkFavoriteLinksService()
  favoriteLinks.value = res.data.data.favoriteLinks
  recommendLinks.value = res.data.data.recommendLinks
  personalLinks.value = res.data.data.personalLinks
  configurations.value = res.data.data.configurations
}

getLinkData()
</script>

<template>
  <div class="am-g am-g-fixed blog-fixed">
    <div class="am-u-lg-8 am-u-sm-12">
      <main class="links-list">
        <div v-if="!isEmpty(favoriteLinks)">
          <div class="title">
            <h3>友链</h3>
          </div>
          <div class="list">
            <ul>
              <div v-for="favoriteLink in favoriteLinks" :key="favoriteLink.id">
                <li>
                  <a :href="favoriteLink.linkUrl">{{ favoriteLink.linkName }}</a>
                  -
                  {{ favoriteLink.linkDescription }}
                </li>
              </div>
            </ul>
          </div>
        </div>
        <div v-if="!isEmpty(recommendLinks)">
          <div class="title">
            <h3>推荐网站</h3>
          </div>
          <div class="list">
            <ul>
              <div v-for="recommendLink in recommendLinks" :key="recommendLink.id">
                <li>
                  <a :href="recommendLink.linkUrl">{{ recommendLink.linkName }}</a>
                  -
                  {{ recommendLink.linkDescription }}
                </li>
              </div>
            </ul>
          </div>
        </div>
        <div v-if="!isEmpty(personalLinks)">
          <div class="title">
            <h3>个人链接</h3>
          </div>
          <div class="list">
            <ul>
              <div v-for="personalLink in personalLinks" :key="personalLink.id">
                <li>
                  <a :href="personalLink.linkUrl">{{ personalLink.linkName }}</a>
                  -
                  {{ personalLink.linkDescription }}
                </li>
              </div>
            </ul>
          </div>
        </div>
        <h2 id="个人链接">链接须知</h2>
        <p>欢迎互换友链 ^_^ 不过请确定贵站可以正常运营.</p>
        <p>
          我的邮箱是
          {{ configurations.websiteEmail }}
          , 格式要求如下:
        </p>
        <ul>
          <li>网站名称：13blog</li>
          <li>网站链接：http://13blog.site</li>
          <li>网站描述：十三的个人博客</li>
        </ul>

        <p>请保证自己的链接长期有效,不然可能会被清理.</p>
      </main>
    </div>
  </div>
</template>

<style lang="less"></style>
