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
import TagDialog from './dialog.vue'
import {
  tagPageQueryService,
  tagUpdateStatueService,
  tagDeleteService,
  tagBatchDeleteService
} from '@/api/tag'

const tagSearchFormRef = ref(null)
const tagSearchForm = ref({
  name: '',
  status: ''
})

const usingSearchForm = ref({
  name: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const total = ref(0)

// 搜索
const search = () => {
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...tagSearchForm.value
  }
  pageQuery()
}

// 清空搜索表单
const clearSearchForm = () => {
  tagSearchFormRef.value.resetFields()
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...tagSearchForm.value
  }
  pageQuery()
}

// 处理表格批量删除标签
const handlerDictBatchDelete = async () => {
  Modal.confirm({
    title: '批量删除标签',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除这些选中标签吗？',
    async onOk() {
      const res = await tagBatchDeleteService(selectedTableRowKeys.value.join(','))
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
    title: '标签名称',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
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

// 修改标签状态
const handlerDictUpdateStatus = async (record) => {
  const res = await tagUpdateStatueService(record.id)
  if (res.data.code === 200) {
    message.success('修改状态成功')
    pageQuery()
  }
}

// 处理表格删除标签
const handlerDictDelete = async (record) => {
  Modal.confirm({
    title: '删除标签',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除该标签吗？',
    async onOk() {
      const res = await tagDeleteService(record.id)
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
  const res = await tagPageQueryService(usingSearchForm.value)
  let tempTableData = res.data.data.result
  for (let i = 0; i < tempTableData.length; i++) {
    tempTableData[i].key = +tempTableData[i].id
  }
  tableData.value = tempTableData
  total.value = res.data.data.total
}

const tagDialogRef = ref(null)

const handleTagSuccess = () => {
  clearSearchForm()
}

pageQuery()
</script>

<template>
  <div class="tag-container">
    <div class="tag-search">
      <a-form :model="tagSearchForm" ref="tagSearchFormRef" layout="inline" autocomplete="off">
        <a-form-item label="标签名称" name="name">
          <a-input v-model:value="tagSearchForm.name" placeholder="标签名称" />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="tagSearchForm.status" style="width: 180px">
            <a-select-option value="0">启用</a-select-option>
            <a-select-option value="1">禁用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div class="tag-search-buttons">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button style="margin-left: 12px" @click="clearSearchForm">清空</a-button>
      </div>
    </div>

    <div class="tag-buttons">
      <a-button type="primary" ghost @click="tagDialogRef.openDialog(null)">
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
        @click="handlerDictBatchDelete"
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

    <div class="tag-table">
      <div class="tag-table-content">
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
            <!-- 操作 -->
            <template v-if="column.key === 'action'">
              <span>
                <a-button type="link" @click="tagDialogRef.openDialog(record)">
                  <template #icon>
                    <EditOutlined />
                  </template>
                  编辑
                </a-button>
                <a-button
                  type="link"
                  danger
                  v-if="record.status === 0"
                  @click="handlerDictUpdateStatus(record)"
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
                  @click="handlerDictUpdateStatus(record)"
                >
                  <template #icon>
                    <CheckOutlined />
                  </template>
                  启用
                </a-button>
                <a-button type="link" danger @click="handlerDictDelete(record)">
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
      <div class="tag-table-page">
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

    <TagDialog ref="tagDialogRef" @success="handleTagSuccess" />
  </div>
</template>

<style lang="less">
.tag-search-buttons {
  margin-top: 10px;
}

.tag-buttons {
  margin-top: 12px;
}

.tag-table {
  margin-top: 12px;

  .tag-table-page {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
}
</style>
