<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const name = ref('')
const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleSubmit() {
  error.value = ''
  loading.value = true
  try {
    await auth.register(email.value, password.value, name.value)
    router.push({ name: 'tasks' })
  } catch (e: any) {
    error.value = e.message || 'Registration failed'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <form class="auth-form" @submit.prevent="handleSubmit">
    <h2>Register</h2>
    <div v-if="error" class="error-message">{{ error }}</div>
    <div class="form-group">
      <label for="name">Name</label>
      <input id="name" v-model="name" type="text" required autocomplete="name" />
    </div>
    <div class="form-group">
      <label for="email">Email</label>
      <input id="email" v-model="email" type="email" required autocomplete="email" />
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input id="password" v-model="password" type="password" required minlength="6" autocomplete="new-password" />
    </div>
    <button type="submit" class="btn btn-primary" :disabled="loading">
      {{ loading ? 'Registering...' : 'Register' }}
    </button>
    <p class="form-footer">
      Already have an account? <router-link to="/login">Login</router-link>
    </p>
  </form>
</template>

<style scoped>
.auth-form {
  max-width: 400px;
  margin: 2rem auto;
}
.auth-form h2 {
  margin-bottom: 1.5rem;
}
.form-group {
  margin-bottom: 1rem;
  text-align: left;
}
.form-group label {
  display: block;
  margin-bottom: 0.25rem;
  font-size: 0.9rem;
  font-weight: 500;
}
.form-group input {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  background: var(--color-input-bg);
  color: inherit;
  font-size: 1rem;
  box-sizing: border-box;
}
.form-footer {
  margin-top: 1rem;
  font-size: 0.9rem;
}
</style>
