<script setup lang="ts">
import { ref } from 'vue'
import { useTaskStore } from '../stores/tasks'

const taskStore = useTaskStore()

const title = ref('')
const description = ref('')
const error = ref('')
const loading = ref(false)

async function handleSubmit() {
  if (!title.value.trim()) return
  error.value = ''
  loading.value = true
  try {
    await taskStore.createTask(title.value.trim(), description.value.trim() || undefined)
    title.value = ''
    description.value = ''
  } catch (e: any) {
    error.value = e.message || 'Failed to create task'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <form class="task-form" @submit.prevent="handleSubmit">
    <div v-if="error" class="error-message">{{ error }}</div>
    <div class="task-form-row">
      <input
        v-model="title"
        type="text"
        placeholder="What needs to be done?"
        required
        class="task-input"
      />
      <button type="submit" class="btn btn-primary" :disabled="loading">
        {{ loading ? 'Adding...' : 'Add' }}
      </button>
    </div>
    <input
      v-model="description"
      type="text"
      placeholder="Description (optional)"
      class="task-input task-desc-input"
    />
  </form>
</template>

<style scoped>
.task-form {
  margin-bottom: 1.5rem;
}
.task-form-row {
  display: flex;
  gap: 0.5rem;
}
.task-input {
  flex: 1;
  padding: 0.5rem 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  background: var(--color-input-bg);
  color: inherit;
  font-size: 1rem;
  box-sizing: border-box;
}
.task-desc-input {
  margin-top: 0.5rem;
  width: 100%;
}
</style>
