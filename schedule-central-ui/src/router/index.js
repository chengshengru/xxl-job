import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: '/job-info',
        name: 'JobInfo',
        component: () => import('@/views/JobInfo.vue')
      },
      {
        path: '/job-group',
        name: 'JobGroup',
        component: () => import('@/views/JobGroup.vue')
      },
      {
        path: '/job-log',
        name: 'JobLog',
        component: () => import('@/views/JobLog.vue')
      },
      {
        path: '/user-manage',
        name: 'UserManage',
        component: () => import('@/views/UserManage.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
