import { createApp } from 'vue'
import pinia from './stores'
import App from './App.vue'
import router from './router'
import '@/utils/time'
import 'ant-design-vue/dist/reset.css'
import './styles/base.less'

const app = createApp(App)

app.use(pinia)
app.use(router)

app.mount('#app')
