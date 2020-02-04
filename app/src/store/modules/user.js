import { login, logout, getInfo } from '@/api/sys/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  id: '',
  username: '',
  name: '',
  email: '',
  avatar: '',
  roles: [],
  permissions: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, user) => {
    state.id = user.id
    state.username = user.username
    state.name = user.name
    state.email = user.email
    state.avatar = user.avatar
    state.roles = user.roles
    state.permissions = user.permissions
  },
  CLEAR_USER: () => {
    state.token = null
    state.id = -1
    state.username = null
    state.name = null
    state.email = null
    state.avatar = null
    state.roles = []
    state.permissions = []
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          reject('检验异常，请重新登录一次。')
        }

        const { permissions } = data

        // roles must be a non-empty array
        if (!permissions || permissions.length <= 0) {
          reject('用户角色为空，请重新设置')
        }

        commit('SET_USER', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('CLEAR_USER')
        removeToken()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 前端 登出
  FedLogout({ commit }) {
    return new Promise(resolve => {
      commit('CLEAR_USER')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
