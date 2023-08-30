arch=$(uname -m)

if [[ $arch == arm64 ]]
then
    compose=docker-compose-arm.yml
    kafkatopics=/opt/kafka/bin/kafka-topics.sh
else
    compose=docker-compose.yml
    kafkatopics=/opt/bitnami/kafka/bin/kafka-topics.sh
fi

kafkacontainer=zeebe-test-bench-kafka-1


docker compose -f $compose up -d postgres
docker compose -f $compose up -d --force-recreate kafka

k_started=""
while [[ $k_started == "" ]]; do
    k_started=$(docker compose logs kafka 2>&1 | grep 'started (kafka.server.KafkaServer)')
done

while read topic; do docker exec -t $kafkacontainer $kafkatopics --create --bootstrap-server kafka:9092 \
    --replication-factor 1 --partitions 1 --if-not-exists --topic "$topic"; done < topics


docker compose -f $compose up -d zeebe
docker compose -f $compose up -d zeebe-simple-monitor
docker compose -f $compose up -d postgres