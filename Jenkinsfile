@Library('jenkins-shared-library')_
node {         
stage("notifySlack") {notifySlack}
stage("slackNotifyStarted") {slackNotifyStarted}
stage("getTestSummary") {getTestSummary}
stage("getFailedTests") {getFailedTests}
stage("slackNotifySuccess") {slackNotifySuccess}
stage("slackNotifyFailure") {slackNotifyFailure}                                         
}
