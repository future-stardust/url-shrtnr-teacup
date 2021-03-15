User Guide
======

Endpoints Roadmap
------

* [Sign Up](#sign-up)
* [Log In](#log-in)
* [Generate alias for URL](#generate-alias-for-url)
* [Save URL with user's alias](#save-url-with-users-alias)
* [List user’s shortened links](#list-users-shortened-links) 
* [Redirect by shortened URL](#redirect-by-shortened-url)
* [Delete shortened link](#delete-shortened-link)

Endpoints
------

### **Sign Up**
```bash
curl --location --request POST 'http://localhost:8080/users/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test@test.com",
	  "password": "testPassword"
  }'
```
#### Output in case of success
```text
User was successfully created.
```

### **Log In**
```bash
curl --location --request POST 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "test@test.com",
	  "password": "testPassword"
  }'
```
#### Output in case of success
```json
{
  "username": "test@test.com.",
  "access_token": "token",
  "token_type": "Bearer",
  "expires_in": 1500
}
```

### **Generate alias for URL**
```bash
curl --location --request POST 'http://localhost:8080/urls/shorten' \
--header 'Authorization: Bearer <token>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://google.com"
}'
```
#### Output in case of success
```text
Shortened URL: http://localhost:8080/r/someAlias
```

### **Save URL with user's alias**
```bash
curl --location --request POST 'http://localhost:8080/urls/shorten' \
--header 'Authorization: Bearer <token>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://google.com",
	  "alias": "userAlias"
  }'
```
#### Output in case of success
```text
Shortened URL: http://localhost:8080/r/userAlias
```

### **List user’s shortened links**
```bash
curl --location --request GET 'http://localhost:8080/urls/' \
--header 'Authorization: Bearer <token>'
```
#### Output in case of success
```json
[
  {
    "alias": "someAlias",
    "url": "https://google.com"
  },
  {
    "alias": "userAlias",
    "url": "https://google.com"
  }
]
```

### **Redirect by shortened URL**
```bash
curl --location --request GET 'localhost:8080/r/someAlias'
```

### **Delete shortened link**
```bash
curl --location --request DELETE 'localhost:8080/urls/example' \
    --header 'Authorization: Bearer <token>'
```
#### Output in case of success
```text
Alias was successfully deleted.
```
