import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { SignupForm } from '../../models/signup-form';
import {SignupService} from "./signup.service";
import { AlertController } from 'ionic-angular';

@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html',
  providers: [SignupService]
})
export class SignupPage {
  signupFormObj: SignupForm = new SignupForm();

  constructor(public navCtrl: NavController,
              private signupService: SignupService,
              private alertController: AlertController) {}

  submit() {
    this.signupService.signup(this.signupFormObj).subscribe( (data: any) => {
      this.alertController.create({title : 'Successfully Logged in', message: 'signup hogya'})
    }, (err: any) => {
      this.alertController.create({title : 'Successfully Logged in', message: 'signup hogya'})
    })
  }

}
