# E-Commerce Full Stack Application

## Latest Tech Stack
- **Backend**: Spring Boot 3.3.5 + Java 21
- **Frontend**: Next.js 15.0.3 + React 19
- **Database**: PostgreSQL 17
- **State Management**: Zustand 5.0.1
- **Styling**: Tailwind CSS 3.4.14
- **Authentication**: JWT with latest security

## Project Structure

```
E-Commerce/
├── backend/                 # Spring Boot API
│   ├── src/main/java/com/example/ecommerce/
│   │   ├── auth/           # Authentication & JWT
│   │   ├── user/           # User management
│   │   ├── product/        # Product catalog
│   │   ├── cart/           # Shopping cart
│   │   ├── order/          # Order processing
│   │   ├── payment/        # Payment integration
│   │   └── config/         # Security & CORS
│   └── src/main/resources/
│       └── db/migration/   # Database migrations
├── frontend/               # Next.js Client
│   ├── app/               # App Router pages
│   ├── components/        # Reusable UI components
│   ├── services/          # API calls
│   └── store/             # Global state
└── docker-compose.yml     # Full stack deployment
```

## Development Flow

### Backend (Spring Boot)
1. **Setup**: `cd backend && ./gradlew bootRun`
2. **Database**: PostgreSQL with Flyway migrations
3. **Security**: JWT authentication with Spring Security
4. **APIs**: RESTful endpoints for auth, products, cart, orders

### Frontend (Next.js)
1. **Setup**: `cd frontend && npm install && npm run dev`
2. **Routing**: App Router with TypeScript
3. **State**: Zustand for cart, React Query for server state
4. **Styling**: Tailwind CSS

### Full Stack
```bash
# Start everything with Docker
docker-compose up --build

# Or run separately:
# Terminal 1: Backend
cd backend && ./gradlew bootRun

# Terminal 2: Frontend  
cd frontend && npm run dev

# Terminal 3: Database
docker run -p 5432:5432 -e POSTGRES_DB=ecommerce -e POSTGRES_PASSWORD=password postgres:15
```

## API Flow
- **Auth**: `/api/auth/login` → JWT cookie → protected routes
- **Products**: `/api/products` → SSR/ISR for SEO
- **Cart**: `/api/cart` → optimistic updates
- **Checkout**: `/api/checkout` → payment integration
- **Orders**: `/api/orders` → order history

## Next Steps
1. Implement authentication module
2. Create product catalog
3. Build shopping cart functionality
4. Add payment integration (Razorpay/Stripe)
5. Implement order management

   mmmm
