import React, { Component } from "react";
import "./Header.css";


export class Header extends Component {

    name = "Akshay Rana";

    render() {
        return this.getSomething()
    }

    getSomething() {
        return <div className="App">
            <div className="App-header row">
                <div className="col-md-2 col-sm-2"><img src="../../assets/images/knoldus_blocklogo.png"/></div>
                <div className="col-md-8 col-sm-8"><h2>Knol Panel</h2>
                </div>
            </div>
        </div>
    }
}