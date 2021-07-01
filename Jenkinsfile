def getHost(){
    def remote = [:]
    remote.name = 'automation'
    remote.host = '128.21.24.61'
    remote.user = 'administrator'
    remote.port = 22
    remote.password = 'P@ssw0rd'
    remote.allowAnyHosts = true
    return remote
}

pipeline {
    agent any
      environment {
           PIPELINE_BUILD_IMAGE = "api-spica"
           PIPELINE_NAME_SPACE = "spica"
           PIPELINE_REPLICA = 2
           PIPELINE_LOAD_BALANCER_PORT=9090
           PIPELINE_EUREKA_SERVICE_ADDRESS = "http://eureka-0.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-1.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-2.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-3.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-4.eureka.cosmos.svc.cluster.local:8080/eureka,http://eureka-5.eureka.cosmos.svc.cluster.local:8080/eureka"
           PIPELINE_IMAGE = "128.21.24.60:8087/${env.PIPELINE_NAME_SPACE}/${env.PIPELINE_BUILD_IMAGE}${env.BUILD_NUMBER}:${env.BUILD_NUMBER}"
           def server = ''
           def name_space = "${env.PIPELINE_NAME_SPACE}"
           def build_image = "${env.PIPELINE_BUILD_IMAGE}"
           def latestTag = sh(returnStdout:  true, script: "git tag --sort=-creatordate | head -n 1").trim()
      }

    stages {
      stage('Checkout') {
            steps {
                script {                 
                   server = getHost()                                   
                }
			checkout([
                        $class                           : 'GitSCM',
                        branches                         : [[name: "refs/tags/${latestTag}"]],
                        userRemoteConfigs                : [[credentialsId: 'gitcredentialadmin', url: 'http://128.21.24.60:8888/spica/api-spica']],
                        doGenerateSubmoduleConfigurations: false
                ])  
            }
        } 
      
        
      stage('build') {
            steps {
              sh "mvn clean package"
           }
      }
           
     stage('dockerized') {
            steps{
                echo "Build Docker images ${env.PIPELINE_BUILD_IMAGE}"
                sh "docker build -t ${env.PIPELINE_BUILD_IMAGE} ."
              
            }
 
      }
    stage('Deploy To Docker Registry') {
            steps {
                echo "Docker Image Tag Name: ${env.PIPELINE_BUILD_IMAGE}"
                sh "docker tag ${env.PIPELINE_BUILD_IMAGE} ${env.PIPELINE_IMAGE}"
                sh "docker push ${env.PIPELINE_IMAGE}"
           }
           

    }
    
    stage("replace Env"){
        steps{
        	sh "printenv"
            echo "replace env"
            sh "ls k8s/"
            sh "envsubst < k8s/deployment.yaml > k8s/deployment${PIPELINE_NAME_SPACE}${PIPELINE_BUILD_IMAGE}.yaml "
            sh "cat k8s/deployment${PIPELINE_NAME_SPACE}${PIPELINE_BUILD_IMAGE}.yaml"            
            
            sh "pwd"
              
        }
    }
    
    stage('deploy to kubernetes') {
            steps {
                sh "ls"
                 script {
                   sshPut remote: server, from: "k8s/deployment${name_space}${build_image}.yaml", into: '.'
                   sshCommand remote: server, command: "kubectl apply -f deployment${name_space}${build_image}.yaml --record=true;kubectl -n ${name_space} rollout status deployment.app/${build_image}"
                }
                 sh "docker rmi -f ${env.PIPELINE_IMAGE}"
           }
           

    }
    
}
}