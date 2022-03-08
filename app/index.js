const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.androidPushNotification = functions.firestore.document("Chats/{docId}").onCreate(
  (snapshot, context) => {
    admin.messaging().sendToTopic(
      "New messages forum",{
        notification: {
          title: "SaveONE life new message",
          body: "Hello there you have a new message"
          }
        }
  )});