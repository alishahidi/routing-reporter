<!-- TOC --><a name="routing-reporter"></a>
# Routing reporter
create, manager and get reports around route

![Logo](https://assets-global.website-files.com/6050a76fa6a633d5d54ae714/609147088669907f652110b0_report-an-issue(about-maps).jpeg)

<!-- TOC start (generated with https://github.com/derlin/bitdowntoc) -->

- [Routing reporter](#routing-reporter)
    * [Configuration](#configuration)
        + [TTLs](#ttls)
            - [Init ttl](#init-ttl)
            - [Like ttl](#like-ttl)
            - [DisLike ttl](#dislike-ttl)
    * [Environment Variables](#environment-variables)
    * [Getting start](#getting-start)
        + [Docker](#docker)
        + [Maven](#maven)
    * [API Reference (v1)](#api-reference-v1)
        + [Register user](#register-user)
            - [Request body](#request-body)
            - [Curl](#curl)
        + [Login user](#login-user)
            - [Request body](#request-body-1)
            - [Curl](#curl-1)
        + [Get user](#get-user)
            - [Curl](#curl-2)
        + [Create report](#create-report)
            - [Request body](#request-body-2)
            - [Curl](#curl-3)
        + [Like report](#like-report)
            - [Curl](#curl-4)
        + [Dislike report](#dislike-report)
            - [Curl](#curl-5)
        + [Get top hours](#get-top-hours)
            - [Curl](#curl-6)
        + [Get all reports](#get-all-reports)
            - [Curl](#curl-7)
        + [Get all reports by type](#get-all-reports-by-type)
            - [Curl](#curl-8)
        + [Accept report](#accept-report)
            - [Curl](#curl-9)
        + [Get around route reports](#get-around-route-reports)
            - [Request body](#request-body-3)
            - [Curl](#curl-10)
    * [Authors](#authors)

<!-- TOC end -->

<!-- TOC --><a name="configuration"></a>
## Configuration
project have some configuration you can set in `application.yaml` file

<!-- TOC --><a name="ttls"></a>
### TTLs
we have some ttl config for report

<!-- TOC --><a name="init-ttl"></a>
#### Init ttl
for example `report.traffic.init.ttl` its set as report ttl when traffic report created or accepted

<!-- TOC --><a name="like-ttl"></a>
#### Like ttl
`report.traffic.like.ttl` its set when report liked

**Note**: ttl for the like section is calculated from the time of liking and is not added to the previous value


<!-- TOC --><a name="dislike-ttl"></a>
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


<!-- TOC --><a name="environment-variables"></a>
## Environment Variables

To change project defaut settings, you will need sett the following environment variables to your system or docker-compose file

`JWT_TOKEN`

`REDIS_HOST`, `REDIS_PORT`

`DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASS`, `DB_SCHEMA`



<!-- TOC --><a name="getting-start"></a>
## Getting start

<!-- TOC --><a name="docker"></a>
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

<!-- TOC --><a name="maven"></a>
### Maven
if you dont want use docker please make sure reddis and postgres is run and start project with:

```bash
mvn spring-boot:run
```


<!-- TOC --><a name="api-reference-v1"></a>
## API Reference (v1)

<!-- TOC --><a name="register-user"></a>
### Register user

```text
  POST /api/v1/auth/regsiter
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing user. |

<!-- TOC --><a name="request-body"></a>
#### Request body
```json
{
	"name": "Ali Shahidi",
	"username": "alishahidi",
	"password": "12345678"
}
```

<!-- TOC --><a name="curl"></a>
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

---
<!-- TOC --><a name="login-user"></a>
### Login user

```text
  POST /api/v1/auth/login
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing user. |

<!-- TOC --><a name="request-body-1"></a>
#### Request body
```json
{
	"username": "alishahidi",
	"password": "12345678"
}
```

<!-- TOC --><a name="curl-1"></a>
#### Curl
```bash
curl --location --request POST '{host}/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
	"username": "alishahidi",
	"password": "12345678"
}'
```

---
<!-- TOC --><a name="get-user"></a>
### Get user

```text
  GET /api/v1/auth

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

<!-- TOC --><a name="curl-2"></a>
#### Curl
```bash
curl --location '{host}/api/v1/auth' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="create-report"></a>
### Create report

```text
  POST /api/v1/report

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing report and report type.| 

<!-- TOC --><a name="request-body-2"></a>
#### Request body
```json
{
  "type": "POLICE",
  "policeType": "HIDDEN",
  "report": {
    "location": "POINT (59.563914262091146 36.301465408673664)"
  }
}
```

<!-- TOC --><a name="curl-3"></a>
#### Curl
```bash
curl --location --request POST '{host}api/v1/report' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {auth_key}' \
--data '{
    "type": "POLICE",
    "policeType": "HIDDEN",
    "report": {
        "location": "POINT (59.563914262091146 36.301465408673664)"
    }
}'
```

---
<!-- TOC --><a name="like-report"></a>
### Like report

```text
  PUT /api/v1/report/like/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. report id.       |

<!-- TOC --><a name="curl-4"></a>
#### Curl
```bash
curl --location --request PUT '{host}/api/v1/report/like/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="dislike-report"></a>
### Dislike report

```text
  PUT /api/v1/report/dislike/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. report id.       |


<!-- TOC --><a name="curl-5"></a>
#### Curl
```bash
curl --location --request PUT '{host}/api/v1/report/dislike/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="get-top-hours"></a>
### Get top hours

```text
  GET /api/v1/report/top/{limit}/{type}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `limit` | `integer` | **Required**. limit of hours count.       |
| `type` | `string` | **Required**. report type.       |

<!-- TOC --><a name="curl-6"></a>
#### Curl
```bash
curl --location '{host}/api/v1/report/top/{limit}/{type}'
```

---
<!-- TOC --><a name="get-all-reports"></a>
### Get all reports

```text
  GET /api/v1/operator/report/get

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

<!-- TOC --><a name="curl-7"></a>
#### Curl
```bash
curl --location '{host}/api/v1/operator/report/get' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="get-all-reports-by-type"></a>
### Get all reports by type

```text
  GET /api/v1/operator/report/get?type=<type>

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `type` | `param` | **Optional**. type of report.       |

<!-- TOC --><a name="curl-8"></a>
#### Curl
```bash
curl --location '{host}/api/v1/operator/report/get?type={type}' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="accept-report"></a>
### Accept report

```text
  PUT /api/v1/operator/report/accept/{id}

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `integer` | **Required**. id of report.       |

<!-- TOC --><a name="curl-9"></a>
#### Curl
```bash
curl --location --request PUT '{host}/api/v1/report/accept/{id}' \
--header 'Authorization: Bearer {auth_key}'
```

---
<!-- TOC --><a name="get-around-route-reports"></a>
### Get around route reports
```text
  PUT /api/v1/route/reports

  Headers:
  Authorization: Bearer YOUR_ACCESS_TOKEN
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `body` | `object` | **Required**. object containing route.       |


<!-- TOC --><a name="request-body-3"></a>
#### Request body
```json
{
  "route": "LINESTRING (59.557710772175795 36.28857021126669, 59.55968013271732 36.292981810238715)"
}
```

<!-- TOC --><a name="curl-10"></a>
#### Curl
```bash
curl --location --request PUT '{host}/api/v1/route/reports' \
--header 'Authorization: Bearer {auth_key}' \
--data '{
    "route": "LINESTRING (59.557710772175795 36.28857021126669, 59.55968013271732 36.292981810238715)"
}'
```

---

<!-- TOC --><a name="authors"></a>
## Authors

- [@alishahidi](https://www.github.com/alishahidi)

