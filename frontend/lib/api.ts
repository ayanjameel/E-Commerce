const API_BASE = 'http://localhost:8080/api'

class ApiClient {
  private getAuthHeaders() {
    const token = localStorage.getItem('token')
    return token ? { Authorization: `Bearer ${token}` } : {}
  }

  async request(endpoint: string, options: RequestInit = {}) {
    const url = `${API_BASE}${endpoint}`
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...this.getAuthHeaders(),
        ...options.headers,
      },
      ...options,
    }

    const response = await fetch(url, config)
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return response.json()
  }

  // Auth
  async login(email: string, password: string) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    })
  }

  async register(email: string, password: string, fullName: string) {
    return this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, fullName }),
    })
  }

  // Products
  async getProducts() {
    return this.request('/products')
  }

  async getProduct(id: string) {
    return this.request(`/products/${id}`)
  }

  // Cart
  async getCart() {
    return this.request('/cart')
  }

  async addToCart(productId: number, quantity: number = 1) {
    return this.request('/cart/add', {
      method: 'POST',
      body: JSON.stringify({ productId, quantity }),
    })
  }

  async updateCartItem(id: number, quantity: number) {
    return this.request(`/cart/${id}`, {
      method: 'PUT',
      body: JSON.stringify({ quantity }),
    })
  }

  async removeFromCart(id: number) {
    return this.request(`/cart/${id}`, {
      method: 'DELETE',
    })
  }

  // Orders
  async checkout() {
    return this.request('/orders/checkout', {
      method: 'POST',
    })
  }

  async getOrders() {
    return this.request('/orders')
  }

  async confirmPayment(orderId: number, paymentId: string) {
    return this.request(`/orders/${orderId}/confirm`, {
      method: 'POST',
      body: JSON.stringify({ paymentId }),
    })
  }
}

export const api = new ApiClient()