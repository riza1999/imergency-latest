import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

import { QueryLogin } from "./types/responseTypes";

admin.initializeApp();
const db = admin.firestore();

export const QUERY_LOGIN = functions.https.onRequest(async (req, res) => {
  const defaultResult: QueryLogin = {
    success: false,
    message: "Nomor telepon atau password salah!",
    user_info: {},
  };
  let result: QueryLogin = defaultResult;

  const { no_handphone, password } = req.body;

  const query = db
    .collection("/users")
    .where("phone_number", "==", no_handphone)
    .where("password", "==", password);

  await query
    .get()
    .then((snapshot) => {
      if (snapshot.empty) {
        console.log("No matching documents. Return default result.");
        return;
      }

      snapshot.forEach((doc) => {
        console.log(doc.id, "=>", doc.data());

        result = {
          success: true,
          message: "OK",
          user_info: doc.data(),
        };
      });
      return;
    })
    .catch((err) => {
      console.log("Error getting documents", err);
    });

  console.log("Result ", result);
  res.send(result);
});
