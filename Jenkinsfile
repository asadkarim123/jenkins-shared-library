#!groovy
@Library('jenkins-shared-library')
node {   
agent any   
stages {       
stage("notifySlack") {           
steps {           notifySlack       
}
        stage("slackNotifyStarted") {           
steps {           slackNotifyStarted       
}
                stage("getTestSummary") {           
steps {           getTestSummary       
}
                        stage("getFailedTests") {           
steps {           getFailedTests       
}
                                stage("slackNotifySuccess") {           
steps {           slackNotifySuccess       
}
                                        stage("slackNotifyFailure") {           
steps {           slackNotifyFailure       
}
                                                
}   
}
}
