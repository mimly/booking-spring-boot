/* eslint-disable no-param-reassign */
export default {
  state: {
    loading: false,
    times: [],
    chosenTime: { loading: false },
    modal: undefined,
    modalTitle: undefined,
  },
  getters: {
    isLoadingAnyTime(state) {
      return state.loading === true;
    },
    isLoading(state) {
      return (time) => state.chosenTime.id === time.id && state.chosenTime.loading === true;
    },
    times(state) {
      return state.times;
    },
    time(state) {
      return (id) => state.times.find((time) => time.id === id) || {};
    },
    chosenTime(state) {
      return state.chosenTime;
    },
    modal(state) {
      return state.modal;
    },
    modalTitle(state) {
      return state.modalTitle;
    },
  },
  mutations: {
    setLoading(store, loading) {
      store.chosenTime.loading = loading;
      store.loading = loading;
    },
    setTimes(store, times) {
      store.times = times.sort((a, b) => a.time > b.time);
    },
    updateTime(store, time) {
      const index = store.times.findIndex(({ id }) => id === time.id);
      store.times.splice(index, 1, time);
    },
    addTime(store, time) {
      store.times.push(time);
      store.times.sort((a, b) => a.time > b.time);
    },
    removeTime(store, time) {
      const index = store.times.findIndex(({ id }) => id === time.id);
      store.times.splice(index, 1);
    },
    setChosenTime(store, time) {
      store.chosenTime = time;
    },
    setModal(store, modal) {
      store.modal = modal;
    },
    setModalTitle(store, title) {
      store.modalTitle = title;
    },
  },
  actions: {
  },
  modules: {
  },
  plugins: [
  ],
};
