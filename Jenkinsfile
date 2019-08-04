pipeline {
  agent any
  stages {
    stage('Maven Build') {
      steps {
        timestamps() {
          echo 'Starting Pre-checks'
        }

        sh 'mvn clean install'
        timestamps() {
          echo 'Finished Build'
        }

      }
    }
    stage('Test(s)') {
      steps {
        echo 'Performing Tests!'
      }
    }
  }
}