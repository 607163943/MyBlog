<script setup>
import { ref, createVNode } from 'vue'
import {
  PlusOutlined,
  DeleteOutlined,
  UndoOutlined,
  SearchOutlined,
  EditOutlined,
  StopOutlined,
  CheckOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import {
  articlePageQueryService,
  articleUpdateStatueService,
  articleDeleteService,
  articleBatchDeleteService
} from '@/api/article'
import { categoryAllService } from '@/api/category'
import { tagAllService } from '@/api/tag'

const articleSearchFormRef = ref(null)
const articleSearchForm = ref({
  title: '',
  status: '',
  categoryId: '',
  tagId: ''
})

const usingSearchForm = ref({
  title: '',
  status: '',
  categoryId: '',
  tagId: '',
  pageNum: 1,
  pageSize: 10
})

const categoryList = ref([])
const tagList = ref([])

const getCategoryList = async () => {
  const res = await categoryAllService()
  categoryList.value = res.data.data
}

const getTagList = async () => {
  const res = await tagAllService()
  tagList.value = res.data.data
}
const total = ref(0)

// 搜索
const search = () => {
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...articleSearchForm.value
  }
  pageQuery()
}

// 清空搜索表单
const clearSearchForm = () => {
  articleSearchFormRef.value.resetFields()
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...articleSearchForm.value
  }
  pageQuery()
}

// 处理表格批量删除分类
const handlerBatchDelete = async () => {
  Modal.confirm({
    title: '批量删除文章',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除这些选中文章吗？',
    async onOk() {
      const res = await articleBatchDeleteService(selectedTableRowKeys.value.join(','))
      if (res.data.code === 200) {
        message.success('删除成功')
        // 重置分页
        usingSearchForm.value.pageNum = 1
        usingSearchForm.value.pageSize = 10
        pageQuery()
      }
    },
    onCancel() {}
  })
}

// 表格字段设置
const columns = [
  {
    title: '文章标题',
    dataIndex: 'title',
    key: 'title'
  },
  {
    title: '分类',
    dataIndex: 'categoryName',
    key: 'categoryName'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '标签',
    key: 'tags'
  },
  {
    title: '浏览量',
    dataIndex: 'viewCount',
    key: 'viewCount'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime'
  },
  {
    title: '操作',
    key: 'action',
    width: 280
  }
]

// 表格数据
const tableData = ref([])

const selectedTableRowKeys = ref([])

const onSelectChange = (selectedRowKeys) => {
  selectedTableRowKeys.value = selectedRowKeys
}

// 修改状态
const handlerUpdateStatus = async (record, status) => {
  await articleUpdateStatueService(record.id, status)
  message.success('修改状态成功')
  pageQuery()
}

// 处理表格删除
const handlerDelete = async (record) => {
  Modal.confirm({
    title: '删除文章',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除该文章吗？',
    async onOk() {
      try {
        await articleDeleteService(record.id)
        message.success('删除成功')
        // 重置分页
        usingSearchForm.value.pageNum = 1
        usingSearchForm.value.pageSize = 10
        pageQuery()
      } catch (e) {
        console.log(e)
      }
    },
    onCancel() {}
  })
}

const pageQuery = async () => {
  const res = await articlePageQueryService(usingSearchForm.value)
  let tempTableData = res.data.data.result
  for (let i = 0; i < tempTableData.length; i++) {
    tempTableData[i].key = +tempTableData[i].id
  }
  tableData.value = tempTableData
  total.value = res.data.data.total
}

// 数据请求初始化
getCategoryList()
getTagList()
pageQuery()
</script>

