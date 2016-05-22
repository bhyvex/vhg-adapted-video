curl -X DELETE localhost:8888/op/content 
curl -X PUT -d @content.xml localhost:8888/op/content -H "Content-type: application/xml"  -v
