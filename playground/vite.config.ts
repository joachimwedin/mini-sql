import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    fs: {
      allow: [
        // Allow the playground project root
        path.resolve(__dirname),
        // Allow access to api dist for npm file: dependency
        path.resolve(__dirname, '../api/dist'),
      ],
    },
  },
})
