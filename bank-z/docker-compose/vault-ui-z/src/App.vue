<template>
  <div
      class="py-4 min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-50 via-purple-100 to-pink-200 dark:bg-gradient-to-br dark:from-gray-800 dark:via-gray-900 dark:to-black px-6"
  >
    <div class="max-w-4xl w-full bg-white dark:bg-gray-900 rounded-xl shadow-2xl p-8">
      <!-- Dark Mode Toggle -->
      <div class="flex justify-end mb-4">
        <button
            @click="toggleDarkMode"
            class="bg-gray-300 dark:bg-gray-700 text-gray-800 dark:text-gray-200 p-2 rounded-full hover:bg-gray-400 dark:hover:bg-gray-600 transition"
            aria-label="Toggle Dark Mode"
        >
          <p v-if="isDarkMode" class="w-6 h-6">☀️</p>
          <p v-else class="w-6 h-6">🌙</p>
        </button>
      </div>

      <div class="text-center mb-8 flex flex-col md:flex-row items-center">
        <div class="ml-10 h-24 w-24 shrink-0 mb-4 md:mb-0">
          <svg viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" fill="#000000"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"><title>file_type_ansible</title><path d="M16,29.951a13.952,13.952,0,1,1,.193-27.9A13.951,13.951,0,0,1,16,29.951Zm-2.221-13.13c.1.1.1.1.193.1C16,18.559,18.027,20.1,19.958,21.745a10.928,10.928,0,0,0,1.255.965.99.99,0,0,0,1.545-.676,1.643,1.643,0,0,0-.1-.676L18.9,12.38c-.579-1.352-1.159-2.8-1.738-4.151a.87.87,0,0,0-.579-.579c-.579-.193-.965.1-1.255.676-2.027,4.731-3.958,9.558-5.986,14.289,0,.1-.1.193-.1.29h1.931c.193,0,.193-.1.29-.193.386-.869.676-1.834,1.062-2.7A31.234,31.234,0,0,1,13.779,16.821Z"></path><path d="M13.779,16.821c-.386,1.062-.869,2.124-1.255,3.186-.386.869-.772,1.834-1.062,2.7a.355.355,0,0,1-.29.1H9.242c0-.1.1-.193.1-.29,2.027-4.731,3.958-9.558,5.986-14.289.29-.579.676-.869,1.255-.676a.87.87,0,0,1,.579.579c.579,1.352,1.159,2.8,1.738,4.151l3.765,8.979a2.978,2.978,0,0,1,.1.869.99.99,0,0,1-1.545.676,14.191,14.191,0,0,1-1.255-.965c-2.027-1.641-4.055-3.186-5.986-4.827C13.876,16.821,13.876,16.821,13.779,16.821Zm2.51-6.275c-.579,1.545-1.159,2.993-1.834,4.441-.1.1,0,.193.1.29C16,16.435,17.448,17.5,18.9,18.656c.29.193.579.483.869.676h0C18.607,16.435,17.448,13.539,16.29,10.546Z" style="fill:#fff"></path><path d="M16.29,10.546c1.159,2.993,2.414,5.889,3.572,8.786h0c-.29-.193-.579-.483-.869-.676-1.448-1.159-2.9-2.221-4.344-3.379-.1-.1-.193-.1-.1-.29C15.035,13.539,15.614,12.091,16.29,10.546Z"></path></g></svg>
        </div>
        <div class="flex-1">
          <h1 class="text-3xl font-extrabold text-gray-800 dark:text-gray-200">
            🔒 Ansible Vault Encrypt/Decrypt
          </h1>
          <p class="text-gray-600 dark:text-gray-400 text-sm">
            Encrypt and decrypt your secrets with ease.
          </p>
        </div>
      </div>

      <!-- Encryption Section -->
      <div class="mb-10">
        <h2 class="text-2xl font-semibold text-gray-700 dark:text-gray-300 mb-4">Encrypt Secret</h2>
        <div class="space-y-6">
          <div>
            <label
                for="vaultPassword"
                class="block text-sm font-medium text-gray-600 dark:text-gray-400"
            >
              Vault Password
            </label>
            <input
                id="vaultPassword"
                type="password"
                v-model="vaultPassword"
                class="w-full mt-2 px-4 py-3 border border-gray-300 dark:border-gray-700 rounded-lg focus:ring-2 focus:ring-purple-300 dark:focus:ring-purple-600 focus:outline-none bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200"
                placeholder="Enter vault password"
            />
          </div>
          <div>
            <label
                for="secret"
                class="block text-sm font-medium text-gray-600 dark:text-gray-400"
            >
              Secret
            </label>
            <textarea
                id="secret"
                v-model="secret"
                rows="4"
                class="w-full mt-2 px-4 py-3 border border-gray-300 dark:border-gray-700 rounded-lg focus:ring-2 focus:ring-purple-300 dark:focus:ring-purple-600 focus:outline-none bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200"
                placeholder="Enter secret to encrypt or crypted payload to decrypt"
                @paste="performCryptographicOperation($event.clipboardData.getData('text'))"
            ></textarea>
          </div>
          <button
              @click="performCryptographicOperation"
              class="w-full bg-purple-600 text-white dark:bg-purple-700 py-3 rounded-lg hover:bg-purple-700 dark:hover:bg-purple-800 focus:ring-2 focus:ring-indigo-400 dark:focus:ring-indigo-600 transition duration-150"
          >
            {{ action }}
          </button>
        </div>
        <div v-if="output" class="mt-6">
          <p class="text-sm font-medium text-gray-600 dark:text-gray-400">Output:</p>
          <div
              class="bg-gray-100 dark:bg-gray-800 text-sm p-4 rounded-lg overflow-x-auto border border-gray-300 dark:border-gray-700 relative"
          >
            <pre class="dark:text-white">{{ output }}</pre>
            <button
                :class="copyAnimation ? 'bg-green-500 text-white' : 'bg-purple-600 text-white'"
                @click="copyToClipboard(output)"
                class="absolute top-2 right-2 px-3 py-1 text-xs rounded-md transition duration-300 ease-in-out bg-purple-600 dark:bg-purple-700 text-white hover:bg-purple-700 dark:hover:bg-purple-800"
            >
              {{ copyAnimation ? 'Copied!' : 'Copy' }}
            </button>
          </div>
        </div>
      </div>

      <div class="mb-10 text-center space-y-6">
        <h2 class="text-xl font-semibold text-gray-700 dark:text-gray-300">Why choose this tool?</h2>
        <div class="flex flex-col md:flex-row justify-center items-center gap-6">
          <div class="flex flex-col items-center">
            <svg viewBox="0 0 24 24" class="w-20 h-20" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M18.5221 18.5208C17.9141 18.8274 17.2272 19 16.5 19L8.4 19C5.41766 19 3 16.6044 3 13.6493C3 11.2001 4.8 8.9375 7.5 8.5C7.59659 8.27034 7.70825 8.04754 7.83373 7.83303M10.9021 5.30015C11.4635 5.10615 12.0645 5 12.6893 5C15.684 5 18.1317 7.32251 18.3 10.25C19.8893 10.9449 21 12.6503 21 14.4969C21 14.7799 20.9739 15.0568 20.924 15.3253M3 3L21 21" stroke="#fbe7ef" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg>
            <p class="text-gray-600 dark:text-gray-400 text-sm mt-2">
              Your data is never sent to a server.
            </p>
          </div>
          <div class="flex flex-col items-center">
            <svg class="w-20 h-20 text-green-600 " xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
            </svg>

            <p class="text-gray-600 dark:text-gray-400 text-sm mt-2">
              Encryption happens in your browser.
            </p>
          </div>
          <div class="flex flex-col items-center">
            <svg class="w-20 h-20 text-blue-300" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" d="M11.35 3.836c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 0 0 .75-.75 2.25 2.25 0 0 0-.1-.664m-5.8 0A2.251 2.251 0 0 1 13.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m8.9-4.414c.376.023.75.05 1.124.08 1.131.094 1.976 1.057 1.976 2.192V16.5A2.25 2.25 0 0 1 18 18.75h-2.25m-7.5-10.5H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V18.75m-7.5-10.5h6.375c.621 0 1.125.504 1.125 1.125v9.375m-8.25-3 1.5 1.5 3-3.75" />
            </svg>
            <p class="text-gray-600 dark:text-gray-400 text-sm mt-2">
              Easy copy/paste of secrets.
            </p>
          </div>
        </div>
      </div>

      <div class="text-center text-gray-600 dark:text-gray-400 text-sm">
        <p>
          &copy; {{ new Date().getFullYear() }} - Jérémy Albrecht - <a class="font-medium text-blue-600 underline dark:text-blue-500 hover:no-underline" href="https://github.com/jeremyalbrecht/ansible-vault-web-ui">Source code</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import vault from "@/vault.js";
