input {
  heartbeat {
    interval => 300
    type => heartbeat
  }
}

output {
  if [type] == "heartbeat" {
   elasticsearch {
      hosts => ["localhost:9200"]
      index => "hb-%{+YYYY.MM.dd}"
      user => "${LOGGING_USERNAME}"
      password => "${LOGGING_PASSWORD}"
    }
  }
}
