input {
  tcp {
    port => ENTER_LOGSTASH_PORT_HERE
    type => cf_app
    host => "0.0.0.0"
  }
}

filter {
  if [type] == "cf_app" {
    grok {
      # Generic Pivotal Cloud Foundry (PFC) logs
      match => { "message" => "%{SYSLOG5424PRI}%{NONNEGINT:syslog5424_ver} +(?:%{TIMESTAMP_ISO8601:syslog5424_ts}|-) +(?:%{HOSTNAME:syslog5424_host}|-) +(?:%{NOTSPACE:syslog5424_app}|-) +(?:%{NOTSPACE:syslog5424_proc}|-) +(?:%{WORD:syslog5424_msgid}|-) +(?:%{SYSLOG5424SD:syslog5424_sd}|-|) +%{GREEDYDATA:syslog5424_msg}" }
    }
    syslog_pri { }
    date {
      match => [ "syslog_timestamp", "MMM  d HH:mm:ss", "MMM dd HH:mm:ss" ]
    }
    if !("_grokparsefailure" in [tags]) {
      mutate {
        replace => [ "@source_host", "%{syslog_hostname}" ]
        replace => [ "@message", "%{syslog_message}" ]
      }
    }
    mutate {
      remove_field => [ "syslog_hostname", "syslog_message", "syslog_timestamp" ]
    }
  }
}

output {
  if [type] == "cf_app" {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "cf-%{+YYYY.MM.dd}"
      user => "${LOGGING_USERNAME}"
      password => "${LOGGING_PASSWORD}"
    }
  }
}
