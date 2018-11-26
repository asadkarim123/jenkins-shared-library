@Library('jenkins-shared-library')_
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import groovy.json.JsonOutput
import hudson.tasks.test.AbstractTestResultAction
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult
node {         
stage("notifySlack") {notifySlack}
stage("slackNotifyStarted") {slackNotifyStarted}
stage("getTestSummary") {getTestSummary}
stage("getFailedTests") {getFailedTests}
stage("slackNotifySuccess") {slackNotifySuccess}
stage("slackNotifyFailure") {slackNotifyFailure}                                         
}
