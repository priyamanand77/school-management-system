import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { loginUser } from '../api/authService'
import axiosClient from '../api/axiosClient'

const initialForm = {
  username: '',
  password: '',
  role: 'STUDENT',
}

function Login() {
  const navigate = useNavigate()
  const [form, setForm] = useState(initialForm)
  const [showPassword, setShowPassword] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [status, setStatus] = useState({ type: '', message: '' })

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      const data = await loginUser(form)
      const token = data?.token || data?.accessToken || data?.jwt

      if (token) {
        localStorage.setItem('authToken', token)
        axiosClient.defaults.headers.common.Authorization = `Bearer ${token}`
      }

      setStatus({ type: 'success', message: 'Login successful.' })
      navigate('/home', { replace: true, state: { auth: data } })
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.message || 'Login failed. Please check your credentials.',
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="grid min-h-screen place-items-center bg-gradient-to-br from-slate-50 to-indigo-50 px-3 py-8 sm:px-5 sm:py-12">
      <section
        className="w-full max-w-md rounded-2xl border border-slate-200 bg-white p-5 text-left shadow-[0_24px_60px_rgba(30,41,59,0.12)] sm:p-10"
        aria-labelledby="login-title"
      >
        <Link to="/" className="text-sm font-bold text-indigo-700">
          SchoolHub
        </Link>

        <div className="mb-7 mt-4">
          <span className="inline-block rounded-full bg-indigo-50 px-2.5 py-1 text-xs font-bold text-indigo-700">
            Welcome back
          </span>
          <h1
            id="login-title"
            className="mb-2 mt-3 text-3xl font-semibold tracking-tight text-slate-900"
          >
            Sign in to your account
          </h1>
          <p className="text-slate-500">Enter your account details to continue.</p>
        </div>

        <form className="grid gap-4.5" onSubmit={handleSubmit}>
          <div className="grid gap-1.5">
            <label className="text-sm font-bold text-slate-700" htmlFor="username">
              Username
            </label>
            <input
              id="username"
              name="username"
              value={form.username}
              onChange={handleChange}
              placeholder="Enter username"
              autoComplete="username"
              required
              className="h-11 w-full rounded-lg border border-slate-300 px-3 text-[15px] text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10"
            />
          </div>

          <div className="grid gap-1.5">
            <label className="text-sm font-bold text-slate-700" htmlFor="password">
              Password
            </label>
            <div className="relative">
              <input
                id="password"
                name="password"
                type={showPassword ? 'text' : 'password'}
                value={form.password}
                onChange={handleChange}
                placeholder="Enter your password"
                autoComplete="current-password"
                required
                className="h-11 w-full rounded-lg border border-slate-300 px-3 pr-20 text-[15px] text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10"
              />
              <button
                type="button"
                onClick={() => setShowPassword((current) => !current)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-sm font-semibold text-indigo-600 hover:text-indigo-800"
                aria-label={showPassword ? 'Hide password' : 'Show password'}
                aria-pressed={showPassword}
              >
                {showPassword ? 'Hide' : 'Show'}
              </button>
            </div>
          </div>

          <div className="grid gap-1.5">
            <label className="text-sm font-bold text-slate-700" htmlFor="role">
              Role
            </label>
            <select
              id="role"
              name="role"
              value={form.role}
              onChange={handleChange}
              className="h-11 w-full rounded-lg border border-slate-300 px-3 text-[15px] text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10"
            >
              <option value="STUDENT">Student</option>
              <option value="TEACHER">Teacher</option>
            </select>
          </div>

          {status.message && (
            <div
              className={`rounded-lg border px-3.5 py-3 text-sm ${
                status.type === 'error'
                  ? 'border-red-200 bg-red-100 text-red-800'
                  : 'border-green-200 bg-green-100 text-green-800'
              }`}
              role={status.type === 'error' ? 'alert' : 'status'}
            >
              {status.message}
            </div>
          )}

          <button
            className="h-12 rounded-lg bg-indigo-600 text-[15px] font-bold text-white transition hover:-translate-y-px hover:bg-indigo-700 disabled:cursor-wait disabled:opacity-65"
            type="submit"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Signing in...' : 'Sign in'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-600">
          Don&apos;t have an account?{' '}
          <Link to="/signup" className="font-bold text-indigo-700 hover:text-indigo-900">
            Create one
          </Link>
        </p>
      </section>
    </main>
  )
}

export default Login
