# Elastic Stack

## Usage
- ./filebeat -e -c ervin-filebeat-dashboard.yml
- ./bin/logstash -f ervin-logstash-dashboard.conf
- su elsearch
- cd /soft/elsearch/bin
- ./elasticsearch
- ./bin/kibana
- http://202.193.56.222:5601/app/kibana
- 添加Logstash索引到Kibana中http://202.193.56.222:5601/app/management/kibana/indexPatterns/create
- 创柱状图/饼图/数据表格->放入Dashboard