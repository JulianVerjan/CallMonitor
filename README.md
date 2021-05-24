# FaceTrackingApp

Passbase coding task.


## Design

App: the content has been adapted to fit for mobile devices. To do that, it has been created flexible layouts using one or more of the following concepts:

-   [Use constraintLayout](https://developer.android.com/training/multiscreen/screensizes#ConstraintLayout)
-   [Avoid hard-coded layout sizes](https://developer.android.com/training/multiscreen/screensizes#TaskUseWrapMatchPar)

## Architecture

The architecture of the application is based on the following points:

-   A single-activity architecture, using the [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) to manage fragment operations.
-   Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) (MVVM) facilitating a [separation](https://en.wikipedia.org/wiki/Separation_of_concerns) of development of the graphical user interface.
-   [S.O.L.I.D](https://en.wikipedia.org/wiki/SOLID) design principles intended to make software designs more understandable, flexible and maintainable.
-   [Modular app architecture](https://proandroiddev.com/build-a-modular-android-app-architecture-25342d99de82) allows to be developed features in isolation, independently from other features.

### Modules

The above graph shows the app modularisation:
-   `:app` depends on `:commons:resources` and indirectly depends on `:features`.
-   `:features` modules depends on `:commons`, `:lib:network`, `:lib:data` and `:lib:model`.
-   `:commons` only depends for possible utils on `:lib`.
-   `:lib` don’t have any dependency.

#### App module

The `:app` module is the main entry of the app which is needed to create the app bundle.  It is also responsible for initiating the [dependency graph]

#### Core module

The `:lib:network` module is an android library  for serving network requests. Providing the data source for the catalog information.

#### Core module

The `:lib:data` module is an android library  and contains the use cases that fetch the data for the view models.

#### Features modules

In the `:features` module you will find the feature for detect a face, creates a videos and then pushed it to the server 


#### Commons modules

The `:commons` module only contains code and resources which are shared between feature modules. Reusing this way resources, 
layouts, views, and components in the different features modules, without the need to duplicate code.


#### Libraries modules

The `:lib` modules basically contains different utilities that can be used by the different modules.

#### Technology

Some of the main libraries used for this app are:

1. Dagger Hilt for handling the dependency injection
2. Fragment navigation for handling the navigation inside the app.
3. ML Kit for face detection and camera 2 API to capture faces and video

For more information related to the libraries used, please check the Dependencies.kt class on the buildSrc project.

#### Brief explanation

I decided to handle one object(SaveVideoResult) that contains the result of saving the video on the backend and the objects from ML Kit to handle the face recognition and video

SaveVideoResult contains the following parameters
- status: An integer code number that tells if the request was successful or not(200/400 code)
- message: An string that show a successful message or error
- isVideoSaved = A boolean that tells if the video was sucessful (true) saved or not (false)

#### Project setup

There is nothing special that should be done to run this project, you just have to clone the repo and run.

If you need access to the Webhook dashboard where the requests are made, this is the url:

https://webhook.site/#!/25960fec-fdb0-468a-9227-0e2768d757b5/ebb58286-0767-4e4f-a555-296e3dfd187c/1

#### What would I do if I had more time

1. I had a confusion with the requirement of the yellow box and in the end, what I did was validate if the face was inside the box and keep the yellow box centered on the screen. If the face is outside the box or a part inside, it is not validated and the video will not be triggered.

If it was necessary to draw the yellow box around the person's face and not keep it centered, I would love to make this change in the future.

2. Due to time it was necessary for me to separate the use cases that I pass to the cameraProvider object, when the face is tracked (bindFaceDetectionUseCase method) I had to pass the preview object and the image analyzer to it, then when a face was recognized, it cleaned the cameraProvider and recreated it case the use case (bindVideoRecordUseCase) with the preview object and the VideoCapture object.

The above is leading to some black screens and somewhat strange animations when doing this cleanup and creation. I would like in the future to review more in depth the ML kit documentation and find a solution to this or maybe see if it is possible to inject more than 3 use cases to the cameraProvider and validate how to do it.

3. If I had had more time I would have tried to organize the code a little better in the feature module and I would have tried to separate it into some utility classes.

4. There are some strings in the feature code that are hardcoded, I would like to fix this as well.

5. Due to time the saveVideoSuccessfully unit test is not working since the status it is returning an error, I would like to be able to fix it too.

#### What was achieved during the test

1. All the points required in the test were implemented using the ML Kit and camer2 API to detect faces and record the video.

2. Coroutines were used to send the video to the server.

3. In the application the information is being handled as if they were states that update the user interface.

4. SOLID and clean architecture principles were applied.

5. An architecture by features was used thinking about the scalability of the project and to allow other developers to understand and scale the project more easily.

6. A viewmodel pattern was used to communicate the changes from the data and network layers to the UI layer.

#### Thanks for the opportunity given, it was a lot of fun for me to take this test

