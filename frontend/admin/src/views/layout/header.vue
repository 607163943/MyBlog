<script setup>
import { defineOptions, createVNode } from 'vue'
import { UserOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { userLogoutService } from '@/api/user'
import { Modal } from 'ant-design-vue'
import { useUserStore } from '@/stores'
defineOptions({
  name: 'LayoutHeader'
})

const userStore = useUserStore()
const router = useRouter()

const onClick = ({ key }) => {
  if (key === 'profile') {
    router.push('/admin/profile')
  } else if (key === 'logout') {
    Modal.confirm({
      title: '确定退出',
      icon: createVNode(ExclamationCircleOutlined),
      content: '你确定要退出吗？',
      async onOk() {
        const res = await userLogoutService()
        if (res.data.code === 200) {
          // 清除令牌和用户信息
          userStore.setToken('')
          userStore.setUserInfo(null)

          // 跳转到登录页面
          router.push('/login')
        }
      },
      onCancel() {}
    })
  }
}
</script>

<template>
  <div class="header-container">
    <a-dropdown>
      <a class="ant-dropdown-link" @click.prevent>
        <div class="user-info">
          <div class="user-info-avatar">
            <a-avatar size="large" style="background-color: #87d068">
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
          </div>
          <div class="user-info-name">
            <span>{{ userStore.userInfo.nickname }}</span>
          </div>
        </div>
      </a>
      <template #overlay>
        <a-menu @click="onClick">
          <a-menu-item key="profile">个人中心</a-menu-item>
          <a-menu-item key="logout">退出登录</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>
</template>

<style lang="less" scoped>
.header-container {
  display: flex;
  justify-content: flex-end;
  padding: 0 48px;

  .user-info {
    display: flex;
    align-items: center;

    .user-info-avatar {
      margin-right: 4px;
    }

    .user-info-name {
      font-size: 18px;
      color: #333;
    }
  }
}
</style>
