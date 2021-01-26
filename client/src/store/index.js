import Vue from 'vue';
import Vuex from 'vuex';
import createMultiTabState from 'vuex-multi-tab-state';
import apiStore from './apiStore';
import authStore from './authStore';
import wsStore from './wsStore';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: { apiStore, authStore, wsStore },
  plugins: [
    createMultiTabState({
      statesPaths: [
        'authStore.message',
        'authStore.messageVariant',
        'authStore.authenticated',
        'authStore.assistant',
      ],
    }),
  ],
});
