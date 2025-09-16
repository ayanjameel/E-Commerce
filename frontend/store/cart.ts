import { create } from 'zustand'
import { api } from '@/lib/api'

interface CartItem {
  id: number
  product: {
    id: number
    name: string
    price: number
    imageUrl: string
  }
  quantity: number
}

interface CartState {
  items: CartItem[]
  isLoading: boolean
  fetchCart: () => Promise<void>
  addToCart: (productId: number, quantity?: number) => Promise<void>
  updateQuantity: (id: number, quantity: number) => Promise<void>
  removeItem: (id: number) => Promise<void>
  clearCart: () => void
  getTotalPrice: () => number
  getTotalItems: () => number
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  isLoading: false,

  fetchCart: async () => {
    set({ isLoading: true })
    try {
      const items = await api.getCart()
      set({ items, isLoading: false })
    } catch (error) {
      set({ isLoading: false })
    }
  },

  addToCart: async (productId: number, quantity = 1) => {
    try {
      await api.addToCart(productId, quantity)
      get().fetchCart()
    } catch (error) {
      throw error
    }
  },

  updateQuantity: async (id: number, quantity: number) => {
    try {
      await api.updateCartItem(id, quantity)
      get().fetchCart()
    } catch (error) {
      throw error
    }
  },

  removeItem: async (id: number) => {
    try {
      await api.removeFromCart(id)
      get().fetchCart()
    } catch (error) {
      throw error
    }
  },

  clearCart: () => {
    set({ items: [] })
  },

  getTotalPrice: () => {
    return get().items.reduce((total, item) => total + (item.product.price * item.quantity), 0)
  },

  getTotalItems: () => {
    return get().items.reduce((total, item) => total + item.quantity, 0)
  },
}))