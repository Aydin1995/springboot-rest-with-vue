import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
let stompClient=null
const handlers=[]
export function connect() {
    console.log("connect!!!!")
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug=()=>{}
    stompClient.connect({}, frame=> {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/activity', message=> {
         for (const handler of handlers) {
             
             handler(JSON.parse(message.body));
         }


        })
    })
}
export function addHandler(handler) {

    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }

    console.log("Disconnected")
}

export function sendMessage(message) {
    console.log("/app/changeMessage");
    stompClient.send("/app/changeMessage", {}, JSON.stringify(message))
}