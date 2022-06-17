pipeline {
  agent any
  stages {
    stage('Clean') {
      steps {
        bat 'gradlew clean'
      }
    }

    stage('Build') {
      steps {
        bat 'gradlew :billing:assembleDebug'
      }
    }

  }
}