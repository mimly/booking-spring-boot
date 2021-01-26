<template>
  <div class="container">
    <section class="col-md-12">
      <b-card class="my-2" bg-variant="light">
        <template #header>
          <h4 class="title-1 text-center font-italic">
            Please sign in
            <Info id="loginInstructions" placement="bottomleft" title="Login Instructions">
<p>
  In total, there are <b>two accounts</b>, one for each assistant, with the following credentials:
  <br><br>
  <ul>
    <li><b>Username: </b><em>Lorem</em><br><b>Password: </b><em>Lorem</em></li>
    <li><b>Username: </b><em>Ipsum</em><br><b>Password: </b><em>Ipsum</em></li>
  </ul>
  <br>
  The login is successfull only if there exists a registered user with given credentials on the
  server side. The password is allowed to be sent in cleartext over an unencrypted channel (HTTPS is
  not required), but should be hashed (use BCrypt) and then stored in a database, e.g. SQLite. Only
  protection against SQL-injections is required. You do <b>not</b> have to implement any protection
  against XSS- or CSRF-attacks.
</p>
            </Info>
          </h4>
        </template>

        <div class="row">
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
          <div class="col-xs-8 col-sm-6 col-md-4">
            <b-form @submit.prevent="login()">
              <b-alert class="text-center" v-if="this.$store.getters.authMessage"
                :variant="this.$store.getters.authMessageVariant" fade show>
                {{ this.$store.getters.authMessage }}
              </b-alert>
              <p>
                <b-form-input id="username" type="text" v-model="username" placeholder="Username"
                  autofocus required/>
              </p>
              <p>
                <b-form-input id="password" type="password" v-model="password"
                  placeholder="Password" required/>
              </p>
              <b-button class="font-bold" size="lg" type="submit" block variant="primary">
                Sign in
              </b-button>
            </b-form>
          </div>
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
        </div>
      </b-card>
    </section>
  </div>
</template>

<script>
import Info from './Info.vue';

export default {
  name: 'Login',
  components: { Info },
  data: () => ({
    username: '',
    password: '',
  }),
  methods: {
    login() {
      this.$store.commit('resetAuthMessage');

      fetch('/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'X-XSRF-TOKEN': `${this.$cookies.get('XSRF-TOKEN')}`,
        },
        body: `username=${this.username}&password=${this.password}`,
      }).then(({ url }) => {
        if (url.match(/^(http|https):\/\/.*\/login\?error$/gi)) {
          this.$store.commit('setAuthMessage', { message: 'Bad credentials', variant: 'danger' });
          return;
        }

        this.$store.dispatch('isAuthenticated', { router: this.$router });
      }).catch(console.error);
    },
  },
};
</script>
