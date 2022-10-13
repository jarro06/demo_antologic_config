---
- kind: repository
  action: modify
  type: maven2-hosted
  name: maven-snapshots
  writePolicy: ALLOW
  blobStoreName: maven
  strictContent: False
  maven:
    versionPolicy: SNAPSHOT
    layoutPolicy: STRICT

- kind: repository
  action: modify
  type: maven2-hosted
  name: maven-releases
  writePolicy: ALLOW_ONCE
  blobStoreName: maven
  strictContent: False
  maven:
    versionPolicy: RELEASE
    layoutPolicy: STRICT

- kind: repository
  action: modify
  type: maven2-proxy
  name: maven-central
  blobStoreName: maven
  strictContent: False
  remoteUrl: ${NEXUS_CLUSTER_URL}/repository/maven-public
  maven:
    versionPolicy: RELEASE
    layoutPolicy: PERMISSIVE

- kind: repository
  action: modify
  type: maven2-group
  name: maven-public
  blobStoreName: maven
  strictContent: True
  memberNames:
    - maven-releases
    - maven-snapshots
    - maven-central

- kind: repository
  action: modify
  type: raw-hosted
  name: raw-hosted
  writePolicy: ALLOW
  blobStoreName: raw
  strictContent: True

- kind: repository
  action: modify
  type: raw-proxy
  name: raw-proxy
  blobStoreName: raw
  strictContent: True
  remoteUrl: ${NEXUS_CLUSTER_URL}/repository/raw-public

- kind: repository
  action: modify
  type: raw-group
  name: raw-public
  blobStoreName: raw
  strictContent: True
  memberNames:
    - raw-hosted
    - raw-proxy

- kind: repository
  action: modify
  type: npm-hosted
  name: npm-hosted
  writePolicy: ALLOW
  blobStoreName: npm
  strictContent: True

- kind: repository
  action: modify
  type: npm-proxy
  name: npm-proxy
  blobStoreName: npm
  strictContent: True
  remoteUrl: ${NEXUS_CLUSTER_URL}/repository/npm-public

- kind: repository
  action: modify
  type: npm-group
  name: npm-public
  blobStoreName: npm
  strictContent: True
  memberNames:
    - npm-hosted
    - npm-proxy
