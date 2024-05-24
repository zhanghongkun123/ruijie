#!/bin/bash
#mvn -v
#java -version
#ls -l /usr/share/maven/
#cat /usr/share/maven/conf/settings.xml
#cat /root/.m2/settings.xml
#mvn -X
#mvn install -Dmaven.test.skip -e
echo $CI_PROJECT_ID
echo $CI_COMMIT_SHA
echo $CI_COMMIT_REF_NAME
mvn --batch-mode verify -Dmaven.test.skip=true sonar:sonar \
    -Dsonar.host.url=http://172.28.85.37:9000 \
    -Dsonar.login=85f07733945baba3efaeea012d14d26a1abe08e7 \
    -Dsonar.gitlab.project_id=$CI_PROJECT_ID \
    -Dsonar.gitlab.commit_sha=$CI_COMMIT_SHA \
    -Dsonar.gitlab.ref_name=$CI_COMMIT_REF_NAME \
    -Dsonar.gitlab.ci_merge_request_iid=$CI_MERGE_REQUEST_IID \
    -Dsonar.gitlab.merge_request_discussion=true \
    -Dsonar.gitlab.json_mode=CODECLIMATE \
    -Dsonar.gitlab.failure_notification_mode=commit-status

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze-preview over."
fi