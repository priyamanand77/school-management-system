import { Link } from 'react-router-dom'

const features = [
  {
    title: 'Student Management',
    description: 'Organize student records, profiles, attendance, and academic details.',
  },
  {
    title: 'Teacher Management',
    description: 'Manage teachers, classes, schedules, and teaching responsibilities.',
  },
  {
    title: 'Academic Insights',
    description: 'Track progress and access important school information in one place.',
  },
]

function Homepage() {
  return (
    <main className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white">
        <nav className="mx-auto flex max-w-6xl items-center justify-between px-5 py-4">
          <Link to="/" className="text-lg font-bold text-indigo-700">
            SchoolHub
          </Link>
          <div className="flex items-center gap-3">
            <Link
              to="/login"
              className="rounded-lg px-4 py-2 text-sm font-semibold text-slate-700 hover:text-indigo-700"
            >
              Log in
            </Link>
            <Link
              to="/signup"
              className="rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:border-indigo-400 hover:text-indigo-700"
            >
              Sign up
            </Link>
          </div>
        </nav>
      </header>

      <section className="mx-auto grid max-w-6xl gap-10 px-5 py-16 md:grid-cols-2 md:items-center md:py-24">
        <div>
          <span className="rounded-full bg-indigo-100 px-3 py-1 text-sm font-bold text-indigo-700">
            Smart school management
          </span>
          <h1 className="mt-6 text-4xl font-bold tracking-tight sm:text-5xl">
            Everything your school needs, in one place
          </h1>
          <p className="mt-5 max-w-xl text-lg leading-8 text-slate-600">
            Simplify daily school operations with a connected platform for students,
            teachers, and administrators.
          </p>
          <div className="mt-8 flex flex-wrap gap-3">
            <Link
              to="/signup"
              className="rounded-lg bg-indigo-600 px-5 py-3 font-bold text-white transition hover:bg-indigo-700"
            >
              Get started
            </Link>
          </div>
        </div>

        <div className="rounded-3xl border border-indigo-100 bg-white p-6 shadow-xl shadow-indigo-100/40">
          <div className="grid grid-cols-2 gap-4">
            <div className="rounded-2xl bg-indigo-600 p-5 text-white">
              <p className="text-sm text-indigo-100">Students</p>
              <p className="mt-2 text-3xl font-bold">1,250+</p>
            </div>
            <div className="rounded-2xl bg-slate-900 p-5 text-white">
              <p className="text-sm text-slate-300">Teachers</p>
              <p className="mt-2 text-3xl font-bold">80+</p>
            </div>
            <div className="col-span-2 rounded-2xl border border-slate-200 bg-slate-50 p-5">
              <p className="text-sm font-semibold text-slate-500">School overview</p>
              <div className="mt-4 grid grid-cols-3 gap-3">
                {[92, 88, 96].map((value) => (
                  <div key={value} className="rounded-xl bg-white p-3 text-center shadow-sm">
                    <p className="text-xl font-bold text-indigo-700">{value}%</p>
                    <p className="mt-1 text-xs text-slate-500">Progress</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="border-t border-slate-200 bg-white">
        <div className="mx-auto max-w-6xl px-5 py-14">
          <h2 className="text-2xl font-bold text-slate-900">Built for everyday schoolwork</h2>
          <div className="mt-8 grid gap-5 md:grid-cols-3">
            {features.map((feature) => (
              <article
                key={feature.title}
                className="rounded-xl border border-slate-200 p-6 shadow-sm"
              >
                <h3 className="font-bold text-slate-900">{feature.title}</h3>
                <p className="mt-2 leading-7 text-slate-600">{feature.description}</p>
              </article>
            ))}
          </div>
        </div>
      </section>
    </main>
  )
}

export default Homepage