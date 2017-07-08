import { Component } from "@angular/core";
import { NavController } from "ionic-angular";
import { LoginForm } from "../../models/login-form";
import {LoginService} from "./login.service";
import { AlertController } from "ionic-angular";
import {SharedService} from "../../services/shared.service";
import {HomePage} from "../home/home";
import { Storage } from "@ionic/storage";

@Component({
  selector: "page-login",
  templateUrl: "login.html",
  providers: [LoginService]
})
export class LoginPage {
  loginFormObj: LoginForm = new LoginForm();

  constructor(public navCtrl: NavController,
              private loginService: LoginService,
              private sharedService: SharedService,
              private storage: Storage,
              private alertController: AlertController) {}

  submit() {
    this.loginService.login(this.loginFormObj).subscribe( (data: any) => {
      console.log(data);
      this.storage.set("user", JSON.stringify(data.data.user));
      this.storage.set("accessToken", JSON.stringify(data.data.accessToken));
      this.sharedService.isLoggedIn = true;
      this.alertController.create({title : "Successfully Logged in", message: ""});
      this.navCtrl.pop();
    }, (err: any) => {
      alert(err);
      console.error(err);
      this.alertController.create({title : "Invalid credentials", message: ""});
    });
  }

}
