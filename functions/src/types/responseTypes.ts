type EmptyObject = {};

export type UserInfo = {
    blood_type: string;
    full_name: string;
    gender: string;
    password: string; // MD5
    phone_number: string;
} | EmptyObject;

export type QueryLogin = {
    success: boolean,
    message: string,
    user_info: UserInfo,
};