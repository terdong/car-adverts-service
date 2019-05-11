# RESTful-web-service_for_car-adverts

This is a project for demonstrating RESTful-oriented car advertising services.

There are two main menus.
- Car advertising service
- Fuel list management

## Before Running

- This service needs DynamoDB.
- Please fill the key:values in server/conf/keys.conf including DynamoDB conf.

## Running

Run this using [sbt](http://www.scala-sbt.org/).

```bash
sbt run
```

And then go to <http://localhost:9000> to see the running web application.

## Funtions

Explains each function.

- Common functions
  - Clicking **Home** button goes to "Home Page". (or "Car Adverts Demo" caption)
  - Clicking **Fuel** button goes to "Fuel Page".

- Home Page(url: "/")
  - Basically this page shows the registered car advert list.
  - Car advert filtering by price function: Clicking **Filter** button shows a list of filtered car advert for over the price you entered in the input tag.
  - Car advert search by id function: Clicking **Search** button shows a car advert by the id you entered in the input tag.
  - Car advert creation function: Clicking **Create** button goes to the car advert creation page.
  - Car advert auto generation function: Each Clicking **Generate100** button once generates 100 random car adverts.
  - Car advert id copy function: Clicking **Copy ID** button copies the ID searching to the clipboard.
  - Car advert modification function: Clicking **Edit** button shows the modal to modify that car advert.
  
- Creation Page(url: "/caradverts/new")
  - You can create a car advert by entering the following fields.
  
- Modification Modal
  - You can modify that car advert. 
  - You also can delete that car advert by clicking delete button.

- Fuel Page(url: "/fuels")
  - Fuel creation function: A fuel will be create when click **Create** button after enter a fuel name in the input tag.
  - Fuel deletion function: Click the **Delete** button to delete the fuel.
  
## Controllers

- `HomeController.scala`:

  Shows how to handle requests related to car adverts.
   
- `FuelController`:
  Shows how to handle requests related to fuels.

## Components

- `Module.scala`:

  Shows how to use Guice to bind all the components needed by your application.
  
- `DynamoDbProvider.scala`:
    
  - A wrapped provider of DynamoDB that provides api about DynamoDB.
  - I used the [scanamo](https://github.com/scanamo/scanamo) library.
  
- `DynamoDbTableManager.scala`:

  a component that creates and manages tables to use in this project.
  
## Used libraries

- In server
  - [play framework 2.7.2](https://github.com/playframework/playframework) - for back-end developing with play-json
  - [java-uuid-generator 3.2.0](https://github.com/cowtowncoder/java-uuid-generator)
  - [scanamo 1.0.0-M9](https://github.com/scanamo/scanamo) - for simply accessing DynamoDB
  
- In client
  - [scala-js 0.6.27](https://github.com/scala-js/scala-js) - for front-end developing
  - [scala-js-dom 0.9.7](https://github.com/scala-js/scala-js-dom)
  - [binding.scala 11.7.0](https://github.com/ThoughtWorksInc/Binding.scala) - for reactive web design
  - [bootstrap 4.3.1](https://github.com/twbs/bootstrap)
  - [bootstrap-native 2.0.26](https://github.com/thednp/bootstrap.native)
  
- For test
  - [scalajs-scripts 1.1.2](https://github.com/vmunier/scalajs-scripts)
  - [mockito-core](https://github.com/mockito/mockito)