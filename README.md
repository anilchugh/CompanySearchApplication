# CompanySearch Application

## Build application 
mvn clean install

## Start up application
mvn spring-boot:run

## Testing

Sample curl request to search for active company : 

curl --location 'http://localhost:8080/www.company.com/search?isActive=true' \
--header 'x-api-key: PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf' \
--header 'Content-Type: application/json' \
--data '{
"companyName" : "BBC LIMITED",
"companyNumber": "07520089"
}'

Sample curl to search for dissolved company :

curl --location 'http://localhost:8080/www.company.com/search' \
--header 'x-api-key: PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf' \
--header 'Content-Type: application/json' \
--data '{
"companyName" : "BBC LIMITED",
"companyNumber": "07520089"
}'

