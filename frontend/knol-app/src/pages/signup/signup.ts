import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { SignupForm } from '../../models/signup-form';
import {SignupService} from "./signup.service";

@Component({
  selector: 'page-signup',
  templateUrl: 'signup.html',
  providers: [SignupService]
})
export class SignupPage {
  signupFormObj: SignupForm = new SignupForm();

  constructor(public navCtrl: NavController, private signupService: SignupService) {}

  submit() {
    this.signupService.login(this.signupFormObj).subscribe( (data: any) => {

    }, (err: any) => {

    })
  }

}
