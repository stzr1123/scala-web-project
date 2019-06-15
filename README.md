# scala-web-project
Following the instruction from the book Modern Web Development with Scala by Denis Kalinin.

Simple Web app that displays weather and other info for Munich.

* Backend is Play 2.7
* React is used for rendering a simple table
* Concise is used for styling
* Users are stored in Postgres

Test user (automatically populated) is `stest` with pass `password123`.

## Run Postgres

Users are stored in Postgres.

```$xslt
# Run Postgres
$ docker run -d --name pg-scala-web-project -e POSTGRES_PASSWORD=1234 -p 5432:5432 -v $(pwd)/misc/data/pg-scala-web-project:/var/lib/postgresql/data postgres
```
```$xslt
# Create scalauser inside postgres
$ docker exec -ti pg-scala-web-project bash
$ su - postgres
$ psql --command "CREATE USER scalauser WITH SUPERUSER PASSWORD 'scalapass'"
```
```$xslt
# Create db scaladb for the project
$ docker exec -ti pg-scala-web-project bash
$ su - postgres
$ createdb -O scalauser scaladb
# If you want to run any queries within psql
$ psql -d scaladb
```

## Run nodejs
```$xslt
# Use nvm to manage node versions
$ nvm install v8.9.4
$ nvm use v8.9.4
$ nvm install-latest-npm
$ npm install
# Bundle and watch for code changes
$ npm run watch
```

## Build and run scala project
```$xslt
$ sbt run
```

## Run tests
```$xslt
$ sbt test
```
