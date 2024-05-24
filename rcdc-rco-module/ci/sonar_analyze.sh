#!/bin/bash

mvn --batch-mode sonar:sonar \
    -Dsonar.host.url=http://172.28.85.37:9000 \
    -Dsonar.login=85f07733945baba3efaeea012d14d26a1abe08e7 \
    -Dsonar.issuesReport.html.enable=true \
    -Dsonar.analysis.mode=preview \
    -Dsonar.preview.excludePlugins=issueassign,scmstats

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze over."
fi