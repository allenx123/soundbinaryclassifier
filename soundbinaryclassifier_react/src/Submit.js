import React, { Fragment } from 'react';
import ReactDOM from 'react-dom';
import { Link } from "react-router-dom";
import queryString from 'query-string';

import {server} from './CallServer.js'

const random = {"brand":"Toyota RAV4"}

export class Submit extends React.Component {
	
    constructor(props) {
        super(props);
    }
    callback = (data) => {
        //alert("data from callback:"+data);
		let response = JSON.parse(data);
		localStorage.setItem("email", response.email);
    }
    toggleButtonState = () => {
        //server = new CallServer();
		this.sharelink = document.getElementById("sharelink").value;
        //alert(this.sharelink);
        let input = {
			shareLink : this.sharelink,
			email : localStorage.getItem("email")
		};		
		
        server.getResult(input, null, 'http://ec2-3-19-27-28.us-east-2.compute.amazonaws.com/api/train');//.then(result => this.setState({result}));
		//server.getResult(input, null, 'http://localhost:8080/api/train');//.then(result => this.setState({result}));
        //alert ("data from Hello:"+data);
    }
	
	authenticate = (code) => {
        let input = {
			code : code
		};
		server.getResult(input, this.callback, 'http://ec2-3-19-27-28.us-east-2.compute.amazonaws.com/api/auth');
		//server.getResult(input, this.callback, 'http://localhost:8080/api/auth');
    }
	
    
    render() {
		let params = queryString.parse(this.props.location.search);
		this.authenticate(params.code);
		
        return (
            <fragment>
			<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"></link>
			<div class='background w3-animate-opacity'>
				<div class='title w3-animate-zoom'>Sound Binary Classifier</div>
				<div class='center'>
					<input class='textbox' type="text" id="sharelink"></input>
				</div>
				<div class="submit"><button class="btn" onClick={this.toggleButtonState}>Submit</button></div>
			</div>
            </fragment>
        );
    }
  }
  