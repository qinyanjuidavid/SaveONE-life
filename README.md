# SaveOne-Life

## Introduction
> - Did you know about 9.2% of the world, or 689 million people, live in extreme poverty on less than $1.90 a day. And there are about 153 million orphans worldwide.
> - According to Light Up Hope Organization, the population of orphaned children in Kenya is estimated to be at three million. Every day, 700 children are orphaned (one every two minutes), with HIV/AIDS contributing to 1/3 of these orphans. As a result, the number of orphans is expected to increase. With the death of their parents who were the bread winners, most children find themselves without sufficient financial, social and emotional support and these children's futures become quite uncertain.
> - They end up straining to access basic needs such as food, clothing, course books, writing materials and even shelter that are essential for normal living. In addition to that, orphans who have been traumatized by the death of their parents can become antisocial. The fact that society appears to have become oblivious to their predicament hasn't helped matters.
> - Those who have found themselves in children’s homes have been a little bit lucky to have some access to shelter, food and clothing but in limited quantities compared to other children 
> - Most of the registered 800 children’s homes in Kenya depend on donors to provide these basic needs of these children and the finances are mostly very limited to providing food for all those 
children they are hosting


## Problem
> - The major problem gotten from the user requirement gathering phase, has been the donors trying to locate the orphanages in the country especially those that are really in interior areas. We got respondents, who were students, from our own campus who sometimes have food, clothing and writing materials to donate. They shared on their difficulty of accessing the locations of the orphanages in the areas around. 

## Solution
> - That’s where the Save ONE-life android app comes in. -it’s an app built with java and google technology that helps donors to locate home orphanages all across the country and make their donations.
> - Knowing which child requires help, when they require this help, and where they actually are to be accorded this help is major step in alleviating the conditions of these orphaned children.
> - We desire that orphaned children will experience a normal life just as children who have found themselves in their families.


## SDGs and their Targets
> SaveOne-Life is an android mobile application that helps solve three United Nations  Sustainable Development Goals(SDG). These goals are;
> #### SDG 1: No poverty
> - Target 1.2: By 2030, reduce at least by half the proportion of men, women and children of all ages living in poverty in all its dimensions according to national definitions.

> #### SDG 2: Zero Hunger
> - Target 2.1: By 2030, end hunger and ensure access by all people, in particular the poor and people in vulnerable situations, including infants, to safe, nutritious and sufficient food all year round. 

> #### SDG 4: Quality Education
> - Target 4.1: By 2030, ensure that all girls and boys complete free, equitable and quality primary and secondary education leading to relevant and effective learning outcomes.


## Scalability of the project.
> SaveONE Life project is scalable,
> We are planning to implement an additional feature regarding the health of the children, whereby Orphanage managers can add and indicate the number of children who are not in good health, thus also being able to integrate the good will health organizations to offer free medical services and even hold medical camps.
>  This good will health organizations will also be able to log into the app and locate those orphaned childfen that are in need.

> M-Pesa is a virtual banking system that provides transaction services through a SIM card. Once the SIM has been inserted into the card slot of the mobile device, users can make payments and transfer money to vendors and family members with SMS messages. 
> In future we are planning to integrate mpesa APIs with our app, so that it can be easy for donors to donate with mpesa with a single touch from the app.
> We are also plaining to add more methods / medium of donations like stripe, paypal and many other.

> We are also planing to extend the app from being used inside the country to whole Africa and the whole world as well.

## App Testing.
> We tested SaveONE-life App with two children's homes as shown on the screenshoots below. The orphanage managers were very excited because they will be able to share their needs via SaveONE life, for the real experience you can download SaveONE life from [Set Up](#SaveOne-Life)

<img src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" data-canonical-src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" width="200" height="400" />


## Technology
SaveOne-Life is an android mobile application developed using Android Studio, Programming language used is JAVA and at the backend we use Google Technology.

## Google Technology Overview
> ### Firebase Auth.
> - We used *firebase auth* to register and login user to the app. Where by donors can opt to register with or continue with google as shown in the screenshot below.
> - <img src="https://user-images.githubusercontent.com/46722362/159160999-a2f9614c-be49-4ee3-ba54-da694318dfdf.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159160999-a2f9614c-be49-4ee3-ba54-da694318dfdf.png"
       width="240" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/159161005-e108a376-1e2e-4fda-b45f-d068dffff38e.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159161005-e108a376-1e2e-4fda-b45f-d068dffff38e.png"
       width="240" height="450" />

