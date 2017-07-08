import React, { Component } from 'react';
import logo from './logo.svg';
import {Login} from './pages/login/Login'
import {Home} from './pages/home/Home'
import './App.css';
import {
    HashRouter,
    Route,
    Redirect,
    IndexRoute,
    Link
} from 'react-router-dom';

class App extends Component {

    name = 'Akshay Rana';

    render() {
        //return this.getSomething()
        return(
            <HashRouter>
                <div>
                    <Route path="/">
                        <Redirect from="" to="login" />
                    </Route>
                    <Route path="/login" component={Login}>
                    </Route>
                    <Route path="/home" component={Home}>
                    </Route>
                </div>
            </HashRouter>
        )
    }
}

export default App;
