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

## Funtions

Explains each function.

- Common functions
  - **Home** 버튼은 현재 페이지로 이동합니다. (또는 "Car Adverts Demo" caption)
  - **Fuel** 버튼은 Fuel 관리 페이지로 이동합니다.

- Home Page(url: "/")
  - Basically this page shows the registered car advert list.
  - Car advert filtering by price function: **Filter** 버튼은 Input Tag에 입력한 가격 이상의 차 광고만 필터링한 리스트를 보여줍니다.
  - Car advert search by id function: **Search** 버튼은 Input Tag에 입력한 ID만 찾아서 화면에 보여줍니다. 
  - Car advert creation function: **Create** 버튼은 자동차 광고 생성 페이지로 이동합니다.
  - Car advert auto generation function: **Generate100** 버튼은 한번 클릭할 때 마다 랜덤한 자동차 광고를 100개 생성합니다.
  - Car advert id copy function: **Copy ID** 버튼은 Search 할 ID를 클립보드로 복사시켜줍니다.
  - Car advert modification function: **Edit** 버튼은 해당 car advert를 수정 할수 있는 modal을 보여줍니다.
  
- Creation Page(url: "/caradverts/new")
  - You can create a car advert by entering the following fields.
  
- Modification Modal
  - You can modify that car advert. 

- Fuel Page(url: "/fuels")
  - Fuel creation function: Input tag에 fuel name을 입력하고 **Create** 버튼을 누르면 fuel이 생성됩니다.
  - Fuel deletion function: **Delete** 버튼을 누르면 해당 fuel이 바로 삭제됩니다.
   

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
