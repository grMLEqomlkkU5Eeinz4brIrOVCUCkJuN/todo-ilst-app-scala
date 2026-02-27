<script setup lang="ts">
import { ref } from 'vue'
import { useTaskStore } from '../stores/tasks'
import type { Task } from '../types'

const props = defineProps<{ task: Task }>()
const taskStore = useTaskStore()

const toggling = ref(false)
const deleting = ref(false)

async function handleToggle() {
  toggling.value = true
  try {
    await taskStore.toggleTask(props.task)
  } finally {
    toggling.value = false
  }
}

async function handleDelete() {
  deleting.value = true
  try {
    await taskStore.deleteTask(props.task.id)
  } finally {
    deleting.value = false
  }
}
</script>

<template>
  <div class="task-item" :class="{ completed: task.completed }">
    <div class="task-left">
      <input
        type="checkbox"
        :checked="task.completed"
        :disabled="toggling"
        @change="handleToggle"
        class="task-checkbox"
      />
      <div class="task-text">
        <span class="task-title">{{ task.title }}</span>
        <span v-if="task.description" class="task-description">{{ task.description }}</span>
      </div>
    </div>
    <button
      class="btn btn-danger btn-small"
      :disabled="deleting"
      @click="handleDelete"
    >
      {{ deleting ? '...' : 'Delete' }}
    </button>
  </div>
</template>

<style scoped>
.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  transition: opacity 0.2s;
}
.task-item.completed {
  opacity: 0.6;
}
.task-item.completed .task-title {
  text-decoration: line-through;
}
.task-left {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  text-align: left;
}
.task-checkbox {
  margin-top: 0.2rem;
  width: 1.1rem;
  height: 1.1rem;
  cursor: pointer;
}
.task-text {
  display: flex;
  flex-direction: column;
}
.task-title {
  font-weight: 500;
}
.task-description {
  font-size: 0.85rem;
  opacity: 0.7;
  margin-top: 0.15rem;
}
</style>
