import React from "react";
import firebaseInstance from "../config/firebase";
import { axiosConf } from "../axiosConf";

const Login = () => {
    const handleSignIn = async () => {
        try {
            const result = await firebaseInstance.auth.signInWithPopup(firebaseInstance.googleProvider); // Google login
            if (!result.user) {
                throw new Error("Google login failed.");
            }
            const idToken = await result.user.getIdToken(); // Get Firebase ID token

            // Send the ID token to the backend
            axiosConf.post("/login", JSON.stringify(idToken))
        } catch (error) {
            console.error("Error during sign-in:", error);
        }
    };

    return (
        <button onClick={handleSignIn}>
            Sign in with Google
        </button>
    );
};

export default Login;
