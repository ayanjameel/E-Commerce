import { create } from 'zustand'
import { api } from '@/lib/api'

interface User {
  id: number
  email: string
  fullName: string
}

interface AuthState {
  user: User | null
  token: string | null
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  register: (email: string, password: string, fullName: string) => Promise<void>
  logout: () => void
  checkAuth: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  token: null,
  isLoading: false,

  login: async (email: string, password: string) => {
    set({ isLoading: true })
    try {
      const response = await api.login(email, password)
      localStorage.setItem('token', response.token)
      set({ user: response.user, token: response.token, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  register: async (email: string, password: string, fullName: string) => {
    set({ isLoading: true })
    try {
      const response = await api.register(email, password, fullName)
      localStorage.setItem('token', response.token)
      set({ user: response.user, token: response.token, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
      throw error
    }
  },

  logout: () => {
    localStorage.removeItem('token')
    set({ user: null, token: null })
  },

  checkAuth: () => {
    const token = localStorage.getItem('token')
    if (token) {
      set({ token })
    }
  },
}))