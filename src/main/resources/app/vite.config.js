import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/daily_custom_stock_screener',
  build: {
    outDir: 'build', // CRA used "build", keep same for consistency
  }
});
