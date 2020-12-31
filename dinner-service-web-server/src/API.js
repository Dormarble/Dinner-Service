import axios from 'axios'

const { token } = window.localStorage;

const api = axios.create({
    baseURL: 'http://http://ec2-15-164-165-148.ap-northeast-2.compute.amazonaws.com/:8080/api',
    headers:{
        'Content-Type': 'application/json',
        Authorization : token && token.length > 0 ? `Bearer ${token}` : ''
    }
})

export default api;
