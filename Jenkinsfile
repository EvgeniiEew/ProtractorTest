def DOCKER_BUILD_CONTAINER = "build-jh_courses-dev"
def DOCKER_DEPLOY_CONTAINER = "jh_courses-dev"
def DOCKER_FRONT_CONTAINER ="jh_courses_front-dev"

pipeline {
    agent {label "jh_courses-dev" }
          stages {
            stage('Pull_bitbucket') {
                steps {
                    sh 'echo " ******  Start build  ****** "'
                    sh 'ls -la'
                }
            }
            stage("Build project") {
                steps {
                    sh "docker build . -t ${DOCKER_BUILD_CONTAINER}"                  
                    sh "docker run -d -v /home/jh_courses-dev/app/:/project --name ${DOCKER_BUILD_CONTAINER} -it ${DOCKER_BUILD_CONTAINER} /bin/bash"
                    sh "docker exec ${DOCKER_BUILD_CONTAINER} bash -c 'cp -R /app_temp/* /project/'"
                    }
            }

            stage("Remove build container") {
                 steps {
                     sh "docker stop ${DOCKER_BUILD_CONTAINER}"
			         sh "docker container prune -f"
                     sh "docker image rm ${DOCKER_BUILD_CONTAINER}"

                 }
             }

            stage("Deploy project(back,nginx,front)") {
                 steps {
                     sh "docker-compose down -v"
			         sh "docker container prune -f"
					 sh "cd ./client && docker build . -t ${DOCKER_FRONT_CONTAINER}"                  
                     sh "docker-compose up -d"
                 }
             } 
        }   

            post { 
                  always {
                    emailext body: '${JELLY_SCRIPT,template="html"}',
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], 
                    subject: "Jenkins Build ${currentBuild.currentResult}: ${env.JOB_NAME}",
                    to: 'aleh.ulaskin@sqilsoft.by , maksim.yakavenka@sqilsoft.by',
                    mimeType: 'text/html'
                 }
                cleanup {
                    cleanWs()
                }
    	} 
}
