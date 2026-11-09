import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { signupUser } from '../api/authService'

const initialForm = {
  username: '',
  password: '',
  email: '',
  firstName: '',
  lastName: '',
  role: 'STUDENT',
}

function Signup() {
  const navigate = useNavigate()
  const [form, setForm] = useState(initialForm)
  const [showPassword, setShowPassword] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [status, setStatus] = useState({ type: '', message: '' })
  const [createdUser, setCreatedUser] = useState(null)

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setStatus({ type: '', message: '' })
    setCreatedUser(null)

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/
    if (!emailPattern.test(form.email.trim())) {
      setStatus({
        type: 'error',
        message: 'Please enter a valid email address.',
      })
      document.getElementById('email')?.focus()
      return
    }

    setIsSubmitting(true)

    try {
      const data = await signupUser(form)
      setCreatedUser(data)
      setForm(initialForm)
      setStatus({
        type: 'success',
        message: 'Account created successfully.',
      })
      navigate('/home', { replace: true, state: { user: data } })
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.message || 'Unable to create account.',
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="grid min-h-screen place-items-center bg-gradient-to-br from-slate-50 to-indigo-50 px-3 py-8 sm:px-5 sm:py-12">
      <section
        className="w-full max-w-xl rounded-2xl border border-slate-200 bg-white p-5 text-left shadow-[0_24px_60px_rgba(30,41,59,0.12)] sm:p-10"
        aria-labelledby="signup-title"
      >
        <Link to="/" className="text-sm font-bold text-indigo-700">
          SchoolHub
        </Link>

        <div className="mb-7 mt-4">
          <span className="inline-block rounded-full bg-indigo-50 px-2.5 py-1 text-xs font-bold text-indigo-700">
            School Management
          </span>
          <h1
            id="signup-title"
            className="mb-2 mt-3 text-3xl font-semibold tracking-tight text-slate-900"
          >
            Create an account
          </h1>
          <p className="text-slate-500">Enter your details to get started.</p>
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

          <div className="grid gap-3.5 sm:grid-cols-2">
            <div className="grid gap-1.5">
              <label className="text-sm font-bold text-slate-700" htmlFor="firstName">
                First name
              </label>
              <input
                id="firstName"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
                placeholder="Enter first name"
                autoComplete="given-name"
                required
                className="h-11 w-full rounded-lg border border-slate-300 px-3 text-[15px] text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10"
              />
            </div>

            <div className="grid gap-1.5">
              <label className="text-sm font-bold text-slate-700" htmlFor="lastName">
                Last name
              </label>
              <input
                id="lastName"
                name="lastName"
                value={form.lastName}
                onChange={handleChange}
                placeholder="Enter last name"
                autoComplete="family-name"
                required
                className="h-11 w-full rounded-lg border border-slate-300 px-3 text-[15px] text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-3 focus:ring-indigo-500/10"
              />
            </div>
          </div>

          <div className="grid gap-1.5">
            <label className="text-sm font-bold text-slate-700" htmlFor="email">
              Email address
            </label>
            <input
              id="email"
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              placeholder="Enter email address"
              autoComplete="email"
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
                autoComplete="new-password"
                minLength={6}
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

          {createdUser && (
            <pre className="m-0 overflow-x-auto rounded-lg bg-slate-900 p-3.5 text-xs text-slate-200">
              {JSON.stringify(createdUser, null, 2)}
            </pre>
          )}

          <button
            className="h-12 rounded-lg bg-indigo-600 text-[15px] font-bold text-white transition hover:-translate-y-px hover:bg-indigo-700 disabled:cursor-wait disabled:opacity-65"
            type="submit"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Creating account...' : 'Create account'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-600">
          Already have an account?{' '}
          <Link to="/login" className="font-bold text-indigo-700 hover:text-indigo-900">
            Sign in
          </Link>
        </p>
      </section>
    </main>
  )
}

export default Signup
