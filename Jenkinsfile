#!groovy

import groovy.json.JsonOutput
import java.util.Optional
import hudson.tasks.test.AbstractTestResultAction
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult

def speedUp = '--configure-on-demand --daemon --parallel'
def nebulaReleaseScope = (env.GIT_BRANCH == 'origin/master') ? '' : "-Prelease.scope=patch"
def nebulaRelease = "-x prepare -x release snapshot ${nebulaReleaseScope}"
def gradleDefaultSwitches = "${speedUp} ${nebulaRelease}"
def gradleAdditionalTestTargets = "integrationTest"
def gradleAdditionalSwitches = "shadowJar"
def slackNotificationChannel = "#alerts"
def author = ""
def message = ""
def testSummary = ""
def total = 0
def failed = 0
def skipped = 0

def isPublishingBranch = { ->
    return env.GIT_BRANCH == 'origin/master' || env.GIT_BRANCH =~ /release.+/
}

def isResultGoodForPublishing = { ->
    return currentBuild.result == null
}

def notifySlack(text, channel, attachments) {
    def slackURL = 'https://hooks.slack.com/services/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'
    def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'

    def payload = JsonOutput.toJson([text: text,
        channel: channel,
        username: "Jenkins",
        icon_url: jenkinsIcon,
        attachments: attachments
    ])

    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

def getGitAuthor = {
    def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
}

def getLastCommitMessage = {
    message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
}

@NonCPS
def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        skipped = testResultAction.getSkipCount()

        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}

@NonCPS
def getFailedTests = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def failedTestsString = "```"

    if (testResultAction != null) {
        def failedTests = testResultAction.getFailedTests()
        
        if (failedTests.size() > 9) {
            failedTests = failedTests.subList(0, 8)
        }

        for(CaseResult cr : failedTests) {
            failedTestsString = failedTestsString + "${cr.getFullDisplayName()}:\n${cr.getErrorDetails()}\n\n"
        }
        failedTestsString = failedTestsString + "```"
    }
    return failedTestsString
}

def populateGlobalVariables = {
    getLastCommitMessage()
    getGitAuthor()
    testSummary = getTestSummary()
}

node {
    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            //sh "./gradlew ${gradleDefaultSwitches} clean build ${gradleAdditionalTestTargets} ${gradleAdditionalSwitches} --refresh-dependencies"
            //step $class: 'JUnitResultArchiver', testResults: '**/TEST-*.xml'
            populateGlobalVariables()

            def buildColor = currentBuild.result == null ? "good" : "warning"
            def buildStatus = currentBuild.result == null ? "Success" : currentBuild.result
            def jobName = "${env.JOB_NAME}"

            // Strip the branch name out of the job name (ex: "Job Name/branch1" -> "Job Name")
            jobName = jobName.getAt(0..(jobName.indexOf('/') - 1))
        
            if (failed > 0) {
                buildStatus = "Failed"

                if (isPublishingBranch()) {
                    buildStatus = "MasterFailed"
                }

                buildColor = "danger"
                def failedTestsString = getFailedTests()

                notifySlack("", slackNotificationChannel, [
                    [
                        title: "${jobName}, build #${env.BUILD_NUMBER}",
                        title_link: "${env.BUILD_URL}",
                        color: "${buildColor}",
                        text: "${buildStatus}\n${author}",
                        "mrkdwn_in": ["fields"],
                        fields: [
                            [
                                title: "Branch",
                                value: "${env.GIT_BRANCH}",
                                short: true
                            ],
                            [
                                title: "Test Results",
                                value: "${testSummary}",
                                short: true
                            ],
                            [
                                title: "Last Commit",
                                value: "${message}",
                                short: false
                            ]
                        ]
                    ],
                    [
                        title: "Failed Tests",
                        color: "${buildColor}",
                        text: "${failedTestsString}",
                        "mrkdwn_in": ["text"],
                    ]
                ])          
            } else {
                notifySlack("", slackNotificationChannel, [
                    [
                        title: "${jobName}, build #${env.BUILD_NUMBER}",
                        title_link: "${env.BUILD_URL}",
                        color: "${buildColor}",
                        author_name: "${author}",
                        text: "${buildStatus}\n${author}",
                        fields: [
                            [
                                title: "Branch",
                                value: "${env.GIT_BRANCH}",
                                short: true
                            ],
                            [
                                title: "Test Results",
                                value: "${testSummary}",
                                short: true
                            ],
                            [
                                title: "Last Commit",
                                value: "${message}",
                                short: true
                            ],
							[
                                title: "BUILD_ID",
                                value: "${env.BUILD_ID}",
                                short: true
                            ],
							[
                                title: "BUILD_TAG",
                                value: "${env.BUILD_TAG}",
                                short: true
                            ],
	                        [
                                title: "CVS_BRANCH",
                                value: "${env.CVS_BRANCH}",
                                short: true
                            ],
							[
                                title: "EXECUTOR_NUMBER",
                                value: "${env.EXECUTOR_NUMBER}",
                                short: true
                            ],
							[
                                title: "GIT_COMMIT",
                                value: "${env.GIT_COMMIT}",
                                short: true
                            ],
							[
                                title: "GIT_URL",
                                value: "${env.GIT_URL}",
                                short: true
                            ],
							[
                                title: "NODE_NAME",
                                value: "${env.NODE_NAME}",
                                short: true
                            ],
							[
                                title: "SVN_REVISION",
                                value: "${env.SVN_REVISION}",
                                short: true
                            ],
							{
    "text": "Would you like to Approve or Decline this incoming Jenkins job?",
    "attachments": 
	[
        {
            "fallback": "na",
            "callback_id": "URL Here",
            "color": "#000000",
            "actions": 
			[
                {
                    "name": "Approve",
                    "text": "Approve",
                    "type": "button",
                    "value": "approve url here"
                },
				                
				{
                    "name": "Decline",
                    "text": "Decline",
					"style": "danger",
                    "type": "button",
                    "value" = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzjCRQZ_7d0Q_2oQ_QJFawWtODegefgJVfHgmRGcdoi7jRQrn1"
                }
            ]
        }
   ]
}
                        ]
                    ]
                ])
            }
        }
        
        if (isPublishingBranch() && isResultGoodForPublishing()) {
            stage ('Publish') {
                sh "./gradlew ${gradleDefaultSwitches}"
            }
        }
    } catch (hudson.AbortException ae) {
        // I ignore aborted builds, but you're welcome to notify Slack here
    } catch (e) {
        def buildStatus = "Failed"

        if (isPublishingBranch()) {
            buildStatus = "MasterFailed"
        }

        notifySlack("", slackNotificationChannel, [
            [
                title: "${env.JOB_NAME}, build #${env.BUILD_NUMBER}",
                title_link: "${env.BUILD_URL}",
                color: "danger",
                author_name: "${author}",
                text: "${buildStatus}",
                fields: [
                    [
                        title: "Branch",
                        value: "${env.GIT_BRANCH}",
                        short: true
                    ],
                    [
                        title: "Test Results",
                        value: "${testSummary}",
                        short: true
                    ],
                    [
                        title: "Last Commit",
                        value: "${message}",
                        short: false
                    ],
                    [
                        title: "Error",
                        value: "${e}",
                        short: false
                    ]
                ]
            ]
        ])

        throw e
    }
}
