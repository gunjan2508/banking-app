# NeoVault — Private Banking Application

Full-stack banking app built with Spring Boot + React.

## 🔗 Branches
- `main` — Production ready
- `dev` — Development branch  
- `feature/account-api` — Complete application code

## 🛠 Tech Stack
- Backend: Java 17, Spring Boot, PostgreSQL, JWT
- Frontend: React, Vite, Framer Motion
- AI Chatbot: Groq (Llama3)

## ✨ Features
- JWT Authentication + RBAC (4 roles)
- Account Management
- Fund Transfers with daily limits
- Loans + EMI Calculator
- Fixed Deposits
- Beneficiaries
- AI Chatbot (Groq)
- Notifications
- Audit Logs
- Admin Panel

## 👥 Roles
- CUSTOMER, TELLER, MANAGER, ADMIN

## 🚀 Setup
1. PostgreSQL — create `banking_db`
2. Configure `application.properties`
3. Run: `mvn spring-boot:run`
4. Frontend: `npm run dev`
