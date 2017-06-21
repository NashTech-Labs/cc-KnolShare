import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { LoginForm } from '../../models/login-form';
import {LoginService} from "./login.service";

@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
  providers: [LoginService]
})
export class LoginPage {
  loginFormObj: LoginForm = new LoginForm();

  constructor(public navCtrl: NavController, private loginService: LoginService) {}

  submit() {
    this.loginService.login(this.loginFormObj).subscribe( (data: any) => {

    }, (err: any) => {

    })
  }

}
