import axios from "axios";

const apiName = process.env.REACT_APP_API_URL;

export default axios.create({
  baseURL: apiName,
  headers: {
    "Content-type": "application/json"
  }
});