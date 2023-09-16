
# Routing reporter

create, manager and get reports around route


![Logo](https://raw.githubusercontent.com/neshan-camp-alishahidi/routing-reporter/main/src/main/resources/logo.jpeg?token=GHSAT0AAAAAACFYD4ZKXLCZPX7EYL75524GZIFMEGQ)


## Configuration
project have some configuration you can set in `application.yaml` file

### TTLs
we have some ttl config for report

#### Init ttl
for example `report.traffic.init.ttl` its set as report ttl when traffic report created or accepted

#### Like ttl
`report.traffic.like.ttl` its set when report liked

**Note**: ttl for the like section is calculated from the time of liking and is not added to the previous value


#### DisLike ttl
`report.traffic.dislike.ttl` its increase from previous value when report disliked

```yaml
report:
  traffic:
    init:
      ttl: 5
    like:
      ttl: 5
    dis-like:
      ttl: 1
```


## Environment Variables

To change project defaut settings, you will need sett the following environment variables to your system or docker-compose file

`JWT_TOKEN`

`REDIS_HOST`, `REDIS_PORT`

`DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASS`, `DB_SCHEMA`



## Getting start

### Docker
if you want use docker for start project you can simple run it with

```bash
  docker compose up -d
```

for re building project
first run:
```bash
mvn package
```
and second:
```bash
docker compose build --no-cache
```

### Maven
if you dont want use docker please make sure reddis and postgres is run and start project with:

```bash
mvn spring-boot:run
```


## API Reference (v1)

### Register user

```http
  POST /api/v1/auth/regsiter
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing user. |

#### Request body
```json
{
	"name": "Ali Shahidi",
	"username": "alishahidi",
	"password": "12345678"
}
```

#### Curl
```bash
curl --location --request POST '{host}/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--data '{
	"name": "Ali Shahidi",
	"username": "alishahidi",
	"password": "12345678"
}'
```

### Login user

```http
  POST /api/v1/auth/login
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing user. |

#### Request body
```json
{
	"username": "alishahidi",
	"password": "12345678"
}
```

#### Curl
```bash
curl --location --request POST '{host}/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
	"username": "alishahidi",
	"password": "12345678"
}'
```

### Get user

```http
  GET /api/v1/auth

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

#### Curl
```bash
curl --location '{host}/api/v1/auth' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {auth_key}'
```

### Create report

```http
  POST /api/v1/report

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing report and report type.| 

#### Request body
```json
{
    "type": "TRAFFIC",
    "report": {
        "location": {
            "coordinates": [
                59.55950236364649,
                36.29250312308071
            ],
            "type": "Point"
        }
    }    
}
```

#### Curl
```bash
curl --location --request POST '{host}api/v1/report' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {auth_key}' \
--data '{
    "type": "TRAFFIC",
    "report": {
        "location": {
            "coordinates": [
                59.55950236364649,
                36.29250312308071
            ],
            "type": "Point"
        }
    }    
}'
```

### Like report

```http
  PUT /api/v1/report/like/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. report id.       |

#### Curl
```bash
curl --location --request PUT '{host}/api/v1/like/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

### Dislike report

```http
  PUT /api/v1/report/dislike/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. report id.       |


#### Curl
```bash
curl --location --request PUT '{host}/api/v1/dislike/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

### Get top hours

```http
  GET /api/v1/report/top/{limit}/{type}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `limit` | `integer` | **Required**. limit of hours count.       |
| `type` | `string` | **Required**. report type.       |

#### Curl
```bash
curl --location '{host}/api/v1/report/top/{limit}/{type}'
```

### Get all reports

```http
  GET /api/v1/operator/report/get

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

#### Curl
```bash
curl --location '{host}/api/v1/operator/report/get' \
--header 'Authorization: Bearer {auth_key}'
```

### Get all reports by type

```http
  GET /api/v1/operator/report/get?type=<type>

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `type` | `param` | **Optional**. type of report.       |

#### Curl
```bash
curl --location '{host}/api/v1/operator/report/get?type={type}' \
--header 'Authorization: Bearer {auth_key}'
```

### Accept report

```http
  PUT /api/v1/operator/report/accept/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. id of report.       |

#### Curl
```bash
curl --location --request PUT '{host}/api/v1/report/accept/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

### Get around route reports
```http
  PUT /api/v1/route/reports

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing route.       |


#### Request body
```json
{
    "route": {         
        "type": "LineString",
        "coordinates": [
          [
            59.557710772175795,
            36.28857021126669
          ],
          [
            59.55968013271732,
            36.292981810238715
          ]
        ]
      }
}
```

#### Curl
```bash
curl --location --request PUT '{host}/api/v1/route/reports' \
--header 'Authorization: Bearer {auth_key}' \
--data '{
    "route": {         
        "type": "LineString",
        "coordinates": [
          [
            59.557710772175795,
            36.28857021126669
          ],
          [
            59.55968013271732,
            36.292981810238715
          ]
        ]
      }
}'
```


## Authors

- [@alishahidi](https://www.github.com/alishahidi)

