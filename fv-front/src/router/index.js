import Vue from 'vue'
import VueRouter from 'vue-router'
import UserView from '../views/User.vue'
import LoginView from "../views/Login";
import PersonInfo from "../views/PersonInfo";
import Role from "../views/Role";
import Menu from "../views/Menu";
import Single from "../views/Single";
import Multiple from "../views/Multiple";
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    redirect: '/user'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/user',
    name: 'user',
    component: UserView
  },
  {
    path: '/personInfo',
    name:'personInfo',
    component: PersonInfo
  },
  {
    path: '/role',
    name:'role',
    component: Role
  },
  {
    path: '/menu',
    name:'menu',
    component: Menu
  },
  {
    path: '/single',
    name:'single',
    component: Single
  },
  {
    path: '/multiple',
    name:'multiple',
    component: Multiple
  }


]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  linkActiveClass:'active',
  routes
})

export default router
