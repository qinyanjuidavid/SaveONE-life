# SaveOne-Life

## Running the project
### Installation by Downloading the APK
- To download the apk click the link [SaveOne-Life]()
- Install the apk on your android mobile phone
### Installation by Cloning to Android Studio
- Download Android Studio
- Clown the repo using the command ``` git clone https://github.com/wykeenjenga/SaveONE-life.git```
- Run the android app on Android Studio


## Impact
SaveOne-Life is an android mobile application that helps solve three United Nations  Sustainable Development Goals(SDG). These goals are;
> #### No-Poverty
> According to the United Nations, one out of five children live in extreme poverty, and the negative effects of poverty and deprivation in the early years have ramifications that can last a lifetime. With SaveOne-Life, donors can be able to explore and donate to orphanages that are facing extreme poverty. Some of the items that can be donated to reduce poverty include; clothings, better housing, beddings, etc.

> #### Quality Education
> To protect the well-being of children and ensure they have access to continued learning, the SaveOne-Life android application enables donors to search for orphanages and donate learning materials such as books, pens, and many more.

> #### Zero-Hunger
> Zero hunger is one of the United Nations SDGS. To enhance and maintain zero hunger among the young generation, SaveONE life provides a platform whereby donors can donate food stuffs to the affected orphanages.

## Scalability of the project.
> SaveONE Life project is scalable,
> If we were to continue, we were to implement addition feature regarding the health of the childrens, whereby Orphan keeper will have to add and Indicate the number of childrens who are not in good health, this also will lead to additional integration with good willing health Sectors. Health Sector will be able to login to the app and locate those poor childrens that are in need.

> Most orphanages in Kenya are founded and run by individuals, not the government. and most wealthiest families have a tendance of adopting 1 or 2 childrens from the orphanage and help them either by educating them and many more. But some families they do adopt the childrens and mistreate them. In future we are planning to add more features whereby the app will be tracking the progress and the number of childrens whom are adopted, including the location and lifestyle of the childrens. This feature will help 
age Masters to track the well being of their childrens.

> M-Pesa is a virtual banking system that provides transaction services through a SIM card. Once the SIM has been inserted into the card slot of the mobile device, users can make payments and transfer money to vendors and family members with SMS messages. In future we are planning to integrate mpesa APIs with our app, so that it can be easy for donors to donate with mpesa with a single touch from the app.

## App Testing.
> We tested SaveONE-life App with two childrens home as shown on the screenshoots below. Orpan keeper were very excited because they will be able to express their needs via SaveONE life, for the real experience you can download SaveONE life from [Set Up](#SaveOne-Life)
> <img src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" data-canonical-src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" width="200" height="400" />


## Technology
SaveOne-Life is an android mobile application developed using Android Studio, Programming language used is JAVA.

### Java - for the frontened 
### Google technology - for the backened
> ##### - Firebase (Firebase Auth, firestore, cloud messaging, functions, storage)
> ##### - Google cloud fucntions


> ### Landing Page
> <img src="https://user-images.githubusercontent.com/46722362/158593532-1b32df9c-acb9-42c5-8312-c106a144b34a.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158593532-1b32df9c-acb9-42c5-8312-c106a144b34a.png"
       width="240" height="450" />

- On landing page we have 4 buttons which include Donate Now, Register an Orphanage, Register as a donor, and Login button.

##Authentication

> ### Donor Authentication 
-  Donor - To authenticate donor we used firebase auth dependecy (firebase-auth). 
```
implementation 'com.google.firebase:firebase-auth:21.0.1'

```
- Donor can opt to Donate without an account, but this has limitations for example he/she(Donor) will not be able to participate on [SaveONE -life forum](#SaveOne-Life).
- If donor wish to participate on SaveONE life forum, he/she must have an account.

> ### Orphange / Children's Home Authentication 
- To authenticate an orphanage, Orphan Keeper is supposed to register the orphange by clicking Register button, he will pass through 4 different fragments sections as shown below.

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
-  On this page Orphanage keeper is supposed to fill in Orphanage Group Photo, Phone number, Till number, Bank account number, Bank Name and country where the orphange is located.
- On this page we used firebase storage dependecy and storage reference class so as to enable user to upload group photo to firebase storage database.
- On this fragment we will create a sharable link of the image uploaded.

### 3. Other Information
- On this page Orphanage keeper is supposed to fill in Brief description about the orphanage, number of children in the orphanage, what they need most (include food, water, clothings, money for school fees) and the location address of the orphanage. 

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
- Orphan keeper is supposed to drag and drop the pin to the actual position of the orphanage.
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
NB: When an orphange is registered is not verified and in future we are planing to create an admin app for the orphanage verification, for the moment we are verfying orphanages from the backened. The process of verification may take 2 to 3 days. 
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
       <img src="https://user-images.githubusercontent.com/46722362/158622027-fecd7fb3-4e1d-4310-9917-50fb2a2f0f32.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158622027-fecd7fb3-4e1d-4310-9917-50fb2a2f0f32.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158622040-181805e4-c144-4d81-a027-7f28cc4322b4.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158622040-181805e4-c144-4d81-a027-7f28cc4322b4.png"
       width="220" height="450" />


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
                                            
         ....
         
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
> - *On orphanage profile page,* Orphan Keeper/ Admin will be able to update profile and other information which include ( group photo, number of childrens, phone number and other account information)
> - Also orphanage will be able to query through and see the donations they have received, this data is restored from firebase firestore.
> - He/She will be able to check and call donors on this fragment (Screenshot no. 3) if they have not received donations and manage all other things from this fragment.

<img src="https://user-images.githubusercontent.com/46722362/158773582-eb526ee5-b817-4c9d-92af-272c612306d2.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158773582-eb526ee5-b817-4c9d-92af-272c612306d2.png"
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

### Developed with <span style="color: #fb8100;">&hearts;</span> by [GDSC TAITA TAVETA UNIVERSITY](https://twitter.com/DscTtu?t=nLFp2oGleW6Tpu3XpzbugQ&s=09).

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
