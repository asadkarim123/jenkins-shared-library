#!/usr/bin/env groovy

/**
* notify slack and set message based on build status
*/
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import hudson.tasks.test.AbstractTestResultAction
import hudson.model.Actionable

def call(String buildStatus = 'STARTED', String channel = '#alerts') {

  // buildStatus of null means successfull
  buildStatus = buildStatus ?: 'SUCCESSFUL'
  channel = channel ?: '#alerts'


  // Default values
  def colorName = 'RED'
  def colorCode = '#458b74'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}] (<${env.RUN_DISPLAY_URL}|Open>) (<${env.RUN_CHANGES_DISPLAY_URL}|  Changes>)'"
  def title = "${env.JOB_NAME} Build: ${env.BUILD_NUMBER}"
  def title_link = "${env.RUN_DISPLAY_URL}"
  def branchName = "${env.BRANCH_NAME}"

  def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
  def author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an'").trim()

  def message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'GREY'
    colorCode = '#f0ffff'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#458b74'
  } else if (buildStatus == 'UNSTABLE') {
    color = 'YELLOW'
    colorCode = '#ffd700'
  } else {
    color = 'RED'
    colorCode = '#cd2626'
  }

  @NonCPS
  def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        def total = testResultAction.getTotalCount()
        def failed = testResultAction.getFailCount()
        def skipped = testResultAction.getSkipCount()

        summary = "Test results:\n\t"
        summary = summary + ("Passed: " + (total - failed - skipped))
        summary = summary + (", Failed: " + failed + " ${testResultAction.failureDiffString}")
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
  }
  def testSummaryRaw = getTestSummary()
  // format test summary as a code block
  def testSummary = "```${testSummaryRaw}```"
  println testSummary.toString()
  
  def slackNotifyStarted() {
    echo 'Notification Start'
    def colorSlack = '#D4DADF'
    populateGitInfo()
    echo 'Git info populated'
    echo author + message
    
    notifySlack("", "#alerts", [
        [
            title: "STARTED: Job `${env.JOB_NAME}` [Build #${env.BUILD_NUMBER}]",
            title_link: "${env.BUILD_URL}",
            color: "${colorSlack}",
            author_name: "${author}",
            text: "${message}",
            actions: [
            [
              type: "button",
              text: "Pipeline",
              url: "${RUN_DISPLAY_URL}"
            ],
            [
              type: "button",
              text: "Changes",
              url: "${RUN_CHANGES_DISPLAY_URL}"
            ],
            [
              type: "button",
              text: "Job history",
              url: "${JOB_DISPLAY_URL}"
            ]
          ],
          fields: [
                [
                    title: "Git Branch",
                    value: "${env.GIT_BRANCH}",
                    short: true
                ],
                [
                    title: "Git Commit",
                    value: "${env.GIT_COMMIT}",
                    short: true
                ]
            ]
        ]
    ])
}

