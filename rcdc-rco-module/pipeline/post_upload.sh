#!/bin/bash


echo ${ARTIFACT_DIR}
echo ${ARTIFACT_FILE_NAME}
echo ${ARTIFACT_REPO_DIR}

#如果制品输出目录为空，使用默认脚本收集到指定制品目录
if [ "`ls -A ${ARTIFACT_DIR}`" = "" ]; then
    echo "artifact dir is empty, use default rule collect artifact..."
    find . -name "*.war" -o -name "*.jar" | grep -v WEB-INF |xargs -i cp -rf '{}' ${ARTIFACT_DIR}
else
    echo "artifact dir is not Empty"
fi

#压缩制品为zip文件,压缩到工作空间目录下
if [ "`ls -A ${ARTIFACT_DIR}`" = "" ]; then
    echo "not find artifact, skip upload"
    exit 0
fi

#校验jar包内容是否包含thrift
function showJarContent() {
  cd $1
  check_dir="/var/pipeline"
  mkdir -p $check_dir
    for jar in $(ls | grep '^rcdc-rco-module-openapi-[0-9.]*-SNAPSHOT.jar');do
      echo "校验jar包是否包含thrift：$jar"
      cp $jar ${check_dir}/$jar
      cd ${check_dir}
      jar -xf ${check_dir}/$jar
      pwd
      ls -al
      tree $pwd -a -N -L 4

      DIR="generated_sources/thrift/"
      res=`ls -A ${DIR}`
        if [ -z $res ];then
          echo "$DIR 目录为空，thrift【【生成失败】】.................."
          tree | grep thrift
          exit 2
        else
          echo "$DIR 目录不为空，thrift【【生成成功】】................"
          tree | grep thrift
        fi
    done
}

showJarContent ${ARTIFACT_DIR}


zip -r -j ${ARTIFACT_FILE_NAME} ${ARTIFACT_DIR}/* 

#上传制品
curl -u ${ARTIFACT_USER_NAME}:${ARTIFACT_PASSWORD} -X POST "${ARTIFACT_REPO_URL}" -F "raw.directory=${ARTIFACT_REPO_DIR}" -F "raw.asset1=@${ARTIFACT_FILE_NAME};type=application/x-gzip" -F "raw.asset1.filename=${ARTIFACT_FILE_NAME}"
if [ $? -ne 0 ];then
  echo "[ERROR] upload artifact failed. Please check your artifact first!"
  exit 2
else
  echo "[INFO] upload artifact Done"
fi