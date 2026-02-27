<script setup lang="ts">
import { onMounted } from 'vue'
import { useTaskStore } from '../stores/tasks'
import TaskForm from './TaskForm.vue'
import TaskItem from './TaskItem.vue'

const taskStore = useTaskStore()

onMounted(() => {
  taskStore.fetchTasks()
})
</script>

<template>
  <div class="task-list-container">
    <h1>My Tasks</h1>
    <TaskForm />
    <div v-if="taskStore.loading" class="loading">Loading tasks...</div>
    <div v-else-if="taskStore.error" class="error-message">{{ taskStore.error }}</div>
    <div v-else-if="taskStore.tasks.length === 0" class="empty-state">
      No tasks yet. Add one above!
    </div>
    <div v-else>
      <TaskItem
        v-for="task in taskStore.tasks"
        :key="task.id"
        :task="task"
      />
    </div>
  </div>
</template>

<style scoped>
.task-list-container h1 {
  margin-bottom: 1.5rem;
}
.loading, .empty-state {
  text-align: center;
  padding: 2rem;
  opacity: 0.7;
}
</style>
