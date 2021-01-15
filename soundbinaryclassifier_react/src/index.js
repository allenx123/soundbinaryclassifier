import React, { Fragment } from "react";
import ReactDOM from 'react-dom';
import "./index.css"
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import {Login} from './Login.js'
import {Submit} from './Submit.js'

export default function App() {
  return (
   <Router>
    <main>

	<Route path="/submit"  component={Submit} />
    <Route path="/"  exact component={Login} />
    </main>
  </Router>
  );
}
ReactDOM.render(<App />, document.getElementById('root'));
