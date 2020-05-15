import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

import {
  QueryLogin,
  QueryFirstAid,
  QueryFirstAidDetail,
} from "./types/responseTypes";

admin.initializeApp();
const db = admin.firestore();

export const QUERY_LOGIN = functions.https.onRequest(async (req, res) => {
  const defaultResult: QueryLogin = {
    success: false,
    message: "Nomor telepon atau password salah!",
    unique_id: "",
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
          unique_id: doc.id,
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

export const QUERY_FIRST_AID = functions.https.onRequest(async (req, res) => {
  const defaultResult: QueryFirstAid = [];
  let result: QueryFirstAid = defaultResult;

  const query = db.collection("/first_aid");

  await query
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        console.log(doc.id, "=>", doc.data());
        let { name } = doc.data();

        result.push(name);
      });
      return;
    })
    .catch((err) => {
      console.log("Error getting documents", err);
    });

  console.log("Result ", result);
  res.send(result);
});

export const QUERY_FIRST_AID_DETAIL = functions.https.onRequest(
  async (req, res) => {
    const defaultResult: QueryFirstAidDetail = {};
    let result: QueryFirstAidDetail = defaultResult;

    const { name } = req.body;

    const query = db.collection("/first_aid");

    await query
      .get()
      .then((snapshot) => {
        snapshot.forEach((doc) => {
          console.log(doc.id, "=>", doc.data());
          if(name === doc.data().name) {
            result = doc.data();
          }
        });
        return;
      })
      .catch((err) => {
        console.log("Error getting documents", err);
      });

    console.log("Result ", result);
    res.send(result);
  }
);
