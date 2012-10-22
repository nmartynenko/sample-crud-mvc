@echo off
rem Set base host
set HOST="http://localhost:8080"
rem Set credentials
set LOGIN="admin@example.com"
set PASSWORD="admin"
rem Temporary cookies storage
set COOKIES_FILE="cookies.txt"

echo.
echo ===Call auth service and store cookies in the file===
curl --cookie-jar %COOKIES_FILE% --data "j_username=%LOGIN%&j_password=%PASSWORD%" %HOST%/login

echo. 
echo ===Call REST service with using of stored cookies===
echo Call for list of glossariasdasdsaes...
curl --cookie %COOKIES_FILE% %HOST%/glossaries

echo.
echo.
echo ===Clear after ourselves===
echo Remove %COOKIES_FILE%
del %COOKIES_FILE%