import { useState } from 'react';
import { RegisterUser} from '../connections/backendConnection';
import './registerPage.css';


function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

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


  return (
    <div className="register-page">

      <div className="form">
        <input type='text' id='username' placeholder='Username' onChange={validateUsername}></input>
        <input type="password" id='password' placeholder='Password' onChange={validatePassword}></input>
        <button onClick={() => RegisterUser(userData)}>Register</button>
      </div>
      
    </div>
  );
}

export default RegisterPage;
