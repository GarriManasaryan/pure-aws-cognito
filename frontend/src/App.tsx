import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import './App.css';
import MyButton from './button/button';
import Login from "./login/login";

function App() {
  return (
  <Router>
      <Routes>
        <Route path="/" element={<MyButton/>}></Route>
        <Route path="/login" element={<Login/>}></Route>
      </Routes>
  </Router>

  );
}

export default App;
