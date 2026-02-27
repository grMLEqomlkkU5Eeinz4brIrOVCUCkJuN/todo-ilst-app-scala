<script setup lang="ts">
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<template>
  <nav class="navbar">
    <div class="navbar-content">
      <router-link to="/" class="navbar-brand">Todo App</router-link>
      <div v-if="auth.isAuthenticated" class="navbar-right">
        <span class="navbar-user">{{ auth.user?.name }}</span>
        <button class="btn btn-small" @click="handleLogout">Logout</button>
      </div>
      <div v-else class="navbar-right">
        <router-link to="/login" class="nav-link">Login</router-link>
        <router-link to="/register" class="nav-link">Register</router-link>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  background-color: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  padding: 0.75rem 1rem;
}
.navbar-content {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.navbar-brand {
  font-size: 1.25rem;
  font-weight: 700;
  color: inherit;
  text-decoration: none;
}
.navbar-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.navbar-user {
  opacity: 0.8;
  font-size: 0.9rem;
}
.nav-link {
  color: inherit;
  text-decoration: none;
  font-size: 0.9rem;
}
.nav-link:hover {
  text-decoration: underline;
}
</style>
