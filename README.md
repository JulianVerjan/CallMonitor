# CallMonitor Task

Nord security coding task.

In this project is found the solution to the task. The project consists of two applications, one that acts as a server application storing data and responding to requests by the client app and the client app from which users can see the call logs and interact with the server app.


## Design(Client App and Server App)

The content of both apps has been adapted to fit for mobile devices. To do that, it has been created flexible layouts using one or more of the following concepts:

-   [Use constraintLayout](https://developer.android.com/training/multiscreen/screensizes#ConstraintLayout)
-   [Avoid hard-coded layout sizes](https://developer.android.com/training/multiscreen/screensizes#TaskUseWrapMatchPar)

## Architecture

The architecture of the applications are based on the following points:

-   A single-activity architecture, using the [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) to manage fragment operations.
-   Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) (MVVM) facilitating a [separation](https://en.wikipedia.org/wiki/Separation_of_concerns) of development of the graphical user interface.
-   [S.O.L.I.D](https://en.wikipedia.org/wiki/SOLID) design principles intended to make software designs more understandable, flexible and maintainable.
-   [Modular app architecture](https://proandroiddev.com/build-a-modular-android-app-architecture-25342d99de82) allows to be developed features in isolation, independently from other features.

### Modules

The above graph shows the app modularisation:
-   `:app` depends on `:commons:resources` and indirectly depends on `:features`.
-   `:serverapp` depends on `:commons:resources` and directly depends on `lib:server` feature.
-   `:features` modules depends on `:commons`, `:lib:network`, `:lib:data` and `:lib:model`.
-   `:commons` only depends for possible utils on `:lib`.
-   `:lib` don’t have any dependency.

#### App module

The `:app` module is the main entry of the client app which is needed to create the app bundle.  It is also responsible for initiating the [dependency graph]

#### Sever app

The `:serverapp` module is the main entry of the server app. is responsible for responding to requests for information and data storage from the client app. The above using the `lib:server` module.

#### Core module(Network)

The `:lib:network` module is an android library  for serving network requests. Providing the data source for the call log, call status and services information.

#### Core module(Data)

The `:lib:data` module is an android library  and contains the use cases that fetch the data for the view models.

#### Core module(Server)

The `:lib:server` module is an android library  and contains the necessary classes to respond to requests and store the information coming from the client app.

#### Features modules

In the `:features` module you will find the feature capable of serving call data to clients on the same network.


#### Commons modules

The `:commons` module only contains code and resources which are shared between feature modules. Reusing this way resources, 
layouts, views, and components in the different features modules, without the need to duplicate code.


#### Libraries modules

The `:lib` modules basically contains different utilities that can be used by the different modules.

#### Technology

Some of the main libraries used for this app are:

1. Dagger Hilt for handling the dependency injection
2. Fragment navigation for handling the navigation inside the app.
3. Room database for storing information in the server app.
4. Ktor for creating embedded http server.
5. Okhttp vs retrofit for information requests from the client app.

For more information related to the libraries used, please check the Dependencies.kt class on the buildSrc project.

#### Project setup

#### Note: I need to highlight that the server app must be executed on a physical device and not on an emulator since the IP in the emulators is different than of a physical device, and due to this issue, there was never communication between the client app and the server app.

#### I could not get the server to work from an android emulator, and therefore the server app must be installed on a smartphone. The client app can run on an emulator or smartphone without any trouble

1. As an initial step you should clone the repository.

To successfully run this project after cloning it you must do the following: 

2. Run the server application and press the start server button.
3. When the server runs you will see the URL of the server you are running on.
4. Copy this URL to `API_BASE_URL` variable inside the `build.grade.kts` file inside the `lib/network project`. #### This is a very important step without this the #### client app will not work.
5. Clean and make project(Hammer button in android studio)
6. Run the client app.
7. Make or receive calls from the device on which you have the client app installed.

Note: If the client app is in an android emulator you can use the phone feature in the emulator which is in the extended controls by pressing the more button to simulate calls.

#### What would I do if I had more time

1. I couldn't find a way to know the server IP dynamically and in advance. Due to the above, the 3rd step is necessary during the project setup.
2. I would have wanted the server code to be in another project outside the mobile project, however, I didn't find a way to do it, I even tried with IntelliJ, but I couldn't do it.
3. There is a bug with the broadcast receiver that I am using to get the incoming calls, and if I press the floating button in the client app to stop the broadcast it stops working, and it is necessary to reinstall the app to get it working again. The previous bug could not be solved because I spent more time on other tasks and noticed this bug at the last minute.
4. If I had had more time I would have tried to organize the code a little better in the feature module and I would have tried to separate it into some utility classes.
5. I did not find a way to run the server app from an emulator and therefore it is necessary that the server app runs on a physical device.
6. There are some strings in the feature code that are hardcoded, I would like to fix this as well.

#### What was achieved during the test

1. All the points required in the test were implemented accoridng to the description.

2. Coroutines were used for the communication with the server app.

3. In the application the information is being handled as if they were states that update the user interface.

4. SOLID and clean architecture principles were applied.

5. An architecture by features was used thinking about the scalability of the project and to allow other developers to understand and scale the project more easily.

6. A viewmodel pattern was used to communicate the changes from the data and network layers to the UI layer.

#### Thanks for the opportunity given, it was a lot of fun for me to take this test

