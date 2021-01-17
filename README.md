# SoundBinaryClassifier
## Introduction
SoundBinaryClassifier provides AI-based APIs to perform audio binary classification. The application is very user-friendly, making it perfect for users with limited machine learning knowledge to employ the power of machine learning in their own applications. 

Here are the steps to use SoundBinaryClassifier:
1. Gather training data for two categories of audio
2. Put the training data onto Google Drive
3. Submit the training data's share link on this application's front-end and wait for training finish
4. Perform audio-based binary prediction by calling this application's prediction API
## Technologies
### Spring Boot
Soundbinaryclassifier’s backend is powered by Spring Boot. Spring Boot was chosen because it simplifies the process of creating RESTful APIs. Through the use of Spring Boot annotations, the integration between backend code and database is very simple. The code is written in Java 1.8.
### MySQL
The database service chosen is MySQL because of its easy integration with Spring Boot. 
### Keras
The core of this application is powered by Keras. The Keras library was used because it makes it easy to write machine learning code to perform data training and predicting. The machine learning code is written in Python.
### React
This application’s front end is designed using React. React was chosen for its ability to create a "single page application" (SPA). Using React libraries, the front-end code is written in JavaScript, HTML5, and CSS.
### AWS
This application is hosted on an AWS Linux virtual-machine server.
## Try it out
To access my application, go to http://ec2-3-19-27-28.us-east-2.compute.amazonaws.com.
## How to call API
### Request format
POST /api/predict HTTP/1.1  
Host: 3.19.27.28:5002  
Content-Type: application/json  
\

### Response format
{  
    "decision": 10  
}
\
or  
{  
    "decision": 20  
}
