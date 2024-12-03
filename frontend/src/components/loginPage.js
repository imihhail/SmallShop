import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { LoginUser } from '../connections/backendConnection'
import './registerPage.css';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate()

  const validateUsername = (e) => {
    setUsername(e.target.value)
  }
  
  const validatePassword = (e) => {
    setPassword(e.target.value)
  }

  const userData = {
    username: username,
    password: password
  };

  const handleLogin = async () => {
    const response = await LoginUser(userData)
    if (response.token) {
      navigate("/home")
    } else {
      alert(response)
    }
  }

  return (
    <div className="register-page">

      <div className="form">

        <input type='text' id='username' placeholder='Username' onChange={validateUsername}></input>
        <input type='password' id='password' placeholder='Password' onChange={validatePassword}></input>
        <button onClick={handleLogin}>Login</button>

        <p>Don't have an account? <a href="/registerpage">Register here</a></p>

      </div>

    </div>
  )
}

export default LoginPage;
