#! /bin/sh

BASE_IP="192.168.150.101"
while getopts "i:" opt; do
    case $opt in
         i)
            BASE_IP=$OPTARG
          ;;
         ?)
            echo "unkonw argument"
            exit 1
          ;;
    esac
done

echo "========= start init CI/CD enviroment=========="
BASE_PATH=/usr/local/src
tar -zxf $(pwd)/tjxt.env.tar.gz -C /usr/local/ --same-permissions

echo "=== 1.begin set JAVA_HOME==="
if command -v java &> /dev/null;then
    java -version       
    echo "INFO:Already Install Jdk"
else
    tee -a /etc/profile << EOF
    export JAVA_HOME=${BASE_PATH}/java
    export PATH=\$JAVA_HOME/bin:\$PATH
    export CLASS_PATH=\$JAVA_HOME/lib
EOF
    source /etc/profile
    if command -v java &> /dev/null;then     
    echo "INFO:Jdk Install Success..."
    else
        echo "ERROR:Jdk Install Fail..." && exit 1
    fi
fi

echo "=== 2.begin config command alias ==="
cp ${BASE_PATH}/.bashrc ~
source ~/.bashrc

echo "=== 3.begin docker compose ==="
# 设置jenkins用户，并关联docker
USER_NAME=jenkins
if id -u ${USER_NAME} >/dev/null 2>&1 ; then
    echo "try create user jenkins"
else
    useradd -U -m jenkins
fi
if id -u ${USER_NAME} >/dev/null 2>&1 ; then
    echo "create user jenkins success"
fi
usermod -aG docker jenkins
usermod -aG root jenkins
chmod 777 /var/run/docker.sock

# 设置虚拟机真实IP
if [ $BASE_IP != "192.168.150.101" ];then
    sed -i -e "s#192.168.150.101#${BASE_IP}#g" ${BASE_PATH}/jenkins/jobs/tjxt-dev-build/config.xml
    sed -i -e "s#192.168.150.101#${BASE_IP}#g" ${BASE_PATH}/nginx/conf/nginx.conf
    sed -i -e "s#192.168.150.101#${BASE_IP}#g" ${BASE_PATH}/gogs/gogs/conf/app.ini
fi


dc -f ${BASE_PATH}/docker-compose.yml up -d

echo "=== 4.begin set cron to clean logs ==="
echo "0 21 * * * root sh ${BASE_PATH}/clean-docker-logs.sh" >> /etc/crontab

echo "========= init CI/CD enviroment success!! =========="

rm -f $(pwd)/tjxt.env.tar.gz

echo "===== please reboot your System ====="