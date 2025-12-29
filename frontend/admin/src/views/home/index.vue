<script setup>
import { homeGetTotalService } from '@/api/home'
import { ref } from 'vue'

const blogCount = ref(0)
const commentCount = ref(0)
const categoryCount = ref(0)
const tagCount = ref(0)
const linkCount = ref(0)

const getTotal = async () => {
  const res = await homeGetTotalService()
  blogCount.value = res.data.data.blogCount
  commentCount.value = res.data.data.commentCount
  categoryCount.value = res.data.data.categoryCount
  tagCount.value = res.data.data.tagCount
  linkCount.value = res.data.data.linkCount
}

getTotal()
</script>

<template>
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid"></div>
      <!-- /.container-fluid -->
    </div>
    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="card-header">
          <h3 class="card-title">管理首页</h3>
        </div>
        <!-- /.card-body -->
        <div class="row" style="margin-top: 40px; border-top: 0px">
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-info">
              <div class="inner">
                <h3>新增博客</h3>

                <p>记心中所想</p>
              </div>
              <div class="icon">
                <i class="fa fa-pencil-square-o"></i>
              </div>
              <a href="/admin/blogs/edit" class="small-box-footer"
                >let's go <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-success">
              <div class="inner">
                <h3>{{ blogCount }}</h3>

                <p>总文章数</p>
              </div>
              <div class="icon">
                <i class="fa fa-list-alt"></i>
              </div>
              <a href="/admin/blogs" class="small-box-footer"
                >More info <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-primary">
              <div class="inner">
                <h3>{{ commentCount }}</h3>

                <p>收到评论数</p>
              </div>
              <div class="icon">
                <i class="fa fa-comments"></i>
              </div>
              <a href="/admin/comments" class="small-box-footer"
                >More info <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-warning">
              <div class="inner">
                <h3>{{ categoryCount }}</h3>

                <p>分类数量</p>
              </div>
              <div class="icon">
                <i class="fa fa-bookmark"></i>
              </div>
              <a href="/admin/categories" class="small-box-footer"
                >More info <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-danger">
              <div class="inner">
                <h3>{{ tagCount }}</h3>

                <p>标签总量</p>
              </div>
              <div class="icon">
                <i class="fa fa-tags"></i>
              </div>
              <a href="/admin/tags" class="small-box-footer"
                >More info <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-4 col-6">
            <!-- small box -->
            <div class="small-box bg-dark">
              <div class="inner">
                <h3>{{ linkCount }}</h3>

                <p>友情链接</p>
              </div>
              <div class="icon">
                <i class="fa fa-heart"></i>
              </div>
              <a href="/admin/links" class="small-box-footer"
                >More info <i class="fa fa-arrow-circle-right"></i
              ></a>
            </div>
          </div>
          <!-- ./col -->
        </div>
      </div>
      <!-- /.container-fluid -->
    </div>
    <!-- /.content -->
  </div>
</template>

<style scoped></style>
