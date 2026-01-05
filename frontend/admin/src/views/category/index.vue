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
import CategoryDialog from './dialog.vue'
import {
  categoryPageQueryService,
  categoryUpdateStatueService,
  categoryDeleteService,
  categoryBatchDeleteService
} from '@/api/category'

const categorySearchFormRef = ref(null)
const categorySearchForm = ref({
  name: '',
  status: '',
  isAsc: 0
})

const usingSearchForm = ref({
  name: '',
  status: '',
  isAsc: 0,
  pageNum: 1,
  pageSize: 10
})

const total = ref(0)

// 搜索
const search = () => {
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...categorySearchForm.value
  }
  pageQuery()
}

// 清空搜索表单
const clearSearchForm = () => {
  categorySearchFormRef.value.resetFields()
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...categorySearchForm.value
  }
  pageQuery()
}

// 处理表格批量删除分类
const handlerBatchDelete = async () => {
  Modal.confirm({
    title: '批量删除分类',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除这些选中分类吗？',
    async onOk() {
      const res = await categoryBatchDeleteService(selectedTableRowKeys.value.join(','))
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
    title: '分类名称',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '排序',
    dataIndex: 'sort',
    key: 'sort'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '文章数(总/发布)',
    key: 'articleCount'
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
const handlerUpdateStatus = async (record) => {
  const res = await categoryUpdateStatueService(record.id)
  if (res.data.code === 200) {
    message.success('修改状态成功')
    pageQuery()
  }
}

// 处理表格删除
const handlerDelete = async (record) => {
  Modal.confirm({
    title: '删除分类',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除该分类吗？',
    async onOk() {
      const res = await categoryDeleteService(record.id)
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

const pageQuery = async () => {
  const res = await categoryPageQueryService(usingSearchForm.value)
  let tempTableData = res.data.data.result
  for (let i = 0; i < tempTableData.length; i++) {
    tempTableData[i].key = +tempTableData[i].id
  }
  tableData.value = tempTableData
  total.value = res.data.data.total
}

const categoryDialogRef = ref(null)

const handleSuccess = () => {
  clearSearchForm()
}

pageQuery()
</script>

<template>
  <div class="category-container">
    <div class="category-search">
      <a-form
        :model="categorySearchForm"
        ref="categorySearchFormRef"
        layout="inline"
        autocomplete="off"
      >
        <a-form-item label="分类名称" name="name">
          <a-input v-model:value="categorySearchForm.name" placeholder="分类名称" />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="categorySearchForm.status" style="width: 180px">
            <a-select-option :value="0">启用</a-select-option>
            <a-select-option :value="1">禁用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="排序" name="isAsc">
          <a-select v-model:value="categorySearchForm.isAsc" style="width: 180px">
            <a-select-option :value="0">升序</a-select-option>
            <a-select-option :value="1">降序</a-select-option>
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
      <a-button type="primary" ghost @click="categoryDialogRef.openDialog(null)">
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
            <!-- 状态 -->
            <template v-if="column.key === 'status'">
              <a-tag :color="record.status === 0 ? 'green' : 'red'">
                {{ record.status === 0 ? '启用' : '禁用' }}
              </a-tag>
            </template>
            <!-- 文章数 -->
            <template v-if="column.key === 'articleCount'">
              <span>{{ record.articleCount }} / {{ record.publishedArticleCount }}</span>
            </template>
            <!-- 操作 -->
            <template v-if="column.key === 'action'">
              <span>
                <a-button type="link" @click="categoryDialogRef.openDialog(record)">
                  <template #icon>
                    <EditOutlined />
                  </template>
                  编辑
                </a-button>
                <a-button
                  type="link"
                  danger
                  v-if="record.status === 0"
                  @click="handlerUpdateStatus(record)"
                >
                  <template #icon>
                    <StopOutlined />
                  </template>
                  禁用
                </a-button>
                <a-button
                  type="link"
                  v-else
                  style="color: #38aa88"
                  @click="handlerUpdateStatus(record)"
                >
                  <template #icon>
                    <CheckOutlined />
                  </template>
                  启用
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

    <CategoryDialog ref="categoryDialogRef" @success="handleSuccess" />
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
