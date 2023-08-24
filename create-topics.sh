sleep 10
while IFS= read -r topic; do
    kafka-topics.sh --create --zookeeper zookeeper:32181 --partitions 1 --replication-factor 1 --if-not-exists --topic "$topic";
done </topics.txt
