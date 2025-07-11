import './assets/tailwind.css'

import { createApp } from 'vue'
import App from './App.vue'
import Toast from "vue-toastification";
import { createHead } from '@unhead/vue'
import "vue-toastification/dist/index.css";

const app = createApp(App)
app.use(createHead())
app.use(Toast, {
    position: "top-right",
    timeout: 3000,
    hideProgressBar: false,
    closeButton: true,
    draggable: true,
    pauseOnHover: true,
});

app.mount('#app')
