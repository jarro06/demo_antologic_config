global:
  scrape_interval:     15s
  evaluation_interval: 15s
  external_labels:
    monitor: 'Alerting'
rule_files:
  - alerting.rules.yml
scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    metrics_path: '/metrics'
    static_configs:
      - targets: ['localhost:9090']
  - job_name: 'prometheus-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['prometheus-node-exporter.${PROJECT_NAME}.svc:9100']
  - job_name: 'grafana-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['grafana-node-exporter.${PROJECT_NAME}.svc:9100']
  - job_name: 'alertmanager-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['alertmanager-node-exporter.${PROJECT_NAME}.svc:9100']
  - job_name: 'jenkins-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['jenkins-node-exporter.${PROJECT_NAME}.svc:9100']
  - job_name: 'sonarqube-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['sonarqube-node-exporter.${PROJECT_NAME}.svc:9100']
  - job_name: 'nexus-node-exporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['nexus-node-exporter.${PROJECT_NAME}.svc:9100']
#  - job_name: 'logging-node-exporter'
#    scrape_interval: 5s
#    static_configs:
#      - targets: ['<ip-of-your-elk>:<prefix-port>91']
alerting:
  alertmanagers:
  - scheme: https
    static_configs:
    - targets:
      - ${ALERTMANAGER_DOMAIN}
