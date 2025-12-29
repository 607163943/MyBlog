<script setup>
import { ref } from 'vue'

const blog = ref({
  blogId: 0,
  blogTitle: '',
  blogTags: '',
  blogSubUrl: '',
  blogCategoryId: 0,
  blogContent: '',
  blogCoverImage: '',
  blogStatus: 1,
  enableComment: 0
})

const categories = ref([])
</script>

<template>
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid"></div>
      <!-- /.container-fluid -->
    </div>
    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">发布文章</h3>
          </div>
          <div class="card-body">
            <!-- 几个基础的输入框，名称、分类等输入框 -->
            <form id="blogForm" onsubmit="return false">
              <div class="form-group" style="display: flex">
                <input type="hidden" id="blogId" name="blogId" v-model="blog.blogId" />
                <input
                  type="text"
                  class="form-control col-sm-6"
                  id="blogName"
                  name="blogName"
                  placeholder="*请输入文章标题(必填)"
                  v-model="blog.blogTitle"
                  required="true"
                />
                &nbsp;&nbsp;
                <input
                  type="text"
                  class="form-control"
                  id="blogTags"
                  name="blogTags"
                  placeholder="请输入文章标签"
                  v-model="blog.blogTags"
                  style="width: 100%"
                />
              </div>
              <div class="form-group" style="display: flex">
                <input
                  type="text"
                  class="form-control col-sm-6"
                  id="blogSubUrl"
                  name="blogSubUrl"
                  v-model="blog.blogSubUrl"
                  placeholder="请输入自定义路径,如:springboot-mybatis,默认为id"
                />
                &nbsp;&nbsp;
                <select
                  class="form-control select2"
                  style="width: 100%"
                  id="blogCategoryId"
                  data-placeholder="请选择分类..."
                  v-model="blog.blogCategoryId"
                >
                  <div v-if="null == categories">
                    <option value="0" selected="selected">默认分类</option>
                  </div>
                  <div v-else>
                    <option
                      v-for="category in categories"
                      :key="category.categoryId"
                      :value="category.categoryId"
                      :selected="blog.blogCategoryId == category.categoryId"
                    >
                      {{ category.categoryName }}
                    </option>
                  </div>
                </select>
              </div>
              <div class="form-group" id="blog-editormd">
                <textarea style="display: none" v-model="blog.blogContent"></textarea>
              </div>
              <div class="form-group">
                <!-- 按钮 -->
                &nbsp;<button
                  class="btn btn-info float-right"
                  style="margin-left: 5px"
                  id="confirmButton"
                >
                  保存文章</button
                >&nbsp; &nbsp;<button
                  class="btn btn-secondary float-right"
                  style="margin-left: 5px"
                  id="cancelButton"
                >
                  返回文章列表</button
                >&nbsp;
              </div>
            </form>
          </div>
        </div>
      </div>
      <!-- /.container-fluid -->
    </div>
    <div class="content">
      <!-- 模态框（Modal） -->
      <div
        class="modal fade"
        id="articleModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="articleModalLabel"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h6 class="modal-title" id="articleModalLabel">信息完善</h6>
            </div>
            <div class="modal-body">
              <form onsubmit="return false">
                <div class="form-group">
                  <div class="col-sm-4">
                    <img
                      v-if="null == blog"
                      id="blogCoverImage"
                      src="../../../public/dist/img/img-upload.png"
                      style="height: 64px; width: 64px"
                    />
                    <img
                      v-else
                      id="blogCoverImage"
                      :src="blog.blogCoverImage"
                      style="width: 160px; height: 120px; display: block"
                    />
                  </div>
                </div>
                <br />
                <div class="form-group">
                  <div class="col-sm-4">
                    <button class="btn btn-info" style="margin-bottom: 5px" id="uploadCoverImage">
                      <i class="fa fa-picture-o"></i>&nbsp;上传封面
                    </button>
                    <button
                      class="btn btn-secondary"
                      style="margin-bottom: 5px"
                      id="randomCoverImage"
                    >
                      <i class="fa fa-random"></i>&nbsp;随机封面
                    </button>
                  </div>
                </div>
                <div class="form-group">
                  <label class="control-label">文章状态:&nbsp;</label>
                  <input
                    name="blogStatus"
                    type="radio"
                    id="publish"
                    checked="true"
                    v-model="blog.blogStatus"
                    value="1"
                  />&nbsp;发布&nbsp;
                  <input
                    name="blogStatus"
                    type="radio"
                    id="draft"
                    value="0"
                    v-model="blog.blogStatus"
                  />&nbsp;草稿&nbsp;
                </div>
                <div class="form-group">
                  <label class="control-label">是否允许评论:&nbsp;</label>
                  <input
                    name="enableComment"
                    type="radio"
                    id="enableCommentTrue"
                    checked="true"
                    v-model="blog.enableComment"
                    value="0"
                  />&nbsp;是&nbsp;
                  <input
                    name="enableComment"
                    type="radio"
                    id="enableCommentFalse"
                    value="1"
                    v-model="blog.enableComment"
                  />&nbsp;否&nbsp;
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button type="button" class="btn btn-primary" id="saveButton">确认</button>
            </div>
          </div>
        </div>
      </div>
      <!-- /.modal -->
    </div>
  </div>
</template>

<style scoped></style>
