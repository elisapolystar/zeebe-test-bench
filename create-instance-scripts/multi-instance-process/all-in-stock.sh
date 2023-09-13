zbctl --insecure create instance multi-instance-process --variables "{     
    "items": [
        {
            "id": 1,
            "value" : 10,
            "inCart": 10,
            "inStock": 20
        },
        {
            "id": 2,
            "value" : 20,
            "inCart": 2,
            "inStock": 10
        }
    ],
    "orderId": "1"
}"