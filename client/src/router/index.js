import Vue from 'vue';
import VueRouter from 'vue-router';
import AdminView from '../views/Admin.vue';
import TimesView from '../views/Times.vue';
import LoginView from '../views/Login.vue';
import store from '../store';

Vue.use(VueRouter);

const routes = [
  { path: '/', redirect: '/times' },
  { path: '/admin', component: AdminView },
  { path: '/times', component: TimesView },
  { path: '/login', component: LoginView },
];

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  if (from.path === '/login') {
    store.commit('resetAuthMessage');
  }

  if (store.getters.isAuthenticated || to.path === '/login' || to.path === '/times') {
    next();
  } else {
    next('/login');
  }
});

export default router;
