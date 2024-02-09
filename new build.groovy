pipeline {
    agent { 
        label 'closer_1' 
        }
    stages {
        stage('git commit') {
            steps {
                echo 'clone repositery consist of build packages'
                git 'https://github.com/Prajakta27d/studentapp-ui.git'
                  }
             }
        stage('build') {
            steps {
                sh '''
                echo "installing maven package for build"
                sudo apt update
                sudo apt install maven -y
                echo "creating package from maven"
                sudo mvn clean 
                sudo mvn package 
                '''
            }
        }
        stage('artifacts') {
            steps {
              echo 'added artifacts in bucket'
              'aws s3 cp /studentapp-ui/target/* s3://myawscdecb22'

             }
           }
        stage('application') {
            steps {
            sh '''
              echo "install tomcat-server"
              wget https://dlcdn.apache.org/tomcat/tomcat-8/v8.5.98/bin/apache-tomcat-8.5.98.zip
              sudo apt-get install unzip -y
              unzip apache-tomcat-8.5.98.zip
              cp ./target/studentapp-2.2-SNAPSHOT.war ./apache-tomcat-8.5.98/webapps/studentapp.war
              sh ./apache-tomcat-8.5.98/bin/catalina.sh start
            '''
            }
        }
        stage('pull repo') {
            steps {
            echo 'pull github repositery'
            git 'https://github.com/Prajakta27d/studentapp-ui.git'

            }
        }
    }
}