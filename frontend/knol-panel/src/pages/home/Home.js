import React, { Component } from "react";
import axios from "axios"

export class Home extends Component {
    email = "";
    token = "";
    constructor(props) {
        super();
        if(localStorage.getItem("email")) {
            this.email = localStorage.getItem("email");
        }
        if(localStorage.getItem("accessToken")) {
            this.token = localStorage.getItem("accessToken");
        }


        let headers = {
            "accessToken": this.token,
            "email": this.email,
            "Content-Type": "application/json"
        }
        var authOptions = {
            method: "GET",
            url: "http://127.0.0.1:9000/knolshare/knolxsession/getAll",
            headers: {
                "accessToken": this.token,
                "email": this.email,
                "Content-Type": "application/json"
            },
            json: true
        };
        ///knolshare/knolxsession/getAll
        axios.get("http://127.0.0.1:9000/knolshare/knolxsession/getAll", {headers: headers})
            .then(res => {
            console.log(res)
    }, err => {
            console.error(err)
        })
    }

    render() {
        const element = (
            <h1>Home Page </h1>
    );
        return element;
    }
}
//setInterval(tick, 1000);