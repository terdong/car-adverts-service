# RESTful-web-service_for_car-adverts
This is a project for demonstrating RESTful-oriented car advertising services.

자세한 내용은 [Contollers](#controllers)에서 보실 수 있습니다.

## Before Running

- 이 서비스는 DynamoDB를 필요로 합니다.
- server/conf/keys.conf 에 DynamoDB를 포함한 key 값들을 채워 주세요.

## Running

Run this using [sbt](http://www.scala-sbt.org/).

```bash
sbt run
```

And then go to <http://localhost:9000> to see the running web application.

## Fetures

각 이벤트들에 대해 설명하겠습니다.

- 시작페이지("/")
  - 등록된 자동차 광고 리스트를 보여줍니다.
  - **Create** 버튼은 자동차 광고 생성 페이지로 이동합니다.
  - **Generate100** 버튼은 한번 클릭할 때 마다 랜덤한 자동차 광고를 100개 생성합니다.
  - ** 
- 

## Controllers

- `HomeController.scala`:

  Shows how to handle simple HTTP requests.
  - 등록된 자동차 광고 리스트를 보여줍니다. 
  - **Create** 버튼을 클릭하면 자동차 광고 생성 페이지로 이동합니다.
  - **
  
  

- `AsyncController.scala`:

  Shows how to do asynchronous programming when handling a request.

- `CountController.scala`:

  Shows how to inject a component into a controller and use the component when
  handling requests.

## Components

- `Module.scala`:

  Shows how to use Guice to bind all the components needed by your application.

- `Counter.scala`:

  An example of a component that contains state, in this case a simple counter.

- `ApplicationTimer.scala`:

  An example of a component that starts when the application starts and stops
  when the application stops.

## Filters

- `Filters.scala`:

  Creates the list of HTTP filters used by your application.

- `ExampleFilter.scala`:

  A simple filter that adds a header to every response.