import {useToast} from "vue-toastification";

export default {
  name: "App",
  beforeMount() {
    document.documentElement.classList.toggle(
        'dark',
        localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)
    )
  },
  data() {
    return {
      vaultPassword: "",
      secret: "",
      output: "",
      action: "Encrypt/Decrypt",
      copyAnimation: false,
      isDarkMode: localStorage.getItem('theme') === 'dark',
    };
  },
  watch: {
    secret(newSecret) {
      if (newSecret.includes(vault.VAULT_HEADER)) {
        this.action = "Decrypt";
      } else if (newSecret.length > 0) {
        this.action = "Encrypt";
      } else {
        this.action = "Encrypt/Decrypt"
      }
    }
  },
  methods: {
    toggleDarkMode() {
      if (this.isDarkMode) {
        localStorage.theme = 'light'
      } else {
        localStorage.theme = 'dark'
      }
      this.isDarkMode = !this.isDarkMode;
      document.documentElement.classList.toggle(
          'dark',
          localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)
      )
    },
    async performCryptographicOperation(text) {
      const toast = useToast();
      let tempSecret = this.secret;
      if (text.length) {
        tempSecret = text;
      }
      if (!this.vaultPassword || !tempSecret) {
        toast.error("Please provide both the vault password and the secret.");
        return;
      }
      if (!tempSecret.includes(vault.VAULT_HEADER)) {
        vault.encrypt(tempSecret, this.vaultPassword).then((encrypted) => {
          this.output = encrypted;
          toast.success("Secret encrypted successfully!");
        }).catch((reason) => {
          toast.error(`An error occurred while encrypting the secret: ${reason}`);
        });
      } else {  // No Ansible header, must be something to encrypt
        vault.decrypt(tempSecret, this.vaultPassword).then((decryptedBuffer) => {
          this.output = decryptedBuffer;
          toast.success("Secret decrypted successfully!");
        })
          .catch((reason) => {
            toast.error(`An error occurred while decrypting the secret: ${reason}`);
          });
      }
    },
    copyToClipboard(content) {
      navigator.clipboard.writeText(content).then(() => {
        this.copyAnimation = true;
        setTimeout(() => {
          this.copyAnimation = false;
        }, 1500); // Reset the button after 1.5 seconds
      });
    },
  },
};
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out;
}

body {
  font-family: 'Inter', sans-serif;
  background-color: #f8f9fd;
}
</style>