@Library('jenkins-shared-library') _
node {
        stage('hello') {
            echo 'Hello World'
        }
        stage('post'){
        notifySlack currentBuild.result
        }
}
