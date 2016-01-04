curl -X DELETE localhost:8888/op/config 
cat config.xml |curl -X PUT -d - localhost:8888/op/config -H "Content-type: application/xml"  
