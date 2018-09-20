pipeline {
    agent any


    stages {
       stage('Build') {
          steps {
             sh 'gradle clean build -x test'
          }
       }
       stage('Deploy'){
                  steps{
                      sh 'cf push account-service -p build/libs/account-service-0.0.1-SNAPSHOT.jar'
                  }
       }
    }
}