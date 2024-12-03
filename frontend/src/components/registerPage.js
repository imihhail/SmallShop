import { useState } from 'react';
import { useNavigate } from 'react-router-dom'
import { RegisterUser} from '../connections/backendConnection';
import './registerPage.css';


function RegisterPage() {
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

  const handleRegistration = async () => {
    const response = await RegisterUser(userData)
    
    if (response.message == "Registration successful") {
      alert("Registration successful")
      navigate("/loginpage")
    } else {
      alert(response.message)
    }
  }


  return (
    <div className="register-page">

      <div className="form">
        <input type='text' id='username' placeholder='Username' onChange={validateUsername}></input>
        <input type="password" id='password' placeholder='Password' onChange={validatePassword}></input>
        <button onClick={handleRegistration}>Register</button>
      </div>
      
    </div>
  );
}

export default RegisterPage;
