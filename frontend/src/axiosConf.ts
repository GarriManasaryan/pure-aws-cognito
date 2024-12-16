import axios, { AxiosHeaders, AxiosRequestHeaders } from 'axios';
import endpointConfigurator from './endpointConfigurator';

const axiosConf = axios.create({

    // // no nginx
    // baseURL: process.env.REACT_APP_BASE_URL + '/api'

    // // nginx
    // baseURL: '/api'

    // all
    baseURL: endpointConfigurator('/api')
    
});


axiosConf.defaults.headers.common = {
    ...axiosConf.defaults.headers.common,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, OPTIONS, PATCH",
    "Access-Control-Expose-Headers": "*",
};

// Set withCredentials
axiosConf.defaults.withCredentials = true;

const changableActionsList:string[] = ['post', 'patch', 'delete']

// Create a new configuration for "backoffice" without modifying the original
const backofficeConf = axios.create({
    ...axiosConf.defaults, // Copy all existing defaults
    baseURL: endpointConfigurator('/backoffice'), // Override baseURL
});

// Override specific settings
backofficeConf.defaults.withCredentials = false;

export { axiosConf, backofficeConf };
