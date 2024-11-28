import './App.css';
import { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './components/registerPage';
import LoginPage from './components/loginPage';
import HomePage from './components/homepage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/registerpage" element={<RegisterPage />} />
        <Route path="/loginpage" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
      </Routes>
    </Router>
  );
}

export default App;
