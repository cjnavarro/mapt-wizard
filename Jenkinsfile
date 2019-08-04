pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
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