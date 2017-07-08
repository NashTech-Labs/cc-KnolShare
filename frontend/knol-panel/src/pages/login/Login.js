import React, { Component } from "react";
import {Router, Route, IndexRoute, hashHistory} from "react-router"
import "./Login.css";
import axios from "axios"
import {Header} from "../header/Header"

export class Login extends Component {

    constructor(props) {
        super();
        this.state = {email: "",password: ""};
        this.handleChanges = this.handleChanges.bind(this)
        this.submit = this.submit.bind(this)
    }

    handleChanges(event) {
        if(event.target.name === "email") {
            this.setState({email: event.target.value})
        } else if (event.target.name === "password") {
            this.setState({password: event.target.value})
        }
    }

    navigate() {
        console.log(this.props);
        this.props.history.push("/home");
    }

    submit() {
        console.log(this.state);
        axios.post("http://127.0.0.1:9000/knolshare/admin/login", this.state)
            .then(res => {
                localStorage.setItem("email", res.data.data.admin.email);
                localStorage.setItem("accessToken", res.data.data.accessToken);
                this.navigate();
            }, err => {
                console.error(err)
            })
    }

    render() {
        const element = (
            <div>
                <Header />
                <div>
                    <div className="col-md-6 col-sm-12">
                        <form onSubmit={this.submit} >
                            <div className="row">
                                <div className="col-md-6 col-md-offset-3">
                                    <input name="email" placeholder="Email" className="form-control" type="email" onChange={this.handleChanges}/>
                                </div>
                                <div className="col-md-6">
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6 col-md-offset-3">
                                    <input name="password" placeholder="Password" minLength="8" className="form-control" type="password" onChange={this.handleChanges}/>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6 col-md-offset-3">
                                    <button type="submit" className="btn btn-primary btn-div knol-color">Submit</button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div className="col-md-6 col-sm-12">
                        <img id="img" src="../../assets/images/businessman-607831_960_720.png"/>
                    </div>
                </div>
            </div>
        );
        return element;
    }
}
//setInterval(tick, 1000);