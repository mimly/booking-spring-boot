/* eslint-disable no-param-reassign */
export default {
  state: {
    message: null,
    messageVariant: 'danger',
    authenticated: false,
    assistant: null,
  },
  getters: {
    authMessage(state) {
      return state.message;
    },
    authMessageVariant(state) {
      return state.messageVariant;
    },
    isAuthenticated(state) {
      return state.authenticated;
    },
    signedIn(state) {
      return state.assistant;
    },
    signedInAvatar(state) {
      return state.assistant ? state.assistant.charAt(0) : state.assistant;
    },
  },
  mutations: {
    resetAuthMessage(store) {
      store.message = null;
      store.messageVariant = 'danger';
    },
    setAuthMessage(store, { message, variant }) {
      store.message = message;
      store.messageVariant = variant;
    },
    authenticate(store, { authenticated, assistant }) {
      store.authenticated = authenticated;
      store.assistant = assistant;
    },
  },
  actions: {
    isAuthenticated({ commit }) {
      fetch('/api/v1/authentication')
        .then((res) => {
          if (!res.ok) {
            commit('authenticate', { authenticated: false, assistant: null });
            return;
          }

          res.json().then(({ authenticated, name }) => {
            commit('authenticate', { authenticated, assistant: name });
          });
        }).catch(console.error);
    },
  },
  modules: {
  },
  plugins: [
  ],
};
