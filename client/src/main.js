import Vue from 'vue';
import VueCookies from 'vue-cookies';
import {
  BootstrapVue, AlertPlugin, FormTimepickerPlugin, IconsPlugin, ListGroupPlugin, ModalPlugin,
  NavbarPlugin, PopoverPlugin,
} from 'bootstrap-vue';
import vueAwesomeCountdown from 'vue-awesome-countdown';
import WebFont from 'webfontloader';
import App from './App.vue';
import router from './router';
import store from './store';

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import 'vue2-animate/dist/vue2-animate.min.css';
import './main.scss';

Vue.use(VueCookies);

// Install BootstrapVue
Vue.use(BootstrapVue);
// Optionally install the BootstrapVue components plugins
Vue.use(AlertPlugin);
Vue.use(FormTimepickerPlugin);
Vue.use(IconsPlugin);
Vue.use(ListGroupPlugin);
Vue.use(ModalPlugin);
Vue.use(NavbarPlugin);
Vue.use(PopoverPlugin);

Vue.use(vueAwesomeCountdown, 'vac');

Vue.config.productionTip = false;

const app = () => new Vue({
  router,
  store,
  render: (h) => h(App),
  created() {
    store.dispatch('connect');
  },
}).$mount('#app');

WebFont.load({
  google: {
    families: [
      'Cinzel:400,500,600,700,800,900',
      'Inconsolata:200,300,400,500,600,700,800,900',
    ],
  },
  classes: false,
  timeout: 3000,
  active: app,
});
