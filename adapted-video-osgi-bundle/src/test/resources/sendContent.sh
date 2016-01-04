curl -X DELETE localhost:8888/op/content 
cat content.xml|curl -X PUT -d - localhost:8888/op/content -H "Content-type: application/xml"  
