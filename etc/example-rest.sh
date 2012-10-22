#!/bin/sh
#Set base host
HOST=http://localhost:8080
#Set credentials
LOGIN=admin@example.com
PASSWORD=admin
#Temporary cookies storage
COOKIES_FILE=./cookies

echo "===Call auth service and store cookies in the file===\n"
curl --cookie-jar $COOKIES_FILE --data "j_username=$LOGIN&j_password=$PASSWORD" $HOST/login

echo "===Call REST service with using of stored cookies==="
echo "Call for list of glossaries...\n"
curl --cookie $COOKIES_FILE $HOST/glossaries

echo "\n"
echo "===Clear after ourselves==="
echo "Remove $COOKIES_FILE"
rm $COOKIES_FILE