@Library('jenkins-shared-library') _
stages {
        stage('hello') {
            echo 'Hello World'
        }
}
post {
always {
notifySlack currentBuild.result
}
}
