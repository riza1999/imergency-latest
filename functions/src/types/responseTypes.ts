type EmptyObject = {};

export type UserInfo = {
    blood_type: string;
    full_name: string;
    gender: string;
    password: string; // MD5
    phone_number: string;
} | EmptyObject;

type FirstAidDetailQNA = {
    title: string;
    detail: string;
}

export type QueryLogin = {
    success: boolean,
    message: string,
    unique_id: string,
    user_info: UserInfo,
};

export type QueryFirstAid = Array<string>;

export type QueryFirstAidDetail =   {
    name: string,
    header_img: string, // "image_allergy.png"
    icon_img: string, // "icon_allergy.png"
    emergency_handling: Array<string>,
    qna: Array<FirstAidDetailQNA>
  } | EmptyObject;