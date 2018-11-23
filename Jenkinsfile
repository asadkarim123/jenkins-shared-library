node ("scala_agent") {
    try {
        stage('hello') {
            echo 'Hello World'
        }
        
        stage('post;){
        post    {
        always      {
        notifySlack currentBuild.result
                }
                    }
                    } 
         }
                    }
