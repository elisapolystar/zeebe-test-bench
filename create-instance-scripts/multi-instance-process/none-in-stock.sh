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
            \"inCart\": 20,
            \"inStock\": 10
        }
    ],
    \"orderId\": \"3\"
}"