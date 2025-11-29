    import { defineConfig } from 'vite';
    import react from '@vitejs/plugin-react'; // Or other framework plugins
    import tailwindcss from 'tailwindcss';

    export default defineConfig({
      plugins: [
        react(), // Include your framework plugin
      ],
      css: {
        postcss: {
          plugins: [tailwindcss()],
        },
      },
    });