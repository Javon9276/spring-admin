import Layout from '@/layout'

const sysRouter = {
  path: '/sys',
  component: Layout,
  redirect: 'noRedirect',
  alwaysShow: true, // will always show the root menu
  name: 'sys',
  meta: {
    title: '系统管理',
    icon: 'cog',
    permission: ['sys:user', 'sys:role', 'sys:permission']
  },
  children: [
    {
      path: '/user',
      component: () => import('@/views/sys/user/index'),
      name: 'user',
      meta: {
        title: '用户管理',
        icon: 'user',
        permission: ['sys:user']
      }
    },
    {
      path: '/role',
      component: () => import('@/views/sys/role/index'),
      name: 'role',
      meta: {
        title: '角色管理',
        icon: 'role',
        permission: ['sys:role']
      }
    },
    {
      path: '/permission',
      component: () => import('@/views/sys/permission/index'),
      name: 'permission',
      meta: {
        title: '权限管理',
        icon: 'permission',
        permission: ['sys:permission']
      }
    }
  ]
}
export default sysRouter
