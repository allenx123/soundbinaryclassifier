import React, { Fragment } from 'react';
import ReactDOM from 'react-dom';
import { Link } from "react-router-dom";
import './App.css';

export class Login extends React.Component {
  render() {
    return (
		<fragment>
		<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"></link>
		<div class='background w3-animate-opacity'>
			<div class='title w3-animate-zoom'>Sound Binary Classifier</div>
			<div class='center'>
				<a href="https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A//www.googleapis.com/auth/drive.readonly%20https%3A//www.googleapis.com/auth/userinfo.profile&access_type=offline&include_granted_scopes=true&response_type=code&state=state_parameter_passthrough_value&redirect_uri=http%3A//ec2-3-19-27-28.us-east-2.compute.amazonaws.com/submit&client_id=694757928711-2oa5reovr0gfu5plbaqac2g46ct49hs5.apps.googleusercontent.com" target="_parent"><button class="btn">Login</button></a>
			</div>
		</div>
		</fragment>
	);
  }
}