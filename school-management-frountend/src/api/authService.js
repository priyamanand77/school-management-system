import axiosClient from './axiosClient'

export const signupUser = async (userData) => {
  const payload = {
    username: userData.username.trim(),
    password: userData.password,
    email: userData.email.trim(),
    firstName: userData.firstName.trim(),
    lastName: userData.lastName.trim(),
    role: userData.role,
  }

  const response = await axiosClient.post('/signup', payload)
  return response.data
}

export const loginUser = async (credentials) => {
  const payload = {
    username: credentials.username.trim(),
    password: credentials.password,
    role: String(credentials.role).trim().toUpperCase(),
  }

  localStorage.removeItem('authToken')
  delete axiosClient.defaults.headers.common.Authorization

  const response = await axiosClient.post('/login', payload)
  return response.data
}
