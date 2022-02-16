dir=`pwd`
cd ../
docker-compose up -d 
cd $dir
docker build . -t node/node-web-appcourses
docker network create courses_default
docker run -p :9000 -p :9001 -d --name jhipster_courses_front -d --net=courses_default -d node/node-web-appcourses

