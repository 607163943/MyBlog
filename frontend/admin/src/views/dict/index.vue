<script setup>
import { ref } from 'vue'
import {
  PlusOutlined,
  DeleteOutlined,
  UndoOutlined,
  SearchOutlined,
  EditOutlined,
  SmileOutlined
} from '@ant-design/icons-vue'

const dictSearchForm = ref({
  dictType: '',
  status: ''
})

const search = (values) => {
  console.log('Success:', values)
}

const columns = [
  {
    name: 'Name',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: 'Age',
    dataIndex: 'age',
    key: 'age'
  },
  {
    title: 'Address',
    dataIndex: 'address',
    key: 'address'
  },
  {
    title: 'Tags',
    key: 'tags',
    dataIndex: 'tags'
  },
  {
    title: 'Action',
    key: 'action'
  }
]

const data = [
  {
    key: '1',
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park',
    tags: ['nice', 'developer']
  },
  {
    key: '2',
    name: 'Jim Green',
    age: 42,
    address: 'London No. 1 Lake Park',
    tags: ['loser']
  },
  {
    key: '3',
    name: 'Joe Black',
    age: 32,
    address: 'Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher']
  }
]

const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
  },
  getCheckboxProps: (record) => ({
    name: record.name
  })
}
</script>

<template>
  <div class="dict-container">
    <div class="dict-search">
      <a-form
        :model="dictSearchForm"
        name="horizontal_login"
        layout="inline"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
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
      <a-button type="primary" ghost>
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
      <div class="dict-table-content">
        <a-table :columns="columns" :data-source="data" :row-selection="rowSelection">
          <template #headerCell="{ column }">
            <template v-if="column.key === 'name'">
              <span>
                <smile-outlined />
                Name
              </span>
            </template>
          </template>

          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'name'">
              <a>
                {{ record.name }}
              </a>
            </template>
            <template v-else-if="column.key === 'tags'">
              <span>
                <a-tag
                  v-for="tag in record.tags"
                  :key="tag"
                  :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
                >
                  {{ tag.toUpperCase() }}
                </a-tag>
              </span>
            </template>
            <template v-else-if="column.key === 'action'">
              <span>
                <a-button type="link">内容管理</a-button>
                <a-button type="link">
                  <template #icon>
                    <EditOutlined />
                  </template>
                  编辑
                </a-button>
                <a-button type="link" danger>禁用</a-button>
                <a-button type="link" danger>
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删除
                </a-button>
              </span>
            </template>
          </template>
        </a-table>
      </div>
      <div class="dict-table-page"></div>
    </div>
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
