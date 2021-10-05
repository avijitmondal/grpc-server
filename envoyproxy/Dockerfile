FROM envoyproxy/envoy:v1.17.0
LABEL maintainer="avijitmondal38@gmail.com"

ADD envoy.copy.yaml /etc/envoy/envoy.yaml
ADD ssh/ /etc/envoy/certs/

EXPOSE 9090
EXPOSE 9901
