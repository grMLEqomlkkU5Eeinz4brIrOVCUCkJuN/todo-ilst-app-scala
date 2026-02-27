export interface User {
  id: number
  email: string
  name: string
}

export interface AuthResponse {
  token: string
  user: User
}

export interface Task {
  id: number
  title: string
  description: string | null
  completed: boolean
  createdAt: string
  updatedAt: string
}

export interface TasksResponse {
  tasks: Task[]
}

export interface ApiError {
  error: string
  message: string
  details?: string
}
