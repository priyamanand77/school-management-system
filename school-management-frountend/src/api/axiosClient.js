import axios from 'axios'

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/auth',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

const savedToken = localStorage.getItem('authToken')
if (savedToken) {
  axiosClient.defaults.headers.common.Authorization = `Bearer ${savedToken}`
}

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (!error.response) {
      return Promise.reject(
        new Error('Cannot reach the API. Check the server URL and make sure CORS is enabled.'),
      )
    }

    if (error.response.status === 401) {
      const responseData = error.response.data
      const backendMessage =
        responseData?.message ||
        responseData?.error ||
        (typeof responseData === 'string' ? responseData : null)

      return Promise.reject(
        new Error(backendMessage || 'Invalid username, password, or role. Please try again.'),
      )
    }

    const responseData = error.response.data
    const message = Array.isArray(responseData?.errors)
      ? responseData.errors[0]?.defaultMessage || responseData.errors[0]?.message
      : responseData?.message ||
        responseData?.error ||
        (typeof responseData === 'string' ? responseData : null) ||
        error.response.statusText ||
        'Unable to complete the request.'

    return Promise.reject(new Error(message))
  },
)

export default axiosClient
