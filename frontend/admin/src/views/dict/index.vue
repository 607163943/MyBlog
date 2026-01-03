<script setup>
import { ref } from 'vue'
import {
  PlusOutlined,
  DeleteOutlined,
  UndoOutlined,
  SearchOutlined,
  EditOutlined,
  StopOutlined,
  CheckOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import DictDialog from './dialog.vue'
import {
  dictPageQueryService,
  dictUpdateStatueService,
  dictDeleteService,
  dictBatchDeleteService
} from '@/api/dict'

const dictSearchFormRef = ref(null)
const dictSearchForm = ref({
  dictType: '',
  status: ''
})

const usingSearchForm = ref({
  dictType: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const total = ref(0)

// 搜索
const search = () => {
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...dictSearchForm.value
  }
  pageQuery()
}

// 清空搜索表单
const clearSearchForm = () => {
  dictSearchFormRef.value.resetFields()
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...dictSearchForm.value
  }
  pageQuery()
}

// 处理表格批量删除字典
const handlerDictBatchDelete = async () => {
  const res = await dictBatchDeleteService(selectedTableRowKeys.value.join(','))
  if (res.data.code === 200) {
    message.success('删除成功')
    // 重置分页
    usingSearchForm.value.pageNum = 1
    usingSearchForm.value.pageSize = 10
    pageQuery()
  }
}

// 表格字段设置
const columns = [
  {
    title: '字典类型',
    dataIndex: 'dictType',
    key: 'dictType'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime'
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

const handleDictTypeClick = (record) => {
  console.log(record)
}

// 修改字典状态
const handlerDictUpdateStatus = async (record) => {
  const res = await dictUpdateStatueService(record.id)
  if (res.data.code === 200) {
    message.success('修改状态成功')
    pageQuery()
  }
}

// 处理表格删除字典
const handlerDictDelete = async (record) => {
  const res = await dictDeleteService(record.id)
  if (res.data.code === 200) {
    message.success('删除成功')
    // 重置分页
    usingSearchForm.value.pageNum = 1
    usingSearchForm.value.pageSize = 10
    pageQuery()
  }
}

const pageQuery = async () => {
  const res = await dictPageQueryService(usingSearchForm.value)
  let tempTableData = res.data.data.result
  for (let i = 0; i < tempTableData.length; i++) {
    tempTableData[i].key = +tempTableData[i].id
  }
  tableData.value = tempTableData
  total.value = res.data.data.total
}

const dictDialogRef = ref(null)

const handleDictSuccess = () => {
  clearSearchForm()
}

pageQuery()
</script>

<template>
  <div class="dict-container">
    <div class="dict-search">
      <a-form :model="dictSearchForm" ref="dictSearchFormRef" layout="inline" autocomplete="off">
        <a-form-item label="字典类型" name="dictType">
          <a-input v-model:value="dictSearchForm.dictType" placeholder="字典类型" />
        </a-form-item>

        <a-form-item label="状态" name="status">
          <a-select v-model:value="dictSearchForm.status" style="width: 180px">
            <a-select-option value="0">启用</a-select-option>
            <a-select-option value="1">禁用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div class="dict-search-buttons">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button style="margin-left: 12px" @click="clearSearchForm">清空</a-button>
      </div>
    </div>

    <div class="dict-buttons">
      <a-button type="primary" ghost @click="dictDialogRef.openDialog(null)">
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

    <div class="dict-table">
      <div class="dict-table-content">
        <a-table
          :columns="columns"
          :pagination="false"
          :data-source="tableData"
          :row-selection="{ selectedRowKeys: selectedTableRowKeys, onChange: onSelectChange }"
        >
          <!-- 表格内容 -->
          <template #bodyCell="{ column, record }">
            <!-- 字典类型 -->
            <template v-if="column.key === 'dictType'">
              <a-button type="link" @click="handleDictTypeClick(record)">{{
                record.dictType
              }}</a-button>
            </template>
            <!-- 状态 -->
            <template v-if="column.key === 'status'">
              <a-tag :color="record.status === 0 ? 'green' : 'red'">
                {{ record.status === 0 ? '启用' : '禁用' }}
              </a-tag>
            </template>
            <!-- 操作 -->
            <template v-if="column.key === 'action'">
              <span>
                <a-button type="link" @click="dictDialogRef.openDialog(record)">
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
      <div class="dict-table-page">
        <a-pagination
          v-model:current="usingSearchForm.pageNum"
          v-model:pageSize="usingSearchForm.pageSize"
          :total="total"
          :show-total="(total) => `总共 ${total} 条`"
          show-less-items
          show-size-changer
          show-quick-jumper
        />
      </div>
    </div>

    <DictDialog ref="dictDialogRef" @success="handleDictSuccess" />
  </div>
</template>

<style lang="less">
.dict-search-buttons {
  margin-top: 10px;
}

.dict-buttons {
  margin-top: 12px;
}

.dict-table {
  margin-top: 12px;

  .dict-table-page {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
}
</style>
