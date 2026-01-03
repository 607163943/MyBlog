<script setup>
import { ref, createVNode } from 'vue'
import {
  PlusOutlined,
  DeleteOutlined,
  UndoOutlined,
  SearchOutlined,
  EditOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import DictValueDialog from './dialog.vue'
import { dictDeleteService, dictBatchDeleteService, dictAllService } from '@/api/dict'
import { dictValuePageQueryService } from '@/api/dict-value'
import { useRoute } from 'vue-router'

const route = useRoute()

const dictValueSearchFormRef = ref(null)
const dictList = ref([])

const getAllDict = async () => {
  const res = await dictAllService()
  if (res.data.code === 200) {
    dictList.value = res.data.data
  }
}
const dictValueSearchForm = ref({
  label: '',
  dictId: Number.parseInt(route.params.dictId)
})

const usingSearchForm = ref({
  label: '',
  dictId: Number.parseInt(route.params.dictId),
  pageNum: 1,
  pageSize: 10
})

const total = ref(0)

// 搜索
const search = () => {
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...dictValueSearchForm.value
  }
  pageQuery()
}

// 清空搜索表单
const clearSearchForm = () => {
  dictValueSearchFormRef.value.resetFields()
  usingSearchForm.value = {
    ...usingSearchForm.value,
    ...dictValueSearchForm.value
  }
  pageQuery()
}

// 处理表格批量删除字典
const handlerDictBatchDelete = async () => {
  Modal.confirm({
    title: '批量删除字典',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除这些选中字典吗？',
    async onOk() {
      const res = await dictBatchDeleteService(selectedTableRowKeys.value.join(','))
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
    title: '字典内容值',
    dataIndex: 'value',
    key: 'value'
  },
  {
    title: '字典内容标签',
    dataIndex: 'label',
    key: 'label'
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

// 处理表格删除字典
const handlerDictDelete = async (record) => {
  Modal.confirm({
    title: '删除字典',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要删除该字典吗？',
    async onOk() {
      const res = await dictDeleteService(record.id)
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
  const res = await dictValuePageQueryService(usingSearchForm.value)
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

getAllDict()
pageQuery()
</script>

<template>
  <div class="dict-value-container">
    <div class="dict-value-search">
      <a-form
        :model="dictValueSearchForm"
        ref="dictValueSearchFormRef"
        layout="inline"
        autocomplete="off"
      >
        <a-form-item label="字典内容标签" name="label">
          <a-input v-model:value="dictValueSearchForm.label" placeholder="字典内容标签" />
        </a-form-item>

        <a-form-item label="字典类型" name="dictId">
          <a-select v-model:value="dictValueSearchForm.dictId" style="width: 180px">
            <a-select-option v-for="dict in dictList" :key="dict.id" :value="dict.id">{{
              dict.dictType
            }}</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div class="dict-value-search-buttons">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button style="margin-left: 12px" @click="clearSearchForm">清空</a-button>
      </div>
    </div>

    <div class="dict-value-buttons">
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

    <div class="dict-value-table">
      <div class="dict-value-table-content">
        <a-table
          :columns="columns"
          :pagination="false"
          :data-source="tableData"
          :row-selection="{ selectedRowKeys: selectedTableRowKeys, onChange: onSelectChange }"
        >
          <!-- 表格内容 -->
          <template #bodyCell="{ column, record }">
            <!-- 操作 -->
            <template v-if="column.key === 'action'">
              <span>
                <a-button type="link" @click="dictDialogRef.openDialog(record)">
                  <template #icon>
                    <EditOutlined />
                  </template>
                  编辑
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
      <div class="dict-value-table-page">
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

    <DictValueDialog ref="dictDialogRef" @success="handleDictSuccess" />
  </div>
</template>

<style lang="less">
.dict-value-search-buttons {
  margin-top: 10px;
}

.dict-value-buttons {
  margin-top: 12px;
}

.dict-value-table {
  margin-top: 12px;

  .dict-value-table-page {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
  }
}
</style>