def slackNotifySuccess() {
    echo 'Notification Success'
    def colorSlack = '#229954'
    def testSummary = getTestSummary()
    echo 'Test summary completed'
    echo 'Test summary' + testSummary
    notifySlack("", "#alerts", [
        [
            title: "SUCCESS: Job `${env.JOB_NAME}` [Build #${env.BUILD_NUMBER}]",
            title_link: "${env.BUILD_URL}",
            color: "${colorSlack}",
            author_name: "${author}",
            text: "${message}",
            actions: [
            [
              type: "button",
              text: "Pipeline",
              url: "${RUN_DISPLAY_URL}",
              style: "primary"
            ],
            [
              type: "button",
              text: "Changes",
              url: "${RUN_CHANGES_DISPLAY_URL}",
              style: "primary"
            ],
            [
              type: "button",
              text: "Job history",
              url: "${JOB_DISPLAY_URL}",
              style: "primary"
            ]
          ],
          fields: [
                [
                    title: "Test Results",
                    value: "${testSummary}",
                    short: true
                ]
            ]
        ]
    ])
}
def slackNotifyFailure(e) {
    echo 'Notify failure'
    def colorSlack = '#FF9FA1'
    def slackMessage = "FAILURE: Job `${env.JOB_NAME}` [<${env.BUILD_URL}|#${env.BUILD_NUMBER}>] (<${env.RUN_DISPLAY_URL}|  Pipeline>)\n\t"
    try{
        def testSummary = getTestSummary()
        echo 'Test summary completed'
        echo 'Test summary' + testSummary
        def failedTests = getFailedTests()
        echo 'Failed tests completed'
        echo 'Failed tests' + failedTests
        slackMessage = slackMessage + "```" + testSummary + "```\n\t"
        slackMessage = slackMessage + "```" + failedTests + "```\n\t"
        if (e != null){
            slackMessage = slackMessage + "```" + e.toString() + "```\n\t"
        }        
    } catch(ex) {
        // Do nothing
        echo 'Could not get test summary for failure'
    }
    
    slackSend channel: '#alerts', color: colorSlack, message: slackMessage
}
ansiColor('xterm') {
    timestamps {
          logstash {
            node(label) {

                    try {

                            container('gcloud') {
                                stage('1 - Clone repository') {
                                    withCredentials([file(credentialsId: 'key-sa', variable: 'GC_KEY')]) {
                                            //sh("gcloud auth activate-service-account --key-file=${GC_KEY};")
                                            //sh("gcloud source repos clone rmg-de-ml --project=${project};")
                                        }
                                    }
                                    checkout scm
                                    echo 'Started'
                                    slackNotifyStarted()
                                }

                            //container('sbt') {
                                //stage('2 - Compile') {
                                    //sh('sbt compile;')
                                   // echo 'Compile complete'
                                }
                                //stage('3 - Check Styles') {
                                    //sh('sbt scalastyle;')
                                    //checkstyle pattern: 'target/scalastyle-result.xml'

                                    //sh('sbt dependencyUpdatesReport;')
                                    //archiveArtifacts artifacts: 'target/dependency-updates.txt', fingerprint: false
                                    //env.DEPENDENCY_UPDATES = readFile 'target/dependency-updates.txt'
                                }
                                stage('2 - Automated Tests') {

                                    parallel "2.1 - Unit Tests": {
                                            sh('sbt clean coverage test;sbt coverageReport;')
                                            step([$class: 'ScoveragePublisher', reportDir: 'target/scala-2.11/scoverage-report', reportFile: 'scoverage.xml'])
                                            junit 'target/junit/*.xml'
                                        },

                                        "2.2 - Integration Tests": {
                                            sh('sleep 5s')
                                       },

                                        "2.3 - Performance Tests": {
                                            sh('sleep 10s')
                                        }

                                }
                                //stage('5 - Package') {
                                    //sh('sbt package;')
                                    //archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
                                    //echo 'Package complete'
                                }
                                stage('3 - Documentation') {
                                    sh('sbt doc;')
                                    publishHTML target: [
                                        allowMissing: false,
                                        alwaysLinkToLastBuild: false,
                                        keepAll: true,
                                        reportDir: 'target/scala-2.11/api',
                                        reportFiles: '*',
                                        reportName: 'Scala Docs'
                                      ]
                                }
                                stage('4 - Publish') {

                                    try{
                                        //sh('sbt publish;')
                                        echo 'Publish complete'
                                    } catch (e) {
                                        retry(2) {
                                            sleep 10
                                            sh('sbt publish;')
                                        }
                                    }

                                }
                                echo 'Successfully completed'
                                slackNotifySuccess()
                                echo 'Success notification sent'
                            }
                        }
                }
            }
        }
    }
}

  JSONObject attachment = new JSONObject();
  attachment.put('author',"jenkins");
  attachment.put('author_link',"https://danielschaaff.com");
  attachment.put('title', title.toString());
  attachment.put('title_link',title_link.toString());
  attachment.put('text', subject.toString());
  attachment.put('fallback', "fallback message");
  attachment.put('color',colorCode);
  attachment.put('mrkdwn_in', ["fields"])
  // JSONObject for branch
  JSONObject branch = new JSONObject();
  branch.put('title', 'Branch');
  branch.put('value', branchName.toString());
  branch.put('short', true);
  // JSONObject for author
  JSONObject commitAuthor = new JSONObject();
  commitAuthor.put('title', 'Author');
  commitAuthor.put('value', author.toString());
  commitAuthor.put('short', true);
  // JSONObject for branch
  JSONObject commitMessage = new JSONObject();
  commitMessage.put('title', 'Commit Message');
  commitMessage.put('value', message.toString());
  commitMessage.put('short', false);
  // JSONObject for test results
  JSONObject testResults = new JSONObject();
  testResults.put('title', 'Test Summary')
  testResults.put('value', testSummary.toString())
  testResults.put('short', false)
  attachment.put('fields', [branch, commitAuthor, commitMessage, testResults]);
  JSONArray attachments = new JSONArray();
  attachments.add(attachment);
  println attachments.toString()

  // Send notifications
  slackSend (color: colorCode, message: subject, attachments: attachments.toString(), channel: channel)

}
