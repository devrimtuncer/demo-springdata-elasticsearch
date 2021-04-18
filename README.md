This demo project has an Elasticsearch BulkProcessor based on Spring Boot.

Follow steps at https://www.elastic.co/downloads/elasticsearch and start Elasticsearch at your local machine. By
default, Elasticsearch runs at http://localhost:9200

Follow steps at https://www.elastic.co/downloads/kibana and start Kibana at your local machine. By default, Kibana runs
at http://localhost:5601

Run this project and browse http://localhost:8080/ to add an item to BulkProcessor. Check your items at Elasticsearch
via Kibana.

Launch Kibana and define an index:

1. Open http://localhost:5601
2. Go to Stack Management / index patterns / Create Index Pattern
3. Type `actions*` (which is defined at `BulkService.java`) and create index pattern
4. Go to Discover and choose `actions*` index pattern

