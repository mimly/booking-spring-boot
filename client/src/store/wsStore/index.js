import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';

const onSessionExpired = (commit, dispatch) => () => {
  // only if the expired session was authenticated
  commit('setAuthMessage', { message: 'Session expired', variant: 'danger' });
  dispatch('isAuthenticated').then(() => dispatch('reconnect'));
};

const onIncomingReservation = (commit) => ({ headers: { origin }, body }) => {
  const payload = JSON.parse(body);

  if (origin === 'POST') {
    commit('addTime', payload);
  } else if (origin === 'DELETE') {
    commit('removeTime', payload);
  } else {
    commit('updateTime', payload);
  }
};

const onReservationStart = (commit, getters) => ({ body }) => {
  commit('setChosenTime', JSON.parse(body));
  commit('setLoading', false);
  commit('setModalTitle', 'Confirm the reservation');
  getters.modal.show('confirm');
};

const onReservationEnd = (commit, getters) => ({ body }) => {
  commit('setLoading', false);
  commit('setChosenTime', { loading: false });
  getters.modal.hide('confirm');
  getters.modal.hide('alert');

  const payload = JSON.parse(body);
  if (payload.message !== undefined) {
    commit('setModalTitle', payload.message);
    getters.modal.show('alert');
  }
};

/* eslint-disable no-param-reassign */
export default {
  state: {
    socket: undefined,
  },
  getters: {
    socket(state) {
      return state.socket;
    },
    isConnected(state) {
      return state.socket !== undefined && state.socket.connected === true;
    },
  },
  mutations: {
    setSocket(store) {
      store.socket = Stomp.over(new SockJS('/ws-api/v1'));
      store.socket.heartbeat.outgoing = 0;
      store.socket.heartbeat.incoming = 60000;
    },
    unsetSocket(store) {
      store.socket = undefined;
    },
  },
  actions: {
    connect({
      commit, dispatch, getters, state,
    }) {
      return new Promise((resolve, reject) => {
        commit('setSocket');
        state.socket.connect(
          {},
          () => {
            state.socket.subscribe('/user/queue/ws-api/v1/session-expired',
              onSessionExpired(commit, dispatch), { id: 'sub-0' });

            state.socket.subscribe('/user/queue/ws-api/v1/times', ({ body }) => {
              commit('setTimes', JSON.parse(body));

              state.socket.subscribe('/topic/ws-api/v1/reservations',
                onIncomingReservation(commit), { id: 'sub-2' });
              state.socket.subscribe('/user/queue/ws-api/v1/reservation-started',
                onReservationStart(commit, getters), { id: 'sub-3' });
              state.socket.subscribe('/user/queue/ws-api/v1/reservation-ended',
                onReservationEnd(commit, getters), { id: 'sub-4' });
            }, { id: 'sub-1' });

            state.socket.send('/app/ws-api/v1/times');

            resolve('Client connected!');
          },
          () => reject(new Error('Something went wrong!')),
        );
      });
    },
    /* eslint-disable no-unused-vars */
    disconnect({ commit, state }) {
      return new Promise((resolve, reject) => state.socket.disconnect(() => {
        commit('unsetSocket');
        resolve('Client disconnected!');
      }));
    },
    reconnect({ dispatch }) {
      return new Promise((resolve, reject) => dispatch('disconnect')
        .then(() => dispatch('connect')
          .then(resolve)
          .catch(reject))
        .catch(reject));
    },
  },
  modules: {
  },
  plugins: [
  ],
};
