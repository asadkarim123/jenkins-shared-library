@Library('jenkins-shared-library') _
node {
        stage('hello') {
            echo 'Hello World'
        }
}
post {
always {
notifySlack currentBuild.result
}
}
