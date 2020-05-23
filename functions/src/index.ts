import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

import {
  QueryLogin,
  QueryFirstAid,
  QueryFirstAidDetail,
  MutationSignup,
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
  const defaultResult: QueryFirstAid = {
    diseases: []
  };
  let result: QueryFirstAid = defaultResult;

  const query = db.collection("/first_aid");

  await query
    .get()
    .then((snapshot) => {
      snapshot.forEach((doc) => {
        console.log(doc.id, "=>", doc.data());
        let { name, icon_img } = doc.data();

        result.diseases.push({ name, icon_img });
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
          if (name === doc.data().name) {
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

export const MUTATION_SIGN_UP = functions.https.onRequest(async (req, res) => {
  let alreadyRegistered: Boolean = false;

  const defaultResult: MutationSignup = {
    success: false,
    message: "",
    unique_id: "",
    user_info: {},
  };
  let result: MutationSignup = defaultResult;

  const {
    no_handphone,
    full_name,
    password,
    birth_date,
    gender,
    blood_type,
  } = req.body;

  const query = db.collection("/users");

  await query
    .where("phone_number", "==", no_handphone)
    .get()
    .then((snapshot) => {
      // If snapshot is not empty, means the phone number is already exist
      if (!snapshot.empty) {
        alreadyRegistered = true;

        result = {
          ...defaultResult,
          success: true,
          message: "No. Handphone sudah terdaftar!",
        };

        res.send(result);
      }
    })
    .catch((err) => {
      console.log("Error getting documents", err);
    });

  // Else, push!
  if (!alreadyRegistered) {
    const newData = {
      phone_number: no_handphone,
      full_name,
      password,
      birth_date,
      gender,
      blood_type,
    };

    await query
      .add(newData)
      .then((doc) => {
        result = {
          success: true,
          message: "Berhasil didaftarkan",
          user_info: req.body,
          unique_id: doc.id,
        };
      })
      .catch((err) => {
        console.log("Error getting documents", err);
      });
    res.send(result);
  }
});