<template>
  <div class="category-container">
    <div class="category-search">
      <a-form
        :model="articleSearchForm"
        ref="articleSearchFormRef"
        layout="inline"
        autocomplete="off"
      >
        <a-form-item label="文章标题" name="title">
          <a-input v-model:value="articleSearchForm.title" placeholder="文章标题" />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="articleSearchForm.status" style="width: 180px">
            <a-select-option :value="0">草稿</a-select-option>
            <a-select-option :value="1">发布</a-select-option>
            <a-select-option :value="2">下线</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="分类" name="categoryId">
          <a-select v-model:value="articleSearchForm.categoryId" style="width: 180px">
            <a-select-option
              v-for="category in categoryList"
              :key="category.id"
              :value="category.id"
            >
              {{ category.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="标签" name="tagId">
          <a-select v-model:value="articleSearchForm.tagId" style="width: 180px">
            <a-select-option v-for="tag in tagList" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div class="category-search-buttons">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button style="margin-left: 12px" @click="clearSearchForm">清空</a-button>
      </div>
    </div>

    <div class="category-buttons">
      <a-button type="primary" ghost @click="$router.push('/admin/article/edit')">
        <template #icon>
          <PlusOutlined />
        </template>
        新增
      </a-button>
      <a-button
        type="primary"
        :disabled="selectedTableRowKeys.length === 0"
        danger
        ghost
        style="margin-left: 12px"
        @click="handlerBatchDelete"
      >
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button style="margin-left: 12px" @click="pageQuery">
        <template #icon>
          <UndoOutlined />
        </template>
        刷新
      </a-button>
    </div>

    <div class="category-table">
      <div class="category-table-content">
        <a-table
          :columns="columns"
          :pagination="false"
          :data-source="tableData"
          :row-selection="{ selectedRowKeys: selectedTableRowKeys, onChange: onSelectChange }"
        >
          <!-- 表格内容 -->
          <template #bodyCell="{ column, record }">
            <!-- 分类 -->
            <template v-if="column.key === 'categoryName'">
              <a-tag color="blue">{{ record.categoryName }}</a-tag>
            </template>
            <!-- 标签 -->
            <template v-if="column.key === 'tags'">
              <a-tag color="blue" v-for="tag in record.tags" :key="tag.id">{{ tag.name }}</a-tag>
            </template>
            <!-- 状态 -->
            <template v-if="column.key === 'status'">
              <a-tag color="default" v-if="record.status === 0">草稿</a-tag>
              <a-tag color="green" v-if="record.status === 1">发布</a-tag>
              <a-tag color="orange" v-if="record.status === 2">下线</a-tag>
            </template>
            <!-- 操作 -->
            <template v-if="column.key === 'action'">
              <span>
                <a-button type="link" @click="$router.push(`/admin/article/preview/${record.id}`)">
                  <template #icon>
                    <EditOutlined />
                  </template>
                  预览
                </a-button>
                <a-button
                  type="link"
                  @click="$router.push(`/admin/article/edit/${record.id}`)"
                  :disabled="record.status === 1"
                >
                  <template #icon>
                    <EditOutlined />
                  </template>
                  编辑
                </a-button>
                <a-button
                  type="link"
                  style="color: #38aa88"
                  v-if="record.status === 0 || record.status === 2"
                  @click="handlerUpdateStatus(record, 1)"
                >
                  <template #icon>
                    <CheckOutlined />
                  </template>
                  发布
                </a-button>
                <a-button
                  type="link"
                  danger
                  v-if="record.status === 1"
                  @click="handlerUpdateStatus(record, 2)"
                >
                  <template #icon>
                    <StopOutlined />
                  </template>
                  下架
                </a-button>
                <a-button type="link" danger @click="handlerDelete(record)">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删除
                </a-button>
              </span>
            </template>
          </template>

          <!-- 空数据 -->
          <template #emptyText>
            <a-empty description="暂无数据" />
          </template>
        </a-table>
      </div>
      <div class="category-table-page">
        <a-pagination
          @change="pageQuery"
          v-model:current="usingSearchForm.pageNum"
          v-model:pageSize="usingSearchForm.pageSize"
          :total="total"
          show-less-items
          :show-total="(total) => `总共 ${total} 条`"
          show-size-changer
          show-quick-jumper
        />
      </div>
    </div>
  </div>
</template>

<style lang="less">
.category-search-buttons {
  margin-top: 10px;
}

.category-buttons {
  margin-top: 12px;
}

.category-table {
  margin-top: 12px;

  .category-table-page {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
}
</style>
