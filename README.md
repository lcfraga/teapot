# teapot

An HTTP API for creating and adjusting beverages, and ordering them from a teapot.


## Minimum requirements

OpenJDK `11` and recent versions of `docker` and `docker-compose`.


## Build

To run the build and produce aggregate test, coverage and static analysis reports, run:

```sh
./gradlew clean check aggregateTestReports detektAll aggregateJacocoReport --info
```

These aggregate reports will be saved to:

* `build/reports/aggregate-tests/index.html`;
* `build/reports/jacoco/aggregate/html/index.html`;
* `build/reports/detekt/detekt.html`.

To build a docker image of our application:

```sh
./gradlew jibDockerBuild
```

If there are formatting errors that detekt can fix, we can run:

```sh
./gradlew detektFormat
```


## Running

The docker composition requires the following ports to be available on the host:

| Docker service  | Host port |
| --------------- | --------- |
| `teapot`        |  `8080`   |
| `postgresql`    |  `5432`   |
| `prometheus`    |  `9090`   |
| `grafana`       |  `3000`   |

If those ports are available, build the docker image and start the docker composition:

```sh
./gradlew jibDockerBuild
docker-compose up
```

Once that's done, we can use the `EXPOSED_TEAPOT_PORT` environment variable to override the host port at which the teapot service is exposed:

```sh
EXPOSED_TEAPOT_PORT=8081 docker-compose up
```


### Monitoring

The main docker composition includes 2 monitoring services:

| Monitoring service                      | URL                    | Credentials   |
| --------------------------------------- | ---------------------- | ------------- |
| [Grafana](https://grafana.com/)         | http://localhost:3000  | `admin:admin` |
| [Prometheus](https://prometheus.io/)    | http://localhost:9090  |               |

You can use the `EXPOSED_GRAFANA_PORT` and `EXPOSED_PROMETHEUS_PORT` environment variables to override the host ports at which the monitoring services are exposed. For example, to expose the Grafana frontend on port `3001` and the Prometheus frontend on port `9091`, run:

```
EXPOSED_GRAFANA_PORT=3001 EXPOSED_PROMETHEUS_PORT=9091 script/docker/compose up
```

Grafana starts with one provisioned dashboard for Prometheus; Prometheus collects metrics from itself, Grafana and our application.


## Running PostgreSQL for development

While developing, it's likely we'll only use the PostgreSQL docker service:

```
docker-compose up postgresql
```

This will start PostgreSQL, which will be available at port `5432`. You can use the `EXPOSED_POSTGRESQL_PORT` environment variable to override the host ports at which the database services are exposed. For example, to expose PostgreSQL on port `5433` run:

```
EXPOSED_POSTGRESQL_PORT=5433 docker-compose up postgresql
```
