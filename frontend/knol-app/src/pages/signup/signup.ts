import { Component } from "@angular/core";
import { NavController } from "ionic-angular";
import { SignupForm } from "../../models/signup-form";
import {SignupService} from "./signup.service";
import { AlertController } from "ionic-angular";
import {SharedService} from "../../services/shared.service";
import {HomePage} from "../home/home";

@Component({
  selector: "page-signup",
  templateUrl: "signup.html",
  providers: [SignupService]
})
export class SignupPage {
  signupFormObj: SignupForm = new SignupForm();

  constructor(public navCtrl: NavController,
              private signupService: SignupService,
              private sharedService: SharedService,
              private alertController: AlertController) {}

  submit() {
    this.signupService.signup(this.signupFormObj).subscribe( (data: any) => {
      console.log(data);
      localStorage.setItem("user", JSON.stringify(data.data));
      this.sharedService.isLoggedIn = true;
      this.navCtrl.push(HomePage);
      this.alertController.create({title : "Successfully Logged in", message: "signup hogya"});
    }, (err: any) => {
      console.log(err);
      this.alertController.create({title : "Successfully Logged in", message: "signup hogya"});
    });
  }

}
