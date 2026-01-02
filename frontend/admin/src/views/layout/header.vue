<script setup>
import { defineOptions } from 'vue'
import { UserOutlined } from '@ant-design/icons-vue'
defineOptions({
  name: 'LayoutHeader'
})
</script>

<template>
  <div class="header-container">
    <div class="user-info">
      <div class="user-info-avatar">
        <a-avatar size="large" style="background-color: #87d068">
          <template #icon>
            <UserOutlined />
          </template>
        </a-avatar>
      </div>
      <div class="user-info-name">
        <span>admin</span>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.header-container {
  display: flex;
  justify-content: flex-end;

  .user-info {
    display: flex;
    align-items: center;

    .user-info-avatar {
      margin-right: 4px;
    }

    .user-info-name {
      margin-right: 48px;
      font-size: 18px;
      color: #333;
    }
  }
}
</style>
