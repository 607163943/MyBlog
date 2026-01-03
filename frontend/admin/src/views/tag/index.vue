<script setup>
import { ref } from 'vue'

const tagSearchForm = ref({
  name: '',
  status: ''
})
const handleFinish = (values) => {
  console.log(values, tagSearchForm)
}
const handleFinishFailed = (errors) => {
  console.log(errors)
}
</script>

<template>
  <div class="tag-container">
    <!-- 搜索 -->
    <div class="search-container">
      <a-form
        layout="inline"
        :model="tagSearchForm"
        @finish="handleFinish"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item label="标签名" name="name">
          <a-input v-model:value="tagSearchForm.name" placeholder="标签名"></a-input>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="tagSearchForm.status" style="width: 120px">
            <a-select-option value="jack">Jack</a-select-option>
            <a-select-option value="lucy">Lucy</a-select-option>
            <a-select-option value="disabled" disabled>Disabled</a-select-option>
            <a-select-option value="Yiminghe">yiminghe</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            :disabled="tagSearchForm.name === '' && tagSearchForm.status === ''"
          >
            Log in
          </a-button>
        </a-form-item>
      </a-form>
    </div>
    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">标签管理</h3>
          </div>
          <!-- /.card-body -->
          <div class="card-body">
            <div class="grid-btn">
              <input
                type="text"
                class="form-control col-1"
                id="tagName"
                name="tagName"
                placeholder="标签名称"
                required="true"
              />&nbsp;&nbsp;&nbsp;
              <button class="btn btn-info" onclick="tagAdd()">
                <i class="fa fa-plus"></i>&nbsp;新增
              </button>
              <button class="btn btn-danger" onclick="deleteTag()">
                <i class="fa fa-trash-o"></i>&nbsp;删除
              </button>
            </div>
            <table id="jqGrid" class="table table-bordered"></table>
            <div id="jqGridPager"></div>
          </div>
          <!-- /.card-body -->
        </div>
      </div>
      <!-- /.container-fluid -->
    </div>
  </div>
</template>

<style lang="scss"></style>
