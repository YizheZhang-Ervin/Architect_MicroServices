# MessageQueue

## Docker

docker pull rabbitmq:3-management docker load -i mq.tar

docker run \
-e RABBITMQ_DEFAULT_USER=root \
-e RABBITMQ_DEFAULT_PASS=root001 \
--name mq \
--hostname mq1 \
-p 15672:15672 \
-p 9000:9000 \
-d \
rabbitmq:3-management