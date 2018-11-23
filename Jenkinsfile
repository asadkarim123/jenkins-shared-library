@Library('jenkins-shared-library') _
node {
        stage('hello') {
            echo 'Hello World'
        }
        stage('post'){
        post{
        always{
        notifySlack currentBuild.result
              }
            }
        }
}
