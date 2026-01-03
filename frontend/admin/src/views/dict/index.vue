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
import DictDialog from './dialog.vue'
import { dictPageQueryService } from '@/api/dict'

const dictSearchForm = ref({
  dictType: '',
  status: ''
})

const search = (values) => {
  console.log('Success:', values)
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

const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
  },
  getCheckboxProps: (record) => ({
    name: record.name
  })
}

const handleDictTypeClick = (record) => {
  console.log(record)
}

const dictDialogRef = ref(null)

const handleDictSuccess = () => {
  console.log('handleDictSuccess')
}

const pageQuery = async () => {
  const res = await dictPageQueryService()
  tableData.value = res.data.data.result
}

pageQuery()
</script>

<template>
  <div class="dict-container">
    <div class="dict-search">
      <a-form :model="dictSearchForm" name="horizontal_login" layout="inline" autocomplete="off">
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
        <a-button type="primary">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button style="margin-left: 12px">清空</a-button>
      </div>
    </div>

    <div class="dict-buttons">
      <a-button type="primary" ghost @click="dictDialogRef.openDialog(null)">
        <template #icon>
          <PlusOutlined />
        </template>
        新增
      </a-button>
      <a-button type="primary" danger ghost style="margin-left: 12px">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button style="margin-left: 12px">
        <template #icon>
          <UndoOutlined />
        </template>
        刷新
      </a-button>
    </div>

    <div class="dict-table">
      <a-table :columns="columns" :data-source="tableData" :row-selection="rowSelection">
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
              <a-button type="link" danger v-if="record.status === 0">
                <template #icon>
                  <StopOutlined />
                </template>
                禁用
              </a-button>
              <a-button type="link" v-else style="color: #38aa88">
                <template #icon>
                  <CheckOutlined />
                </template>
                启用
              </a-button>
              <a-button type="link" danger>
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
}
</style>
