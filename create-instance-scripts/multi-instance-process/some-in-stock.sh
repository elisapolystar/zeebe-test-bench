zbctl --insecure create instance multi-instance-process --variables "{     
    \"items\": [
        {
            \"id\": 1,
            \"value\" : 10,
            \"inCart\": 10,
            \"inStock\": 5
        },
        {
            \"id\": 2,
            \"value\" : 20,
            \"inCart\": 2,
            \"inStock\": 10
        }
    ],
    \"orderId\": \"2\"
}"

sleep 60

zbctl --insecure publish message "payment-received" --correlationKey "2"