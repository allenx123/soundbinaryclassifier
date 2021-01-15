//import React from 'react';
//import ReactDOM from 'react-dom';

//for more on fetch API: https://attacomsian.com/blog/using-javascript-fetch-api-to-get-and-post-data

//sample server url
//url = 'http://3.19.27.28:5002/api/auth';

// create request object
/*
const postRequest = new Request(url, {
    method: 'POST',
    headers: new Headers({
        'Content-Type': 'application/json'
    })
});


const getRequest = new Request(url, {
  method: 'GET',
  headers: new Headers({
      'Content-Type': 'application/json'
  })
});
*/

export class CallServer {

    getResult = (input, callback, url) => {
	/*
	  let getRequest = new Request(url, {
	    method: 'GET',
		headers: new Headers({
			'Content-Type': 'application/json'
		})
	  });
	  */
	  let postRequest = new Request(url, {
		  method: 'POST',
		  headers: new Headers({
				'Content-Type': 'application/json'
		  }),
		  body: JSON.stringify(input)
	  });
	  
	  //alert('post request body='+postRequest.body);
	  
	  /*
      if (input) {        
        let inputStr = JSON.stringify(input);
        alert('Using POST with input='+inputStr);

        //postRequest.body = inputStr;
		
		//alert('post request body='+postRequest.body);
		
      }
	  */
      fetch(postRequest)
      .then(res => res.json())
      .then(res =>
        { 
          let data = JSON.stringify( res ); 
          //alert ('response='+data);
		  if (callback) {
			  callback(data);
		  }
        });
    };
  }
  
export const server = new CallServer();