import { defineStore } from 'pinia'
import { ref } from 'vue'
import { api } from '../api/client'
import type { Task, TasksResponse } from '../types'

export const useTaskStore = defineStore('tasks', () => {
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchTasks(): Promise<void> {
    loading.value = true
    error.value = null
    try {
      const data = await api.get<TasksResponse>('/api/tasks')
      tasks.value = data.tasks
    } catch (e: any) {
      error.value = e.message || 'Failed to fetch tasks'
    } finally {
      loading.value = false
    }
  }

  async function createTask(title: string, description?: string): Promise<Task> {
    const task = await api.post<Task>('/api/tasks', { title, description })
    tasks.value.unshift(task)
    return task
  }

  async function updateTask(id: number, title: string, description: string | null, completed: boolean): Promise<void> {
    const updated = await api.put<Task>(`/api/tasks/${id}`, { title, description, completed })
    const index = tasks.value.findIndex(t => t.id === id)
    if (index !== -1) {
      tasks.value[index] = updated
    }
  }

  async function deleteTask(id: number): Promise<void> {
    await api.delete(`/api/tasks/${id}`)
    tasks.value = tasks.value.filter(t => t.id !== id)
  }

  async function toggleTask(task: Task): Promise<void> {
    await updateTask(task.id, task.title, task.description, !task.completed)
  }

  return { tasks, loading, error, fetchTasks, createTask, updateTask, deleteTask, toggleTask }
})
