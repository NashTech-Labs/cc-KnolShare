import { Component } from "@angular/core";
import { NavController } from "ionic-angular";
import { SignupForm } from "../../models/signup-form";
import {SignupService} from "./signup.service";
import { AlertController } from "ionic-angular";
import {SharedService} from "../../services/shared.service";
import {HomePage} from "../home/home";
import { Storage } from "@ionic/storage";

@Component({
  selector: "page-signup",
  templateUrl: "signup.html",
  providers: [SignupService]
})
export class SignupPage {
  signupFormObj: SignupForm = new SignupForm();

  constructor(public navCtrl: NavController,
              private signupService: SignupService,
              private storage: Storage,
              private sharedService: SharedService,
              private alertController: AlertController) {}

  submit() {
    this.signupService.signup(this.signupFormObj).subscribe( (data: any) => {
      this.storage.set("user", JSON.stringify(data.data.user));
      this.storage.set("accessToken", JSON.stringify(data.data.accessToken));
      this.sharedService.isLoggedIn = true;
      this.navCtrl.pop();
      this.alertController.create({title : "Successfully Logged in", message: "signup hogya"});
    }, (err: any) => {
      alert(err);
      console.error(err);
      this.alertController.create({title : "Successfully Logged in", message: "signup hogya"});
    });
  }

}
