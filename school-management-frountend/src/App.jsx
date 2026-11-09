import { Navigate, Route, Routes } from 'react-router-dom'
import Signup from './pages/signup.jsx'
import Homepage from './pages/homepage.jsx'
import Login from './pages/login.jsx'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Homepage />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/home" element={<Homepage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
