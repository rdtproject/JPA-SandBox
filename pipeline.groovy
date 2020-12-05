pipeline {
    agent any

    tools {
        jdk "openjdk-11"
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'Rdt', url: 'https://github.com/rdtproject/JPA-SandBox.git'
            }
        }
        stage('Build') {
            steps {                            
                sh './mvnw clean package'
            }

            post {                
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'                    
                    emailext attachLog: true, body: 'Please go to ${BUILD_URL} and verify the build', compressLog: true, subject: "Job \'${JOB_NAME}\' (build ${BUILD_NUMBER}) ${currentBuild.result}", to: 'test@jenkins'
                }
            }
        }
        stage('SonarQube analysis') {
            steps {
                // this groovy method comes from Sonar plugin installed in Jenkins (Manage Jenkins, configure system)
                withSonarQubeEnv(installationName: 'Sonar on NAS') {                
                    sh './mvnw sonar:sonar'            
                }
            }
        }        
    }
}