> ### Firebase firestore.
> - We used firebase firestore to store orphanages, users / donors, location and chats message from the forum data so as we can access data from the backend with ease. 

> ### Firebase functions and Google cloud.
> - We used firebase functions to listen to any changes on Chats collection from firestore that is when a new message is added or sent the function is supposed to triger a push notification to the users subscribed to the topic on SaveONE life forum.
> - We used Google cloud to write and deploy the function.

> ### Firebase cloud messaging.
> - We used firebase cloud messaging dependecy to send push notifications when there is a new message in SaveONE life forum.

> ### Google cloud.
> - Google cloud was used to create google map api that helped us to implement Google maps on our project.

> ### Firebase Storage.
> - This dependecy helped us to store user profile images when opehanage upload their group photo.



# App functionality and Google technology application
> ### Landing Page
> <img src="https://user-images.githubusercontent.com/46722362/159177572-b96ff294-b3c4-4495-9884-ffdf931cffbb.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159177572-b96ff294-b3c4-4495-9884-ffdf931cffbb.png"
       width="240" height="460" />

- On landing page we have 4 buttons which include Donate Now, Register an Orphanage, Register as a donor, and Login button.

## Authentication
> ## Donor Authentication 
> -  Donor - To authenticate donor we used firebase auth dependecy (firebase-auth). 
```
implementation 'com.google.firebase:firebase-auth:21.0.1'

```
- Donor can opt to Donate without an account, but this has limitations for example he/she(Donor) will not be able to participate on [SaveONE -life forum](#Home Page).
> - If donor wish to participate on SaveONE life forum, he/she must have an account.

> ## Orphange / Children's Home Authentication 
> - To authenticate an orphanage, Orphanage manager is supposed to register the orphange by clicking Register button, he will pass through 4 different fragments sections as shown below.
> - An oprhanage is required to be verified to get more donations, Verification process requires an orphanage to channel $5 (annually) and exact location of the orphanage if it was not provided.
> - Verification process will help donors to be more conviced that the orphanage exists.
> - Channeled ($5) money is used by TTU GDSC for the maintenance of SaveONE life. Money can be channeled using mpesa at the moment.
> - Below are the profile pages for the verified and not verified orphanage.
> - <img src="https://user-images.githubusercontent.com/46722362/159164989-359ddf2f-0797-41f4-9e1a-56eea52454fa.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159164989-359ddf2f-0797-41f4-9e1a-56eea52454fa.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/159165000-a3131ab2-6075-47a6-955c-452b38654155.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159165000-a3131ab2-6075-47a6-955c-452b38654155.png"
       width="220" height="450" />

### 1. Basic Information 
- On this page Orphanage keeper is supposed to fill in name, email and password of the orphange as shown.
- The following dependencies were used.

```
implementation 'com.google.firebase:firebase-storage:20.0.0'
implementation 'com.google.firebase:firebase-database:20.0.3'
implementation 'com.google.firebase:firebase-firestore:24.0.1'
implementation 'com.google.firebase:firebase-auth:21.0.1'
```
- We used firestore database with a collection name Orphanage to store orphanage data.
- Orphange data is stored as a HashMap on the firestore database.

       
### 2. Additional Information
-  On this page Orphanage manager is supposed to fill in Orphanage Group Photo, Phone number, Till number, Bank account number, Bank Name and country where the orphange is located.
- On this page we used firebase storage dependecy and storage reference class so as to enable user to upload group photo to firebase storage database.
- On this fragment we will create a sharable link of the image uploaded.

### 3. Other Information
- On this page Orphanage manager is supposed to fill in Brief description about the orphanage, number of children in the orphanage, what they need most (include food, water, clothings, money for school fees) and the location address of the orphanage. 

<img src="https://user-images.githubusercontent.com/46722362/158599594-b612623f-3de1-447e-b65a-1d48805fcc24.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158599594-b612623f-3de1-447e-b65a-1d48805fcc24.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158608189-5a1560c6-0f69-4b70-9d99-5fb20b8a9731.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158608189-5a1560c6-0f69-4b70-9d99-5fb20b8a9731.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158609052-5ab9ab48-04a3-4dc2-baa0-e05c88a33f39.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158609052-5ab9ab48-04a3-4dc2-baa0-e05c88a33f39.png"
       width="220" height="450" />

       
- ### 4. Orphanage location
- Orphanage location fragment shows a google map showing the current location of the user.
- Orphanage manager is supposed to drag and drop the pin to the actual position of the orphanage and get the current coordinates, on button click, coordinates are recorded to the firestore database.
- We used google map API, the following dependecies were used  
- We created Google Map Api from google clouds
 ```
 build.grandle(:app)
 
 dependencies{
 implementation 'com.google.android.gms:play-services-maps:18.0.2'
 implementation 'com.google.android.gms:play-services-location:19.0.1'
 }
 
 dependencies{
 classpath 'com.google.gms:google-services:4.3.10'
 classpath 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1'
 
 }
 
 //on the manifest file  we added a meta data with a string of API key value 
 <meta-data
      android:name="com.google.android.geo.API_KEY"
      android:value="@string/map" />
      
 //This legacy name (com.google.android.geo.API_KEY)  allows authentication to the Android Maps API
 ```
 
<img src="https://user-images.githubusercontent.com/46722362/158611030-d17d2023-3971-4d5c-9331-f1b152c09775.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158611030-d17d2023-3971-4d5c-9331-f1b152c09775.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158611060-44edd280-e8ad-45b8-8aa5-6116edbcb019.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158611060-44edd280-e8ad-45b8-8aa5-6116edbcb019.png"
       width="220" height="450" />
       
```
NB: When an orphange is registered it should be verified and in future we are planing to create an admin app for the orphanage verification, for the moment we are verfying orphanages from the backened. The process of verification may take 2 to 3 days. 
```

## Home Page
- On home page we have 3 fragments.
- We used tablayout to show the 3 pages, which are;

> ### 1. Home
> - On home page we are fetching orphanages data from firestore database, and presenting them at the frontened as cardviews in the recyclerview view.
> - Each cardview is showing some and importance details about the orphange which include name, location address, number of children's in the orphanage and a donate button at the bottom.
> - When donate here button is clicked it shows the detailed fragment about the orphanage and a donate floating action button at the bottom.
> -  On this detailed fragment page it shows map view with a marker showing the exact location of the orphanage home whereby if you click on marker it shows direction to the orphanage location on Google map (but in future we are planing to show it on the map).
> - Detailed fragment shows more about the the orphanage, contacts and donations medium which are bank, phone number (can be used to call the orphanage when clicked) and donate na mpesa.
> - The above methods are used for the liquid donation (eg money etc), while the floating action button at the bottom opens a dialog for the solid donations which include *(clothings, beddings, food stuffs and educational materials )* as shown in screenshot (3).
> - When donor donates the above items, Data is recorded to the firestore dabase with the name **Donations** and a document name of the orphanage email.
> - Below screenshots shows the whole process.

<img src="https://user-images.githubusercontent.com/46722362/158621972-77ce0bd3-b684-4fcb-84e8-fa190adf9c8c.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158621972-77ce0bd3-b684-4fcb-84e8-fa190adf9c8c.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/159177583-4c26811e-eb00-4ac3-b23c-f3c2cc3de287.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159177583-4c26811e-eb00-4ac3-b23c-f3c2cc3de287.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/159177585-059e2723-b8d6-4fae-a8ff-50b026d4391e.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159177585-059e2723-b8d6-4fae-a8ff-50b026d4391e.png"
       width="220" height="460" />


> ### 2. MapView
> - SaveONE life mapview shows the different location of the orphanages across east Africa.
> - Orphanages are being represented by a custom marker as shown in the screenshots
> - On this fragment we used a Google map whereby data (Location Coordinates) are fetched from firestore database which include coordinates and name of the orphnage.
> - We used a model view to store fetched data as a List 
> 
```
List<LocationModel> listdata;//model

...get data from firestore...
for (int i = 0; i < listdata.size(); i++) {
            //add makers
            googleMap.addMarker(new MarkerOptions()
                    .position(listdata.get(i).getLatLng())
                    .title(listdata.get(i).getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            new getBitmap().getBitmap(String.valueOf(R.drawable.custom_maker),
                                            120,120, getContext()))));
                                            }
```
> - Here is a screenshot showing orphanages from different locations.

<img src="https://user-images.githubusercontent.com/46722362/158626612-4231b919-8da9-430e-bb96-8a5280b46c94.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158626612-4231b919-8da9-430e-bb96-8a5280b46c94.png"
       width="220" height="450" />


> ### 3. Public Forum (SaveONE life)
> Is a plaform whereby public and all community can chat and give reviews or comments about orphanage homes.
> On this fragment we used;
> #### Firestore database for storing messages
> - With a collection name (Chats), time+date as the document path.
> - Messages are saved as hashmap inside an array, which include (message, email, name, time and data).
> - To send a message you can either type or use Google Speech Recognizer.
 ```
 ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
 message.setText(result.get(0).toString().toLowerCase());
```
> - Only logged users are allowed to send a message.
> - At first onCreateView we retrive messages from firestore, we used an adapter and recycler view to hold data.

> #### Google Cloud Function
> - We used a google cloud function to listen to any changes on Chats collection, when a message is written or deleted.
> - This function will triggger firebase a push notification to those users that are subscribed to the topic.
> - We used the code below for push notifications
```
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.androidPushNotification = functions.firestore.document('Chats/{uid}').onWrite( async (event) => {
  
  let t = "You have a new message";
  let c = "Hello there you have a new message from SaveONE life Forum";

  var message = {
    notification: {
      title: t,
      body: c,
    },

    topic: 'chats',
    
    };

});
```

> #### Firebase Cloud Messaging
> - To send push notifications to the user's we used ``` implementation 'com.google.firebase:firebase-messaging' ``` dependecy and a Chat notification class that extend FirebaseMessagingService and overrided onMessageReceived to handle everything.
> ##Screenshots
>
> <img src="https://user-images.githubusercontent.com/46722362/158769950-ba3377b7-ef8e-46ec-ae5a-cb231f1b26c9.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158769950-ba3377b7-ef8e-46ec-ae5a-cb231f1b26c9.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158769919-a05a1bcc-79cd-44c3-8676-0b4ac8175cc7.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158769919-a05a1bcc-79cd-44c3-8676-0b4ac8175cc7.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158769975-8b7b6753-261b-4645-8b0f-9822546bc6ab.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158769975-8b7b6753-261b-4645-8b0f-9822546bc6ab.png"
       width="220" height="450" />


## Orphanage Profile Page
> - *On orphanage profile page,* Orphanage manager/ Admin will be able to update profile and other information which include ( group photo, number of childrens, phone number and other account information)
> - Also orphanage will be able to query through and see the donations they have received, this data is restored from firebase firestore.
> - He/She will be able to check and call donors on this fragment (Screenshot no. 3) if they have not received donations and manage all other things from this fragment.

<img src="https://user-images.githubusercontent.com/46722362/159164989-359ddf2f-0797-41f4-9e1a-56eea52454fa.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/159164989-359ddf2f-0797-41f4-9e1a-56eea52454fa.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158773627-628c9b36-bb68-4f4e-9b83-219d12416876.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158773627-628c9b36-bb68-4f4e-9b83-219d12416876.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158773637-8923cd30-d2f3-46f5-be1b-b9f779dde6ff.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158773637-8923cd30-d2f3-46f5-be1b-b9f779dde6ff.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158773650-dbaabe5a-c6d8-4dc4-b6ee-fbed1bc1e6cb.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158773650-dbaabe5a-c6d8-4dc4-b6ee-fbed1bc1e6cb.png"
       width="220" height="450" />


# Running the project
## Installation by Downloading the APK
- To download the apk click the link [SaveOne-Life]()
- Install the apk on your android mobile phone
## Installation by Cloning to Android Studio
- Download Android Studio
- Clown the repo using the command ``` git clone https://github.com/wykeenjenga/SaveONE-life.git```
- Run the android app on Android Studio

## License
```
MIT License

Copyright (c) 2022 GDSC Taita Taveta University.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```

## Thanks Note
Thanks to Google Developer Students Club for contributing towards the growth of [@DSCTTU](https://twitter.com/DscTtu?t=nLFp2oGleW6Tpu3XpzbugQ&s=09).

### Developed with <span style="color: #fb8100;">&hearts;</span> by [GDSC TAITA TAVETA UNIVERSITY](https://twitter.com/DscTtu?t=nLFp2oGleW6Tpu3XpzbugQ&s=09).
