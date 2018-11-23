@Library('jenkins-shared-library') _
node {
        stage('hello') {
            echo 'Hello World'
        }
}
post {
  always {
          node('post'){
notifySlack currentBuild.result
          }
}
}
